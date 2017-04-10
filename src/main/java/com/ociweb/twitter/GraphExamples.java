package com.ociweb.twitter;

import java.io.IOException;
import java.io.ObjectInputStream;

import com.ociweb.pronghorn.pipe.Pipe;
import com.ociweb.pronghorn.pipe.PipeConfig;
import com.ociweb.pronghorn.stage.PronghornStage;
import com.ociweb.pronghorn.stage.scheduling.GraphManager;
import com.ociweb.pronghorn.stage.test.ConsoleJSONDumpStage;
import com.ociweb.pronghorn.stage.test.PipeCleanerStage;
import com.ociweb.pronghorn.util.Appendables;
import com.ociweb.twitter.stages.text.TextContentRouter;
import com.ociweb.twitter.stages.text.TextContentRouterBloom;
import com.ociweb.twitter.stages.text.TextContentRouterStage;
import com.twitter.hbc.httpclient.auth.Authentication;

public class GraphExamples {
	
//	static void watchUserFollowing(GraphManager gm, Authentication auth) {
//		PipeConfig<TwitterEventSchema> hoseFeedPipeConfig = new PipeConfig<TwitterEventSchema>(TwitterEventSchema.instance, 100, 1024);
//		
//		Pipe<TwitterEventSchema> pipe = new Pipe<TwitterEventSchema>(hoseFeedPipeConfig);
//		
//		HosebirdUserFeedStage feed = new HosebirdUserFeedStage(gm, auth, pipe, 0 );
//		
//		PronghornStage consume = new ConsoleJSONDumpStage(gm, pipe);
//	}
//	
//	static void simpleTokenize(GraphManager gm, Authentication auth) {
//		PipeConfig<TwitterEventSchema> hoseFeedPipeConfig = new PipeConfig<TwitterEventSchema>(TwitterEventSchema.instance, 100, 1024);
//		
//		Pipe<TwitterEventSchema> output = new Pipe<TwitterEventSchema>(hoseFeedPipeConfig);
//		
//		HosebirdUserFeedStage feed = new HosebirdUserFeedStage(gm, auth, output, 0 );
//		
//		Pipe<TwitterEventSchema>[] results = new Pipe[2];
//		
//		results[0] = new Pipe<TwitterEventSchema>(hoseFeedPipeConfig);
//		results[1] = new Pipe<TwitterEventSchema>(hoseFeedPipeConfig);
//		
//		PronghornStage consumeA = new PipeCleanerStage(gm, results[0]);
//		PronghornStage consumeB = new PipeCleanerStage(gm, results[1]);
//		
//		int field = TwitterEventSchema.MSG_USERPOST_101_FIELD_TEXT_22;
//		TextContentRouter textFilter = new TextContentRouter() {
//	
//			@Override
//			public void clear() {
//				// TODO Auto-generated method stub
//				System.err.println("CLEAR");
//			}
//	
//			@Override
//			public void text(byte[] backing, int pos, int len, int mask) {
//				// TODO Auto-generated method stub
//				System.err.print("word: ");
//				Appendables.appendUTF8(System.err, backing, pos, len, mask);
//				System.err.println();
//			}
//	
//			@Override
//			public void url(byte[] backing, int pos, int len, int mask) {
//				// TODO Auto-generated method stub
//				System.err.print("url: ");
//				Appendables.appendUTF8(System.err, backing, pos, len, mask);
//				System.err.println();
//			}
//	
//			@Override
//			public int route() {
//				// TODO Auto-generated method stub
//				return 0;
//			}
//			
//		};
//		TextContentRouterStage router = new TextContentRouterStage(gm, output, results, field, textFilter);
//				
//	}

	
	
//	static void wordFilter(GraphManager gm, Authentication auth) {
//		
//		String terms = "romance";
//		HosebirdFeedStage feed = new HosebirdFeedStage(gm, auth, terms, 0, output );
//
//		PipeConfig<TwitterEventSchema> hoseFeedPipeConfig = new PipeConfig<TwitterEventSchema>(TwitterEventSchema.instance, 100, 1024);
//		
//		Pipe<TwitterEventSchema> output = new Pipe<TwitterEventSchema>(hoseFeedPipeConfig);
//		
//		Pipe<TwitterEventSchema>[] results = new Pipe[2];
//		
//		results[0] = new Pipe<TwitterEventSchema>(hoseFeedPipeConfig);
//		results[1] = new Pipe<TwitterEventSchema>(hoseFeedPipeConfig);
//		
//		PronghornStage consumeA = new PipeCleanerStage(gm, results[0]);
//		
//	//	PronghornStage consumeA = new ConsoleJSONDumpStage(gm, results[0]);
//		
//		PronghornStage consumeB = new ConsoleJSONDumpStage(gm, results[1]);
//		
//		int field = TwitterEventSchema.MSG_USERPOST_101_FIELD_TEXT_22;
//		
//		
//		TextContentRouterBloom rules;
//		try {
//			rules = new TextContentRouterBloom(
//					                           new ObjectInputStream( GraphExamples.class.getResourceAsStream("/bookWords.dat")),
//					                            0,1);
//			TextContentRouterStage router = new TextContentRouterStage(gm, output, results, field, rules);
//			
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		
//		
//				
//	}
	
	
}
