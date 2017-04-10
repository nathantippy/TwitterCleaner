package com.ociweb.twitter.stages;

import com.ociweb.pronghorn.network.schema.ClientHTTPRequestSchema;
import com.ociweb.pronghorn.pipe.Pipe;
import com.ociweb.pronghorn.pipe.PipeReader;
import com.ociweb.pronghorn.pipe.PipeWriter;
import com.ociweb.pronghorn.stage.PronghornStage;
import com.ociweb.pronghorn.stage.scheduling.GraphManager;
import com.ociweb.twitter.MyAuth;
import com.ociweb.twitter.TwitterStreamControlSchema;

public class RequestTwitterUserStreamStage extends PronghornStage {

	private final Pipe<ClientHTTPRequestSchema> output;
	private final Pipe<TwitterStreamControlSchema> control;
	
	private final String ck;
	private final String cs;
	private final String token;
	private final String secret;
	private final int id;	
	
	public RequestTwitterUserStreamStage(GraphManager graphManager, 
											String ck, String cs, String token, String secret, int id,
											Pipe<TwitterStreamControlSchema> control,
			                                Pipe<ClientHTTPRequestSchema> output) {
		super(graphManager, control, output);
		this.control = control;
		this.output = output;
		
		this.ck = ck;
		this.cs = cs;
		this.token = token;
		this.secret = secret;
		this.id = id;
		
	}

	
	@Override
	public void startup() {
		
		streamingRequest(output, ck, cs, token, secret, id);
		
	}
	
	@Override
	public void run() {
		
		//TODO: (short easy task) twitter wants a growing back-off here, we must record the last time we connected and DO NOT read until suffecient time has passed.
		
		while (PipeReader.tryReadFragment(control)) {
			
			int id = PipeReader.getMsgIdx(control);
			switch (id) {
				case -1:
					requestShutdown();
					return;				
				default:
					streamingRequest(output, ck, cs, token, secret, id);
			}
			
			PipeReader.releaseReadLock(control);
			
		}

	}
	
	@Override
	public void shutdown() {
		
		
	}

	public static void streamingRequest(Pipe<ClientHTTPRequestSchema> pipe, String ck, String cs, String token, String secret, int id) {
		MyAuth myAuth = new MyAuth(ck, cs, token, secret);
		
		int port = 443;
		
		String host = "userstream.twitter.com";// api.twitter.com";
		
		StringBuilder authHeader = new StringBuilder();
		String rawQuery = "stall_warnings=true&with=followings";
		String path2 = "/1.1/user.json";
		myAuth.addHeaders(authHeader, rawQuery, port, "https", "GET", host, path2);
	
		//TODO: must run for 10K selection multiple times and see if its a different segment, if so this is fine....
		
		
		String path = path2+"?"+rawQuery;///"/1.1/user.json?stall_warnings=true&with=followings";//"/oauth/request_token";
		// /sitestream.twitter.com/1.1/site.json?follow=6253282 
		
		CharSequence headers = authHeader.toString()+"\r\n"; 
	
		PipeWriter.tryWriteFragment(pipe, ClientHTTPRequestSchema.MSG_HTTPGET_100);
		PipeWriter.writeInt(pipe, ClientHTTPRequestSchema.MSG_HTTPGET_100_FIELD_LISTENER_10, id);
		PipeWriter.writeInt(pipe, ClientHTTPRequestSchema.MSG_HTTPGET_100_FIELD_PORT_1, port);
		PipeWriter.writeUTF8(pipe, ClientHTTPRequestSchema.MSG_HTTPGET_100_FIELD_HOST_2, host);
		PipeWriter.writeUTF8(pipe, ClientHTTPRequestSchema.MSG_HTTPGET_100_FIELD_PATH_3, path);
		PipeWriter.writeUTF8(pipe, ClientHTTPRequestSchema.MSG_HTTPGET_100_FIELD_HEADERS_7, headers);			
		PipeWriter.publishWrites(pipe);
	}

}
