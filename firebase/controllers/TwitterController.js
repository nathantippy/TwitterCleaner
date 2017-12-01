var Twit = require('twit');
var request = require('request');
import express from 'express';
import * as bodyParser from 'body-parser';
import admin from '../auth/Admin';
import config from '../auth/Config';

var configData = config.twitter;

const twitterHandler = express();
twitterHandler.use(bodyParser.json());
twitterHandler.use(bodyParser.urlencoded({extended: true}));

twitterHandler.get('/api/twitter/suggestFollows', (req, res) => {
    var params = {
        q: "programming OR java",
        count: 100 //Maximum limit, don't change
    };
    var T = new Twit(configData);
    T.get('search/tweets', params, function (err, data, response) {
        var tweets = data;
        var jsonData = [];
        for (var user = 0; user < tweets.statuses.length; user++) {
            var userID = tweets.statuses[user].user.id;
            var screenName = tweets.statuses[user].user.screen_name;
            var userName = tweets.statuses[user].user.name;
            var reason = "programming";
            if (findIndexOf(jsonData, userID) == -1) {
                jsonData.push(
                    {
                        "userid": userID,
                        "screenname": screenName,
                        "username": userName,
                        "reason": reason
                    }
                )
            }
        }
        //console.log(jsonData.length);
        request({
            url: "http://localhost:5000/api/user/rojowolf21/follows",
            method: "POST",
            json: true,
            body: jsonData
        }, function (error, response, body) {
            //console.log(response);
        });
        res.send(JSON.stringify(jsonData));
    });
});

twitterHandler.post('/api/twitter/suggestFollows', (req, res) => {
    configData.consumer_key = req.body.consumerKey;
    configData.consumer_secret = req.body.consumerSecret;
    var params = {
        q: req.body.query,
        count: 100 //Maximum limit, don't change
    };
    var T = new Twit(configData);
    T.get('search/tweets', params, function(err, data, response){
        var tweets = data;
        var jsonData = [];
        for (var user = 0; user < tweets.statuses.length; user++) {
            var userID = tweets.statuses[user].user.id;
            var screenName = tweets.statuses[user].user.screen_name;
            var userName = tweets.statuses[user].user.name;
            var reason = "programming";
            if (findIndexOf(jsonData, userID) == -1) {
                jsonData.push(
                    {
                        "userid": userID,
                        "screenname": screenName,
                        "username": userName,
                        "reason": reason
                    }
                )
            }
        }
        request({
            url: "http://localhost:5000/api/user/rojowolf21/follows",
            method: "POST",
            json: true,
            body: jsonData
        }, function(error, response, body){
            //console.log(response);
        });
        res.send(JSON.stringify(jsonData));
    });
});

twitterHandler.get('/api/twitter/suggestUnfollows', (req, res) => {
    var params = {
        count: 800 //Maximum value don't change
    };
    var T = new Twit(configData);
    T.get('statuses/home_timeline', params, function(err, data, response){
        var tweets = data;
        var jsonData = [];
        for (var user = 0; user < tweets.length; user++) {
            var text = tweets[user].text;
            if (text.indexOf("of") !== -1) {
                var userID = tweets[user].user.id;
                var screenName = tweets[user].user.screen_name;
                var userName = tweets[user].user.name;
                var reason = "Using the word of";
                if (findIndexOf(jsonData, userID) == -1) {
                    jsonData.push(
                        {
                            "userid": userID,
                            "screenname": screenName,
                            "username": userName,
                            "reason": reason
                        }
                    )
                }
            }
        }
        request({
            url: "http://localhost:5000/api/user/rojowolf21/unfollows",
            method: "POST",
            json: true,
            body: jsonData
        }, function(error, response, body){
            //console.log(response);
        });
        res.send(JSON.stringify(jsonData));
    });
});

twitterHandler.post('/api/twitter/suggestUnfollows', (req, res) => {
    configData.consumer_key = req.body.consumerKey;
    configData.consumer_secret = req.body.consumerSecret;
    var params = {
        count: 800 //Maximum value don't change
    };
    var T = new Twit(configData);
    T.get('statuses/home_timeline', params, function(err, data, response){
        var tweets = data;
        var jsonData = [];
        for (var user = 0; user < tweets.length; user++) {
            var text = tweets[user].text;
            if (text.indexOf("of") !== -1) {
                var userID = tweets[user].user.id;
                var screenName = tweets[user].user.screen_name;
                var userName = tweets[user].user.name;
                var reason = "Using the word of";
                if (findIndexOf(jsonData, userID) == -1) {
                    jsonData.push(
                        {
                            "userid": userID,
                            "screenname": screenName,
                            "username": userName,
                            "reason": reason
                        }
                    )
                }
            }
        }
        request({
            url: "http://localhost:5000/api/user/rojowolf21/unfollows",
            method: "POST",
            json: true,
            body: jsonData
        }, function(error, response, body){
            //console.log(response);
        });
        res.send(JSON.stringify(jsonData));
    });
});

twitterHandler.post('/api/twitter/follow', (req, res) => {
    configData.consumer_key = req.body.consumerKey;
    configData.consumer_secret = req.body.consumerSecret;
    var params = {
        user_id: req.body.userID
    };
    var T = new Twit(configData);
    T.post('friendships/create', params, function(err, data, response){
        if(err != null){
            //console.log(err);
        }
    });
    res.status(200).send("Created a new friendship!");
});

twitterHandler.post('/api/twitter/unfollow', (req, res) => {
    configData.consumer_key = req.body.consumerKey;
    configData.consumer_secret = req.body.consumerSecret;
    var params = {
        user_id: req.body.userID
    };
    var T = new Twit(configData);
    T.post('friendships/destroy', params, function(err, data, response){
        if(err != null){
            //console.log(err);
        }
    });
    res.status(200).send("Destroyed a friendship!");
});

function findIndexOf(array, newUserID){
    for(var i = 0; i < array.length; i++){
        if(array[i].userid == newUserID){
            //console.log("it happened!");
            return i;
        }
    }
    return -1;
}

module.exports = twitterHandler;
