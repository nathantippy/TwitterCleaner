'use strict';

Object.defineProperty(exports, "__esModule", {
  value: true
});

var _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; };

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

var _react = require('react');

var _react2 = _interopRequireDefault(_react);

var _reactstrap = require('reactstrap');

var _reactRouterDom = require('react-router-dom');

var _axios = require('axios');

var _axios2 = _interopRequireDefault(_axios);

var _isEqual = require('lodash/isEqual');

var _isEqual2 = _interopRequireDefault(_isEqual);

var _AppModule = require('../scss/App.module.scss');

var _AppModule2 = _interopRequireDefault(_AppModule);

var _Loading = require('./Loading');

var _Loading2 = _interopRequireDefault(_Loading);

var _NavBar = require('./NavBar');

var _NavBar2 = _interopRequireDefault(_NavBar);

var _Main = require('./Main');

var _Main2 = _interopRequireDefault(_Main);

var _SettingsPage = require('./SettingsPage');

var _SettingsPage2 = _interopRequireDefault(_SettingsPage);

var _Login = require('./Login');

var _Login2 = _interopRequireDefault(_Login);

var _firebase = require('./config/firebase');

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _possibleConstructorReturn(self, call) { if (!self) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return call && (typeof call === "object" || typeof call === "function") ? call : self; }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function, not " + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; }

var App = function (_Component) {
  _inherits(App, _Component);

  function App() {
    var _ref;

    var _temp, _this, _ret;

    _classCallCheck(this, App);

    for (var _len = arguments.length, args = Array(_len), _key = 0; _key < _len; _key++) {
      args[_key] = arguments[_key];
    }

    return _ret = (_temp = (_this = _possibleConstructorReturn(this, (_ref = App.__proto__ || Object.getPrototypeOf(App)).call.apply(_ref, [this].concat(args))), _this), _this.state = {
      accounts: [],
      isFollow: true,
      user: null,
      loaded: false,
      isFetching: true
    }, _this.getFollow = function () {
      var follow = void 0;
      if (_this.state.isFollow) {
        follow = 'follow';
      } else {
        follow = 'unfollow';
      }
      return follow;
    }, _this.getUsername = function () {
      return localStorage.getItem('username');
    }, _this.getTwitterAccounts = function () {
      var follow = _this.getFollow();
      var username = "rojowolf21"; //this.getUsername();
      _axios2.default.get('https://twittercleaner-7476e.firebaseio.com/users/' + username + '/' + follow + 's.json').then(function (res) {
        _this.setState({ accounts: res.data, isFetching: false });
      }).catch(function (err) {
        console.log(err);
      });
    }, _this.handleUserLogin = function (user, accessToken, accessTokenSecret) {
      _this.setState({
        user: user,
        accessToken: accessToken,
        accessTokenSecret: accessTokenSecret
      });
      _this.getTwitterAccounts();
    }, _this.handleUserLogout = function () {
      _firebase.auth.signOut().then(function () {
        _this.setState({ user: null, accessToken: '', accessTokenSecret: '' });
      });
    }, _this.handleRemove = function (userid) {
      var newAccounts = _this.state.accounts.slice();
      var index = newAccounts.findIndex(function (a) {
        return a.userid === userid;
      });
      newAccounts.splice(index, 1);
      _this.setState({ accounts: newAccounts });
    }, _this.toggleClick = function () {
      _this.setState({
        isFollow: !_this.state.isFollow
      });
    }, _temp), _possibleConstructorReturn(_this, _ret);
  }

  _createClass(App, [{
    key: 'componentWillMount',
    value: function componentWillMount() {
      var _this2 = this;

      _firebase.auth.onAuthStateChanged(function (user) {
        if (user) {
          console.log(user);
          _this2.setState({ user: user, loaded: true });
        } else {
          _this2.setState({ loaded: true });
        }
      });
    }
  }, {
    key: 'componentDidMount',
    value: function componentDidMount() {
      if (localStorage.getItem('username') !== null) {
        this.getTwitterAccounts();
      }
    }
  }, {
    key: 'componentDidUpdate',
    value: function componentDidUpdate(prevProps, prevState) {
      if (prevState.isFollow !== this.state.isFollow) {
        this.setState({ isFetching: true });
        this.props.history.push('' + this.getFollow());
        this.getTwitterAccounts();
      }
    }
  }, {
    key: 'render',
    value: function render() {
      var _this3 = this;

      var _state = this.state,
          accounts = _state.accounts,
          isFollow = _state.isFollow,
          isFetching = _state.isFetching;

      if (!this.state.loaded) return _react2.default.createElement(_Loading2.default, null);
      if (this.state.user === null || this.state.username === null) {
        return _react2.default.createElement(
          'div',
          null,
          _react2.default.createElement(_reactRouterDom.Redirect, { from: '/', to: '/login' }),
          _react2.default.createElement(_reactRouterDom.Route, {
            exact: true,
            path: '/login',
            render: function render(props) {
              return _react2.default.createElement(_Login2.default, _extends({}, props, { handleUserLogin: _this3.handleUserLogin }));
            }
          })
        );
      } else {
        return _react2.default.createElement(
          'div',
          { className: _AppModule2.default.App },
          _react2.default.createElement(
            _reactstrap.Container,
            null,
            _react2.default.createElement(_NavBar2.default, {
              handleUserLogout: this.handleUserLogout,
              user: this.state.user
            }),
            _react2.default.createElement(
              _reactRouterDom.Switch,
              null,
              _react2.default.createElement(_reactRouterDom.Route, { path: '/settings', component: _SettingsPage2.default }),
              _react2.default.createElement(_reactRouterDom.Route, {
                exact: true,
                path: '/follow',
                render: function render(props) {
                  return _react2.default.createElement(_Main2.default, _extends({}, props, {
                    toggle: _this3.toggleClick,
                    isFetching: isFetching,
                    accounts: accounts,
                    isFollow: isFollow,
                    handleRemove: _this3.handleRemove
                  }));
                }
              }),
              _react2.default.createElement(_reactRouterDom.Route, {
                exact: true,
                path: '/unfollow',
                render: function render(props) {
                  return _react2.default.createElement(_Main2.default, _extends({}, props, {
                    toggle: _this3.toggleClick,
                    isFetching: isFetching,
                    accounts: accounts,
                    isFollow: isFollow,
                    handleRemove: _this3.handleRemove
                  }));
                }
              }),
              _react2.default.createElement(_reactRouterDom.Redirect, { from: '/', to: '/follow' })
            )
          )
        );
      }
    }
  }]);

  return App;
}(_react.Component);

exports.default = (0, _reactRouterDom.withRouter)(App);