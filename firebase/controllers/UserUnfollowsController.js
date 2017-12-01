import admin from '../auth/Admin';
import express from 'express';
import * as bodyParser from 'body-parser';

const userUnfollowsHandler = express();
userUnfollowsHandler.use(bodyParser.json());
userUnfollowsHandler.use(bodyParser.urlencoded({extended: true}));

userUnfollowsHandler.get('/api/user/:userid/unfollows', (req, res) => {
    var userID = req.params.userid;
    var ref = admin.database().ref('/users/' + userID + '/unfollows');
    ref.once("value", function(snapshot){
        res.status(200).send(snapshot.val());
    });
});

userUnfollowsHandler.post('/api/user/:userid/unfollows', (req, res) => {
    var userID = req.params.userid;
    var objectToSend = req.body;
    var ref = admin.database().ref('/users/' + userID);
    var followsRef = ref.child('unfollows');
    followsRef.set(objectToSend);
    res.status(200).send("Posted new unfollow suggestions!");
});


module.exports = userUnfollowsHandler;