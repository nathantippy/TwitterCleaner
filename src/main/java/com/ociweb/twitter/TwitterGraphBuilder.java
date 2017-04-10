package com.ociweb.twitter;

import com.ociweb.pronghorn.network.NetGraphBuilder;
import com.ociweb.pronghorn.network.schema.ClientHTTPRequestSchema;
import com.ociweb.pronghorn.network.schema.NetResponseSchema;
import com.ociweb.pronghorn.pipe.Pipe;
import com.ociweb.pronghorn.pipe.PipeConfig;
import com.ociweb.pronghorn.stage.scheduling.GraphManager;
import com.ociweb.twitter.stages.RequestTwitterUserStreamStage;
import com.ociweb.twitter.stages.json.TwitterEventSchema;
import com.ociweb.twitter.stages.json.TwitterJSONToTwitterEventsStage;

public class TwitterGraphBuilder {


	
	public static Pipe<TwitterEventSchema> openTwitterUserStream(GraphManager gm, String consumerKey, String consumerSecret, String token, String secret) {
		
		////////////////////////////
		//basic HTTPs client logic
		///////////////////////////            
		int maxRequesters = 1;
		int clientRequestsCount = 8;
		int clientRequestSize = 1<<12;		
		Pipe<ClientHTTPRequestSchema>[] clientRequestsPipes = Pipe.buildPipes(maxRequesters, new PipeConfig<ClientHTTPRequestSchema>(ClientHTTPRequestSchema.instance, clientRequestsCount, clientRequestSize));
		
		int maxListeners =  1;
		int clientResponseCount = 32;
		int clientResponseSize = 1<<17;
		Pipe<NetResponseSchema>[] clientResponsesPipes = Pipe.buildPipes(maxListeners, new PipeConfig<NetResponseSchema>(NetResponseSchema.instance, clientResponseCount, clientResponseSize));
		
		int maxPartialResponses = 1;
		NetGraphBuilder.buildHTTPClientGraph(gm, maxPartialResponses, clientResponsesPipes, clientRequestsPipes); 
		
		////////////////////////
		//twitter specific logic
		////////////////////////
		
		Pipe<TwitterStreamControlSchema> streamControlPipe = TwitterStreamControlSchema.instance.newPipe(4, 0);
		final int PIPE_IDX = 0;
		new RequestTwitterUserStreamStage(gm, consumerKey, consumerSecret, token, secret, PIPE_IDX, streamControlPipe, clientRequestsPipes[0]);
					
		int tweetsCount = 32;
		return TwitterJSONToTwitterEventsStage.buildStage(gm, clientResponsesPipes[PIPE_IDX], streamControlPipe, clientResponsesPipes, tweetsCount);
	
	}

}
