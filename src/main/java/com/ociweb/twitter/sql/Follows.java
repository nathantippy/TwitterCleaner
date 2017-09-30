package com.ociweb.twitter.sql;

/**
 * Created by Eric Wolfe on 9/30/17.
 */
public class Follows {

    public final String settingName;
    public final int userId;

    public Follows(String settingName, int userId){
        this.settingName = settingName;
        this.userId = userId;
    }
}
