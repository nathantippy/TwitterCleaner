package com.ociweb.twitter;

import java.io.File;
import java.util.List;

import com.ociweb.pronghorn.network.NetGraphBuilder;
import com.ociweb.pronghorn.network.ServerCoordinator;
import com.ociweb.pronghorn.network.ServerPipesConfig;
import com.ociweb.pronghorn.network.http.ModuleConfig;
import com.ociweb.pronghorn.network.schema.TwitterEventSchema;
import com.ociweb.pronghorn.pipe.Pipe;
import com.ociweb.pronghorn.pipe.util.hash.LongHashTable;
import com.ociweb.pronghorn.stage.scheduling.GraphManager;

public class GraphBuilder  {

	private final File staticFilesPathRootIndex;	
	private final List<CustomerAuth> users;
	private final boolean isTLS = false;

	private final String bindHost = "127.0.0.1";
	private final int bindPort = 8081;
	
	public GraphBuilder(List<CustomerAuth> users, File staticFilesPathRootIndex) {
		this.users = users;
		this.staticFilesPathRootIndex = staticFilesPathRootIndex;
	}

	public void buildGraph(GraphManager gm) {
		
		GraphManager.addDefaultNota(gm, GraphManager.SCHEDULE_RATE, 10_000_000);

		LongHashTable table = new LongHashTable(LongHashTable.computeBits(users.size()));
		Pipe<TwitterEventSchema>[] unsubcriptions = new Pipe[users.size()];
		int c = 0;
		
		for(CustomerAuth a: users) {
			Pipe<TwitterEventSchema> tweets = GraphBuilderUtil.openTwitterUserStream(gm, a.consumerKey, a.consumerSecret, a.token, a.secret);		
		
			////////Only pass along those users tweets which are about books.
			Pipe<TwitterEventSchema> sellers = tweets;//GraphBuilderUtil.badWordUsers(gm, tweets);
			
			///////We only pass this on as a person to block if they show up 2 times 
			//NOTE: a file name is passed in to remember the repeat count so we can continue of reboot
			Pipe<TwitterEventSchema> repeaters = GraphBuilderUtil.repeatingFieldFilter(gm, sellers, 4, TwitterEventSchema.MSG_USERPOST_101_FIELD_NAME_52, new File("repeaters"+a.id+".dat") );
			
			//////We only pass this user on if we have never recommended that we un follow them before 
			//NOTE: a file name is passed in here so it can save "seen" user names and continue to block duplicates after a "reboot"
			Pipe<TwitterEventSchema> uniques = GraphBuilderUtil.uniqueFieldFilter(gm, repeaters, TwitterEventSchema.MSG_USERPOST_101_FIELD_NAME_52, new File("uniques"+a.id+".dat") );
						
			//these uniques must be sent to the right rest module and added to table so they can be looked up.
			LongHashTable.setItem(table, a.id, c);
			unsubcriptions[c++] = uniques;
	
		}
		
		ServerPipesConfig serverConfig = new ServerPipesConfig(false, isTLS, 1);
		ServerCoordinator serverCoord = new ServerCoordinator(isTLS, bindHost, bindPort, 
				                            serverConfig,"/unfollow?user=1234");
	
		
		NetGraphBuilder.buildHTTPServerGraph(gm, new RestModules(this, unsubcriptions, table, staticFilesPathRootIndex), serverCoord, serverConfig);

	}

}
