package com.ociweb.twitter.sql;

/**
 * Created by Eric Wolfe on 9/30/17.
 */
public class Unfollows {

    public final String settingName;
    public final int userId;

    public Unfollows(String settingName, int userId){
        this.settingName = settingName;
        this.userId = userId;
    }
}
