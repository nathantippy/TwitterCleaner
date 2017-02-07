package com.ociweb.twitter;

import java.util.concurrent.TimeUnit;

import com.ociweb.pronghorn.stage.monitor.MonitorConsoleStage;
import com.ociweb.pronghorn.stage.scheduling.GraphManager;
import com.ociweb.pronghorn.stage.scheduling.StageScheduler;
import com.ociweb.pronghorn.stage.scheduling.ThreadPerStageScheduler;

public class TwitterCleaner {

	private static TwitterCleaner instance;
	
	private final GraphManager gm;
	
    static {
        System.setProperty("org.apache.commons.logging.Log",
                           "org.apache.commons.logging.impl.NoOpLog");
    }
    
	public TwitterCleaner() {
		
		gm = new GraphManager();		
		GraphManager.addDefaultNota(gm, GraphManager.SCHEDULE_RATE, 2_000_000);//every 2 ms
	}
	
	public static void main(String[] args) {

		instance = new TwitterCleaner();
		
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
		
		//TODO: may need to load data here
		
		//before building this part we need to design a graph of the proposed data flow.
		
		//TODO: create HosebirdFeedStages instance
			
		
		//TODO: use Green Lightning import and build web server to host the site
		
		
		
		
		
	}

	
	
	
}
