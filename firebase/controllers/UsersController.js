import express from 'express';
import * as bodyParser from 'body-parser';
import admin from '../auth/Admin';

const usersHandler = express();
usersHandler.use(bodyParser.json());
usersHandler.use(bodyParser.urlencoded({extended: true}));

usersHandler.get('/api/users', (req, res) => {
    var ref = admin.database().ref('/users');
    ref.once("value", function(snapshot){
        res.status(200).send(snapshot.val());
    });
});

usersHandler.patch('/api/users/:userid.:consumerkey.:consumersecret', (req,res) => {
    var userID = req.params.userid;
    var consumerKey = req.params.consumerkey;
    var consumerSecret = req.params.consumersecret;
    var ref = admin.database().ref('/users');
    var objectToSend = {
        [userID]: {
            follows:{
                info: "null"
            },
            unfollows:{
                info: "null"
            },
            settings:{
                info: "null"
            },
            consumerkey: consumerKey,
            consumersecret: consumerSecret
        }
    };
    ref.update(objectToSend);
    res.status(200).send("Created a new user!");
});

module.exports = usersHandler;