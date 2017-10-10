package com.ociweb.twitter;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

import com.ociweb.pronghorn.network.NetGraphBuilder;
import com.ociweb.pronghorn.network.schema.ClientHTTPRequestSchema;
import com.ociweb.pronghorn.network.schema.HTTPRequestSchema;
import com.ociweb.pronghorn.network.schema.NetResponseSchema;
import com.ociweb.pronghorn.network.schema.ServerResponseSchema;
import com.ociweb.pronghorn.network.schema.TwitterEventSchema;
import com.ociweb.pronghorn.network.schema.TwitterStreamControlSchema;
import com.ociweb.pronghorn.network.twitter.RequestTwitterFriendshipStage;
import com.ociweb.pronghorn.network.twitter.RequestTwitterQueryStreamStage;
import com.ociweb.pronghorn.network.twitter.RequestTwitterUserStreamStage;
import com.ociweb.pronghorn.network.twitter.TwitterJSONToTwitterEventsStage;
import com.ociweb.pronghorn.pipe.Pipe;
import com.ociweb.pronghorn.pipe.PipeConfig;
import com.ociweb.pronghorn.pipe.util.hash.LongHashTable;
import com.ociweb.pronghorn.stage.filter.FlagFilterStage;
import com.ociweb.pronghorn.stage.filter.PassRepeatsFilterStage;
import com.ociweb.pronghorn.stage.filter.PassUniquesFilterStage;
import com.ociweb.pronghorn.stage.scheduling.GraphManager;
import com.ociweb.pronghorn.stage.test.PipeCleanerStage;
import com.ociweb.twitter.stages.text.TextContentRouterBloom;
import com.ociweb.twitter.stages.text.TextContentRouterStage;

public class GraphBuilderUtil {
	
