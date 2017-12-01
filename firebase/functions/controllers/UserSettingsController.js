'use strict';

var _Admin = require('../auth/Admin');

var _Admin2 = _interopRequireDefault(_Admin);

var _express = require('express');

var _express2 = _interopRequireDefault(_express);

var _bodyParser = require('body-parser');

var bodyParser = _interopRequireWildcard(_bodyParser);

function _interopRequireWildcard(obj) { if (obj && obj.__esModule) { return obj; } else { var newObj = {}; if (obj != null) { for (var key in obj) { if (Object.prototype.hasOwnProperty.call(obj, key)) newObj[key] = obj[key]; } } newObj.default = obj; return newObj; } }

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var userSettingsHandler = (0, _express2.default)();
userSettingsHandler.use(bodyParser.json());
userSettingsHandler.use(bodyParser.urlencoded({ extended: true }));

userSettingsHandler.get('/api/user/:userid/settings', function (req, res) {
    var userID = req.params.userid;
    var ref = _Admin2.default.database().ref('/users/' + userID + '/settings');
    ref.once("value", function (snapshot) {
        res.status(200).send(snapshot.val());
    });
});

userSettingsHandler.patch('/api/user/:userid/settings', function (req, res) {
    var userID = req.params.userid;
    var jsonData = req.body;
    var ref = _Admin2.default.database().ref('/users/' + userID);
    var settingsRef = ref.child('settings');
    settingsRef.update(jsonData);
    res.status(200).send("Updated user settings!");
});

module.exports = userSettingsHandler;