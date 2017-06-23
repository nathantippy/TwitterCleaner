package com.ociweb.twitter;

import java.io.File;
import java.util.List;

import com.ociweb.gl.api.Builder;
import com.ociweb.gl.api.GreenCommandChannel;
import com.ociweb.gl.api.MsgApp;
import com.ociweb.gl.api.MsgRuntime;
import com.ociweb.pronghorn.pipe.Pipe;
import com.ociweb.pronghorn.stage.scheduling.GraphManager;
import com.ociweb.twitter.rest.SuggestionListRestModule;
import com.ociweb.twitter.schema.TwitterEventSchema;
import com.ociweb.twitter.stages.PublishTwitterUsersStage;

public class TwitterCleanupServerBehavior implements MsgApp {

	private int REST_ROUTE;
	
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
			
		builder.setDefaultRate(10_000_000); //10 ms


		REST_ROUTE = builder.registerRoute("/unfollow?user=#{userId}");

		builder.enableTelemetry(true);

	}

	@Override
	public void declareBehavior(MsgRuntime runtime) {

		GraphManager gm = MsgRuntime.getGraphManager(runtime);
		//GraphManager.addDefaultNota(gm, GraphManager.SCHEDULE_RATE, 20_000_000);//every 20 ms
				
		for(CustomerAuth a: users) {
			Pipe<TwitterEventSchema> tweets = TwitterGraphBuilder.openTwitterUserStream(gm, a.consumerKey, a.consumerSecret, a.token, a.secret);		
		
			////////Only pass along those users tweets which are about books.
			Pipe<TwitterEventSchema> sellers = TwitterGraphBuilder.badWordUsers(gm, tweets);
			
			///////We only pass this on as a person to block if they show up 2 times 
			//NOTE: a file name is passed in to remember the repeat count so we can continue of reboot
			Pipe<TwitterEventSchema> repeaters = TwitterGraphBuilder.repeatingFieldFilter(gm, sellers, 4, TwitterEventSchema.MSG_USERPOST_101_FIELD_NAME_52, new File("repeaters"+a.id+".dat") );
			
			//////We only pass this user on if we have never recommended that we un follow them before 
			//NOTE: a file name is passed in here so it can save "seen" user names and continue to block duplicates after a "reboot"
			Pipe<TwitterEventSchema> uniques = TwitterGraphBuilder.uniqueFieldFilter(gm, repeaters, TwitterEventSchema.MSG_USERPOST_101_FIELD_NAME_52, new File("uniques"+a.id+".dat") );
						
			PublishTwitterUsersStage stage = new PublishTwitterUsersStage(gm, "unfollow/"+a.id, a, uniques, 
					runtime.newCommandChannel(GreenCommandChannel.DYNAMIC_MESSAGING));
	
			
		}
			
		int maxClients = 10;
		runtime.addRestListener(new SuggestionListRestModule(runtime, maxClients),REST_ROUTE).addSubscription("unfollow/%u");
		
	}

}
