package com.ociweb.twitter;

import com.ociweb.pronghorn.network.schema.HTTPRequestSchema;
import com.ociweb.pronghorn.pipe.DataInputBlobReader;
import com.ociweb.pronghorn.pipe.DataOutputBlobWriter;
import com.ociweb.pronghorn.pipe.Pipe;
import com.ociweb.pronghorn.pipe.util.hash.LongHashTable;
import com.ociweb.pronghorn.stage.PronghornStage;
import com.ociweb.pronghorn.stage.scheduling.GraphManager;

public class ParamRouterStage extends PronghornStage {

	private final Pipe<HTTPRequestSchema>[] inputs;
	private final Pipe<HTTPRequestSchema>[] outputs;
	private final LongHashTable table;
	
	public ParamRouterStage(GraphManager gm, 
			                LongHashTable table, 
			                Pipe<HTTPRequestSchema>[] inputs,
			                Pipe<HTTPRequestSchema>[] outputs) {
		super(gm,inputs,outputs);
		this.table = table;
		this.inputs = inputs;
		this.outputs = outputs;
	}

	
	@Override
	public void run() {
		int i = inputs.length;
		while (--i>=0) {
			process(table, inputs[i], outputs);
		}
	}


	private void process(LongHashTable table, 
			             Pipe<HTTPRequestSchema> sourcePipe, 
			             Pipe<HTTPRequestSchema>[] outputs) {
		
		while (Pipe.hasContentToRead(sourcePipe) 
			  && Pipe.peekMsg(sourcePipe, HTTPRequestSchema.MSG_RESTREQUEST_300)) {
						
			
			DataInputBlobReader<HTTPRequestSchema> stream = Pipe.peekInputStream(sourcePipe, HTTPRequestSchema.MSG_RESTREQUEST_300_FIELD_PARAMS_32);
			long userId = stream.readPackedLong();
			
			System.err.println("NOTE: the user ID value is "+userId);
			
			int pipeIdx = LongHashTable.getItem(table, userId);
						
			Pipe<HTTPRequestSchema> targetPipe = outputs[pipeIdx];
			if (Pipe.hasRoomForWrite(targetPipe)) {
			    
				int size = Pipe.addMsgIdx(targetPipe, HTTPRequestSchema.MSG_RESTREQUEST_300);
				
				Pipe.addLongValue(Pipe.takeLong(sourcePipe), targetPipe); //connectionId
				Pipe.addIntValue(Pipe.takeInt(sourcePipe), targetPipe);   //sequence
				Pipe.addIntValue(Pipe.takeInt(sourcePipe), targetPipe);   //verb
								
				DataOutputBlobWriter<HTTPRequestSchema> targetStream = Pipe.openOutputStream(targetPipe);
				DataInputBlobReader<HTTPRequestSchema> sourceStream = Pipe.openInputStream(sourcePipe);
				sourceStream.readInto(targetStream, sourceStream.available());
				targetStream.closeLowLevelField();
				
				Pipe.addIntValue(Pipe.takeInt(sourcePipe), targetPipe);   //revision
				Pipe.addIntValue(Pipe.takeInt(sourcePipe), targetPipe);   //request context
			
				Pipe.confirmLowLevelWrite(targetPipe, size);
				Pipe.publishWrites(targetPipe);
				
			} else {
				//can not write
				break;
			}	
		}
	}

}
