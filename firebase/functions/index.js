'use strict';

Object.defineProperty(exports, "__esModule", {
    value: true
});
exports.handleTwitter = exports.handleUserSettings = exports.handleUserUnfollows = exports.handleUserFollows = exports.handleUsers = undefined;

var _firebaseFunctions = require('firebase-functions');

var functions = _interopRequireWildcard(_firebaseFunctions);

var _express = require('express');

var _express2 = _interopRequireDefault(_express);

var _UsersController = require('./controllers/UsersController');

var _UsersController2 = _interopRequireDefault(_UsersController);

var _UserFollowsController = require('./controllers/UserFollowsController');

var _UserFollowsController2 = _interopRequireDefault(_UserFollowsController);

var _UserUnfollowsController = require('./controllers/UserUnfollowsController');

var _UserUnfollowsController2 = _interopRequireDefault(_UserUnfollowsController);

var _UserSettingsController = require('./controllers/UserSettingsController');

var _UserSettingsController2 = _interopRequireDefault(_UserSettingsController);

var _TwitterController = require('./controllers/TwitterController');

var _TwitterController2 = _interopRequireDefault(_TwitterController);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _interopRequireWildcard(obj) { if (obj && obj.__esModule) { return obj; } else { var newObj = {}; if (obj != null) { for (var key in obj) { if (Object.prototype.hasOwnProperty.call(obj, key)) newObj[key] = obj[key]; } } newObj.default = obj; return newObj; } }

/*import React from 'react';
import {renderToString} from 'react-dom/server';
import App from './src/App';
import TwitterButton from './src/TwitterButton';
import Header from './src/Header';
import getFacts from './src/facts';*/
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
var handleUsers = exports.handleUsers = functions.https.onRequest(_UsersController2.default);
var handleUserFollows = exports.handleUserFollows = functions.https.onRequest(_UserFollowsController2.default);
var handleUserUnfollows = exports.handleUserUnfollows = functions.https.onRequest(_UserUnfollowsController2.default);
var handleUserSettings = exports.handleUserSettings = functions.https.onRequest(_UserSettingsController2.default);
var handleTwitter = exports.handleTwitter = functions.https.onRequest(_TwitterController2.default);