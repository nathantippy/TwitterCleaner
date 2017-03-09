package com.ociweb.twitter.stages.text;

import java.io.IOException;
import java.io.ObjectInputStream;

import com.ociweb.pronghorn.util.BloomFilter;

public class TextContentRouterBloom implements TextContentRouter {

	BloomFilter filter;
	int route;
	int defaultRoute;
	int textFoundRoute;
	
	public void TextContentRouterBloom(ObjectInputStream bloomFilterStream, int defaultRoute, int textFoundRoute) throws ClassNotFoundException, IOException {
		
		filter = (BloomFilter)bloomFilterStream.readObject();
		bloomFilterStream.close();
		this.defaultRoute = defaultRoute;
		this.route = defaultRoute;
		this.textFoundRoute = textFoundRoute;
	}
	
	@Override
	public void clear() {
		route = defaultRoute;
	}

	@Override
	public void text(byte[] backing, int pos, int len, int mask) {
		if (filter.mayContain(backing, pos, len, mask)) {
			route = textFoundRoute;
		} 
	}

	@Override
	public void url(byte[] backing, int pos, int len, int mask) {
		//no change.
	}

	@Override
	public int route() {
		return route;
	}

}
