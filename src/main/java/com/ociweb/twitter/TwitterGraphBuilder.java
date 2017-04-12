package com.ociweb.twitter;

import com.ociweb.gl.api.CommandChannel;
import com.ociweb.gl.api.GreenRuntime;
import com.ociweb.pronghorn.network.NetGraphBuilder;
import com.ociweb.pronghorn.network.schema.ClientHTTPRequestSchema;
import com.ociweb.pronghorn.network.schema.NetResponseSchema;
import com.ociweb.pronghorn.pipe.Pipe;
import com.ociweb.pronghorn.pipe.PipeConfig;
import com.ociweb.pronghorn.stage.scheduling.GraphManager;
import com.ociweb.twitter.schema.TwitterEventSchema;
import com.ociweb.twitter.schema.TwitterStreamControlSchema;
import com.ociweb.twitter.stages.PublishTwitterUsersStage;
import com.ociweb.twitter.stages.RequestTwitterUserStreamStage;
import com.ociweb.twitter.stages.json.TwitterJSONToTwitterEventsStage;

public class TwitterGraphBuilder {
	
	public static Pipe<TwitterEventSchema> openTwitterUserStream(GraphManager gm, String consumerKey, String consumerSecret, String token, String secret) {
		
		////////////////////////////
		//pipes for holding all HTTPs client requests
		///////////////////////////            
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
		
		Pipe<TwitterStreamControlSchema> streamControlPipe = TwitterStreamControlSchema.instance.newPipe(4, 0);
		final int PIPE_IDX = 0;
		
		////////////////////
		//Stage will open the Twitter stream and reconnect it upon request
		////////////////////		
		new RequestTwitterUserStreamStage(gm, consumerKey, consumerSecret, token, secret, PIPE_IDX, streamControlPipe, clientRequestsPipes[0]);
					
		/////////////////////
		//Stage will parse JSON streaming from Twitter servers and convert it to a pipe contaning twitter events
		/////////////////////
		int tweetsCount = 32;
		return TwitterJSONToTwitterEventsStage.buildStage(gm, clientResponsesPipes[PIPE_IDX], streamControlPipe, clientResponsesPipes, tweetsCount);
	
	}

	public static void publishEvents(GraphManager gm, String topic, GreenRuntime runtime, CustomerAuth a, Pipe<TwitterEventSchema> tweets) {
				
		new PublishTwitterUsersStage(gm, topic, a, tweets, runtime.newCommandChannel(CommandChannel.DYNAMIC_MESSAGING));
		
	}

	
//	public static void buildSomething(Pipe<TwitterEventSchema> input) {
//		
//		
//		PipeConfig<TwitterEventSchema> hoseFeedPipeConfig = new PipeConfig<TwitterEventSchema>(TwitterEventSchema.instance, 100, 1024);		
//	//	Pipe<TwitterEventSchema> output = new Pipe<TwitterEventSchema>(hoseFeedPipeConfig);		
//		Pipe<TwitterEventSchema>[] results = new Pipe[2];		
//		results[0] = new Pipe<TwitterEventSchema>(hoseFeedPipeConfig);
//		results[1] = new Pipe<TwitterEventSchema>(hoseFeedPipeConfig);	
//		
//		PronghornStage consumeA = new PipeCleanerStage(gm, results[0]);		
//	//	PronghornStage consumeA = new ConsoleJSONDumpStage(gm, results[0]);		
//		PronghornStage consumeB = new ConsoleJSONDumpStage(gm, results[1]);	
//		
//		
//		int field = TwitterEventSchema.MSG_USERPOST_101_FIELD_TEXT_22;
//		TextContentRouterBloom rules;
//		try {
//			rules = new TextContentRouterBloom(
//					                           new ObjectInputStream( GraphExamples.class.getResourceAsStream("/bookWords.dat")),
//					                            0,1);
//			TextContentRouterStage router = new TextContentRouterStage(gm, input, results, field, rules);
//			
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
	
	
	
	
	
	
	
}
