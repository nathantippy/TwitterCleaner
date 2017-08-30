package com.ociweb.pronghorn.network.twitter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ociweb.pronghorn.network.NetResponseJSONStage;
import com.ociweb.pronghorn.network.schema.NetResponseSchema;
import com.ociweb.pronghorn.network.schema.TwitterEventSchema;
import com.ociweb.pronghorn.network.schema.TwitterStreamControlSchema;
import com.ociweb.pronghorn.pipe.Pipe;
import com.ociweb.pronghorn.pipe.PipeWriter;
import com.ociweb.pronghorn.pipe.util.hash.LongHashTable;
import com.ociweb.pronghorn.stage.scheduling.GraphManager;
import com.ociweb.pronghorn.util.parse.MapJSONToPipeBuilder;

public class TwitterJSONToTwitterEventsStage extends NetResponseJSONStage<TwitterEventSchema,TwitterKey> {

	private static final Logger logger = LoggerFactory.getLogger(TwitterJSONToTwitterEventsStage.class);
	
	public static final int MAX_TWEET_TEXT_SIZE = 1<<10; //140*6 must have 800 or so for full utf8 (do not change this constant) NO SUPPORT FOR DM...
	private final Pipe<TwitterStreamControlSchema> control;

	public TwitterJSONToTwitterEventsStage(GraphManager graphManager,
			                               Pipe<NetResponseSchema> input, 
			                               Pipe<TwitterStreamControlSchema> control,
			                               Pipe<TwitterEventSchema> output) {
				
		super(graphManager, TwitterKey.class, customJSONMapper(), input, output, control);
		this.control = control;
	}
	
	@Override
	protected void processCloseEvent(byte[] hostBacking, int hostPos, int hostLen, int hostMask, int port) {
		
		while (!PipeWriter.hasRoomForWrite(control)) {
			logger.info("error this pipe should be empty since it only contains reset comands. Make pipe longer");
		}
		
		//send requst to re-connect the stream again.
		PipeWriter.tryWriteFragment(control, TwitterStreamControlSchema.MSG_RECONNECT_100);
		PipeWriter.publishWrites(control);
	
	}
	
