package com.ociweb.twitter;

import java.io.File;

import com.ociweb.pronghorn.network.config.HTTPSpecification;
import com.ociweb.pronghorn.network.http.HTTP1xRouterStageConfig;
import com.ociweb.pronghorn.network.http.ModuleConfig;
import com.ociweb.pronghorn.network.http.RouterStageConfig;
import com.ociweb.pronghorn.network.module.FileReadModuleStage;
import com.ociweb.pronghorn.network.schema.HTTPRequestSchema;
import com.ociweb.pronghorn.network.schema.ServerResponseSchema;
import com.ociweb.pronghorn.network.schema.TwitterEventSchema;
import com.ociweb.pronghorn.pipe.Pipe;
import com.ociweb.pronghorn.pipe.PipeConfig;
import com.ociweb.pronghorn.pipe.util.hash.LongHashTable;
import com.ociweb.pronghorn.stage.scheduling.GraphManager;
import com.ociweb.twitter.stages.ListUsersModuleStage;

final class RestModules implements ModuleConfig {

	private final GraphBuilder twitterCleanupServerBehavior;
	private final Pipe<TwitterEventSchema>[] unsubPipes;
	private final LongHashTable table;
	private final File staticFilesPathRootIndex;

	RestModules(GraphBuilder twitterCleanupServerBehavior,
			    Pipe<TwitterEventSchema>[] unsubPipes, LongHashTable table, File staticFilesPathRootIndex) {
		this.twitterCleanupServerBehavior = twitterCleanupServerBehavior;
		this.unsubPipes = unsubPipes;
		this.table = table;
		this.staticFilesPathRootIndex = staticFilesPathRootIndex;
	}

	@Override
	public int moduleCount() {
		return 3;
	}

	@Override
	public Pipe<ServerResponseSchema>[] registerModule(
							int moduleInstance, 
							GraphManager graphManager,
							RouterStageConfig routerConfig, 
							Pipe<HTTPRequestSchema>[] inputPipes) {
		
		PipeConfig<ServerResponseSchema> config = ServerResponseSchema.instance.newPipeConfig(8, 1<<10);
		
		int i = inputPipes.length;
		Pipe<ServerResponseSchema>[] outputPipes = new Pipe[i];
		while (--i>=0) {
			outputPipes[i] = new Pipe<ServerResponseSchema>(config);
		}
		
		
		
		switch (moduleInstance) {
			case 0:
				//static files					
			    FileReadModuleStage.newInstance(graphManager, inputPipes, outputPipes,
				   	                            routerConfig.httpSpec(), staticFilesPathRootIndex);
				
				routerConfig.registerRoute("/${filePath}");//no headers needed
				break;
			case 1:
				//url for unfollows
				ListUsersModuleStage.newInstance(graphManager, inputPipes, outputPipes,
						                        unsubPipes, table, routerConfig.httpSpec());
		
				routerConfig.registerRoute("/unfollow?user=#{twitterId}");//no headers needed
				break;
			case 2:
				ListUsersModuleStage.newInstance(graphManager, inputPipes, outputPipes, unsubPipes, table, routerConfig.httpSpec());
				routerConfig.registerRoute("/follow?user=#{twitterId}");
			default:
				
		}
		
		return outputPipes;
	}


}