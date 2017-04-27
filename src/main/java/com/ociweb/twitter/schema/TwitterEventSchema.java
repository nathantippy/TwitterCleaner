package com.ociweb.twitter.schema;

import com.ociweb.pronghorn.pipe.FieldReferenceOffsetManager;
import com.ociweb.pronghorn.pipe.MessageSchema;
import com.ociweb.pronghorn.pipe.Pipe;
import com.ociweb.pronghorn.pipe.PipeReader;
import com.ociweb.pronghorn.pipe.PipeWriter;

public class TwitterEventSchema extends MessageSchema{

	public final static FieldReferenceOffsetManager FROM = new FieldReferenceOffsetManager(
		    new int[]{0xc040000e,0x80000000,0x90000000,0xac000000,0xac000001,0x80000001,0x80000002,0x80000003,0xac000002,0xac000003,0x80000004,0xac000004,0xac000005,0xac000006,0xc020000e,0xc0400010,0x80000000,0x90000000,0xac000000,0xac000001,0x80000001,0x80000002,0x80000003,0xac000002,0xac000003,0x80000004,0xac000004,0xac000005,0xac000006,0x90000001,0xac000007,0xc0200010},
		    (short)0,
		    new String[]{"User","Flags","UserId","Name","ScreenName","FavouritesCount","FollowersCount","FriendsCount",
		    "CreatedAt","Description","ListedCount","Language","TimeZone","Location",null,"UserPost",
		    "Flags","UserId","Name","ScreenName","FavouritesCount","FollowersCount","FriendsCount",
		    "CreatedAt","Description","ListedCount","Language","TimeZone","Location","PostId",
		    "Text",null},
		    new long[]{100, 31, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 0, 101, 31, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 21, 22, 0},
		    new String[]{"global",null,null,null,null,null,null,null,null,null,null,null,null,null,null,"global",
		    null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null},
		    "TwitterEvent.xml",
		    new long[]{2, 2, 0},
		    new int[]{2, 2, 0});

    private TwitterEventSchema() {
        super(FROM);
    }
    
    
    public static final TwitterEventSchema instance = new TwitterEventSchema();
    
	
    public static final int FLAG_POSSIBLY_SENSITIVE       = 0b00000000_00000001;
    public static final int FLAG_FAVORITED                = 0b00000000_00000010;
  //  public static final int FLAG_RETWEET                  = 0b00000000_00000100;
    public static final int FLAG_RETWEETED                = 0b00000000_00001000;
  //  public static final int FLAG_RETWEETED_BY_ME          = 0b00000000_00010000;
    public static final int FLAG_TRUNCATED                = 0b00000000_00100000;
    
    public static final int FLAG_USER_PROTECTED           = 0b10000000_00000000;
    public static final int FLAG_USER_VERIFIED            = 0b01000000_00000000;
    public static final int FLAG_USER_FOLLOW_REQUEST_SENT = 0b00100000_00000000;
    public static final int FLAG_USER_GEO_ENABLED         = 0b00010000_00000000;
    public static final int FLAG_USER_IS_TRANSLATOR       = 0b00001000_00000000;
    public static final int FLAG_USER_IS_CONTRIBUTORS     = 0b00000100_00000000;

