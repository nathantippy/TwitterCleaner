"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

var _react = require("react");

var _react2 = _interopRequireDefault(_react);

var _semanticUiReact = require("semantic-ui-react");

var _reactRouterDom = require("react-router-dom");

var _SettingsButtonModule = require("../scss/SettingsButton.module.scss");

var _SettingsButtonModule2 = _interopRequireDefault(_SettingsButtonModule);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _possibleConstructorReturn(self, call) { if (!self) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return call && (typeof call === "object" || typeof call === "function") ? call : self; }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function, not " + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; }

var Settings = function (_Component) {
  _inherits(Settings, _Component);

  function Settings() {
    var _ref;

    var _temp, _this, _ret;

    _classCallCheck(this, Settings);

    for (var _len = arguments.length, args = Array(_len), _key = 0; _key < _len; _key++) {
      args[_key] = arguments[_key];
    }

    return _ret = (_temp = (_this = _possibleConstructorReturn(this, (_ref = Settings.__proto__ || Object.getPrototypeOf(Settings)).call.apply(_ref, [this].concat(args))), _this), _this.state = {
      width: window.innerWidth
    }, _this.handleWindowSizeChange = function () {
      _this.setState({ width: window.innerWidth });
    }, _temp), _possibleConstructorReturn(_this, _ret);
  }

  _createClass(Settings, [{
    key: "componentWillMount",
    value: function componentWillMount() {
      window.addEventListener("resize", this.handleWindowSizeChange);
    }
  }, {
    key: "componentWillUnmount",
    value: function componentWillUnmount() {
      window.removeEventListener("resize", this.handleWindowSizeChange);
    }
  }, {
    key: "render",
    value: function render() {
      var width = this.state.width;

      var isMobile = width <= 500;
      if (isMobile) {
        return _react2.default.createElement(
          _reactRouterDom.Link,
          { to: "/settings" },
          _react2.default.createElement(_semanticUiReact.Icon, { name: "settings", size: "large", className: _SettingsButtonModule2.default.settingsIcon })
        );
      } else {
        return _react2.default.createElement(
          _reactRouterDom.Link,
          { to: "/settings" },
          _react2.default.createElement(
            _semanticUiReact.Button,
            { className: _SettingsButtonModule2.default.iconButton },
            _react2.default.createElement(_semanticUiReact.Icon, { name: "settings", size: "large", className: _SettingsButtonModule2.default.bigIcon }),
            "Settings"
          )
        );
      }
    }
  }]);

  return Settings;
}(_react.Component);

exports.default = Settings;