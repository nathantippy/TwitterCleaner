package com.ociweb.twitter;

import com.ociweb.pronghorn.network.schema.HTTPRequestSchema;
import com.ociweb.pronghorn.network.schema.ServerResponseSchema;
import com.ociweb.pronghorn.pipe.DataInputBlobReader;
import com.ociweb.pronghorn.pipe.Pipe;
import com.ociweb.pronghorn.stage.PronghornStage;
import com.ociweb.pronghorn.stage.scheduling.GraphManager;

public class AuthStage extends PronghornStage {

	private final Pipe<HTTPRequestSchema>[] inputPipes;
	private final Pipe<ServerResponseSchema>[] outputPipes;
    
	public AuthStage(GraphManager graphManager,
						Pipe<HTTPRequestSchema>[] inputPipes,
					    Pipe<ServerResponseSchema>[] outputPipes) {
		super(graphManager,inputPipes,outputPipes);
		this.inputPipes = inputPipes;
		this.outputPipes = outputPipes;		
		assert(inputPipes.length == outputPipes.length);
				
	}

	@Override
	public void run() {
		
		int x = inputPipes.length;
		while (--x>=0) {
			run(inputPipes[x],outputPipes[x]);
		}
	}

	private void run(Pipe<HTTPRequestSchema> input,
			         Pipe<ServerResponseSchema> output) {
		
		while (Pipe.hasContentToRead(input)) {
			int msgIdx = Pipe.takeMsgIdx(input);
			
		    switch(msgIdx) {
		        case HTTPRequestSchema.MSG_RESTREQUEST_300:
				
		        	long fieldChannelId = Pipe.takeLong(input); 
		        	int fieldSequence = Pipe.takeInt(input); 
		        	int fieldVerb = Pipe.takeInt(input); 
		        	System.out.println("new auth request verb "+fieldVerb);
		        	
		        	DataInputBlobReader<HTTPRequestSchema> stream = Pipe.openInputStream(input);
		        	stream.readUTF(System.out);
		        	System.out.println();
		        	int fieldRevision = Pipe.takeInt(input);
		        	int fieldRequestContext = Pipe.takeInt(input); 
		        	
				break;
		        case -1:
		           //requestShutdown();
		        break;
		    }
		    
		    Pipe.confirmLowLevelRead(input, Pipe.sizeOf(input, msgIdx));
		    Pipe.releaseReadLock(input);
			
			
		}
		
	}

}
