package com.ociweb.twitter.stages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ociweb.pronghorn.network.config.HTTPContentType;
import com.ociweb.pronghorn.network.config.HTTPContentTypeDefaults;
import com.ociweb.pronghorn.network.config.HTTPHeader;
import com.ociweb.pronghorn.network.config.HTTPRevision;
import com.ociweb.pronghorn.network.config.HTTPSpecification;
import com.ociweb.pronghorn.network.config.HTTPVerb;
import com.ociweb.pronghorn.network.config.HTTPVerbDefaults;
import com.ociweb.pronghorn.network.module.ByteArrayPayloadResponseStage;
import com.ociweb.pronghorn.network.schema.HTTPRequestSchema;
import com.ociweb.pronghorn.network.schema.ServerResponseSchema;
import com.ociweb.pronghorn.network.schema.TwitterEventSchema;
import com.ociweb.pronghorn.pipe.DataInputBlobReader;
import com.ociweb.pronghorn.pipe.DataOutputBlobWriter;
import com.ociweb.pronghorn.pipe.Pipe;
import com.ociweb.pronghorn.pipe.PipeReader;
import com.ociweb.pronghorn.pipe.PipeWriter;
import com.ociweb.pronghorn.pipe.RawDataSchema;
import com.ociweb.pronghorn.pipe.util.hash.LongHashTable;
import com.ociweb.pronghorn.stage.scheduling.GraphManager;
import com.ociweb.pronghorn.util.Appendables;

