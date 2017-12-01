'use strict';

Object.defineProperty(exports, "__esModule", {
  value: true
});

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

var _react = require('react');

var _react2 = _interopRequireDefault(_react);

var _TwitterAccountModule = require('../scss/TwitterAccount.module.scss');

var _TwitterAccountModule2 = _interopRequireDefault(_TwitterAccountModule);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _possibleConstructorReturn(self, call) { if (!self) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return call && (typeof call === "object" || typeof call === "function") ? call : self; }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function, not " + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; }

console.log("Hello from Twitter Account");

var TwitterAccount = function (_Component) {
  _inherits(TwitterAccount, _Component);

  function TwitterAccount() {
    _classCallCheck(this, TwitterAccount);

    return _possibleConstructorReturn(this, (TwitterAccount.__proto__ || Object.getPrototypeOf(TwitterAccount)).apply(this, arguments));
  }

  _createClass(TwitterAccount, [{
    key: 'render',
    value: function render() {
      var follow = this.props.isFollow;
      return _react2.default.createElement(
        'div',
        { className: this.props.className },
        _react2.default.createElement(
          'div',
          null,
          _react2.default.createElement(
            'span',
            {
              style: {
                fontSize: '24px'
              } },
            this.props.name
          ),
          _react2.default.createElement(
            'span',
            {
              style: {
                fontSize: '16px'
              } },
            ' @' + this.props.username
          ),
          _react2.default.createElement('br', null),
          _react2.default.createElement(
            'span',
            null,
            'Reason: ' + this.props.reason
          ),
          _react2.default.createElement('br', null),
          _react2.default.createElement(
            'button',
            {
              className: _TwitterAccountModule2.default.ghostButton,
              onClick: this.props.handleRemove },
            'Never ',
            follow === true ? 'Follow' : 'Unfollow'
          ),
          _react2.default.createElement(
            'button',
            {
              className: _TwitterAccountModule2.default.ghostButton,
              onClick: this.props.handleRemove },
            follow === true ? 'Follow' : 'Unfollow'
          )
        )
      );
    }
  }]);

  return TwitterAccount;
}(_react.Component);

exports.default = TwitterAccount;