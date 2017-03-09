package com.ociweb.twitter.stages.text;

import com.ociweb.pronghorn.util.TrieParser;
import com.ociweb.pronghorn.util.TrieParserReader;

public class TextContentRouterTrie implements TextContentRouter{

	private TrieParser parser;
	private TrieParserReader parserReader;
	private int route;
	private int defaultRoute;
	
	public void TextContentRouterTrie(TrieParser parser, int defaultRoute) {
		
		this.parser = parser;		
		this.parserReader = new TrieParserReader(3);
		this.defaultRoute = defaultRoute;
		this.route = defaultRoute;
	}
	
	
	@Override
	public void clear() {
		route = defaultRoute;
	}

	@Override
	public void text(byte[] backing, int pos, int len, int mask) {

		int id = (int)TrieParserReader.query(parserReader, parser, backing, pos, len, mask);
		
		if (id!=-1) {
			route = id;
		}
	}

	@Override
	public void url(byte[] backing, int pos, int len, int mask) {
		//do nothing.
	}

	@Override
	public int route() {
		return route;
	}

}
