package com.ociweb.twitter.example;

import com.ociweb.pronghorn.network.schema.TwitterEventSchema;
import com.ociweb.pronghorn.pipe.Pipe;
import com.ociweb.pronghorn.stage.scheduling.FixedThreadsScheduler;
import com.ociweb.pronghorn.stage.scheduling.GraphManager;
import com.ociweb.pronghorn.stage.scheduling.StageScheduler;
import com.ociweb.pronghorn.stage.scheduling.ThreadPerStageScheduler;
import com.ociweb.pronghorn.stage.test.ConsoleJSONDumpStage;
import com.ociweb.twitter.GraphBuilderUtil;

public class ExampleQueryWatcher {

	
	public static void main(String[] args) {
		
		//DO NOT check in these values they must be provided for every run.
        String consumerKey = getOptArg("consumerKey","-ck",args,null);
        String consumerSecret = getOptArg("consumerSecret","-cs",args,null);
        
        String token = getOptArg("token","-t",args,null);
        String secret = getOptArg("secret","-s",args,null);
        
        GraphManager gm = new GraphManager();  
        gm.addDefaultNota(gm, GraphManager.SCHEDULE_RATE, 20_000_000);//never run more frequently than ever 20 ms
        
        
		Pipe<TwitterEventSchema> tweets = GraphBuilderUtil.openTwitterQueryStream(gm, 
														consumerKey, consumerSecret, token, secret);		
		
		ConsoleJSONDumpStage dump = ConsoleJSONDumpStage.newInstance(gm, tweets);

		gm.enableTelemetry(8098);
		
		StageScheduler s = //new ThreadPerStageScheduler(gm);
				           new FixedThreadsScheduler(gm, 4);
		s.startup();
		
		gm.blockUntilStageBeginsShutdown(dump);
		s.shutdown();
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
