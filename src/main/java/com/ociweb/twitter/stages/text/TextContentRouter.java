package com.ociweb.twitter.stages.text;

public interface TextContentRouter {

	void clear();

	void text(byte[] backing, int pos, int len, int mask);
	
	void url(byte[] backing, int pos, int len, int mask);

	int route();

}
