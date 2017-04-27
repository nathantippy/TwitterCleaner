package com.ociweb.twitter.schema;

import com.ociweb.pronghorn.pipe.FieldReferenceOffsetManager;
import com.ociweb.pronghorn.pipe.MessageSchema;
import com.ociweb.pronghorn.pipe.Pipe;
import com.ociweb.pronghorn.pipe.PipeReader;
import com.ociweb.pronghorn.pipe.PipeWriter;

public class HoseBirdSubscriptionSchema extends MessageSchema<HoseBirdSubscriptionSchema> {

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
	    
    
    private HoseBirdSubscriptionSchema() {
        super(FROM);
    }
    
    
    public static final HoseBirdSubscriptionSchema instance = new HoseBirdSubscriptionSchema();

    
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


	public static void consume(Pipe<HoseBirdSubscriptionSchema> input) {
	    while (PipeReader.tryReadFragment(input)) {
	        int msgIdx = PipeReader.getMsgIdx(input);
	        switch(msgIdx) {
	            case MSG_CLEARUSERS_7:
	                consumeClearUsers(input);
	            break;
	            case MSG_CLEARWORDS_8:
	                consumeClearWords(input);
	            break;
	            case MSG_RECONNECT_9:
	                consumeReconnect(input);
	            break;
	            case MSG_DUMPCREDENTIALS_10:
	                consumeDumpCredentials(input);
	            break;
	            case MSG_ADDWORD_20:
	                consumeAddWord(input);
	            break;
	            case MSG_REMOVEWORD_25:
	                consumeRemoveWord(input);
	            break;
	            case MSG_ADDUSER_30:
	                consumeAddUser(input);
	            break;
	            case MSG_REMOVEUSER_35:
	                consumeRemoveUser(input);
	            break;
	            case -1:
	               //requestShutdown();
	            break;
	        }
	        PipeReader.releaseReadLock(input);
	    }
	}

	public static void consumeClearUsers(Pipe<HoseBirdSubscriptionSchema> input) {
	}
	public static void consumeClearWords(Pipe<HoseBirdSubscriptionSchema> input) {
	}
	public static void consumeReconnect(Pipe<HoseBirdSubscriptionSchema> input) {
	}
	public static void consumeDumpCredentials(Pipe<HoseBirdSubscriptionSchema> input) {
	}
	public static void consumeAddWord(Pipe<HoseBirdSubscriptionSchema> input) {
	    StringBuilder fieldWord = PipeReader.readUTF8(input,MSG_ADDWORD_20_FIELD_WORD_21,new StringBuilder(PipeReader.readBytesLength(input,MSG_ADDWORD_20_FIELD_WORD_21)));
	}
	public static void consumeRemoveWord(Pipe<HoseBirdSubscriptionSchema> input) {
	    StringBuilder fieldWord = PipeReader.readUTF8(input,MSG_REMOVEWORD_25_FIELD_WORD_21,new StringBuilder(PipeReader.readBytesLength(input,MSG_REMOVEWORD_25_FIELD_WORD_21)));
	}
	public static void consumeAddUser(Pipe<HoseBirdSubscriptionSchema> input) {
	    long fieldUser = PipeReader.readLong(input,MSG_ADDUSER_30_FIELD_USER_31);
	}
	public static void consumeRemoveUser(Pipe<HoseBirdSubscriptionSchema> input) {
	    long fieldUser = PipeReader.readLong(input,MSG_REMOVEUSER_35_FIELD_USER_31);
	}

	public static boolean publishClearUsers(Pipe<HoseBirdSubscriptionSchema> output) {
	    boolean result = false;
	    if (PipeWriter.tryWriteFragment(output, MSG_CLEARUSERS_7)) {
	        PipeWriter.publishWrites(output);
	        result = true;
	    }
	    return result;
	}
	public static boolean publishClearWords(Pipe<HoseBirdSubscriptionSchema> output) {
	    boolean result = false;
	    if (PipeWriter.tryWriteFragment(output, MSG_CLEARWORDS_8)) {
	        PipeWriter.publishWrites(output);
	        result = true;
	    }
	    return result;
	}
	public static boolean publishReconnect(Pipe<HoseBirdSubscriptionSchema> output) {
	    boolean result = false;
	    if (PipeWriter.tryWriteFragment(output, MSG_RECONNECT_9)) {
	        PipeWriter.publishWrites(output);
	        result = true;
	    }
	    return result;
	}
	public static boolean publishDumpCredentials(Pipe<HoseBirdSubscriptionSchema> output) {
	    boolean result = false;
	    if (PipeWriter.tryWriteFragment(output, MSG_DUMPCREDENTIALS_10)) {
	        PipeWriter.publishWrites(output);
	        result = true;
	    }
	    return result;
	}
	public static boolean publishAddWord(Pipe<HoseBirdSubscriptionSchema> output, CharSequence fieldWord) {
	    boolean result = false;
	    if (PipeWriter.tryWriteFragment(output, MSG_ADDWORD_20)) {
	        PipeWriter.writeUTF8(output,MSG_ADDWORD_20_FIELD_WORD_21, fieldWord);
	        PipeWriter.publishWrites(output);
	        result = true;
	    }
	    return result;
	}
	public static boolean publishRemoveWord(Pipe<HoseBirdSubscriptionSchema> output, CharSequence fieldWord) {
	    boolean result = false;
	    if (PipeWriter.tryWriteFragment(output, MSG_REMOVEWORD_25)) {
	        PipeWriter.writeUTF8(output,MSG_REMOVEWORD_25_FIELD_WORD_21, fieldWord);
	        PipeWriter.publishWrites(output);
	        result = true;
	    }
	    return result;
	}
	public static boolean publishAddUser(Pipe<HoseBirdSubscriptionSchema> output, long fieldUser) {
	    boolean result = false;
	    if (PipeWriter.tryWriteFragment(output, MSG_ADDUSER_30)) {
	        PipeWriter.writeLong(output,MSG_ADDUSER_30_FIELD_USER_31, fieldUser);
	        PipeWriter.publishWrites(output);
	        result = true;
	    }
	    return result;
	}
	public static boolean publishRemoveUser(Pipe<HoseBirdSubscriptionSchema> output, long fieldUser) {
	    boolean result = false;
	    if (PipeWriter.tryWriteFragment(output, MSG_REMOVEUSER_35)) {
	        PipeWriter.writeLong(output,MSG_REMOVEUSER_35_FIELD_USER_31, fieldUser);
	        PipeWriter.publishWrites(output);
	        result = true;
	    }
	    return result;
	}

}
