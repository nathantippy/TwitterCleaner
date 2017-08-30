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
import com.ociweb.pronghorn.pipe.DataOutputBlobWriter;
import com.ociweb.pronghorn.pipe.Pipe;
import com.ociweb.pronghorn.pipe.PipeWriter;
import com.ociweb.pronghorn.util.Appendables;

public class BearerUtil {

	private static final CharSequence host = "api.twitter.com";
	private static final CharSequence path = "/oauth2/token";
	
	public static void bearerRequest(Pipe<ClientHTTPRequestSchema> pipe, String ck, String cs, int port, int httpRequestResponseId) {
		
		PipeWriter.tryWriteFragment(pipe, ClientHTTPRequestSchema.MSG_HTTPPOST_101);
		PipeWriter.writeInt(pipe, ClientHTTPRequestSchema.MSG_HTTPPOST_101_FIELD_LISTENER_10, httpRequestResponseId);
		PipeWriter.writeInt(pipe, ClientHTTPRequestSchema.MSG_HTTPPOST_101_FIELD_PORT_1, port);
		PipeWriter.writeUTF8(pipe, ClientHTTPRequestSchema.MSG_HTTPPOST_101_FIELD_HOST_2, host);
		PipeWriter.writeUTF8(pipe, ClientHTTPRequestSchema.MSG_HTTPPOST_101_FIELD_PATH_3, path);
		
		//builder.append("Accept-Encoding: gzip\r\n");//Accept: */*\r\n");	
		//
		//Content-Type: application/x-www-form-urlencoded;charset=UTF-8
		//Authorization: Basic <64 ENCODED bearerTokenCred>
		//Accept-Encoding: gzip
		//User-Agent: My Twitter App v1.0.23
		
		DataOutputBlobWriter<ClientHTTPRequestSchema> stream = PipeWriter.outputStream(pipe);
		DataOutputBlobWriter.openField(stream);		
		writeHeaders(ck, cs, stream);
		DataOutputBlobWriter.closeHighLevelField(stream, ClientHTTPRequestSchema.MSG_HTTPPOST_101_FIELD_HEADERS_7);
		
		PipeWriter.writeUTF8(pipe, ClientHTTPRequestSchema.MSG_HTTPPOST_101_FIELD_PAYLOAD_5, "grant_type=client_credentials");		
		PipeWriter.publishWrites(pipe);
	}


	private static void writeHeaders(String ck, String cs, DataOutputBlobWriter<ClientHTTPRequestSchema> stream) {
		byte[] btc = (ck+':'+cs).getBytes();  //TODO: not GC free...
		stream.append("Authorization: Basic ");
		Appendables.appendBase64(stream, btc, 0, btc.length, Integer.MAX_VALUE);
		stream.append("\r\nContent-Type: application/x-www-form-urlencoded;charset=UTF-8\r\n");
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
