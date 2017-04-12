package com.ociweb.twitter;

import java.util.List;

import com.ociweb.gl.api.Builder;
import com.ociweb.gl.api.GreenApp;
import com.ociweb.gl.api.GreenRuntime;
import com.ociweb.pronghorn.pipe.Pipe;
import com.ociweb.pronghorn.stage.scheduling.GraphManager;
import com.ociweb.twitter.rest.SuggestionListRestModule;
import com.ociweb.twitter.schema.TwitterEventSchema;

public class TwitterCleanupServerBehavior implements GreenApp {

	private int REST_ROUTE;
	private final List<CustomerAuth> users;
	
	public TwitterCleanupServerBehavior(List<CustomerAuth> users) {
		this.users = users;
	}
	
	@Override
	public void declareConfiguration(Builder builder) {

		boolean isTLS = false;
		boolean isLarge = true;
		String bindHost = "127.0.0.1";
		int bindPort = 8081;
		builder.enableServer(isTLS, isLarge, bindHost, bindPort);

		REST_ROUTE = builder.registerRoute("/unfollow?user=%i");
		
		//TODO: Nathan needs to add default 404 support incaase the URL is spelled wrong.
		
	}

	@Override
	public void declareBehavior(GreenRuntime runtime) {
		
		//TODO: need to span one gm to another here so we can rebuild users data. (use disk hand off just before publish after dupe removal)
		
		GraphManager gm = GreenRuntime.getGraphManager(runtime);
		GraphManager.addDefaultNota(gm, GraphManager.SCHEDULE_RATE, 20_000_000);//every 20 ms
				
		for(CustomerAuth a: users) {
			Pipe<TwitterEventSchema> tweets = TwitterGraphBuilder.openTwitterUserStream(gm, a.consumerKey, a.consumerSecret, a.token, a.secret);		
		
			//TODO: must add language filter here
			
			//TODO: must add duplicate filter.
			
			
		
			
			TwitterGraphBuilder.publishEvents(gm, "unfollow", runtime, a, tweets);
		}
			
		
		
		
	}

	@Override
	public void declareParallelBehavior(GreenRuntime runtime) {

		int maxClients = 10;
		runtime.addRestListener(new SuggestionListRestModule(runtime, maxClients),REST_ROUTE).addSubscription("unfollow"); //TODO: will become  unfollow/%u
		
	}

}
