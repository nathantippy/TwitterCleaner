package com.ociweb.twitter.sql;

/**
 * Created by Eric Wolfe on 9/28/17.
 */

public class Users {
    public final int id;
    public final String username;
    public final String password;
    public final String email;
    public final String twitterName;
    public final String consumerKey;
    public final String consumerSecret;
    public final String token;
    public final String tokenSecret;

    public Users(
            int id, String username, String password, String email, String twitterName, String consumerKey,
            String consumerSecret, String token, String tokenSecret) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.twitterName = twitterName;
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        this.token = token;
        this.tokenSecret = tokenSecret;
    }
}