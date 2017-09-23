package com.ociweb.twitter.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseLogin {
    //Global values
    private Connection connection = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    public void readDatabase(){
        String driverName = "com.mysql.jdbc.Driver";
        Class.forName(driverName);

        String serverName = "localhost";
        String mydatabase = "twitter-cleaner";
        String url = "jdbc:mysql://" + serverName + "/" + mydatabase;

        String username = "root";
        String password = "root";
        connection = DriverManager.getConnection(url, username, password);
    }
}