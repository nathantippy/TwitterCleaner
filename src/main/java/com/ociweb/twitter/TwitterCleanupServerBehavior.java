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
	private int STATIC_FILE_ROUTE;
	
	private final List<CustomerAuth> users;
	
	public TwitterCleanupServerBehavior(List<CustomerAuth> users) {
		this.users = users;
	}
	
	@Override
	public void declareConfiguration(Builder builder) {

		boolean isTLS = true;
		boolean isLarge = false;
		String bindHost = "127.0.0.1";
		int bindPort = 8081;
		builder.enableServer(isTLS, isLarge, bindHost, bindPort);
		builder.parallelism(2);
		
		builder.setDefaultRate(20_000_000); //20 ms
		
		//TODO: do not enable, it will max out CPU, need to investigate....
		//builder.limitThreads(); //looks at the hardware and picks an appropriate value.

		REST_ROUTE = builder.registerRoute("/unfollow?user=%u");
		STATIC_FILE_ROUTE = builder.registerRoute("/%b");//TODO: if this is first it ignores the rest of the paths, should fix someday...
		
		
		builder.enableTelemetry(true);
		
		//TODO: Nathan needs to add default 404 support incaase the URL is spelled wrong.
		
	}

	@Override
	public void declareBehavior(GreenRuntime runtime) {
		
		//TODO: need to span one gm to another here so we can rebuild users data. (use disk hand off just before publish after dupe removal)
		
		GraphManager gm = GreenRuntime.getGraphManager(runtime);
		//GraphManager.addDefaultNota(gm, GraphManager.SCHEDULE_RATE, 20_000_000);//every 20 ms
				
		for(CustomerAuth a: users) {
			Pipe<TwitterEventSchema> tweets = TwitterGraphBuilder.openTwitterUserStream(gm, a.consumerKey, a.consumerSecret, a.token, a.secret);		
		
			Pipe<TwitterEventSchema> sellers = TwitterGraphBuilder.bookSellers(gm, tweets);
			
			//TODO: must add duplicate filter...
						
			TwitterGraphBuilder.publishEvents(gm, "unfollow", runtime, a, sellers);
		}
			
		int maxClients = 10;
		runtime.addRestListener(new SuggestionListRestModule(runtime, maxClients),REST_ROUTE).addSubscription("unfollow"); //TODO: will become  unfollow/%u
		
		
	}

	@Override
	public void declareParallelBehavior(GreenRuntime runtime) {

		
		
		runtime.addFileServer("/site/index.html", STATIC_FILE_ROUTE); 
		
	}

}
