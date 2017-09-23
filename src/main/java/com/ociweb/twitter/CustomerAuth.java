package com.ociweb.twitter;

public class CustomerAuth {

	public final String name;
	public final String screenName;
	public final String consumerKey;
	public final String consumerSecret;
	public final String token;
	public final String secret;
	public final long   id;
	//add name, screen name and other etc.
	
	public CustomerAuth(String name, String screenName, String consumerKey, String consumerSecret, String token, String secret, long id ) {
		this.name = name;
		this.screenName = screenName;
		this.consumerKey = consumerKey;
		this.consumerSecret = consumerSecret;
		this.token = token;
		this.secret = secret;
		this.id = id;
	}
	
}