	private static MapJSONToPipeBuilder customJSONMapper() {
		MapJSONToPipeBuilder mapper = new MapJSONToPipeBuilder(TwitterEventSchema.instance, TwitterKey.class, TwitterEventSchema.MSG_USERPOST_101, TwitterEventSchema.MSG_USERPOST_101_FIELD_FLAGS_31) {

			private LongHashTable table;
			private LongHashTable bitTable;

			@Override
			public int getLoc(long id) {
				
				 if (null == table) {
					 
						table = new LongHashTable(10);
						
						LongHashTable.setItem(table,  buildUniqueId(TwitterKey.USER, TwitterKey.POSSIBLY_SENSITIVE), TwitterEventSchema.MSG_USERPOST_101_FIELD_FLAGS_31 );
						LongHashTable.setItem(table,  buildUniqueId(TwitterKey.USER, TwitterKey.PROTECTED), TwitterEventSchema.MSG_USERPOST_101_FIELD_FLAGS_31 );
						LongHashTable.setItem(table,  buildUniqueId(TwitterKey.USER, TwitterKey.FAVORITED), TwitterEventSchema.MSG_USERPOST_101_FIELD_FLAGS_31 );
						LongHashTable.setItem(table,  buildUniqueId(TwitterKey.USER, TwitterKey.RETWEETED), TwitterEventSchema.MSG_USERPOST_101_FIELD_FLAGS_31 );					
						LongHashTable.setItem(table,  buildUniqueId(TwitterKey.USER, TwitterKey.TRUNCATED), TwitterEventSchema.MSG_USERPOST_101_FIELD_FLAGS_31 );
						LongHashTable.setItem(table,  buildUniqueId(TwitterKey.USER, TwitterKey.VERIFIED), TwitterEventSchema.MSG_USERPOST_101_FIELD_FLAGS_31 );
						LongHashTable.setItem(table,  buildUniqueId(TwitterKey.USER, TwitterKey.GEO_ENABLED), TwitterEventSchema.MSG_USERPOST_101_FIELD_FLAGS_31 );
						LongHashTable.setItem(table,  buildUniqueId(TwitterKey.USER, TwitterKey.FOLLOW_REQUEST_SENT), TwitterEventSchema.MSG_USERPOST_101_FIELD_FLAGS_31 );
						
						
						LongHashTable.setItem(table, buildUniqueId(TwitterKey.USER, TwitterKey.ID), TwitterEventSchema.MSG_USERPOST_101_FIELD_USERID_51);
						LongHashTable.setItem(table, buildUniqueId(TwitterKey.USER, TwitterKey.NAME), TwitterEventSchema.MSG_USERPOST_101_FIELD_NAME_52);
						LongHashTable.setItem(table, buildUniqueId(TwitterKey.USER, TwitterKey.SCREEN_NAME), TwitterEventSchema.MSG_USERPOST_101_FIELD_SCREENNAME_53);
						LongHashTable.setItem(table, buildUniqueId(TwitterKey.USER, TwitterKey.FAVOURITES_COUNT), TwitterEventSchema.MSG_USERPOST_101_FIELD_FAVOURITESCOUNT_54);
						LongHashTable.setItem(table, buildUniqueId(TwitterKey.USER, TwitterKey.FOLLOWERS_COUNT), TwitterEventSchema.MSG_USERPOST_101_FIELD_FOLLOWERSCOUNT_55);
						LongHashTable.setItem(table, buildUniqueId(TwitterKey.USER, TwitterKey.FRIENDS_COUNT), TwitterEventSchema.MSG_USERPOST_101_FIELD_FRIENDSCOUNT_56);
						LongHashTable.setItem(table, buildUniqueId(TwitterKey.USER, TwitterKey.CREATED_AT), TwitterEventSchema.MSG_USERPOST_101_FIELD_CREATEDAT_57);
						LongHashTable.setItem(table, buildUniqueId(TwitterKey.USER, TwitterKey.DESCRIPTION), TwitterEventSchema.MSG_USERPOST_101_FIELD_DESCRIPTION_58);
						LongHashTable.setItem(table, buildUniqueId(TwitterKey.USER, TwitterKey.LISTED_COUNT), TwitterEventSchema.MSG_USERPOST_101_FIELD_LISTEDCOUNT_59);
						LongHashTable.setItem(table, buildUniqueId(TwitterKey.USER, TwitterKey.LANG), TwitterEventSchema.MSG_USERPOST_101_FIELD_LANGUAGE_60);
						LongHashTable.setItem(table, buildUniqueId(TwitterKey.USER, TwitterKey.TIME_ZONE), TwitterEventSchema.MSG_USERPOST_101_FIELD_TIMEZONE_61);
						LongHashTable.setItem(table, buildUniqueId(TwitterKey.USER, TwitterKey.LOCATION), TwitterEventSchema.MSG_USERPOST_101_FIELD_LOCATION_62);
						
						LongHashTable.setItem(table, buildUniqueId(TwitterKey.ID_STR), TwitterEventSchema.MSG_USERPOST_101_FIELD_POSTID_21);
						LongHashTable.setItem(table, buildUniqueId(TwitterKey.TEXT), TwitterEventSchema.MSG_USERPOST_101_FIELD_TEXT_22);
					 
					 
				 }
				
				 
				 return  LongHashTable.getItem(table, id);
		
			}

			
			private LongHashTable maskTable() {
				if (null == bitTable) {
					
					LongHashTable maskTable = new LongHashTable(8);
										
					//LongHashTable.setItem(maskTable, buildUniqueId(TwitterKey.USER, TwitterKey.RETWEETED), TwitterEventSchema.FLAG_RETWEET); //deprecate FLAG_RETWEETED_BY_ME
					LongHashTable.setItem(maskTable, buildUniqueId(TwitterKey.USER, TwitterKey.POSSIBLY_SENSITIVE), TwitterEventSchema.FLAG_POSSIBLY_SENSITIVE );
					LongHashTable.setItem(maskTable, buildUniqueId(TwitterKey.USER, TwitterKey.PROTECTED), TwitterEventSchema.FLAG_USER_PROTECTED);
					LongHashTable.setItem(maskTable, buildUniqueId(TwitterKey.USER, TwitterKey.FAVORITED), TwitterEventSchema.FLAG_FAVORITED);
					LongHashTable.setItem(maskTable, buildUniqueId(TwitterKey.USER, TwitterKey.RETWEETED), TwitterEventSchema.FLAG_RETWEETED);					
					LongHashTable.setItem(maskTable, buildUniqueId(TwitterKey.USER, TwitterKey.TRUNCATED), TwitterEventSchema.FLAG_TRUNCATED);
					LongHashTable.setItem(maskTable, buildUniqueId(TwitterKey.USER, TwitterKey.VERIFIED), TwitterEventSchema.FLAG_USER_VERIFIED);
					LongHashTable.setItem(maskTable, buildUniqueId(TwitterKey.USER, TwitterKey.GEO_ENABLED), TwitterEventSchema.FLAG_USER_GEO_ENABLED);
					LongHashTable.setItem(maskTable, buildUniqueId(TwitterKey.USER, TwitterKey.FOLLOW_REQUEST_SENT), TwitterEventSchema.FLAG_USER_FOLLOW_REQUEST_SENT);
					
					LongHashTable.setItem(maskTable, buildUniqueId(TwitterKey.USER, TwitterKey.CONTRIBUTORS_ENABLED), TwitterEventSchema.FLAG_USER_IS_CONTRIBUTORS);
					//LongHashTable.setItem(maskTable, buildUniqueId(TwitterKey.USER, TwitterKey.FOLLOW_REQUEST_SENT), TwitterEventSchema.FLAG_USER_IS_TRANSLATOR);
					
					bitTable = maskTable;
				}
				
				
				return bitTable;
			}
			
			@Override
			public int bitMask(long id) {
				 return LongHashTable.getItem(maskTable(), id);
			}

			@Override
			public boolean usesBitMask(long id) {
				 return LongHashTable.hasItem(maskTable(), id);
			}
			
		};
		return mapper;
	}

	public static Pipe<TwitterEventSchema> buildStage(GraphManager gm, Pipe<NetResponseSchema> clientResponsesPipe ,
			Pipe<TwitterStreamControlSchema> control,
			Pipe<NetResponseSchema>[] clientResponsesPipes, int tweetsCount) {
		
		Pipe<TwitterEventSchema> hosePipe = TwitterEventSchema.instance.newPipe(tweetsCount, MAX_TWEET_TEXT_SIZE);
		
		new TwitterJSONToTwitterEventsStage(gm, clientResponsesPipe, control, hosePipe);
		return hosePipe;
	}

}
