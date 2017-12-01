import config from './Config';
var admin = require("firebase-admin");

var serviceAccount = config.firebaseAdminSDK;
var cred = config.firebaseAdmin;
cred.credential = admin.credential.cert(serviceAccount);

admin.initializeApp(cred);

module.exports = admin;