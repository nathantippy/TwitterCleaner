import * as functions from 'firebase-functions';
/*import React from 'react';
import {renderToString} from 'react-dom/server';
import App from './src/App';
import TwitterButton from './src/TwitterButton';
import Header from './src/Header';
import getFacts from './src/facts';*/
import express from 'express';
import usersHandler from './controllers/UsersController';
import userFollowsHandler from './controllers/UserFollowsController';
import userUnfollowsHandler from './controllers/UserUnfollowsController';
import userSettingsHandler from './controllers/UserSettingsController';
import twitterHandler from './controllers/TwitterController';

console.log("hello people!");

/*const app = express();

app.use(function(req, res, next) {
    res.header("Access-Control-Allow-Origin", "*");
    res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
    next();
});

app.get('/', (req, res) => {
    res.send("Hi");
});

export let handleRoutes = functions.https.onRequest(app);*/
export let handleUsers = functions.https.onRequest(usersHandler);
export let handleUserFollows = functions.https.onRequest(userFollowsHandler);
export let handleUserUnfollows = functions.https.onRequest(userUnfollowsHandler);
export let handleUserSettings = functions.https.onRequest(userSettingsHandler);
export let handleTwitter = functions.https.onRequest(twitterHandler);
