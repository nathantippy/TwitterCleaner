package com.ociweb.twitter;

import com.ociweb.gl.api.CommandChannel;
import com.ociweb.pronghorn.pipe.DataInputBlobReader;
import com.ociweb.pronghorn.pipe.Pipe;
import com.ociweb.pronghorn.pipe.PipeReader;
import com.ociweb.pronghorn.stage.PronghornStage;
import com.ociweb.pronghorn.stage.scheduling.GraphManager;
import com.ociweb.twitter.stages.json.TwitterEventSchema;

public class PublishTwitterUsersStage extends PronghornStage {

	private final CommandChannel cc;
	private final String topic; 
	private final Auth a;
	private final Pipe<TwitterEventSchema> input;
	
	protected PublishTwitterUsersStage(GraphManager graphManager, String topicRoot, Auth a, Pipe<TwitterEventSchema> input, CommandChannel cc) {
		
		super(graphManager, input, CommandChannel.getOutputPipes(cc));
		
		this.cc = cc;
		this.topic = topicRoot;//+"/"+a.id; //TODO: not garbage free, fix 
		
		this.a = a;
		this.input = input;
		
	}
	
	@Override
	public void startup() {
		
		
	}

	@Override
	public void run() {
		
				
		if (PipeReader.peekMsg(input, TwitterEventSchema.MSG_USERPOST_101)) {
			//there is no undo of this open so we peek first to ensure we will be needing it.
						
			cc.openTopic(topic).ifPresent((writer) -> {
				boolean ok = PipeReader.tryReadFragment(input);
				assert(ok) : "we just checked this so it should not have failed";
								
				writer.writePackedLong(PipeReader.readLong(input, TwitterEventSchema.MSG_USER_100_FIELD_USERID_51));
				
				DataInputBlobReader<TwitterEventSchema> reader;
				
				//NOTE: name is UTF8 encoded so we just take the bytes directly
				reader = PipeReader.inputStream(input, TwitterEventSchema.MSG_USER_100_FIELD_NAME_52);
				writer.writeShort(reader.available());
				writer.writeStream(reader, reader.available());
				
				//NOTE: screen name is UTF8 encoded so we just take the bytes directly
				reader = PipeReader.inputStream(input, TwitterEventSchema.MSG_USER_100_FIELD_SCREENNAME_53);
				writer.writeShort(reader.available());
				writer.writeStream(reader, reader.available());				
				
				writer.publish();
				
				PipeReader.releaseReadLock(input);				
				
			});
		}
			
	}

	@Override
	public void shutdown() {
		
	}
	
	
	
}