	public static Pipe<ServerResponseSchema>[] twitterFriendshipGraph(GraphManager gm,
			                       boolean unfollow,
			                       List<CustomerAuth> users, 
			                       Pipe<HTTPRequestSchema>[] fromRouter, 
			                       PipeConfig<ServerResponseSchema> outputConfig
								 ) {
				
		Pipe<ServerResponseSchema>[] outputs = Pipe.buildPipes(users.size(), outputConfig);
		
		int maxListeners = users.size();
		int maxRequesters = users.size();
		int maxPartialResponses = 1;
		
		
		////////////////////////////
		//pipes for holding all HTTPs client requests
		///////////////////////////*            
		int clientRequestsCount = 8;
		int clientRequestSize = 1<<12;		
		Pipe<ClientHTTPRequestSchema>[] clientRequestsPipes = Pipe.buildPipes(maxRequesters, new PipeConfig<ClientHTTPRequestSchema>(ClientHTTPRequestSchema.instance, clientRequestsCount, clientRequestSize));
		
		////////////////////////////
		//pipes for holding all HTTPs responses from server
		///////////////////////////      

		int clientResponseCount = 32;
		int clientResponseSize = 1<<17;
		Pipe<NetResponseSchema>[] clientResponsesPipes = Pipe.buildPipes(maxListeners, new PipeConfig<NetResponseSchema>(NetResponseSchema.instance, clientResponseCount, clientResponseSize));
			
		////////////////////////////
		//standard HTTPs client subgraph building with TLS handshake logic
		///////////////////////////   
		NetGraphBuilder.buildHTTPClientGraph(gm, 
				             maxPartialResponses, 
				             clientResponsesPipes, clientRequestsPipes);
		
		int i = users.size();
		Pipe<HTTPRequestSchema>[] inputs = Pipe.buildPipes(i, fromRouter[0].config());
		LongHashTable table = new LongHashTable(LongHashTable.computeBits(i));
		while (--i>=0) {
		
			CustomerAuth user = users.get(i);
			
			LongHashTable.setItem(table, user.id, i);//look up pipe based on twitter id
			
			if (unfollow) {
				
				RequestTwitterFriendshipStage.newUnFollowInstance(gm, 
						user.consumerKey, user.consumerSecret, user.token, user.consumerSecret, 
						inputs[i], outputs[i], 
						i, clientRequestsPipes[i], clientResponsesPipes[i]);
				
			} else {
				
				RequestTwitterFriendshipStage.newFollowInstance(gm, 
						user.consumerKey, user.consumerSecret, user.token, user.consumerSecret, 
						inputs[i], outputs[i], 
						i, clientRequestsPipes[i], clientResponsesPipes[i]);
				
			}
		}
		
		//takes user requests and routes them to specific Frienship stages based on user.
		new ParamRouterStage(gm, table, fromRouter, inputs);
		
		return outputs;
	}
	
	
	public static Pipe<TwitterEventSchema> openTwitterUserStream(GraphManager gm,
			                            String consumerKey, String consumerSecret, 
			                            String token, String secret) {
		
		////////////////////////////
		//pipes for holding all HTTPs client requests
		///////////////////////////*            
		int maxRequesters = 1;
		int clientRequestsCount = 8;
		int clientRequestSize = 1<<12;		
		Pipe<ClientHTTPRequestSchema>[] clientRequestsPipes = Pipe.buildPipes(maxRequesters, new PipeConfig<ClientHTTPRequestSchema>(ClientHTTPRequestSchema.instance, clientRequestsCount, clientRequestSize));
		
		////////////////////////////
		//pipes for holding all HTTPs responses from server
		///////////////////////////      
		int maxListeners =  1;
		int clientResponseCount = 32;
		int clientResponseSize = 1<<17;
		Pipe<NetResponseSchema>[] clientResponsesPipes = Pipe.buildPipes(maxListeners, new PipeConfig<NetResponseSchema>(NetResponseSchema.instance, clientResponseCount, clientResponseSize));
		
		////////////////////////////
		//standard HTTPs client subgraph building with TLS handshake logic
		///////////////////////////   
		int maxPartialResponses = 1;
		NetGraphBuilder.buildHTTPClientGraph(gm, maxPartialResponses, clientResponsesPipes, clientRequestsPipes); 
		
		////////////////////////
		//twitter specific logic
		////////////////////////
		int tweetsCount = 32;
		
		Pipe<TwitterStreamControlSchema> streamControlPipe = TwitterStreamControlSchema.instance.newPipe(8, 0);
		final int HTTP_REQUEST_RESPONSE_USER_ID = 0;
		
		////////////////////
		//Stage will open the Twitter stream and reconnect it upon request
		////////////////////		
		new RequestTwitterUserStreamStage(gm, consumerKey, consumerSecret, token, secret, HTTP_REQUEST_RESPONSE_USER_ID, streamControlPipe, clientRequestsPipes[0]);
					
		/////////////////////
		//Stage will parse JSON streaming from Twitter servers and convert it to a pipe containing twitter events
		/////////////////////
		int bottom = 0;//bottom is 0 because response keeps all results at the root
		return TwitterJSONToTwitterEventsStage.buildStage(gm, false, bottom, clientResponsesPipes[HTTP_REQUEST_RESPONSE_USER_ID], streamControlPipe, tweetsCount);
	
	}
	
