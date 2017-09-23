package com.ociweb.twitter.mysql;

public class User {
    public final int id;
    public final String username;
    public final String password;
    public final String email;
    public final String consumerKey;
    public final String consumerSecret;
    public final String token;
    public final String tokenSecret;

    public User(
            int id, String username, String password, String email, String consumerKey,
            String consumerSecret, String token, String tokenSecret) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        this.token = token;
        this.tokenSecret = tokenSecret;
    }
}