public class ListUsersModuleStage<  T extends Enum<T> & HTTPContentType,
									R extends Enum<R> & HTTPRevision,
									V extends Enum<V> & HTTPVerb,
									H extends Enum<H> & HTTPHeader> extends ByteArrayPayloadResponseStage<T,R,V,H> {
	
	private static final Logger logger = LoggerFactory.getLogger(ListUsersModuleStage.class);
	
	private static final int MSG_SIZE = Pipe.sizeOf(RawDataSchema.instance, RawDataSchema.MSG_CHUNKEDSTREAM_1);
	private final Pipe<HTTPRequestSchema>[] inputs; 
	private final Pipe<ServerResponseSchema>[] outputs;
	private final Pipe<TwitterEventSchema>[] twitterEvents; 

	private final Pipe<RawDataSchema>[] resultsBuffer;
	
	private final LongHashTable lookupTable;

	
    
	private byte[] responseHeader = "{\"data\":[".getBytes();
	private byte[] responseTail = "]}".getBytes();
    	
	private int bodyPos = -1;
	private int bodyLen = 0;
	private byte[] bodyBacking = null;
	private int bodyMask = 0;

	private final int maxEntries = 1000; //1000 is an entire days worth
	
	private final int maxJSONTextPerUser = 80; //TODO: check this value for id + name + screen_name
	
	public static ListUsersModuleStage newInstance(GraphManager graphManager, Pipe<HTTPRequestSchema>[] inputPipes,
			Pipe<ServerResponseSchema>[] outputPipes, Pipe<TwitterEventSchema>[] twitterEvents,
			LongHashTable table, HTTPSpecification httpSpec) {
		
		return new ListUsersModuleStage(graphManager, inputPipes, outputPipes, twitterEvents, table,
										httpSpec);
	}

	protected ListUsersModuleStage(GraphManager graphManager, 
			                      Pipe<HTTPRequestSchema>[] inputs, 
			                      Pipe<ServerResponseSchema>[] outputs,
			                      Pipe<TwitterEventSchema>[] twitterEvents, 
			                      LongHashTable table, 
			                      HTTPSpecification httpSpec) {
		super(graphManager,inputs, outputs, httpSpec, twitterEvents);
		this.inputs = inputs;
		this.outputs = outputs;
		this.twitterEvents = twitterEvents;
		this.lookupTable = table;

		int maxClients = inputs.length;

		
		this.resultsBuffer = new Pipe[maxClients];
		int i = maxClients;
		while (--i>=0) {
			this.resultsBuffer[i] = RawDataSchema.instance.newPipe(maxEntries, maxJSONTextPerUser);
			this.resultsBuffer[i].initBuffers();
		}
		
	}

	@Override
	public void run() {
		
		//////////////
		//load the list of values
		/////////////
		int x = twitterEvents.length;
		while (--x>=0) {
			consumeNewValues(twitterEvents[x],x);
		}
		/////////
		
		super.run();
			
	}
				

	private void asNeededCollectNewBody(Pipe<RawDataSchema> buffer) {
		if (bodyPos<0) {
			bodyLen=0;
			
			assert(Pipe.releasePendingCount(buffer)==0) : "found pending count "+Pipe.releasePendingCount(buffer);
			
			if (Pipe.peekMsg(buffer, RawDataSchema.MSG_CHUNKEDSTREAM_1) ) {
				
				int msgIdx = Pipe.takeMsgIdx(buffer);
				assert(RawDataSchema.MSG_CHUNKEDSTREAM_1 == msgIdx);
				int meta = Pipe.takeRingByteMetaData(buffer);
				int len = Pipe.takeRingByteLen(buffer);

				if (len>0) {
					bodyLen = len;
				}
				bodyPos = Pipe.bytePosition(meta, buffer, len);
				bodyBacking = Pipe.byteBackingArray(meta, buffer);
				bodyMask = Pipe.blobMask(buffer);
				
				Pipe.confirmLowLevelRead(buffer, MSG_SIZE);
				Pipe.readNextWithoutReleasingReadLock(buffer);//hold until we write it all.

				//TODO: do not run while more than 3/4 of the max messages..								
				int mask = Pipe.blobMask(buffer);
				while (isSafeToAddWithoutOverlap(buffer, mask) && 
					   Pipe.peekMsg(buffer, RawDataSchema.MSG_CHUNKEDSTREAM_1) ) {
	
					int msgIdx2 = Pipe.takeMsgIdx(buffer);
					assert(RawDataSchema.MSG_CHUNKEDSTREAM_1 == msgIdx2);
					int meta2 = Pipe.takeRingByteMetaData(buffer);
					int len2 = Pipe.takeRingByteLen(buffer);
						
					if (len2>0) {
						bodyLen += len2;
					}
					
					assert(bodyLen <= buffer.sizeOfBlobRing) : "error len "+bodyLen+" is larger than blob "+buffer.sizeOfBlobRing;
					
					Pipe.confirmLowLevelRead(buffer, MSG_SIZE);
					Pipe.readNextWithoutReleasingReadLock(buffer);//hold until we write it all.

				}
			}
		
		}
	}

	private boolean isSafeToAddWithoutOverlap(Pipe<RawDataSchema> buffer, int mask) {
		int mHead = mask&Pipe.getBlobHeadPosition(buffer);
		return (mask&(Pipe.bytesReadBase(buffer)+buffer.maxVarLen)) < mHead;

	}
	
	public boolean consumeNewValues(Pipe<TwitterEventSchema> input, int index) {
		
		while (PipeReader.tryReadFragment(input)) {
		    int msgIdx = PipeReader.getMsgIdx(input);
		    switch(msgIdx) {
		        case TwitterEventSchema.MSG_USER_100:
		        			        	
					accumulateNewData(index, input,  
							TwitterEventSchema.MSG_USER_100_FIELD_FLAGS_31,
							TwitterEventSchema.MSG_USER_100_FIELD_NAME_52,
							TwitterEventSchema.MSG_USER_100_FIELD_SCREENNAME_53
							);

				break;
		        case TwitterEventSchema.MSG_USERPOST_101:
		        	
					accumulateNewData(index, input,  
					     TwitterEventSchema.MSG_USERPOST_101_FIELD_USERID_51,
					     TwitterEventSchema.MSG_USERPOST_101_FIELD_NAME_52,
					     TwitterEventSchema.MSG_USERPOST_101_FIELD_SCREENNAME_53
							);
					
				break;
		        case -1:
		           //requestShutdown();
		        break;
		    }
		    PipeReader.releaseReadLock(input);
		}

		
		return true;
	}

	private void accumulateNewData(int index, Pipe<TwitterEventSchema> input,
						int userIdField, 
			            int nameField,
			            int screenNameField	) {
		
			Pipe<RawDataSchema> buffer = resultsBuffer[index];
			//make room, remove older values if required since new values are more important

			assert(bodyPos<0);
						
			if (!Pipe.hasRoomForWrite(buffer)) {
				//logger.trace("skipped old record in order to keep just the newest values");
				Pipe.skipNextFragment(buffer);
			}
			
			//at this point we MUST have room
			Pipe.presumeRoomForWrite(buffer);
			
			Pipe.addMsgIdx(buffer, RawDataSchema.MSG_CHUNKEDSTREAM_1);
			
			DataOutputBlobWriter<RawDataSchema> target = Pipe.openOutputStream(buffer);
			
			/////////////////////////////
			//add one users data
			/////////////////////////////
				
			long userId = PipeReader.readLong(input, userIdField);
			
			target.append("\n{\"user\":");//new line and beginning of object
			Appendables.appendValue(target, "{\"id\":", userId, "}");
			target.append(",{\"name\":\"");
			PipeReader.readUTF8(input, nameField, target);
			target.append("\"}");
			target.append(",{\"screen_name\":\"");
			PipeReader.readUTF8(input, screenNameField, target);
			target.append("\"}");
			target.append("},");//ending of object with trailing comma 
			
			DataOutputBlobWriter.closeLowLevelField(target);
					
//			//debug/////////////////////////
//			StringBuilder debug = new StringBuilder("User: ");
//			PipeReader.readUTF8(input, nameField, debug);
//			debug.append(",");
//			PipeReader.readUTF8(input, screenNameField, debug);
//			System.out.println(debug);
//			////////////////////////////////
			
			Pipe.confirmLowLevelWrite(buffer, Pipe.sizeOf(RawDataSchema.instance, RawDataSchema.MSG_CHUNKEDSTREAM_1));			
			Pipe.publishWrites(buffer);
	}

	@Override
	protected void appendPrefix(DataOutputBlobWriter<ServerResponseSchema> outputStream) {
		outputStream.write(responseHeader);
	}

	@Override
	protected void appendSuffix(DataOutputBlobWriter<ServerResponseSchema> outputStream) {	
		outputStream.write(responseTail);
	}
	
	@Override
	protected int prefixCount() {
		return responseHeader.length;
	}
	
	@Override
	protected int suffixCount() {
		return responseTail.length;
	}
	
	@Override
	protected byte[] payload( GraphManager gm, 
			                      DataInputBlobReader<HTTPRequestSchema> params,
			                      HTTPVerbDefaults verb) {
		//we only do get.
		if (verb != HTTPVerbDefaults.GET) {
			return null;
		}
		//read this user from the only URL param.
		long userId = DataInputBlobReader.readPackedLong(params);
		//find the pipe for this user
		int index = LongHashTable.getItem(lookupTable, userId);
		
		asNeededCollectNewBody(resultsBuffer[index]);
		
//		//debug/////////////////////////
//		StringBuilder debug = new StringBuilder();
//		Appendables.appendUTF8(debug, bodyBacking, bodyPos, (bodyLen>0?(bodyLen-1):0), bodyMask);
//		System.out.println(debug);
//		////////////////////////////////
		
		try {
			
			definePayload(bodyBacking, bodyPos, (bodyLen>0?(bodyLen-1):0), bodyMask);

		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
			
		//only after it gets written...
		Pipe.releaseAllPendingReadLock(resultsBuffer[index]);
		bodyPos = -1;
		bodyLen = 0;
			
		return null; //never cache this so we return null.
	}

	@Override
	protected byte[] contentType() {
		return HTTPContentTypeDefaults.JSON.getBytes();
	}


}
