package com.ociweb.twitter.mysql;

//For getting users
import java.util.ArrayList;
import java.util.List;
//For connecting to the database
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    //Global values
    private Connection connection = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    public Database(){

    }

    public boolean testConnection(){
        //Test value
        boolean testStatus = false;
        //Setup for connecting to the database
        String driverName = "com.mysql.jdbc.Driver";
        String serverName = "localhost:8889";
        String mydatabase = "twitter-cleaner";
        String url = "jdbc:mysql://" + serverName + "/" + mydatabase;
        String username = "root";
        String password = "root";

        try {
            Class.forName(driverName).newInstance();
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (connection != null){
                testStatus = true;
                try {
                    connection.close();
                } catch (SQLException e){
                    System.out.println(e);
                }
            }
        }
        return testStatus;
    }

    public List<User> getUsers(){
        List<User> users = new ArrayList<User>();
        //Test value
        boolean testStatus = false;
        //Setup for connecting to the database
        String driverName = "com.mysql.jdbc.Driver";
        String serverName = "localhost:8889";
        String mydatabase = "twitter-cleaner";
        String url = "jdbc:mysql://" + serverName + "/" + mydatabase;
        String username = "root";
        String password = "root";

        try {
            Class.forName(driverName).newInstance();
            connection = DriverManager.getConnection(url, username, password);
            preparedStatement = connection.prepareStatement("SELECT * FROM user WHERE username = ?");
            preparedStatement.setString(1, "erwolfe");
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                users.add(new User(
                        resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3),
                        resultSet.getString(4), resultSet.getString(5), resultSet.getString(6),
                        resultSet.getString(7), resultSet.getString(8)));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return users;
    }
}