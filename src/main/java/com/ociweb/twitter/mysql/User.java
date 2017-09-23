package com.ociweb.twitter.mysql;

public class User {
    public final unsigned int id;
    public final String username;
    public final String password;
    public final String email;
    public final String consumerKey;
    public final String consumerSecret;
    public final String token;
    public final String tokenSecret;

    public User(
            unsigned int id, String username, String password, String email, String consumerKey,
            String consumerSecret, String token, String tokenSecret) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.consumerKey = userToken;
        this.consumerSecret = userKey;
        this.token = secretToken;
        this.tokenSecret = secretKey;
    }
}