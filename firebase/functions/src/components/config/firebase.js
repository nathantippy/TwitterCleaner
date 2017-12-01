'use strict';

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.ref = exports.auth = exports.provider = exports.app = undefined;

var _firebase = require('firebase');

var _firebase2 = _interopRequireDefault(_firebase);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var config = {
  apiKey: '',
  authDomain: '',
  databaseURL: '',
  projectId: '',
  storageBucket: '',
  messagingSenderId: ''
};

var app = exports.app = _firebase2.default.initializeApp(config);
var provider = exports.provider = new _firebase2.default.auth.TwitterAuthProvider();
var auth = exports.auth = _firebase2.default.auth();
var ref = exports.ref = _firebase2.default.database().ref();
exports.default = _firebase2.default;