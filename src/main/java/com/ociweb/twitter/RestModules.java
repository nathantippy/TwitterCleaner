package com.ociweb.twitter;

import java.io.File;

import com.ociweb.pronghorn.network.http.HTTP1xRouterStageConfig;
import com.ociweb.pronghorn.network.http.ModuleConfig;
import com.ociweb.pronghorn.network.http.RouterStageConfig;
import com.ociweb.pronghorn.network.module.FileReadModuleStage;
import com.ociweb.pronghorn.network.schema.HTTPRequestSchema;
import com.ociweb.pronghorn.network.schema.ServerResponseSchema;
import com.ociweb.pronghorn.pipe.Pipe;
import com.ociweb.pronghorn.pipe.util.hash.LongHashTable;
import com.ociweb.pronghorn.stage.scheduling.GraphManager;
import com.ociweb.twitter.schema.TwitterEventSchema;
import com.ociweb.twitter.stages.UnfollowModuleStage;

final class RestModules implements ModuleConfig {

	private final TwitterCleanupServerBehavior twitterCleanupServerBehavior;
	private final Pipe<TwitterEventSchema>[] unsubPipes;
	private final LongHashTable table;
	private final File staticFilesPathRootIndex;

	RestModules(TwitterCleanupServerBehavior twitterCleanupServerBehavior,
			    Pipe<TwitterEventSchema>[] unsubPipes, LongHashTable table, File staticFilesPathRootIndex) {
		this.twitterCleanupServerBehavior = twitterCleanupServerBehavior;
		this.unsubPipes = unsubPipes;
		this.table = table;
		this.staticFilesPathRootIndex = staticFilesPathRootIndex;
	}

	@Override
	public int moduleCount() {
		return 2;
	}

	@Override
	public Pipe<ServerResponseSchema>[] registerModule(
							int moduleInstance, 
							GraphManager graphManager,
							RouterStageConfig routerConfig, 
							Pipe<HTTPRequestSchema>[] inputPipes) {
		
		Pipe<ServerResponseSchema>[] outputPipes = new Pipe[inputPipes.length];
						
		switch (moduleInstance) {
			case 0:
				//static files					
			 	FileReadModuleStage.newInstance(graphManager, inputPipes, outputPipes,
					                           ((HTTP1xRouterStageConfig)routerConfig).httpSpec,
					                           staticFilesPathRootIndex);
				
				routerConfig.registerRoute("/${filePath}");//no headers needed
				break;
			case 1:
				//url for unfollows
				UnfollowModuleStage.newInstance(graphManager, inputPipes, outputPipes, unsubPipes, table);
				
				//keep the header Id constant
				int id = routerConfig.headerId("header".getBytes());
				
				routerConfig.registerRoute("/unfollow/#{twitterId}");//no headers needed
			default:
				
		}
		
		return outputPipes;
	}
}