package com.ociweb.twitter.stages;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Base64;

import javax.net.ssl.HttpsURLConnection;

import com.ociweb.pronghorn.network.schema.ClientHTTPRequestSchema;
import com.ociweb.pronghorn.pipe.Pipe;
import com.ociweb.pronghorn.pipe.PipeWriter;
import com.ociweb.pronghorn.util.Appendables;

public class BearerUtil {

	

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
	
}
