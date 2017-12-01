"use strict";

var _Config = require("./Config");

var _Config2 = _interopRequireDefault(_Config);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var admin = require("firebase-admin");

var serviceAccount = _Config2.default.firebaseAdminSDK;
var cred = _Config2.default.firebaseAdmin;
cred.credential = admin.credential.cert(serviceAccount);

admin.initializeApp(cred);

module.exports = admin;