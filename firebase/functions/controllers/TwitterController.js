'use strict';

var _express = require('express');

var _express2 = _interopRequireDefault(_express);

var _bodyParser = require('body-parser');

var bodyParser = _interopRequireWildcard(_bodyParser);

var _Admin = require('../auth/Admin');

var _Admin2 = _interopRequireDefault(_Admin);

var _Config = require('../auth/Config');

var _Config2 = _interopRequireDefault(_Config);

function _interopRequireWildcard(obj) { if (obj && obj.__esModule) { return obj; } else { var newObj = {}; if (obj != null) { for (var key in obj) { if (Object.prototype.hasOwnProperty.call(obj, key)) newObj[key] = obj[key]; } } newObj.default = obj; return newObj; } }

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var Twit = require('twit');
var request = require('request');


var configData = _Config2.default.twitter;

var twitterHandler = (0, _express2.default)();
twitterHandler.use(bodyParser.json());
twitterHandler.use(bodyParser.urlencoded({ extended: true }));

twitterHandler.get('/api/twitter/suggestFollows', function (req, res) {
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
                jsonData.push({
                    "userid": userID,
                    "screenname": screenName,
                    "username": userName,
                    "reason": reason
                });
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

twitterHandler.post('/api/twitter/suggestFollows', function (req, res) {
    configData.consumer_key = req.body.consumerKey;
    configData.consumer_secret = req.body.consumerSecret;
    var params = {
        q: req.body.query,
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
                jsonData.push({
                    "userid": userID,
                    "screenname": screenName,
                    "username": userName,
                    "reason": reason
                });
            }
        }
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

twitterHandler.get('/api/twitter/suggestUnfollows', function (req, res) {
    var params = {
        count: 800 //Maximum value don't change
    };
    var T = new Twit(configData);
    T.get('statuses/home_timeline', params, function (err, data, response) {
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
                    jsonData.push({
                        "userid": userID,
                        "screenname": screenName,
                        "username": userName,
                        "reason": reason
                    });
                }
            }
        }
        request({
            url: "http://localhost:5000/api/user/rojowolf21/unfollows",
            method: "POST",
            json: true,
            body: jsonData
        }, function (error, response, body) {
            //console.log(response);
        });
        res.send(JSON.stringify(jsonData));
    });
});

twitterHandler.post('/api/twitter/suggestUnfollows', function (req, res) {
    configData.consumer_key = req.body.consumerKey;
    configData.consumer_secret = req.body.consumerSecret;
    var params = {
        count: 800 //Maximum value don't change
    };
    var T = new Twit(configData);
    T.get('statuses/home_timeline', params, function (err, data, response) {
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
                    jsonData.push({
                        "userid": userID,
                        "screenname": screenName,
                        "username": userName,
                        "reason": reason
                    });
                }
            }
        }
        request({
            url: "http://localhost:5000/api/user/rojowolf21/unfollows",
            method: "POST",
            json: true,
            body: jsonData
        }, function (error, response, body) {
            //console.log(response);
        });
        res.send(JSON.stringify(jsonData));
    });
});

twitterHandler.post('/api/twitter/follow', function (req, res) {
    configData.consumer_key = req.body.consumerKey;
    configData.consumer_secret = req.body.consumerSecret;
    var params = {
        user_id: req.body.userID
    };
    var T = new Twit(configData);
    T.post('friendships/create', params, function (err, data, response) {
        if (err != null) {
            //console.log(err);
        }
    });
    res.status(200).send("Created a new friendship!");
});

twitterHandler.post('/api/twitter/unfollow', function (req, res) {
    configData.consumer_key = req.body.consumerKey;
    configData.consumer_secret = req.body.consumerSecret;
    var params = {
        user_id: req.body.userID
    };
    var T = new Twit(configData);
    T.post('friendships/destroy', params, function (err, data, response) {
        if (err != null) {
            //console.log(err);
        }
    });
    res.status(200).send("Destroyed a friendship!");
});

function findIndexOf(array, newUserID) {
    for (var i = 0; i < array.length; i++) {
        if (array[i].userid == newUserID) {
            //console.log("it happened!");
            return i;
        }
    }
    return -1;
}

module.exports = twitterHandler;