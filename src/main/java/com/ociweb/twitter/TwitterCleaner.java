package com.ociweb.twitter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.ociweb.pronghorn.pipe.FieldReferenceOffsetManager;
import com.ociweb.pronghorn.stage.scheduling.FixedThreadsScheduler;
import com.ociweb.pronghorn.stage.scheduling.GraphManager;
import com.ociweb.pronghorn.stage.scheduling.StageScheduler;
import com.ociweb.pronghorn.stage.scheduling.ThreadPerStageScheduler;
import com.ociweb.pronghorn.util.parse.JSONStreamVisitorEnumGenerator;

public class TwitterCleaner  {

	private final String consumerKey;
	private final String consumerSecret;
	private final String token;
	private final String secret;
	
	JSONStreamVisitorEnumGenerator enumGen = new JSONStreamVisitorEnumGenerator();
	
	public TwitterCleaner(String consumerKey, String consumerSecret, String token, String secret) {
		this.consumerKey = consumerKey;
		this.consumerSecret = consumerSecret;
		this.token = token;
		this.secret = secret;
	}

	public static void main(String[] args) {
		
		//DO NOT check in these values they must be provided for every run.
        String consumerKey = getOptArg("consumerKey","-ck",args,"rnuv1kvMagTr4QldKjjU74U6Y");
        String consumerSecret = getOptArg("consumerSecret","-cs",args,"pjTAU2YCx0TDPLuKq0Hgate0sifRmEHAToNY4CNZ0Lx70U1AnA");
        
        String token = getOptArg("token","-t",args,"1536719148-YctcWHN6ChyzMWehnXepeCDW6o1HMzxxKrTRHsR");
        String secret = getOptArg("secret","-s",args,"Q25xzPsxRMGXVqUzBpupY4X2gfsrGYhN4qZR0q8ekJZAg");

		TwitterCleaner instance = new TwitterCleaner(consumerKey, consumerSecret, token, secret);
    	instance.run();
		
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
	
	
	public void run() {
	
		List<CustomerAuth> users = fetchCustomersFromDB();
		File staticFilesPathRootIndex = new File("");
				
		GraphManager gm = new GraphManager();
		
		GraphBuilder behavior = new GraphBuilder(users, staticFilesPathRootIndex);
		behavior.buildGraph(gm);
			 
		boolean awesomeDebug = true;//false;
		if (awesomeDebug) {
			gm.enableTelemetry(8091); 
		}
		
		StageScheduler scheduler = StageScheduler.defaultScheduler(gm);
				
		scheduler.startup();
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
		    public void run() {
		    	scheduler.shutdown();
		    	scheduler.awaitTermination(7, TimeUnit.SECONDS);
		    }
		});
		
	}

	private List<CustomerAuth> fetchCustomersFromDB() {
		List<CustomerAuth> users = new ArrayList<CustomerAuth>();
		
		long id = 1234;//fake id, must be changed to real twitter id;
		
		users.add(new CustomerAuth(consumerKey, consumerSecret, token, secret, id));
		return users;
	}

}
