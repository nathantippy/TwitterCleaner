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
import java.util.Base64;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

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
import com.ociweb.twitter.stages.OAuth2ParserStage;
import com.ociweb.twitter.stages.RequestTwitterStreamStage;
import com.ociweb.twitter.stages.TwitterEventDumpStage;
import com.ociweb.twitter.stages.json.TwitterEventSchema;
import com.ociweb.twitter.stages.json.TwitterKey;
import com.ociweb.twitter.stages.text.TextContentRouterBloom;
import com.ociweb.twitter.stages.text.TextContentRouterStage;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;

import twitter4j.JSONObject;

public class AuthTesting  {

	private final String consumerKey;
	private final String consumerSecret;
	private final String token;
	private final String secret;
	
	JSONStreamVisitorEnumGenerator enumGen = new JSONStreamVisitorEnumGenerator();
	
	public AuthTesting(String consumerKey, String consumerSecret, String token, String secret) {
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

//        RollingBloomFilter testFilter = new RollingBloomFilter(10_000, .00001) ;// 10K users being bad at once?? one it 100K mistake
//        System.err.println("fitler size: "+ testFilter.estimatedSize());// 64K (2x) 128K (3x) 600MB for 5K usesr filter
//        

//        //follow 1K daily or 182K per 6 mo.
//        RollingBloomFilter testFilter = new RollingBloomFilter(182_500, .0001) ;// 10K users being bad at once?? one it 10K mistake
//        System.err.println("fitler size: "+ testFilter.estimatedSize());// 1M for each user, 5 G for all filters?  may bump up later.
    
        
		AuthTesting instance = new AuthTesting(consumerKey, consumerSecret, token, secret);
    	instance.run();
	
		
	}


