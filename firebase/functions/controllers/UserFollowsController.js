'use strict';

var _express = require('express');

var _express2 = _interopRequireDefault(_express);

var _bodyParser = require('body-parser');

var bodyParser = _interopRequireWildcard(_bodyParser);

var _Admin = require('../auth/Admin');

var _Admin2 = _interopRequireDefault(_Admin);

function _interopRequireWildcard(obj) { if (obj && obj.__esModule) { return obj; } else { var newObj = {}; if (obj != null) { for (var key in obj) { if (Object.prototype.hasOwnProperty.call(obj, key)) newObj[key] = obj[key]; } } newObj.default = obj; return newObj; } }

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var userFollowsHandler = (0, _express2.default)();
userFollowsHandler.use(bodyParser.json());
userFollowsHandler.use(bodyParser.urlencoded({ extended: true }));

userFollowsHandler.get('/api/user/:userid/follows', function (req, res) {
    var userID = req.params.userid;
    var ref = _Admin2.default.database().ref('/users/' + userID + '/follows');
    ref.once("value", function (snapshot) {
        res.status(200).send(snapshot.val());
    });
});

userFollowsHandler.post('/api/user/:userid/follows', function (req, res) {
    var userID = req.params.userid;
    var objectToSend = req.body;
    var ref = _Admin2.default.database().ref('/users/' + userID);
    var followsRef = ref.child('follows');
    followsRef.set(objectToSend);
    res.status(200).send("Posted new follow suggestions!");
});

module.exports = userFollowsHandler;