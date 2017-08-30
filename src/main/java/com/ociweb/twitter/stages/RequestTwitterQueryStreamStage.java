package com.ociweb.twitter.stages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ociweb.pronghorn.network.schema.ClientHTTPRequestSchema;
import com.ociweb.pronghorn.network.schema.NetResponseSchema;
import com.ociweb.pronghorn.network.schema.TwitterStreamControlSchema;
import com.ociweb.pronghorn.pipe.DataInputBlobReader;
import com.ociweb.pronghorn.pipe.Pipe;
import com.ociweb.pronghorn.pipe.PipeReader;
import com.ociweb.pronghorn.pipe.PipeWriter;
import com.ociweb.pronghorn.stage.PronghornStage;
import com.ociweb.pronghorn.stage.scheduling.GraphManager;
import com.ociweb.pronghorn.util.TrieParserReader;
import com.ociweb.pronghorn.util.parse.JSONParser;

//TODO: the reconnect message can be taken as a notification of consumption so we can start the timer again.
public class RequestTwitterQueryStreamStage extends PronghornStage {

	private static final Logger logger = LoggerFactory.getLogger(RequestTwitterQueryStreamStage.class);
	
	private final Pipe<TwitterStreamControlSchema> streamControlPipe;
	private final Pipe<ClientHTTPRequestSchema> httpRequest;
	private BearerExtractor bearerVisitor;
	private final int httpRequestResponseId;
	private final int bearerRequestResponseId;
	private final int twitterBearerPort;
	private final String consumerKey; 
	private final String consumerSecret;
	private final Pipe<NetResponseSchema> bearerInputPipe;
	
	//NOTE: instead of streaming which leaves the socket open with a few requests comming in slowly we will
	//      connect once every  minute and request the next block to process.
	//      * we get all the data at once for optmized batching
	//      * we can eliminate the need for user auth
	//      * we can add many more than 400 keywords.
	//      * updates are slowed for 400 to once every 40 minutes.
	
	
	//https://api.twitter.com/1.1/search/tweets.json?q=%40twitterapi since:2015-12-21
	//                                                 "java",""c++" since:2017-03-20
	
	// https://dev.twitter.com/rest/public/search
	
	public RequestTwitterQueryStreamStage(GraphManager gm,
			String consumerKey, String consumerSecret, 
			int httpRequestResponseId, int bearerRequestResponseId, 			
			Pipe<TwitterStreamControlSchema> streamControlPipe, //in
			Pipe<NetResponseSchema> bearerInputPipe,
			Pipe<ClientHTTPRequestSchema> httpRequest //out
			) {
		
		super(gm, streamControlPipe, httpRequest);
		
		this.streamControlPipe = streamControlPipe;
		this.httpRequest = httpRequest;
	    this.httpRequestResponseId = httpRequestResponseId;
	    this.bearerRequestResponseId = bearerRequestResponseId;
	    this.twitterBearerPort = 443;
	    this.consumerKey = consumerKey;
	    this.consumerSecret = consumerSecret;
		this.bearerInputPipe = bearerInputPipe;
		
	}
	
	@Override
	public void startup() {
		//who does this parse??
		BearerExtractor bearerVisitor = new BearerExtractor();
		
		//TODO: different it to route this elsewhere.
		BearerUtil.bearerRequest(httpRequest, consumerKey, consumerSecret, twitterBearerPort, httpRequestResponseId);
		
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
//	CharSequence headers = "Authorization: Bearer "+bearerVisitor.getBearer()+"\r\n";
//	
//	
//	
//	
////		CharSequence host = "sitestream.twitter.com";
////	CharSequence path = "/1.1/site.json?"+payloadBuilder.toString();
//	
//	
//	//https://api.twitter.com/1.1/search/tweets.json?q=%40twitterapi since:2015-12-21
//	//                                                 "java",""c++" since:2017-03-20
//	
//	
//	//TODO: add support for all query mechanisms https://dev.twitter.com/rest/public/search
//	//      or them together so we can gather all the data at once
//	//      query contains 6-9 days of tweets
//	//      quer should only have 10 keywords
//	//      prebuild the entire query so it can be locked down with position and date also this allows for exclusion of words and tracking of bad players
//	//      we know that running 1 per minute what the full cycle time is so we can report the latency.
//	
//	//      also use since_id keep the max id found for the last time we made thate query....??
//	
//	CharSequence path ="1.1/search/tweets.json?q=\"java\",\"c++\"&result_type=recent%40since:2017-03-20";
//	CharSequence host ="api.twitter.com";
//			
//			
//	PipeWriter.tryWriteFragment(requestSubscriptionPipe, ClientHTTPRequestSchema.MSG_HTTPGET_100);
//	PipeWriter.writeInt(requestSubscriptionPipe, ClientHTTPRequestSchema.MSG_HTTPGET_100_FIELD_LISTENER_10, id);
//	PipeWriter.writeInt(requestSubscriptionPipe, ClientHTTPRequestSchema.MSG_HTTPGET_100_FIELD_PORT_1, port);
//	PipeWriter.writeUTF8(requestSubscriptionPipe, ClientHTTPRequestSchema.MSG_HTTPGET_100_FIELD_HOST_2, host);
//	PipeWriter.writeUTF8(requestSubscriptionPipe, ClientHTTPRequestSchema.MSG_HTTPGET_100_FIELD_PATH_3, path);
//	PipeWriter.writeUTF8(requestSubscriptionPipe, ClientHTTPRequestSchema.MSG_HTTPGET_100_FIELD_HEADERS_7, headers);
//			
//	PipeWriter.publishWrites(requestSubscriptionPipe);
	
	
	//TODO: check new OAuth2ParserStage?? TODO: upon reconnect may need new bearer request..
	
	
	private void consumeAnyNewBearer() {
		while (PipeReader.tryReadFragment(bearerInputPipe)) {
			
			if (PipeReader.getMsgIdx(bearerInputPipe)==NetResponseSchema.MSG_RESPONSE_101) {
				
				long con = PipeReader.readLong(bearerInputPipe, NetResponseSchema.MSG_RESPONSE_101_FIELD_CONNECTIONID_1);
				int flags = PipeReader.readInt(bearerInputPipe, NetResponseSchema.MSG_RESPONSE_101_FIELD_CONTEXTFLAGS_5);
				
				DataInputBlobReader<NetResponseSchema> stream = PipeReader.inputStream(bearerInputPipe,  NetResponseSchema.MSG_RESPONSE_101_FIELD_PAYLOAD_3);		

				short statusCode =stream.readShort();
				
				if (200!=statusCode) {
					logger.info("error got code:{}",statusCode);
					PipeReader.releaseReadLock(bearerInputPipe);
					continue;
				}
				
				int headerId = stream.readShort();
				
				while (-1 != headerId) { //end of headers will be marked with -1 value
					//determine the type
					
					int headerValue = stream.readShort();
					//NOTE: we could confirm this is the right type and that heeaderId is content type.
					
					//read next
					headerId = stream.readShort();
					
				}
				TrieParserReader jsonReader = JSONParser.newReader();

				JSONParser.parse(stream, jsonReader, bearerVisitor);
				
				
				
			} else {
				System.out.println("unknown "+PipeReader.getMsgIdx(bearerInputPipe));
			}
			
			PipeReader.releaseReadLock(bearerInputPipe);
		};
	}
	
}