	public void run() {
		
		GraphManager gm = new GraphManager();		
		GraphManager.addDefaultNota(gm, GraphManager.SCHEDULE_RATE, 20_000_000);//every 20 ms
		
		//TODO: must be notified upon connection close so we can reopen the original request.
		
		Pipe<TwitterEventSchema> tweets = TwitterGraphBuilder.openTwitterUserStream(gm, consumerKey, consumerSecret, token, secret);
		
		buildStreamingTweetsConsumer(gm, tweets);
		
		
		MonitorConsoleStage.attach(gm);
		
		ThreadPerStageScheduler scheduler = new ThreadPerStageScheduler(gm);
		
		scheduler.startup();
		
		try {
			Thread.sleep(10_000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//enumGen.generate(System.out);

		//		
		scheduler.shutdown();
//		
		scheduler.awaitTermination(2, TimeUnit.SECONDS);
		
		
	}
	
	private void buildStreamingTweetsConsumer(GraphManager gm, Pipe<TwitterEventSchema> hosePipe) {
		
		
		//	PipeCleanerStage.newInstance(gm, hosePipe);	
		new TwitterEventDumpStage(gm, hosePipe);		
	//	ConsoleJSONDumpStage.newInstance(gm, hosePipe);
		
		
//		PipeConfig<TwitterEventSchema> hoseFeedPipeConfig = new PipeConfig<TwitterEventSchema>(TwitterEventSchema.instance, 100, 1024);		
//	//	Pipe<TwitterEventSchema> output = new Pipe<TwitterEventSchema>(hoseFeedPipeConfig);		
//		Pipe<TwitterEventSchema>[] results = new Pipe[2];		
//		results[0] = new Pipe<TwitterEventSchema>(hoseFeedPipeConfig);
//		results[1] = new Pipe<TwitterEventSchema>(hoseFeedPipeConfig);	
//		
//		PronghornStage consumeA = new PipeCleanerStage(gm, results[0]);		
//	//	PronghornStage consumeA = new ConsoleJSONDumpStage(gm, results[0]);		
//		PronghornStage consumeB = new ConsoleJSONDumpStage(gm, results[1]);		
//		int field = TwitterEventSchema.MSG_USERPOST_101_FIELD_TEXT_22;
//		TextContentRouterBloom rules;
//		try {
//			rules = new TextContentRouterBloom(
//					                           new ObjectInputStream( GraphExamples.class.getResourceAsStream("/bookWords.dat")),
//					                            0,1);
//			TextContentRouterStage router = new TextContentRouterStage(gm, hosePipe, results, field, rules);
//			
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
	}



	//TODO: also do backchecking for all followers as main loop has time to scrub all history.

	private void bearerRequest(Pipe<ClientHTTPRequestSchema> pipe, String ck, String cs, int port) {
		String bearerTokenCred = ck+':'+cs;
		byte[] btc = bearerTokenCred.getBytes();
		
		StringBuilder builder = new StringBuilder();
		
		//builder.append("Accept-Encoding: gzip\r\n");//Accept: */*\r\n");		
		
		builder.append("Authorization: Basic ");		
		try {
			Appendables.appendBase64(builder, btc, 0, btc.length, Integer.MAX_VALUE);
		} catch (IOException e) {
			e.printStackTrace();
		}
		builder.append("\r\nContent-Type: application/x-www-form-urlencoded;charset=UTF-8\r\n");
		
	
 		//Content-Type: application/x-www-form-urlencoded;charset=UTF-8
		//Authorization: Basic <64 ENCODED bearerTokenCred>
		//Accept-Encoding: gzip
		//User-Agent: My Twitter App v1.0.23
		
		
		int id = 42;
		CharSequence host = "api.twitter.com";
		CharSequence path = "/oauth2/token";
		CharSequence headers = builder.toString();
	
		
		PipeWriter.tryWriteFragment(pipe, ClientHTTPRequestSchema.MSG_HTTPPOST_101);
		PipeWriter.writeInt(pipe, ClientHTTPRequestSchema.MSG_HTTPPOST_101_FIELD_LISTENER_10, id);
		PipeWriter.writeInt(pipe, ClientHTTPRequestSchema.MSG_HTTPPOST_101_FIELD_PORT_1, port);
		PipeWriter.writeUTF8(pipe, ClientHTTPRequestSchema.MSG_HTTPPOST_101_FIELD_HOST_2, host);
		PipeWriter.writeUTF8(pipe, ClientHTTPRequestSchema.MSG_HTTPPOST_101_FIELD_PATH_3, path);
		PipeWriter.writeUTF8(pipe, ClientHTTPRequestSchema.MSG_HTTPPOST_101_FIELD_HEADERS_7, headers);		
		PipeWriter.writeUTF8(pipe, ClientHTTPRequestSchema.MSG_HTTPPOST_101_FIELD_PAYLOAD_5, "grant_type=client_credentials");		
		PipeWriter.publishWrites(pipe);
	}
	

	
	private static String encodeKeys(String consumerKey, String consumerSecret) {
		try {
			String encodedConsumerKey = URLEncoder.encode(consumerKey, "UTF-8");
			String encodedConsumerSecret = URLEncoder.encode(consumerSecret, "UTF-8");
			
			String fullKey = encodedConsumerKey + ":" + encodedConsumerSecret;
			byte[] encodedBytes = Base64.getEncoder().encode(fullKey.getBytes());
			return new String(encodedBytes);  
		}
		catch (UnsupportedEncodingException e) {
			return new String();
		}
	}
	


	
	// Writes a request to a connection
	private static boolean writeRequest(HttpsURLConnection connection, String textBody) {
		try {
			BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
			wr.write(textBody);
			wr.flush();
			wr.close();
				
			return true;
		}
		catch (IOException e) { return false; }
	}
		
		
	// Reads a response for a given connection and returns it as a string.
	private static String readResponse(HttpsURLConnection connection) {
		try {
			StringBuilder str = new StringBuilder();
				
			BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line = "";
			while((line = br.readLine()) != null) {
				str.append(line + System.getProperty("line.separator"));
			}
			return str.toString();
		}
		catch (IOException e) { return new String(); }
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
