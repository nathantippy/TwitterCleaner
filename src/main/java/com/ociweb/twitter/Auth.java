package com.ociweb.twitter;

public class Auth {
	
	public final String consumerKey;
	public final String consumerSecret;
	public final String token;
	public final String secret;
	public final long   id;
	//add name, screen name and other etc.
	
	public Auth(String consumerKey, String consumerSecret, String token, String secret, long id ) {
		this.consumerKey = consumerKey;
		this.consumerSecret = consumerSecret;
		this.token = token;
		this.secret = secret;
		this.id = id;
	}
	
}
