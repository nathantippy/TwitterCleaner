package com.ociweb.pronghorn.network.schema;

import com.ociweb.pronghorn.pipe.FieldReferenceOffsetManager;
import com.ociweb.pronghorn.pipe.MessageSchema;
import com.ociweb.pronghorn.pipe.Pipe;
import com.ociweb.pronghorn.pipe.PipeReader;
import com.ociweb.pronghorn.pipe.PipeWriter;

public class TwitterStreamControlSchema extends MessageSchema<TwitterStreamControlSchema> {

	public final static FieldReferenceOffsetManager FROM = new FieldReferenceOffsetManager(
		    new int[]{0xc0400001,0xc0200001},
		    (short)0,
		    new String[]{"Reconnect",null},
		    new long[]{100, 0},
		    new String[]{"global",null},
		    "TwitterUserStreamControl.xml", //TODO: remove user from name...
		    new long[]{2, 2, 0},
		    new int[]{2, 2, 0});
	    

    private TwitterStreamControlSchema() {
        super(FROM);
    }
    
    
    public static final TwitterStreamControlSchema instance = new TwitterStreamControlSchema();
    
	public static final int MSG_RECONNECT_100 = 0x00000000;


	public static void consume(Pipe<TwitterStreamControlSchema> input) {
	    while (PipeReader.tryReadFragment(input)) {
	        int msgIdx = PipeReader.getMsgIdx(input);
	        switch(msgIdx) {
	            case MSG_RECONNECT_100:
	                consumeReconnect(input);
	            break;
	            case -1:
	               //requestShutdown();
	            break;
	        }
	        PipeReader.releaseReadLock(input);
	    }
	}

	public static void consumeReconnect(Pipe<TwitterStreamControlSchema> input) {
	}

	public static boolean publishReconnect(Pipe<TwitterStreamControlSchema> output) {
	    boolean result = false;
	    if (PipeWriter.tryWriteFragment(output, MSG_RECONNECT_100)) {
	        PipeWriter.publishWrites(output);
	        result = true;
	    }
	    return result;
	}


}
