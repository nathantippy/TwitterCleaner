package com.ociweb.twitter;

import java.util.ArrayList;
import java.util.List;

import com.ociweb.gl.api.GreenRuntime;
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
        String consumerKey = getOptArg("consumerKey","-ck",args,null);
        String consumerSecret = getOptArg("consumerSecret","-cs",args,null);
        
        String token = getOptArg("token","-t",args,null);
        String secret = getOptArg("secret","-s",args,null);

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
		
		GreenRuntime.run(new TwitterCleanupServerBehavior(users));

		
		
	}

	private List<CustomerAuth> fetchCustomersFromDB() {
		List<CustomerAuth> users = new ArrayList<CustomerAuth>();
		
		long id = 1234;//fake id, must be changed to real twitter id;
		
		users.add(new CustomerAuth(consumerKey,consumerSecret, token, secret, id));
		return users;
	}
	




	
}
