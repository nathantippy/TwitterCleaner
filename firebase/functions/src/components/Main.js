'use strict';

Object.defineProperty(exports, "__esModule", {
  value: true
});

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

var _react = require('react');

var _react2 = _interopRequireDefault(_react);

var _reactstrap = require('reactstrap');

var _TwitterAccountList = require('./TwitterAccountList');

var _TwitterAccountList2 = _interopRequireDefault(_TwitterAccountList);

var _SettingsButton = require('./SettingsButton');

var _SettingsButton2 = _interopRequireDefault(_SettingsButton);

var _Toggle = require('./Toggle');

var _Toggle2 = _interopRequireDefault(_Toggle);

var _FilterOptions = require('./FilterOptions');

var _FilterOptions2 = _interopRequireDefault(_FilterOptions);

var _MainModule = require('../scss/Main.module.scss');

var _MainModule2 = _interopRequireDefault(_MainModule);

var _FetchingData = require('./FetchingData');

var _FetchingData2 = _interopRequireDefault(_FetchingData);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _possibleConstructorReturn(self, call) { if (!self) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return call && (typeof call === "object" || typeof call === "function") ? call : self; }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function, not " + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; }

var Main = function (_Component) {
  _inherits(Main, _Component);

  function Main() {
    _classCallCheck(this, Main);

    return _possibleConstructorReturn(this, (Main.__proto__ || Object.getPrototypeOf(Main)).apply(this, arguments));
  }

  _createClass(Main, [{
    key: 'render',
    value: function render() {
      return _react2.default.createElement(
        'div',
        null,
        _react2.default.createElement(
          'div',
          { className: _MainModule2.default.container },
          _react2.default.createElement(_SettingsButton2.default, null),
          _react2.default.createElement(_Toggle2.default, this.props),
          _react2.default.createElement(_FilterOptions2.default, null)
        ),
        _react2.default.createElement(
          _reactstrap.Row,
          null,
          _react2.default.createElement(
            _reactstrap.Col,
            { md: '12', sm: '12' },
            this.props.isFetching ? _react2.default.createElement(_FetchingData2.default, null) : _react2.default.createElement(_TwitterAccountList2.default, {
              accounts: this.props.accounts,
              isFollow: this.props.isFollow,
              handleRemove: this.props.handleRemove.bind(this)
            })
          )
        )
      );
    }
  }]);

  return Main;
}(_react.Component);

exports.default = Main;