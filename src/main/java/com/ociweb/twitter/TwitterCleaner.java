package com.ociweb.twitter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

import com.ociweb.gl.api.GreenRuntime;
import com.ociweb.pronghorn.network.NetResponseDumpStage;
import com.ociweb.pronghorn.network.NetResponseJSONStage;
import com.ociweb.pronghorn.network.schema.ClientHTTPRequestSchema;
import com.ociweb.pronghorn.pipe.MessageSchema;
import com.ociweb.pronghorn.pipe.Pipe;
import com.ociweb.pronghorn.pipe.PipeWriter;
import com.ociweb.pronghorn.pipe.util.hash.IntHashTable;
import com.ociweb.pronghorn.pipe.util.hash.LongHashTable;
import com.ociweb.pronghorn.stage.PronghornStage;
import com.ociweb.pronghorn.stage.monitor.MonitorConsoleStage;
import com.ociweb.pronghorn.stage.scheduling.GraphManager;
import com.ociweb.pronghorn.stage.scheduling.ThreadPerStageScheduler;
import com.ociweb.pronghorn.stage.test.ConsoleJSONDumpStage;
import com.ociweb.pronghorn.stage.test.PipeCleanerStage;
import com.ociweb.pronghorn.util.Appendables;
import com.ociweb.pronghorn.util.RollingBloomFilter;
import com.ociweb.pronghorn.util.TrieParser;
import com.ociweb.pronghorn.util.TrieParserReader;
import com.ociweb.pronghorn.util.parse.JSONStreamParser;
import com.ociweb.pronghorn.util.parse.JSONStreamVisitorCapture;
import com.ociweb.pronghorn.util.parse.JSONStreamVisitorEnumGenerator;
import com.ociweb.pronghorn.util.parse.JSONStreamVisitorToPipe;
import com.ociweb.pronghorn.util.parse.MapJSONToPipeBuilder;
import com.ociweb.twitter.schema.TwitterEventSchema;
import com.ociweb.twitter.stages.OAuth2ParserStage;
import com.ociweb.twitter.stages.RequestTwitterStreamStage;
import com.ociweb.twitter.stages.TwitterEventDumpStage;
import com.ociweb.twitter.stages.json.TwitterKey;
import com.ociweb.twitter.stages.text.TextContentRouterBloom;
import com.ociweb.twitter.stages.text.TextContentRouterStage;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;

import twitter4j.JSONObject;

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
