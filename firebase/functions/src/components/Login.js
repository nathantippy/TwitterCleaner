'use strict';

Object.defineProperty(exports, "__esModule", {
  value: true
});

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

var _react = require('react');

var _react2 = _interopRequireDefault(_react);

var _semanticUiReact = require('semantic-ui-react');

var _reactRouterDom = require('react-router-dom');

var _firebase = require('./config/firebase');

var _firebase2 = _interopRequireDefault(_firebase);

var _LoginModule = require('../scss/Login.module.scss');

var _LoginModule2 = _interopRequireDefault(_LoginModule);

var _NavBar = require('./NavBar');

var _NavBar2 = _interopRequireDefault(_NavBar);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _possibleConstructorReturn(self, call) { if (!self) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return call && (typeof call === "object" || typeof call === "function") ? call : self; }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function, not " + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; }

var appToken = void 0;

var db = _firebase2.default.database();

console.log("Hello from login!");

var Login = function (_Component) {
  _inherits(Login, _Component);

  function Login() {
    var _ref;

    var _temp, _this, _ret;

    _classCallCheck(this, Login);

    for (var _len = arguments.length, args = Array(_len), _key = 0; _key < _len; _key++) {
      args[_key] = arguments[_key];
    }

    return _ret = (_temp = (_this = _possibleConstructorReturn(this, (_ref = Login.__proto__ || Object.getPrototypeOf(Login)).call.apply(_ref, [this].concat(args))), _this), _this.login = function () {
      _firebase.auth.signInWithPopup(_firebase.provider).then(function (res) {
        var username = res.additionalUserInfo.username;
        localStorage.setItem('username', username);
        var user = res.user;
        var accessToken = res.credential.accessToken;
        var accessTokenSecret = res.credential.secret;
        _this.props.handleUserLogin(user, accessToken, accessTokenSecret);
      }).catch(function (err) {
        console.log(err);
      });
    }, _temp), _possibleConstructorReturn(_this, _ret);
  }

  _createClass(Login, [{
    key: 'render',
    value: function render() {
      return _react2.default.createElement(
        'div',
        { className: _LoginModule2.default.body },
        _react2.default.createElement(_NavBar2.default, null),
        _react2.default.createElement('img', {
          className: _LoginModule2.default.tweetImage,
          src: 'http://www.mobyaffiliates.com/wp-content/uploads/2017/11/twitter-down.png'
        }),
        _react2.default.createElement(
          'div',
          { className: _LoginModule2.default.content },
          _react2.default.createElement(
            'h1',
            null,
            'Welcome to Twitter Cleaner!'
          ),
          _react2.default.createElement('br', null),
          _react2.default.createElement('br', null),
          _react2.default.createElement(
            'h3',
            null,
            'Twitter Cleaner suggests Twitter users for you to follow and unfollow',
            _react2.default.createElement('br', null),
            'based on your defined preferences.'
          ),
          _react2.default.createElement(
            'p',
            null,
            'To continue, please login with your Twitter Account.'
          ),
          _react2.default.createElement(
            _semanticUiReact.Button,
            { onClick: this.login, className: _LoginModule2.default.iconButton },
            _react2.default.createElement(_semanticUiReact.Icon, { name: 'twitter', size: 'large', className: _LoginModule2.default.twitterIcon }),
            'Log In'
          )
        )
      );
    }
  }]);

  return Login;
}(_react.Component);

exports.default = Login;