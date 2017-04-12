package com.ociweb.twitter.schema;

import com.ociweb.pronghorn.pipe.FieldReferenceOffsetManager;
import com.ociweb.pronghorn.pipe.MessageSchema;

public class HoseBirdSubscriptionSchema extends MessageSchema {

	public final static FieldReferenceOffsetManager FROM = new FieldReferenceOffsetManager(
		    new int[]{0xc0400001,0xc0200001,0xc0400001,0xc0200001,0xc0400001,0xc0200001,0xc0400001,0xc0200001,0xc0400002,0xa8000000,0xc0200002,0xc0400002,0xa8000000,0xc0200002,0xc0400002,0x90000000,0xc0200002,0xc0400002,0x90000000,0xc0200002},
		    (short)0,
		    new String[]{"ClearUsers",null,"ClearWords",null,"Reconnect",null,"DumpCredentials",null,"AddWord",
		    "Word",null,"RemoveWord","Word",null,"AddUser","User",null,"RemoveUser","User",null},
		    new long[]{7, 0, 8, 0, 9, 0, 10, 0, 20, 21, 0, 25, 21, 0, 30, 31, 0, 35, 31, 0},
		    new String[]{"global",null,"global",null,"global",null,"global",null,"global",null,null,"global",
		    null,null,"global",null,null,"global",null,null},
		    "HoseBirdSubscription.xml",
		    new long[]{2, 2, 0},
		    new int[]{2, 2, 0});
	    
	        
	public static final int MSG_CLEARUSERS_7 = 0x00000000;
	public static final int MSG_CLEARWORDS_8 = 0x00000002;
	public static final int MSG_RECONNECT_9 = 0x00000004;
	public static final int MSG_DUMPCREDENTIALS_10 = 0x00000006;
	public static final int MSG_ADDWORD_20 = 0x00000008;
	public static final int MSG_ADDWORD_20_FIELD_WORD_21 = 0x01400001;
	public static final int MSG_REMOVEWORD_25 = 0x0000000b;
	public static final int MSG_REMOVEWORD_25_FIELD_WORD_21 = 0x01400001;
	public static final int MSG_ADDUSER_30 = 0x0000000e;
	public static final int MSG_ADDUSER_30_FIELD_USER_31 = 0x00800001;
	public static final int MSG_REMOVEUSER_35 = 0x00000011;
	public static final int MSG_REMOVEUSER_35_FIELD_USER_31 = 0x00800001;
	    
	    
	    private HoseBirdSubscriptionSchema() {
	        super(FROM);
	    }
	    
	    
	    public static final HoseBirdSubscriptionSchema instance = new HoseBirdSubscriptionSchema();
	
}