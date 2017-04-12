package com.ociweb.twitter;

import java.util.Optional;

import com.ociweb.gl.api.CommandChannel;
import com.ociweb.gl.api.GreenRuntime;
import com.ociweb.gl.api.NetResponseWriter;
import com.ociweb.gl.api.PayloadReader;
import com.ociweb.gl.api.PubSubListener;
import com.ociweb.gl.api.RestListener;
import com.ociweb.pronghorn.network.config.HTTPContentTypeDefaults;
import com.ociweb.pronghorn.network.config.HTTPVerb;
import com.ociweb.pronghorn.pipe.DataInputBlobReader;
import com.ociweb.pronghorn.pipe.DataOutputBlobWriter;
import com.ociweb.pronghorn.pipe.Pipe;
import com.ociweb.pronghorn.pipe.PipeReader;
import com.ociweb.pronghorn.pipe.PipeWriter;
import com.ociweb.pronghorn.pipe.RawDataSchema;
import com.ociweb.pronghorn.pipe.util.hash.LongHashTable;
import com.ociweb.pronghorn.util.Appendables;

public class ExampleREST implements RestListener, PubSubListener {

	private final CommandChannel cc;
	private final Pipe<RawDataSchema>[] resultsBuffer;
	
	private final LongHashTable lookupTable;
	private int clientIdCounter;
    
	public ExampleREST(final GreenRuntime runtime, int maxClients) {
		
		this.cc = runtime.newCommandChannel(CommandChannel.NET_RESPONDER);
       
		//TODO: by adding exclusive topics we can communnicated pont to point 
		//runtime.setExclusiveTopics(cc,"myTopic","myOtherTopic");
		
		this.lookupTable = new LongHashTable( 1+LongHashTable.computeBits(maxClients) );
				
		int maxEntries = 1000; //1000 is an entire days worth
		
		int maxJSONTextPerUser = 80; //TODO: check this value for id + name + screen_name
		
		this.resultsBuffer = new Pipe[maxClients];
		int i = maxClients;
		while (--i>=0) {
			this.resultsBuffer[i] = RawDataSchema.instance.newPipe(maxEntries, maxJSONTextPerUser);
			this.resultsBuffer[i].initBuffers();
		}
	}

	@Override
	public boolean restRequest(int routeId, long connectionId, long sequenceCode, HTTPVerb verb, final PayloadReader request) {
		
		int statusCode = 200;
		HTTPContentTypeDefaults contentType = HTTPContentTypeDefaults.JSON;
		int context = END_OF_RESPONSE;
		
		long user = request.readPackedLong(); //TODO: confirm that the capture logic returns the values in this format.
		byte zero = request.readByte();
		assert(0 == zero);
		
		
		Pipe<RawDataSchema> buffer = resultsBuffer[lookupPipeIdx(user)];
		
				
		int length = -1;		
		Optional<NetResponseWriter> writer = cc.openHTTPResponse(connectionId, sequenceCode, statusCode, context, contentType, length);
		 
		writer.ifPresent((outputStream)->{
			 
			outputStream.writeUTF8Text("{\"data\":");
			
			//TODO: check pipe length
			int max = 4;
			while (--max>=0 && PipeReader.tryReadFragment(buffer)) 	{
				
					DataInputBlobReader<RawDataSchema> input = PipeReader.inputStream(buffer, RawDataSchema.MSG_CHUNKEDSTREAM_1_FIELD_BYTEARRAY_2);				
					outputStream.writeStream(input, input.available());
					
					PipeReader.releaseReadLock(buffer);
					
			}
			
			outputStream.writeUTF8Text("}");	
	
			outputStream.publish(); 
		
		 } );
		
		
		
		return writer.isPresent();
	}

	@Override
	public boolean message(CharSequence topic, PayloadReader payload) {

		
		System.err.println("got new message: "+topic);
		
		
		//CharSequence[] topicLevels = Appendables.split(topic, '/');		
		//0 = major topic (follow or unflollow)
		//1 = twitter clientId long numeric
		long clientId = 0;//TopicUtil.extractLong(topic,1);
		Pipe<RawDataSchema> buffer = resultsBuffer[lookupPipeIdx(clientId)];
		
		while (payload.hasRemainingBytes()) {
			
			//make room, remove older values if required since new values are more important
			while (!PipeWriter.hasRoomForWrite(buffer)) {
				PipeReader.tryReadFragment(buffer);
				PipeReader.releaseReadLock(buffer);
			}
			
			//at this point we MUST have room
			boolean ok = PipeWriter.tryWriteFragment(buffer, RawDataSchema.MSG_CHUNKEDSTREAM_1);
			assert(ok) : "should not happen since we just made rome above this point";
				
			
			DataOutputBlobWriter<RawDataSchema> target = PipeWriter.outputStream(buffer);
			DataOutputBlobWriter.openField(target);
			/////////////////////////////
			//add one users data
			/////////////////////////////
				
			long userId = payload.readPackedLong();
			
			target.append("\n{\"user\":");//new line and beginning of object
			
			Appendables.appendValue(target, "{\"id\":", userId, "}");			
			
			target.append(",{\"name\":\"");
			payload.readUTF(target);
			target.append("\"}");
			
			target.append(",{\"screen_name\":\"");
			payload.readUTF(target);
			target.append("\"}");			
						
			target.append("},");//ending of object with trailing comma 
			
			DataOutputBlobWriter.closeHighLevelField(target, RawDataSchema.MSG_CHUNKEDSTREAM_1_FIELD_BYTEARRAY_2);
			
			PipeWriter.publishWrites(buffer);
		}

		
		return true;
	}

	private int lookupPipeIdx(long userId) {
		
		//System.err.println("lookup user "+userId);
		
		return 0;
//		if (LongHashTable.hasItem(lookupTable, userId)) {
//			return LongHashTable.getItem(lookupTable, userId);
//		} else {
//			int newValue = clientIdCounter++;			
//			LongHashTable.setItem(lookupTable, userId, newValue);
//			return newValue;
//		}
		
	}

}
