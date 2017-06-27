package com.ociweb.twitter.stages;

import com.ociweb.pronghorn.network.config.HTTPContentType;
import com.ociweb.pronghorn.network.config.HTTPHeader;
import com.ociweb.pronghorn.network.config.HTTPRevision;
import com.ociweb.pronghorn.network.config.HTTPSpecification;
import com.ociweb.pronghorn.network.config.HTTPVerb;
import com.ociweb.pronghorn.network.http.AbstractRestStage;
import com.ociweb.pronghorn.network.module.AbstractPayloadResponseStage;
import com.ociweb.pronghorn.network.schema.HTTPRequestSchema;
import com.ociweb.pronghorn.network.schema.ServerResponseSchema;
import com.ociweb.pronghorn.pipe.Pipe;
import com.ociweb.pronghorn.pipe.util.hash.LongHashTable;
import com.ociweb.pronghorn.stage.scheduling.GraphManager;
import com.ociweb.twitter.schema.TwitterEventSchema;

public class UnfollowModuleStage<   T extends Enum<T> & HTTPContentType,
									R extends Enum<R> & HTTPRevision,
									V extends Enum<V> & HTTPVerb,
									H extends Enum<H> & HTTPHeader> extends AbstractRestStage<T,R,V,H> {
	
	private final Pipe<HTTPRequestSchema>[] inputs; 
	private final Pipe<ServerResponseSchema>[] outputs;
	private final Pipe<TwitterEventSchema>[] unsubPipes; 
	private final LongHashTable table;
    	
	public static UnfollowModuleStage newInstance(GraphManager graphManager, Pipe<HTTPRequestSchema>[] inputPipes,
			Pipe<ServerResponseSchema>[] outputPipes, Pipe<TwitterEventSchema>[] unsubPipes,
			LongHashTable table) {
		return new UnfollowModuleStage(graphManager, inputPipes, outputPipes, unsubPipes, table,
										HTTPSpecification.defaultSpec());
	}

	protected UnfollowModuleStage(GraphManager graphManager, 
			                      Pipe<HTTPRequestSchema>[] inputs, 
			                      Pipe<ServerResponseSchema>[] outputs,
			                      Pipe<TwitterEventSchema>[] unsubPipes, 
			                      LongHashTable table, HTTPSpecification httpSpec) {
		super(graphManager, inputs, outputs, httpSpec);
		this.inputs = inputs;
		this.outputs = outputs;
		this.unsubPipes = unsubPipes;
		this.table = table;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}



}
