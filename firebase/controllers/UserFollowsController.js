import express from 'express';
import * as bodyParser from 'body-parser';
import admin from '../auth/Admin';

const userFollowsHandler = express();
userFollowsHandler.use(bodyParser.json());
userFollowsHandler.use(bodyParser.urlencoded({extended: true}));

userFollowsHandler.get('/api/user/:userid/follows', (req, res) => {
    var userID = req.params.userid;
    var ref = admin.database().ref('/users/' + userID + '/follows');
    ref.once("value", function(snapshot){
        res.status(200).send(snapshot.val());
    });
});

userFollowsHandler.post('/api/user/:userid/follows', (req, res) => {
    var userID = req.params.userid;
    var objectToSend = req.body;
    var ref = admin.database().ref('/users/' + userID);
    var followsRef = ref.child('follows');
    followsRef.set(objectToSend);
    res.status(200).send("Posted new follow suggestions!");
});

module.exports = userFollowsHandler;