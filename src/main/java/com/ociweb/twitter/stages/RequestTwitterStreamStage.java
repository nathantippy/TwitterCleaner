package com.ociweb.twitter.stages;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ociweb.pronghorn.network.schema.ClientHTTPRequestSchema;
import com.ociweb.pronghorn.network.schema.NetResponseSchema;
import com.ociweb.pronghorn.pipe.DataInputBlobReader;
import com.ociweb.pronghorn.pipe.Pipe;
import com.ociweb.pronghorn.pipe.PipeReader;
import com.ociweb.pronghorn.pipe.PipeWriter;
import com.ociweb.pronghorn.stage.PronghornStage;
import com.ociweb.pronghorn.stage.scheduling.GraphManager;
import com.ociweb.pronghorn.util.Appendables;
import com.ociweb.pronghorn.util.TrieParserReader;
import com.ociweb.pronghorn.util.parse.JSONParser;
import com.ociweb.twitter.HoseBirdSubscriptionSchema;

public class RequestTwitterStreamStage extends PronghornStage {

	private final Logger logger = LoggerFactory.getLogger(RequestTwitterStreamStage.class);
	
	private final Pipe<NetResponseSchema> bearerInputPipe;
	private final Pipe<HoseBirdSubscriptionSchema> subscriptionRequest;
	private final Pipe<ClientHTTPRequestSchema> requestSubscriptionPipe;
	
	private final String filePath;
	private File primaryFile;
	private File backupFile;
	
	private BearerExtractor bearerVisitor;
	private boolean isDirty = false;
	private List<Long> users;
	private List<String> words;
	
	private StringBuilder workspace;
	
	
	public RequestTwitterStreamStage(GraphManager graphManager, 
				                    Pipe<NetResponseSchema> bearerInputPipe, 
				                    Pipe<HoseBirdSubscriptionSchema> subscriptionRequest,
				                    Pipe<ClientHTTPRequestSchema> requestSubscriptionPipe, 
				                    String filePath) {
		
		super(graphManager, join(bearerInputPipe, subscriptionRequest), requestSubscriptionPipe);
		this.bearerInputPipe = bearerInputPipe;
		this.subscriptionRequest = subscriptionRequest;
		this.requestSubscriptionPipe = requestSubscriptionPipe;
		this.filePath = filePath;
		
	}

	@Override
	public void startup() {
		bearerVisitor = new BearerExtractor();
		users = new ArrayList<Long>();
		words = new ArrayList<String>();
		workspace = new StringBuilder();
		
		///load old file		
		
		primaryFile = new File(filePath);
		backupFile = new File(filePath+".bak");
		
		if (primaryFile.exists()) {
			try {
				load(primaryFile);
			} catch (IOException ex) {
				if (backupFile.exists()) {
					try {					
						load(backupFile);
						isDirty = true;		
					} catch (IOException ex2) {					
						logger.warn("unable to load data ",ex2);						
					}
				}
			}			
		}
		
	}
	
