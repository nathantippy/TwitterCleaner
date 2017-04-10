package com.ociweb.twitter;

import java.util.concurrent.TimeUnit;

import com.ociweb.pronghorn.stage.monitor.MonitorConsoleStage;
import com.ociweb.pronghorn.stage.scheduling.GraphManager;
import com.ociweb.pronghorn.stage.scheduling.StageScheduler;
import com.ociweb.pronghorn.stage.scheduling.ThreadPerStageScheduler;
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
		
		
		//GraphExamples.watchUserFollowing(gm, auth);
		
		
		//GraphExamples.simpleTokenize(gm, auth);
		
		//GraphExamples.wordFilter(gm, auth);
		
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