	public static Pipe<TwitterEventSchema>[] openTwitterQueryStream(GraphManager gm, 
																    String[] queryText, int[] queryRoutes,
			                                                        String consumerKey, String consumerSecret) {


		int maxQRoute = 0;
		int j = queryRoutes.length;
		while (--j>=0) {
			maxQRoute = Math.max(maxQRoute, queryRoutes[j]);
		}
				
		final int bearerPipeIdx = maxQRoute+1;
		int maxListeners =  bearerPipeIdx+1;
			
		
		////////////////////////////
		//pipes for holding all HTTPs client requests
		///////////////////////////*            
		int maxRequesters = 1;
		int requesterIdx = 0;
		int clientRequestsCount = 8;
		int clientRequestSize = 1<<12;		
		Pipe<ClientHTTPRequestSchema>[] clientRequestsPipes = Pipe.buildPipes(maxRequesters, new PipeConfig<ClientHTTPRequestSchema>(ClientHTTPRequestSchema.instance, clientRequestsCount, clientRequestSize));
		
		////////////////////////////
		//pipes for holding all HTTPs responses from server
		///////////////////////////      

		int clientResponseCount = 32;
		int clientResponseSize = 1<<17;
		Pipe<NetResponseSchema>[] clientResponsesPipes = Pipe.buildPipes(maxListeners, new PipeConfig<NetResponseSchema>(NetResponseSchema.instance, clientResponseCount, clientResponseSize));
			
		////////////////////////////
		//standard HTTPs client subgraph building with TLS handshake logic
		///////////////////////////   
		int maxPartialResponses = 1;
		NetGraphBuilder.buildHTTPClientGraph(gm, 
				             maxPartialResponses, 
				             clientResponsesPipes, clientRequestsPipes); 
		
		////////////////////////
		//twitter specific logic
		////////////////////////
		
		final int tweetsCount = 32;
		final int bottom = 2;//bottom is 2 because multiple responses are wrapped in an array
		int queryGroups = maxQRoute+1;
		
		Pipe<TwitterStreamControlSchema>[] controlPipes = new Pipe[queryGroups];
		Pipe<TwitterEventSchema>[] eventPipes = new Pipe[queryGroups];
		int k = queryGroups;
		while (--k>=0) {
			//we use a different JSON parser for each group of queries.			
			eventPipes[k] = TwitterJSONToTwitterEventsStage.buildStage(gm, true, bottom, 
											clientResponsesPipes[k], 
											controlPipes[k] = TwitterStreamControlSchema.instance.newPipe(8, 0), 
											tweetsCount);
		}
			
		RequestTwitterQueryStreamStage.newInstance(gm, consumerKey, consumerSecret,
											tweetsCount, queryRoutes, queryText,
				                            bearerPipeIdx,
				                            //the parser detected bad bearer or end of data, reconnect
				                            //also sends the PostIds of every post decoded
				                            controlPipes, 
				                            clientResponsesPipes[bearerPipeIdx], //new bearer response
				                            clientRequestsPipes[requesterIdx]); //requests bearers and queries

		return eventPipes;		
		
	}
	
	
	//////////////////////////////////
	
	
	
	public static Pipe<TwitterEventSchema> possiblySensistive(GraphManager gm, Pipe<TwitterEventSchema> input) {
		
		Pipe<TwitterEventSchema> results = TwitterEventSchema.instance.newPipe(100, 1024);
		new FlagFilterStage(gm, input, results, TwitterEventSchema.MSG_USER_100_FIELD_FLAGS_31, TwitterEventSchema.FLAG_POSSIBLY_SENSITIVE);
		return results;
		
	}
	
	/**
	 * Messages will only pass if the selected field contains a unique string that has not been seen before.
	 */
	public static Pipe<TwitterEventSchema> uniqueFieldFilter(GraphManager gm, Pipe<TwitterEventSchema> input, int field, File storageFile) {
		
		Pipe<TwitterEventSchema> results = TwitterEventSchema.instance.newPipe(100, 1024);		
		new PassUniquesFilterStage<>(gm, input, results, field, storageFile);		
		return results;
		
	}	
			
	/**
	 * Messages will only pass if the selected field repeats some value repeatCount times, then it will pass the message along
	 */
	public static Pipe<TwitterEventSchema> repeatingFieldFilter(GraphManager gm, Pipe<TwitterEventSchema> input, int repeatCount, int field, File storageFile) {
		
		Pipe<TwitterEventSchema> results = TwitterEventSchema.instance.newPipe(100, 1024);		
		new PassRepeatsFilterStage(gm, input, results, repeatCount, field, storageFile);		
		return results;
		
	}	
	
	public static Pipe<TwitterEventSchema>  badWordUsers(GraphManager gm, Pipe<TwitterEventSchema> input) {
		
		
		PipeConfig<TwitterEventSchema> pipeConfig = TwitterEventSchema.instance.newPipeConfig(100, 1024);		
	
		Pipe<TwitterEventSchema>[] results = new Pipe[2];		
		results[0] = new Pipe<TwitterEventSchema>(pipeConfig);
		results[1] = new Pipe<TwitterEventSchema>(pipeConfig);
		
		PipeCleanerStage.newInstance(gm, results[0]);		
		
		
		int field = TwitterEventSchema.MSG_USERPOST_101_FIELD_TEXT_22;
		TextContentRouterBloom rules;
		try {
			rules = new TextContentRouterBloom(
					                           new ObjectInputStream( GraphBuilderUtil.class.getResourceAsStream("/badWords.dat")),
					                            0,1);
			TextContentRouterStage router = new TextContentRouterStage(gm, input, results, field, rules);
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return results[1];
	}
	
	
	
	
	
	
	
	
}