	private void load(File file) throws IOException {
		
		try {
			ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file));			
			users =	(List<Long>) inputStream.readObject();
			words = (List<String>) inputStream.readObject();
			inputStream.close();
			isDirty = false;			
		} catch (IOException | ClassNotFoundException ex) {
			logger.info("unable to load file {} ",file,ex);			
		}		
		
	}
	
	private void saveAsNeeded() {
		if (isDirty) {
			
			//backup copy 
			if (primaryFile.exists()) {
				if (backupFile.exists()) {
					if (backupFile.delete()) {
						primaryFile.renameTo(backupFile);
					}
				}
				primaryFile.delete();
			}
			//save file
			
			try {
				
				ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(primaryFile));
			    outputStream.writeObject(users);
			    outputStream.writeObject(words);
			    outputStream.close();
				isDirty = false;
			    
			} catch (IOException ex) {
				//unable to save			    
				logger.info("unable to save to {} will try again",primaryFile,ex);
			}
						
			//NOTE: should send the long tail position when saved for anyone who cares...
			
			
		}
	}

	@Override
	public void run() {
		
		do {
			consumeAnyNewBearer();
					
			if (PipeReader.peekMsg(subscriptionRequest, HoseBirdSubscriptionSchema.MSG_RECONNECT_9) && !bearerVisitor.hasBearer()) {
				//wait we need the bearer to do this next operation.
				break;
			}
			
			if (PipeReader.tryReadFragment(subscriptionRequest)) {
				int msgIdx = PipeReader.getMsgIdx(subscriptionRequest);
				switch (msgIdx) {
					
				    case HoseBirdSubscriptionSchema.MSG_ADDUSER_30:				    	
				    	Long user = PipeReader.readLong(subscriptionRequest, HoseBirdSubscriptionSchema.MSG_ADDUSER_30_FIELD_USER_31);
				    	if (!users.contains(user)) {
				    		System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXx  added new user"+user);
				    		users.add(user);
				    	}
				    	isDirty = true;
					break;
					
					case HoseBirdSubscriptionSchema.MSG_ADDWORD_20:
						workspace.setLength(0);
						String word = PipeReader.readUTF8(subscriptionRequest, HoseBirdSubscriptionSchema.MSG_ADDWORD_20_FIELD_WORD_21, workspace).toString();
				    	if (!words.contains(word)) {
				    		words.add(word);
				    	}
				    	isDirty = true;
				    break;
					
					case HoseBirdSubscriptionSchema.MSG_CLEARUSERS_7:
						users.clear();
						isDirty = true;
					break;
					
					case HoseBirdSubscriptionSchema.MSG_CLEARWORDS_8:
						words.clear();
						isDirty = true;
					break;
					
					case HoseBirdSubscriptionSchema.MSG_DUMPCREDENTIALS_10:
						bearerVisitor.reset();
					break;
					
					case HoseBirdSubscriptionSchema.MSG_RECONNECT_9:
						
						connect();
						
					break;
					
					case HoseBirdSubscriptionSchema.MSG_REMOVEUSER_35:
						Long removeUser = PipeReader.readLong(subscriptionRequest, HoseBirdSubscriptionSchema.MSG_REMOVEUSER_35_FIELD_USER_31);						
						users.remove(removeUser);
						isDirty = true;						
					break;
					
					case HoseBirdSubscriptionSchema.MSG_REMOVEWORD_25:
						workspace.setLength(0);
						String removeWord = PipeReader.readUTF8(subscriptionRequest, HoseBirdSubscriptionSchema.MSG_ADDWORD_20_FIELD_WORD_21, workspace).toString();
						
						
						words.remove(removeWord);
						isDirty = true;
												
					break;
				
					default:
						logger.info("unknown message type");
				}
				
				PipeReader.releaseReadLock(subscriptionRequest);
				
			}
	 		
			
		} while (PipeReader.hasContentToRead(subscriptionRequest));
		
		saveAsNeeded();
	}


	private void connect() {
		// TODO Auto-generated method stub
	
		
		//simple get with track words
		//        https://stream.twitter.com/1.1/statuses/filter.json?track=twitter   //post??
		
		
		//        https://sitestream.twitter.com/1.1/site.json?follow=6253282     //get??
		
		
		//NOTE: instead of streaming which leaves the socket open with a few requests comming in slowly we will
		//      connect once every  minute and request the next block to process.
		//      * we get all the data at once for optmized batching
		//      * we can eliminate the need for user auth
		//      * we can add many more than 400 keywords.
		//      * updates are slowed for 400 to once every 40 minutes.
		
		
		//https://api.twitter.com/1.1/search/tweets.json?q=%40twitterapi since:2015-12-21
		//                                                 "java",""c++" since:2017-03-20
		
		// https://dev.twitter.com/rest/public/search
		
		System.err.println(words.size() +"  "+users.size()+"  counts");
		
		if (false && words.size()>0) {
			int id = 43;
			int port = 443;
			//CharSequence host = "stream.twitter.com";
			
			StringBuilder payloadBuilder = new StringBuilder();
			payloadBuilder.append("track=");
			int i = words.size();
			while (--i>=0) {
				payloadBuilder.append(words.get(i));
				if (i>0) {
					payloadBuilder.append(',');
				}
			}
			
			
			//payloadBuilder.append(words.toString().replaceAll("[", "").replaceAll("]", ""));
			System.err.println(payloadBuilder);
			
			
			CharSequence headers = "Authorization: Bearer "+bearerVisitor.getBearer()+"\r\n";
			
			//CharSequence path = "/1.1/statuses/filter.json";
			//CharSequence payload = payloadBuilder.toString();
		
			
			CharSequence path ="1.1/statuses/user_timeline.json";
			CharSequence host ="api.twitter.com";
			CharSequence payload = "count=1&screen_name=lgladdy";
			
			PipeWriter.tryWriteFragment(requestSubscriptionPipe, ClientHTTPRequestSchema.MSG_HTTPPOST_101);
			PipeWriter.writeInt(requestSubscriptionPipe, ClientHTTPRequestSchema.MSG_HTTPPOST_101_FIELD_LISTENER_10, id);
			PipeWriter.writeInt(requestSubscriptionPipe, ClientHTTPRequestSchema.MSG_HTTPPOST_101_FIELD_PORT_1, port);
			PipeWriter.writeUTF8(requestSubscriptionPipe, ClientHTTPRequestSchema.MSG_HTTPPOST_101_FIELD_HOST_2, host);
			PipeWriter.writeUTF8(requestSubscriptionPipe, ClientHTTPRequestSchema.MSG_HTTPPOST_101_FIELD_PATH_3, path);
			PipeWriter.writeUTF8(requestSubscriptionPipe, ClientHTTPRequestSchema.MSG_HTTPPOST_101_FIELD_HEADERS_7, headers);		
			PipeWriter.writeUTF8(requestSubscriptionPipe, ClientHTTPRequestSchema.MSG_HTTPPOST_101_FIELD_PAYLOAD_5, payload);		
			PipeWriter.publishWrites(requestSubscriptionPipe);
		}
		
		//TODO: modify this class to only support app only api gets, no user streams can be supported.
		
		
		if (false && users.size()>0) {
			
			int id = 44;
			int port = 443;
			
			StringBuilder payloadBuilder = new StringBuilder();
			payloadBuilder.append("follow=");		
			int i = users.size();
			while (--i>=0) {
				
				Appendables.appendValue(payloadBuilder, users.get(i));
				if (i>0) {
					payloadBuilder.append(',');
				}
				
			}
			
			//payloadBuilder.append(users.toString().replaceAll("[", "").replaceAll("]", ""));
			System.err.println(payloadBuilder);
			
			
			CharSequence headers = "Authorization: Bearer "+bearerVisitor.getBearer()+"\r\n";
			
			
			
			
		//		CharSequence host = "sitestream.twitter.com";
		//	CharSequence path = "/1.1/site.json?"+payloadBuilder.toString();
			
			
			//https://api.twitter.com/1.1/search/tweets.json?q=%40twitterapi since:2015-12-21
			//                                                 "java",""c++" since:2017-03-20
			
			
			//TODO: add support for all query mechanisms https://dev.twitter.com/rest/public/search
			//      or them together so we can gather all the data at once
			//      query contains 6-9 days of tweets
			//      quer should only have 10 keywords
			//      prebuild the entire query so it can be locked down with position and date also this allows for exclusion of words and tracking of bad players
			//      we know that running 1 per minute what the full cycle time is so we can report the latency.
			
			//      also use since_id keep the max id found for the last time we made thate query....??
			
			CharSequence path ="1.1/search/tweets.json?q=\"java\",\"c++\"&result_type=recent%40since:2017-03-20";
			CharSequence host ="api.twitter.com";
					
					
			PipeWriter.tryWriteFragment(requestSubscriptionPipe, ClientHTTPRequestSchema.MSG_HTTPGET_100);
			PipeWriter.writeInt(requestSubscriptionPipe, ClientHTTPRequestSchema.MSG_HTTPGET_100_FIELD_LISTENER_10, id);
			PipeWriter.writeInt(requestSubscriptionPipe, ClientHTTPRequestSchema.MSG_HTTPGET_100_FIELD_PORT_1, port);
			PipeWriter.writeUTF8(requestSubscriptionPipe, ClientHTTPRequestSchema.MSG_HTTPGET_100_FIELD_HOST_2, host);
			PipeWriter.writeUTF8(requestSubscriptionPipe, ClientHTTPRequestSchema.MSG_HTTPGET_100_FIELD_PATH_3, path);
			PipeWriter.writeUTF8(requestSubscriptionPipe, ClientHTTPRequestSchema.MSG_HTTPGET_100_FIELD_HEADERS_7, headers);
					
			PipeWriter.publishWrites(requestSubscriptionPipe);
						
			
		}
		
		
		
		
	}

	
	private void consumeAnyNewBearer() {
		while (PipeReader.tryReadFragment(bearerInputPipe)) {
			
			if (PipeReader.getMsgIdx(bearerInputPipe)==NetResponseSchema.MSG_RESPONSE_101) {
				
				long con = PipeReader.readLong(bearerInputPipe, NetResponseSchema.MSG_RESPONSE_101_FIELD_CONNECTIONID_1);
				
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
