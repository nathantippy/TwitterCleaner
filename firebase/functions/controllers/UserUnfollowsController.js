'use strict';

var _Admin = require('../auth/Admin');

var _Admin2 = _interopRequireDefault(_Admin);

var _express = require('express');

var _express2 = _interopRequireDefault(_express);

var _bodyParser = require('body-parser');

var bodyParser = _interopRequireWildcard(_bodyParser);

function _interopRequireWildcard(obj) { if (obj && obj.__esModule) { return obj; } else { var newObj = {}; if (obj != null) { for (var key in obj) { if (Object.prototype.hasOwnProperty.call(obj, key)) newObj[key] = obj[key]; } } newObj.default = obj; return newObj; } }

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var userUnfollowsHandler = (0, _express2.default)();
userUnfollowsHandler.use(bodyParser.json());
userUnfollowsHandler.use(bodyParser.urlencoded({ extended: true }));

userUnfollowsHandler.get('/api/user/:userid/unfollows', function (req, res) {
    var userID = req.params.userid;
    var ref = _Admin2.default.database().ref('/users/' + userID + '/unfollows');
    ref.once("value", function (snapshot) {
        res.status(200).send(snapshot.val());
    });
});

userUnfollowsHandler.post('/api/user/:userid/unfollows', function (req, res) {
    var userID = req.params.userid;
    var objectToSend = req.body;
    var ref = _Admin2.default.database().ref('/users/' + userID);
    var followsRef = ref.child('unfollows');
    followsRef.set(objectToSend);
    res.status(200).send("Posted new unfollow suggestions!");
});

module.exports = userUnfollowsHandler;