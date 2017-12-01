import admin from '../auth/Admin';
import express from 'express';
import * as bodyParser from 'body-parser';

const userSettingsHandler = express();
userSettingsHandler.use(bodyParser.json());
userSettingsHandler.use(bodyParser.urlencoded({extended: true}));

userSettingsHandler.get('/api/user/:userid/settings', (req, res) => {
    var userID = req.params.userid;
    var ref = admin.database().ref('/users/' + userID + '/settings');
    ref.once("value", function(snapshot){
        res.status(200).send(snapshot.val());
    });
});

userSettingsHandler.patch('/api/user/:userid/settings', (req, res) => {
    var userID = req.params.userid;
    var jsonData = req.body;
    var ref = admin.database().ref('/users/' + userID);
    var settingsRef = ref.child('settings');
    settingsRef.update(jsonData);
    res.status(200).send("Updated user settings!");
});

module.exports = userSettingsHandler;