    public static final int MSG_USER_100 = 0x00000000;
    public static final int MSG_USER_100_FIELD_FLAGS_31 = 0x00000001;
    public static final int MSG_USER_100_FIELD_USERID_51 = 0x00800002;
    public static final int MSG_USER_100_FIELD_NAME_52 = 0x01600004;
    public static final int MSG_USER_100_FIELD_SCREENNAME_53 = 0x01600006;
    public static final int MSG_USER_100_FIELD_FAVOURITESCOUNT_54 = 0x00000008;
    public static final int MSG_USER_100_FIELD_FOLLOWERSCOUNT_55 = 0x00000009;
    public static final int MSG_USER_100_FIELD_FRIENDSCOUNT_56 = 0x0000000a;
    public static final int MSG_USER_100_FIELD_CREATEDAT_57 = 0x0160000b;
    public static final int MSG_USER_100_FIELD_DESCRIPTION_58 = 0x0160000d;
    public static final int MSG_USER_100_FIELD_LISTEDCOUNT_59 = 0x0000000f;
    public static final int MSG_USER_100_FIELD_LANGUAGE_60 = 0x01600010;
    public static final int MSG_USER_100_FIELD_TIMEZONE_61 = 0x01600012;
    public static final int MSG_USER_100_FIELD_LOCATION_62 = 0x01600014;
    public static final int MSG_USERPOST_101 = 0x0000000f;
    public static final int MSG_USERPOST_101_FIELD_FLAGS_31 = 0x00000001;
    public static final int MSG_USERPOST_101_FIELD_USERID_51 = 0x00800002;
    public static final int MSG_USERPOST_101_FIELD_NAME_52 = 0x01600004;
    public static final int MSG_USERPOST_101_FIELD_SCREENNAME_53 = 0x01600006;
    public static final int MSG_USERPOST_101_FIELD_FAVOURITESCOUNT_54 = 0x00000008;
    public static final int MSG_USERPOST_101_FIELD_FOLLOWERSCOUNT_55 = 0x00000009;
    public static final int MSG_USERPOST_101_FIELD_FRIENDSCOUNT_56 = 0x0000000a;
    public static final int MSG_USERPOST_101_FIELD_CREATEDAT_57 = 0x0160000b;
    public static final int MSG_USERPOST_101_FIELD_DESCRIPTION_58 = 0x0160000d;
    public static final int MSG_USERPOST_101_FIELD_LISTEDCOUNT_59 = 0x0000000f;
    public static final int MSG_USERPOST_101_FIELD_LANGUAGE_60 = 0x01600010;
    public static final int MSG_USERPOST_101_FIELD_TIMEZONE_61 = 0x01600012;
    public static final int MSG_USERPOST_101_FIELD_LOCATION_62 = 0x01600014;
    public static final int MSG_USERPOST_101_FIELD_POSTID_21 = 0x00800016;
    public static final int MSG_USERPOST_101_FIELD_TEXT_22 = 0x01600018;


    public static void consume(Pipe<TwitterEventSchema> input) {
        while (PipeReader.tryReadFragment(input)) {
            int msgIdx = PipeReader.getMsgIdx(input);
            switch(msgIdx) {
                case MSG_USER_100:
                    consumeUser(input);
                break;
                case MSG_USERPOST_101:
                    consumeUserPost(input);
                break;
                case -1:
                   //requestShutdown();
                break;
            }
            PipeReader.releaseReadLock(input);
        }
    }

