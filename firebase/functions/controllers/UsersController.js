'use strict';

var _express = require('express');

var _express2 = _interopRequireDefault(_express);

var _bodyParser = require('body-parser');

var bodyParser = _interopRequireWildcard(_bodyParser);

var _Admin = require('../auth/Admin');

var _Admin2 = _interopRequireDefault(_Admin);

function _interopRequireWildcard(obj) { if (obj && obj.__esModule) { return obj; } else { var newObj = {}; if (obj != null) { for (var key in obj) { if (Object.prototype.hasOwnProperty.call(obj, key)) newObj[key] = obj[key]; } } newObj.default = obj; return newObj; } }

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _defineProperty(obj, key, value) { if (key in obj) { Object.defineProperty(obj, key, { value: value, enumerable: true, configurable: true, writable: true }); } else { obj[key] = value; } return obj; }

var usersHandler = (0, _express2.default)();
usersHandler.use(bodyParser.json());
usersHandler.use(bodyParser.urlencoded({ extended: true }));

usersHandler.get('/api/users', function (req, res) {
    var ref = _Admin2.default.database().ref('/users');
    ref.once("value", function (snapshot) {
        res.status(200).send(snapshot.val());
    });
});

usersHandler.patch('/api/users/:userid.:consumerkey.:consumersecret', function (req, res) {
    var userID = req.params.userid;
    var consumerKey = req.params.consumerkey;
    var consumerSecret = req.params.consumersecret;
    var ref = _Admin2.default.database().ref('/users');
    var objectToSend = _defineProperty({}, userID, {
        follows: {
            info: "null"
        },
        unfollows: {
            info: "null"
        },
        settings: {
            info: "null"
        },
        consumerkey: consumerKey,
        consumersecret: consumerSecret
    });
    ref.update(objectToSend);
    res.status(200).send("Created a new user!");
});

module.exports = usersHandler;