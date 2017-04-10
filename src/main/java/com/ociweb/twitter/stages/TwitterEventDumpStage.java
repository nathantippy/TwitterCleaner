package com.ociweb.twitter.stages;

import com.ociweb.pronghorn.pipe.Pipe;
import com.ociweb.pronghorn.pipe.PipeReader;
import com.ociweb.pronghorn.stage.PronghornStage;
import com.ociweb.pronghorn.stage.scheduling.GraphManager;
import com.ociweb.twitter.stages.json.TwitterEventSchema;

public class TwitterEventDumpStage extends PronghornStage {

	private final Pipe<TwitterEventSchema> input;
	
	public TwitterEventDumpStage(GraphManager graphManager, Pipe<TwitterEventSchema> input) {
		super(graphManager, input, NONE);
		this.input = input;
	}

	@Override
	public void run() {
		
		while (PipeReader.tryReadFragment(input)) {
		
			System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
			//int pos = Pipe.getWorkingBlobHeadPosition(input)
			long id = PipeReader.readLong(input, TwitterEventSchema.MSG_USERPOST_101_FIELD_USERID_51);
			System.out.println("id:"+id);
			
			PipeReader.readUTF8(input, TwitterEventSchema.MSG_USERPOST_101_FIELD_NAME_52, System.out);
			System.out.println();
			PipeReader.readUTF8(input, TwitterEventSchema.MSG_USERPOST_101_FIELD_SCREENNAME_53, System.out);
			System.out.println();			
			
			PipeReader.readUTF8(input, TwitterEventSchema.MSG_USERPOST_101_FIELD_CREATEDAT_57, System.out);
			System.out.println();
			
			PipeReader.readUTF8(input, TwitterEventSchema.MSG_USERPOST_101_FIELD_DESCRIPTION_58, System.out);
			System.out.println();
			PipeReader.readUTF8(input, TwitterEventSchema.MSG_USERPOST_101_FIELD_LANGUAGE_60, System.out);
			System.out.println();
			PipeReader.readUTF8(input, TwitterEventSchema.MSG_USERPOST_101_FIELD_TIMEZONE_61, System.out);
			System.out.println();
			PipeReader.readUTF8(input, TwitterEventSchema.MSG_USERPOST_101_FIELD_LOCATION_62, System.out);
			System.out.println();
			PipeReader.readUTF8(input, TwitterEventSchema.MSG_USERPOST_101_FIELD_TEXT_22, System.out);
			System.out.println();
			
			PipeReader.releaseReadLock(input);
		}
	}

}
