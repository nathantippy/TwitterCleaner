package com.ociweb.twitter.schema;

import com.ociweb.pronghorn.pipe.FieldReferenceOffsetManager;
import com.ociweb.pronghorn.pipe.MessageSchema;

public class TwitterStreamControlSchema extends MessageSchema {

	public final static FieldReferenceOffsetManager FROM = new FieldReferenceOffsetManager(
		    new int[]{0xc0400001,0xc0200001},
		    (short)0,
		    new String[]{"Reconnect",null},
		    new long[]{100, 0},
		    new String[]{"global",null},
		    "TwitterUserStreamControl.xml", //TODO: remove user from name...
		    new long[]{2, 2, 0},
		    new int[]{2, 2, 0});
	    
	        
	public static final int MSG_RECONNECT_100 = 0x00000000;
	    
	    
	    private TwitterStreamControlSchema() {
	        super(FROM);
	    }
	    
	    
	    public static final TwitterStreamControlSchema instance = new TwitterStreamControlSchema();
	
}