    public static void consumeUser(Pipe<TwitterEventSchema> input) {
        int fieldFlags = PipeReader.readInt(input,MSG_USER_100_FIELD_FLAGS_31);
        long fieldUserId = PipeReader.readLong(input,MSG_USER_100_FIELD_USERID_51);
        StringBuilder fieldName = PipeReader.readUTF8(input,MSG_USER_100_FIELD_NAME_52,new StringBuilder(PipeReader.readBytesLength(input,MSG_USER_100_FIELD_NAME_52)));
        StringBuilder fieldScreenName = PipeReader.readUTF8(input,MSG_USER_100_FIELD_SCREENNAME_53,new StringBuilder(PipeReader.readBytesLength(input,MSG_USER_100_FIELD_SCREENNAME_53)));
        int fieldFavouritesCount = PipeReader.readInt(input,MSG_USER_100_FIELD_FAVOURITESCOUNT_54);
        int fieldFollowersCount = PipeReader.readInt(input,MSG_USER_100_FIELD_FOLLOWERSCOUNT_55);
        int fieldFriendsCount = PipeReader.readInt(input,MSG_USER_100_FIELD_FRIENDSCOUNT_56);
        StringBuilder fieldCreatedAt = PipeReader.readUTF8(input,MSG_USER_100_FIELD_CREATEDAT_57,new StringBuilder(PipeReader.readBytesLength(input,MSG_USER_100_FIELD_CREATEDAT_57)));
        StringBuilder fieldDescription = PipeReader.readUTF8(input,MSG_USER_100_FIELD_DESCRIPTION_58,new StringBuilder(PipeReader.readBytesLength(input,MSG_USER_100_FIELD_DESCRIPTION_58)));
        int fieldListedCount = PipeReader.readInt(input,MSG_USER_100_FIELD_LISTEDCOUNT_59);
        StringBuilder fieldLanguage = PipeReader.readUTF8(input,MSG_USER_100_FIELD_LANGUAGE_60,new StringBuilder(PipeReader.readBytesLength(input,MSG_USER_100_FIELD_LANGUAGE_60)));
        StringBuilder fieldTimeZone = PipeReader.readUTF8(input,MSG_USER_100_FIELD_TIMEZONE_61,new StringBuilder(PipeReader.readBytesLength(input,MSG_USER_100_FIELD_TIMEZONE_61)));
        StringBuilder fieldLocation = PipeReader.readUTF8(input,MSG_USER_100_FIELD_LOCATION_62,new StringBuilder(PipeReader.readBytesLength(input,MSG_USER_100_FIELD_LOCATION_62)));
    }
    public static void consumeUserPost(Pipe<TwitterEventSchema> input) {
        int fieldFlags = PipeReader.readInt(input,MSG_USERPOST_101_FIELD_FLAGS_31);
        long fieldUserId = PipeReader.readLong(input,MSG_USERPOST_101_FIELD_USERID_51);
        StringBuilder fieldName = PipeReader.readUTF8(input,MSG_USERPOST_101_FIELD_NAME_52,new StringBuilder(PipeReader.readBytesLength(input,MSG_USERPOST_101_FIELD_NAME_52)));
        StringBuilder fieldScreenName = PipeReader.readUTF8(input,MSG_USERPOST_101_FIELD_SCREENNAME_53,new StringBuilder(PipeReader.readBytesLength(input,MSG_USERPOST_101_FIELD_SCREENNAME_53)));
        int fieldFavouritesCount = PipeReader.readInt(input,MSG_USERPOST_101_FIELD_FAVOURITESCOUNT_54);
        int fieldFollowersCount = PipeReader.readInt(input,MSG_USERPOST_101_FIELD_FOLLOWERSCOUNT_55);
        int fieldFriendsCount = PipeReader.readInt(input,MSG_USERPOST_101_FIELD_FRIENDSCOUNT_56);
        StringBuilder fieldCreatedAt = PipeReader.readUTF8(input,MSG_USERPOST_101_FIELD_CREATEDAT_57,new StringBuilder(PipeReader.readBytesLength(input,MSG_USERPOST_101_FIELD_CREATEDAT_57)));
        StringBuilder fieldDescription = PipeReader.readUTF8(input,MSG_USERPOST_101_FIELD_DESCRIPTION_58,new StringBuilder(PipeReader.readBytesLength(input,MSG_USERPOST_101_FIELD_DESCRIPTION_58)));
        int fieldListedCount = PipeReader.readInt(input,MSG_USERPOST_101_FIELD_LISTEDCOUNT_59);
        StringBuilder fieldLanguage = PipeReader.readUTF8(input,MSG_USERPOST_101_FIELD_LANGUAGE_60,new StringBuilder(PipeReader.readBytesLength(input,MSG_USERPOST_101_FIELD_LANGUAGE_60)));
        StringBuilder fieldTimeZone = PipeReader.readUTF8(input,MSG_USERPOST_101_FIELD_TIMEZONE_61,new StringBuilder(PipeReader.readBytesLength(input,MSG_USERPOST_101_FIELD_TIMEZONE_61)));
        StringBuilder fieldLocation = PipeReader.readUTF8(input,MSG_USERPOST_101_FIELD_LOCATION_62,new StringBuilder(PipeReader.readBytesLength(input,MSG_USERPOST_101_FIELD_LOCATION_62)));
        long fieldPostId = PipeReader.readLong(input,MSG_USERPOST_101_FIELD_POSTID_21);
        StringBuilder fieldText = PipeReader.readUTF8(input,MSG_USERPOST_101_FIELD_TEXT_22,new StringBuilder(PipeReader.readBytesLength(input,MSG_USERPOST_101_FIELD_TEXT_22)));
    }

