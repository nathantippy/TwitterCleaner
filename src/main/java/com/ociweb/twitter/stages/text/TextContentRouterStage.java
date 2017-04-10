package com.ociweb.twitter.stages.text;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ociweb.pronghorn.pipe.Pipe;
import com.ociweb.pronghorn.pipe.PipeReader;
import com.ociweb.pronghorn.stage.PronghornStage;
import com.ociweb.pronghorn.stage.scheduling.GraphManager;
import com.ociweb.pronghorn.util.ByteConsumer;
import com.ociweb.pronghorn.util.TrieParser;
import com.ociweb.pronghorn.util.TrieParserReader;
import com.ociweb.twitter.stages.json.TwitterEventSchema;

public class TextContentRouterStage extends PronghornStage {

    private final Logger logger = LoggerFactory.getLogger(TextContentRouterStage.class);
    
    private final Pipe<TwitterEventSchema> input;
    private final Pipe<TwitterEventSchema>[] output;
    
    private final byte[] tweet = new byte[2000];
    private final int fieldLoc;
    
    private TrieParser trie;
    private TrieParserReader reader;
    
    private boolean moveInProgress = false;
    private int     moveTarget = -1;

    private final TextContentRouter filter;
    private final ByteConsumer textWrapper;
    private final ByteConsumer urlWrapper;
    
    public TextContentRouterStage(GraphManager graphManager, Pipe<TwitterEventSchema> input, Pipe<TwitterEventSchema>[] output, int fieldLoc, TextContentRouter filter) {
        super(graphManager, input, output);
        this.input = input;
        this.output = output;
        this.fieldLoc = fieldLoc;
        this.filter = filter;
        
        this.textWrapper = new ByteConsumer() {
			@Override
			public void consume(byte[] backing, int pos, int len, int mask) {
				filter.text(backing, pos, len, mask);
			}

			@Override
			public void consume(byte value) {
				throw new UnsupportedOperationException();
				
			}        	
        };
        
        this.urlWrapper = new ByteConsumer() {
			@Override
			public void consume(byte[] backing, int pos, int len, int mask) {
				filter.url(backing, pos, len, mask);
			}   
			
			@Override
			public void consume(byte value) {
				throw new UnsupportedOperationException();
				
			}  
        };        
    }
  
    
    @Override
    public void startup() {
        
        trie = TextContentSplitter.parser;
        reader = TextContentSplitter.reader();

    }

    @Override
    public void run() {
        
        if (moveInProgress) {
            if (!PipeReader.tryMoveSingleMessage(input, output[moveTarget])) {
                return;
            } else {
                moveInProgress = false;
                moveTarget = -1;
                PipeReader.releaseReadLock(input);
            }
        }

        while (PipeReader.tryReadFragment(input) ) {

                int len = PipeReader.readBytes(input, fieldLoc, tweet, 0);
                tweet[len]=32;//force 1 extra space on the end so parser knows when to stop.
                
                filter.clear();                
                
                TrieParserReader.parseSetup(reader, tweet, 0, len+1, Integer.MAX_VALUE);

                while (TrieParserReader.parseHasContent(reader)) {
                                        
                    long result = TrieParserReader.parseNext(reader, trie);
                    if (-1==result) {
                        logger.info("unable to parse value abandoned, moving on to next, not expected to happen.");
                        PipeReader.releaseReadLock(input);                        
                        return;
                    }
                    if (2==result) {
                    	//we have one text
                    	TrieParserReader.capturedFieldBytes(reader, 0, textWrapper);
                    	
                    }
                    if (3==result || 4==result) {
                    	// extracted URL
                    	TrieParserReader.capturedFieldBytes(reader, 0, urlWrapper);
                      
                    }
                    //all others ignored
                }                
            
                final int target = filter.route();
                if (!PipeReader.tryMoveSingleMessage(input, output[target])) {
                    moveTarget = target;
                    moveInProgress = true;
                    return;
                }
                
                PipeReader.releaseReadLock(input);            
        }        
    }
}
