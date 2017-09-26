package com.ociweb.twitter.stages.text;

import java.io.IOException;
import java.io.ObjectInputStream;

import com.ociweb.pronghorn.util.Appendables;
import com.ociweb.pronghorn.util.BloomFilter;

public class TextContentRouterBloomPhrase implements TextContentRouter {

	BloomFilter filter;
	int route;
	int defaultRoute;
	int textFoundRoute;
	int[] previousPos;
	int curPos;
	int curCount;
	
	public TextContentRouterBloomPhrase(ObjectInputStream bloomFilterStream, int defaultRoute, int textFoundRoute, int depth) throws ClassNotFoundException, IOException {
		
		filter = (BloomFilter)bloomFilterStream.readObject();
		bloomFilterStream.close();
		this.defaultRoute = defaultRoute;
		this.route = defaultRoute;
		this.textFoundRoute = textFoundRoute;		
		
		this.previousPos=new int[depth];		
		
	}
	
	@Override
	public void clear() {
		route = defaultRoute;
		curPos = 0;
		curCount = 0;
	}

	@Override
	public void text(byte[] backing, int pos, int len, int mask) {

		curCount++;		
		previousPos[curPos++]=pos;
		if (curPos>=previousPos.length) {
			curPos=0;
		}

		int d = Math.min(previousPos.length, curCount);
		final int limit = pos+len;
		while (d>0) {
			int pIdx = curPos-d;
			if (pIdx<0) {
				pIdx+=previousPos.length;
			}
			int oldPos = previousPos[pIdx];			
			int oldLen = oldPos<limit? limit-oldPos : limit+(backing.length-oldPos);
			
			//NOTE: last portion of hash will get recomputed multiple times, someday this could be replaced with more optimal single pass design.
			if (filter.mayContain(backing, oldPos, oldLen, mask)) {
				route = textFoundRoute;
			}
			
			d--;
		}
		
	}

	@Override
	public void url(byte[] backing, int pos, int len, int mask) {
		//no change.
	}

	@Override
	public int route() {
		//System.err.println(route);
		return route;
	}

}