    public static boolean publishUser(Pipe<TwitterEventSchema> output, int fieldFlags, long fieldUserId, CharSequence fieldName, CharSequence fieldScreenName, int fieldFavouritesCount, int fieldFollowersCount, int fieldFriendsCount, CharSequence fieldCreatedAt, CharSequence fieldDescription, int fieldListedCount, CharSequence fieldLanguage, CharSequence fieldTimeZone, CharSequence fieldLocation) {
        boolean result = false;
        if (PipeWriter.tryWriteFragment(output, MSG_USER_100)) {
            PipeWriter.writeInt(output,MSG_USER_100_FIELD_FLAGS_31, fieldFlags);
            PipeWriter.writeLong(output,MSG_USER_100_FIELD_USERID_51, fieldUserId);
            PipeWriter.writeUTF8(output,MSG_USER_100_FIELD_NAME_52, fieldName);
            PipeWriter.writeUTF8(output,MSG_USER_100_FIELD_SCREENNAME_53, fieldScreenName);
            PipeWriter.writeInt(output,MSG_USER_100_FIELD_FAVOURITESCOUNT_54, fieldFavouritesCount);
            PipeWriter.writeInt(output,MSG_USER_100_FIELD_FOLLOWERSCOUNT_55, fieldFollowersCount);
            PipeWriter.writeInt(output,MSG_USER_100_FIELD_FRIENDSCOUNT_56, fieldFriendsCount);
            PipeWriter.writeUTF8(output,MSG_USER_100_FIELD_CREATEDAT_57, fieldCreatedAt);
            PipeWriter.writeUTF8(output,MSG_USER_100_FIELD_DESCRIPTION_58, fieldDescription);
            PipeWriter.writeInt(output,MSG_USER_100_FIELD_LISTEDCOUNT_59, fieldListedCount);
            PipeWriter.writeUTF8(output,MSG_USER_100_FIELD_LANGUAGE_60, fieldLanguage);
            PipeWriter.writeUTF8(output,MSG_USER_100_FIELD_TIMEZONE_61, fieldTimeZone);
            PipeWriter.writeUTF8(output,MSG_USER_100_FIELD_LOCATION_62, fieldLocation);
            PipeWriter.publishWrites(output);
            result = true;
        }
        return result;
    }
    public static boolean publishUserPost(Pipe<TwitterEventSchema> output, int fieldFlags, long fieldUserId, CharSequence fieldName, CharSequence fieldScreenName, int fieldFavouritesCount, int fieldFollowersCount, int fieldFriendsCount, CharSequence fieldCreatedAt, CharSequence fieldDescription, int fieldListedCount, CharSequence fieldLanguage, CharSequence fieldTimeZone, CharSequence fieldLocation, long fieldPostId, CharSequence fieldText) {
        boolean result = false;
        if (PipeWriter.tryWriteFragment(output, MSG_USERPOST_101)) {
            PipeWriter.writeInt(output,MSG_USERPOST_101_FIELD_FLAGS_31, fieldFlags);
            PipeWriter.writeLong(output,MSG_USERPOST_101_FIELD_USERID_51, fieldUserId);
            PipeWriter.writeUTF8(output,MSG_USERPOST_101_FIELD_NAME_52, fieldName);
            PipeWriter.writeUTF8(output,MSG_USERPOST_101_FIELD_SCREENNAME_53, fieldScreenName);
            PipeWriter.writeInt(output,MSG_USERPOST_101_FIELD_FAVOURITESCOUNT_54, fieldFavouritesCount);
            PipeWriter.writeInt(output,MSG_USERPOST_101_FIELD_FOLLOWERSCOUNT_55, fieldFollowersCount);
            PipeWriter.writeInt(output,MSG_USERPOST_101_FIELD_FRIENDSCOUNT_56, fieldFriendsCount);
            PipeWriter.writeUTF8(output,MSG_USERPOST_101_FIELD_CREATEDAT_57, fieldCreatedAt);
            PipeWriter.writeUTF8(output,MSG_USERPOST_101_FIELD_DESCRIPTION_58, fieldDescription);
            PipeWriter.writeInt(output,MSG_USERPOST_101_FIELD_LISTEDCOUNT_59, fieldListedCount);
            PipeWriter.writeUTF8(output,MSG_USERPOST_101_FIELD_LANGUAGE_60, fieldLanguage);
            PipeWriter.writeUTF8(output,MSG_USERPOST_101_FIELD_TIMEZONE_61, fieldTimeZone);
            PipeWriter.writeUTF8(output,MSG_USERPOST_101_FIELD_LOCATION_62, fieldLocation);
            PipeWriter.writeLong(output,MSG_USERPOST_101_FIELD_POSTID_21, fieldPostId);
            PipeWriter.writeUTF8(output,MSG_USERPOST_101_FIELD_TEXT_22, fieldText);
            PipeWriter.publishWrites(output);
            result = true;
        }
        return result;
    }


}
