package com.ociweb.twitter.stages;

import java.io.IOException;
import java.nio.Buffer;

import com.ociweb.pronghorn.network.config.HTTPHeaderKeyDefaults;
import com.ociweb.pronghorn.network.schema.NetResponseSchema;
import com.ociweb.pronghorn.pipe.DataInputBlobReader;
import com.ociweb.pronghorn.pipe.Pipe;
import com.ociweb.pronghorn.pipe.PipeReader;
import com.ociweb.pronghorn.stage.PronghornStage;
import com.ociweb.pronghorn.stage.scheduling.GraphManager;
import com.ociweb.pronghorn.util.TrieParserReader;
import com.ociweb.pronghorn.util.parse.JSONParser;
import com.ociweb.pronghorn.util.parse.JSONVisitor;

public class OAuth2ParserStage extends PronghornStage {

	private Pipe<NetResponseSchema> input;
	
	
	public OAuth2ParserStage(GraphManager graphManager, Pipe<NetResponseSchema> input) {
		super(graphManager, input, NONE);
		this.input = input;
	}

	@Override
	public void run() {
		
		
		while (PipeReader.tryReadFragment(input)) {
			
			if (PipeReader.getMsgIdx(input)==NetResponseSchema.MSG_RESPONSE_101) {
				
				long con = PipeReader.readLong(input, NetResponseSchema.MSG_RESPONSE_101_FIELD_CONNECTIONID_1);
				
				DataInputBlobReader<NetResponseSchema> stream = PipeReader.inputStream(input,  NetResponseSchema.MSG_RESPONSE_101_FIELD_PAYLOAD_3);		

				
				System.out.println(HTTPHeaderKeyDefaults.values()[6]);
				
				short statusCode =stream.readShort();
				
				if (200!=statusCode) {
					System.out.println("error got code:"+statusCode);
				}
				
				int headerId = stream.readShort();
				
				while (-1 != headerId) { //end of headers will be marked with -1 value
					//determine the type
					
					int headerValue = stream.readShort();
					//is this what we need?
					
					//read next
					headerId = stream.readShort();
					
				}
								
//				StringBuilder builder = new StringBuilder();
//				//since payload is last we can use available as the length of the payload.
//				int length = stream.available();
//				try {
//					DataInputBlobReader.readUTF(stream, length, builder);
//				} catch (IOException e) {
//					throw new RuntimeException(e);
//				}
//
//				System.out.println(builder);
				
				//TODO: parse.
				
				
				TrieParserReader jsonReader = JSONParser.newReader();
				JSONVisitor visitor = buildVisitor();
				JSONParser.parse(stream, jsonReader, visitor);
				
				
				

				
			} else {
				System.out.println("unknown "+PipeReader.getMsgIdx(input));
			}
			
			PipeReader.releaseReadLock(input);
		};
		
		
		
	}

	private JSONVisitor buildVisitor() {
		
		
		return new BearerExtractor();
		
//		return new JSONVisitor() {
//
//			StringBuilder builder = new StringBuilder();
//			
//			@Override
//			public Appendable stringValue() {
//				return builder;
//			}
//
//			@Override
//			public void stringValueComplete() {
//				System.err.println(builder);
//			}
//
//			@Override
//			public Appendable stringName(int fieldIndex) {
//				return builder;
//			}
//
//			@Override
//			public void stringNameComplete() {
//				System.err.println(builder);
//			}
//
//			@Override
//			public void arrayBegin() {
//				System.err.println('[');
//			}
//
//			@Override
//			public void arrayEnd() {
//				System.err.println(']');
//			}
//
//			@Override
//			public void arrayIndexBegin(int instance) {
//				System.err.println("idx: "+instance);
//			}
//
//			@Override
//			public void numberValue(long m, byte e) {
//				System.err.println("A number");
//				
//			}
//
//			@Override
//			public void nullValue() {
//				System.err.println("null");
//			}
//
//			@Override
//			public void booleanValue(boolean b) {
//				System.err.println("boolean "+b);
//			}
//
//			@Override
//			public void objectEnd() {
//				System.err.println("}");
//			}
//
//			@Override
//			public void objectBegin() {
//				System.err.println("}");
//			}
//			
//		};
	}

}
