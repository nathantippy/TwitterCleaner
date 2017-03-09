package com.ociweb.twitter;

import java.util.concurrent.TimeUnit;

import com.ociweb.pronghorn.adapter.twitter.HosebirdFeedStage;
import com.ociweb.pronghorn.adapter.twitter.TwitterEventSchema;
import com.ociweb.pronghorn.pipe.Pipe;
import com.ociweb.pronghorn.pipe.PipeConfig;
import com.ociweb.pronghorn.stage.PronghornStage;
import com.ociweb.pronghorn.stage.monitor.MonitorConsoleStage;
import com.ociweb.pronghorn.stage.scheduling.GraphManager;
import com.ociweb.pronghorn.stage.scheduling.StageScheduler;
import com.ociweb.pronghorn.stage.scheduling.ThreadPerStageScheduler;
import com.ociweb.pronghorn.stage.test.ConsoleJSONDumpStage;
import com.ociweb.pronghorn.stage.test.PipeCleanerStage;
import com.ociweb.pronghorn.util.Appendables;
import com.ociweb.twitter.stages.text.TextContentRouter;
import com.ociweb.twitter.stages.text.TextContentRouterStage;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;

public class TwitterCleaner {

	private static TwitterCleaner instance;
	
	private final GraphManager gm;
	private final Authentication auth;
	
    static {//do not remove this fixes some issues in Hosebird parser
        System.setProperty("org.apache.commons.logging.Log",
                           "org.apache.commons.logging.impl.NoOpLog");
    }
    
	public TwitterCleaner(Authentication auth) {
		this.auth = auth;
		gm = new GraphManager();		
		GraphManager.addDefaultNota(gm, GraphManager.SCHEDULE_RATE, 2_000_000);//every 2 ms
	}
	
	public static void main(String[] args) {

		//DO NOT check in these values they must be provided for every run.
        String consumerKey = getOptArg("consumerKey","-ck",args,null);
        String consumerSecret = getOptArg("consumerSecret","-cs",args,null);
        String token = getOptArg("token","-t",args,null);
        String secret = getOptArg("secret","-s",args,null);
        		
		instance = new TwitterCleaner(new OAuth1(consumerKey, consumerSecret, token, secret));
		
		instance.buildGraph();
		
		instance.startup();
	}
	
	private void startup() {
		boolean debug = false;
		if (debug) {
			////////////////
			///FOR DEBUG GENERATE A PICTURE OF THE SERVER
			////////////////	
			final MonitorConsoleStage attach =  MonitorConsoleStage.attach(gm);
		}
		
		////////////////
		//CREATE A SCHEDULER TO RUN THE SERVER
		////////////////
		final StageScheduler scheduler = new ThreadPerStageScheduler(gm);
				
		//////////////////
		//UPON CTL-C SHUTDOWN OF SERVER DO A CLEAN SHUTDOWN
		//////////////////
	    Runtime.getRuntime().addShutdownHook(new Thread() {
	        public void run() {
	        		scheduler.shutdown();
	                scheduler.awaitTermination(30, TimeUnit.SECONDS);
	
	        }
	    });
		
	    ///////////////// 
	    //START RUNNING THE SERVER
	    /////////////////        
	    scheduler.startup();
	}


	private void buildGraph() {
		
		
		//simpleConsoleWatch();
		
		
		simpleTokenize();
		
		
		
	}

	private void simpleConsoleWatch() {
		PipeConfig<TwitterEventSchema> hoseFeedPipeConfig = new PipeConfig<TwitterEventSchema>(TwitterEventSchema.instance, 100, 1024);
		
		Pipe<TwitterEventSchema> output = new Pipe<TwitterEventSchema>(hoseFeedPipeConfig);
		
		HosebirdFeedStage feed = new HosebirdFeedStage(gm, auth, output, 0 );
		
		PronghornStage consume = new ConsoleJSONDumpStage(gm, output);
	}
	
	private void simpleTokenize() {
		PipeConfig<TwitterEventSchema> hoseFeedPipeConfig = new PipeConfig<TwitterEventSchema>(TwitterEventSchema.instance, 100, 1024);
		
		Pipe<TwitterEventSchema> output = new Pipe<TwitterEventSchema>(hoseFeedPipeConfig);
		
		HosebirdFeedStage feed = new HosebirdFeedStage(gm, auth, output, 0 );
		
		Pipe<TwitterEventSchema>[] results = new Pipe[2];
		
		results[0] = new Pipe<TwitterEventSchema>(hoseFeedPipeConfig);
		results[1] = new Pipe<TwitterEventSchema>(hoseFeedPipeConfig);
		
		PronghornStage consumeA = new PipeCleanerStage(gm, results[0]);
		PronghornStage consumeB = new PipeCleanerStage(gm, results[1]);
		
		int field = TwitterEventSchema.MSG_USERPOST_101_FIELD_TEXT_22;
		TextContentRouter textFilter = new TextContentRouter() {

			@Override
			public void clear() {
				// TODO Auto-generated method stub
				System.err.println("CLEAR");
			}

			@Override
			public void text(byte[] backing, int pos, int len, int mask) {
				// TODO Auto-generated method stub
				System.err.print("word: ");
				Appendables.appendUTF8(System.err, backing, pos, len, mask);
				System.err.println();
			}

			@Override
			public void url(byte[] backing, int pos, int len, int mask) {
				// TODO Auto-generated method stub
				System.err.print("url: ");
				Appendables.appendUTF8(System.err, backing, pos, len, mask);
				System.err.println();
			}

			@Override
			public int route() {
				// TODO Auto-generated method stub
				return 0;
			}
			
		};
		TextContentRouterStage router = new TextContentRouterStage(gm, output, results, field, textFilter);
				
	}
	

    private static String getOptArg(String longName, String shortName, String[] args, String defaultValue) {
        String prev = null;
        for (String token : args) {
            if (longName.equals(prev) || shortName.equals(prev)) {
                if (token == null || token.trim().length() == 0 || token.startsWith("-")) {
                    return defaultValue;
                }
                return token.trim();
            }
            prev = token;
        }
        return defaultValue;
    }
	
	
	
}
