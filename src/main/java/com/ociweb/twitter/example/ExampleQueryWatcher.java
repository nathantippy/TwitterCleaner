package com.ociweb.twitter.example;

import com.ociweb.pronghorn.network.schema.TwitterEventSchema;
import com.ociweb.pronghorn.pipe.Pipe;
import com.ociweb.pronghorn.stage.PronghornStage;
import com.ociweb.pronghorn.stage.scheduling.FixedThreadsScheduler;
import com.ociweb.pronghorn.stage.scheduling.GraphManager;
import com.ociweb.pronghorn.stage.scheduling.StageScheduler;
import com.ociweb.pronghorn.stage.scheduling.ThreadPerStageScheduler;
import com.ociweb.pronghorn.stage.test.ConsoleJSONDumpStage;
import com.ociweb.pronghorn.stage.test.ConsoleSummaryStage;
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
        
		
		String[] queryText = new String[] {
				"java OR c++ OR python OR c# OR go OR html OR css OR js OR .net OR node",
				"abstract OR object OR null OR opengl OR stack OR software OR pointer OR script OR method OR  function"
		};
		int[] queryRoutes = new int[] {0};
        
		Pipe<TwitterEventSchema>[] tweets = GraphBuilderUtil.openTwitterQueryStream(gm,
				                                        queryText, queryRoutes,
														consumerKey, consumerSecret);		
		
		PronghornStage dump = null;
		
		int i = tweets.length;
		while (--i>=0) {
			
			dump = ConsoleSummaryStage.newInstance(gm, tweets[i]);
			
			
			//dump = ConsoleJSONDumpStage.newInstance(gm, tweets[i]);
		}
		
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
