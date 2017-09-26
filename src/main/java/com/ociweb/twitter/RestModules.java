package com.ociweb.twitter;

import java.io.File;
import java.util.List;

import com.ociweb.pronghorn.network.config.HTTPSpecification;
import com.ociweb.pronghorn.network.http.HTTP1xRouterStageConfig;
import com.ociweb.pronghorn.network.http.ModuleConfig;
import com.ociweb.pronghorn.network.http.RouterStageConfig;
import com.ociweb.pronghorn.network.module.FileReadModuleStage;
import com.ociweb.pronghorn.network.schema.HTTPRequestSchema;
import com.ociweb.pronghorn.network.schema.ServerResponseSchema;
import com.ociweb.pronghorn.network.schema.TwitterEventSchema;
import com.ociweb.pronghorn.network.twitter.RequestTwitterFriendshipStage;
import com.ociweb.pronghorn.pipe.Pipe;
import com.ociweb.pronghorn.pipe.PipeConfig;
import com.ociweb.pronghorn.pipe.util.hash.LongHashTable;
import com.ociweb.pronghorn.stage.scheduling.GraphManager;
import com.ociweb.pronghorn.stage.test.ConsoleJSONDumpStage;
import com.ociweb.twitter.stages.ListUsersModuleStage;

final class RestModules implements ModuleConfig {

	private final GraphBuilder twitterCleanupServerBehavior;
	private final Pipe<TwitterEventSchema>[] unsubPipes;
	private final LongHashTable table;
	private final File staticFilesPathRootIndex;
	private final List<CustomerAuth> users;
	

	RestModules(GraphBuilder twitterCleanupServerBehavior,
			    List<CustomerAuth> users, 
			    Pipe<TwitterEventSchema>[] unsubPipes, 
			    LongHashTable table, 
			    File staticFilesPathRootIndex) {
		
		this.twitterCleanupServerBehavior = twitterCleanupServerBehavior;
		this.unsubPipes = unsubPipes;
		this.table = table;
		this.staticFilesPathRootIndex = staticFilesPathRootIndex;
		this.users = users;
	}

	@Override
	public int moduleCount() {
		return 4;
	}

	@Override
	public Pipe<ServerResponseSchema>[] registerModule(
							int moduleInstance, 
							GraphManager graphManager,
							RouterStageConfig routerConfig, 
							Pipe<HTTPRequestSchema>[] inputPipes) {
		
		PipeConfig<ServerResponseSchema> config = ServerResponseSchema.instance.newPipeConfig(8, 1<<10);

		Pipe<ServerResponseSchema>[] outputPipes = null;
		
		switch (moduleInstance) {
			case 0:
			{
				//static files	
				outputPipes = Pipe.buildPipes(inputPipes.length, config);
			    FileReadModuleStage.newInstance(graphManager, inputPipes, outputPipes,
				   	                            routerConfig.httpSpec(), staticFilesPathRootIndex);
				
				routerConfig.registerRoute("/${filePath}");//no headers needed
			}
				break;
			case 1:
			{
				//url for unfollows
				outputPipes = Pipe.buildPipes(inputPipes.length, config);
				ListUsersModuleStage.newInstance(graphManager, inputPipes, outputPipes,
						                        unsubPipes, table, routerConfig.httpSpec());
		
				routerConfig.registerRoute("/unfollow?user=#{twitterId}");//no headers needed
			}
				break;
			case 2:
			{	
				outputPipes = GraphBuilderUtil.twitterFriendshipGraph(graphManager, false, users, inputPipes, config);
				routerConfig.registerRoute("/friendshipCreate?user=#{userId}&friend=#{friendId}");//no headers needed
			}
				break;
			case 3:
			{	
				outputPipes = GraphBuilderUtil.twitterFriendshipGraph(graphManager, true, users, inputPipes, config);
     			routerConfig.registerRoute("/friendshipDestroy?user=#{userId}&friend=#{friendId}");//no headers needed
			}
				break;			
//			case 4:
//			{
//				outputPipes = Pipe.buildPipes(inputPipes.length, config);
//				new AuthStage(graphManager, inputPipes, outputPipes);
//				
//								
//				//what headers?
//				routerConfig.registerRoute("/auth${authArg}");//no headers needed
//			}	
//				break;
			
			default:
				
		}
		
		return outputPipes;
	}


}