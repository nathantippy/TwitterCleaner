'use strict';

Object.defineProperty(exports, "__esModule", {
  value: true
});

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

var _react = require('react');

var _react2 = _interopRequireDefault(_react);

var _semanticUiReact = require('semantic-ui-react');

var _Modal = require('./Modal');

var _Modal2 = _interopRequireDefault(_Modal);

var _OptionsModule = require('../scss/Options.module.scss');

var _OptionsModule2 = _interopRequireDefault(_OptionsModule);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _possibleConstructorReturn(self, call) { if (!self) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return call && (typeof call === "object" || typeof call === "function") ? call : self; }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function, not " + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; }

var Options = function (_Component) {
  _inherits(Options, _Component);

  function Options() {
    var _ref;

    var _temp, _this, _ret;

    _classCallCheck(this, Options);

    for (var _len = arguments.length, args = Array(_len), _key = 0; _key < _len; _key++) {
      args[_key] = arguments[_key];
    }

    return _ret = (_temp = (_this = _possibleConstructorReturn(this, (_ref = Options.__proto__ || Object.getPrototypeOf(Options)).call.apply(_ref, [this].concat(args))), _this), _this.state = {
      isModal: false
    }, _temp), _possibleConstructorReturn(_this, _ret);
  }

  _createClass(Options, [{
    key: 'openModal',
    value: function openModal() {
      this.setState({ isModal: true });
    }
  }, {
    key: 'closeModal',
    value: function closeModal() {
      this.setState({ isModal: false });
    }
  }, {
    key: 'render',
    value: function render() {
      var _this2 = this;

      return _react2.default.createElement(
        'div',
        null,
        _react2.default.createElement(
          _Modal2.default,
          { isOpen: this.state.isModal },
          _react2.default.createElement(
            'h1',
            { className: _OptionsModule2.default.title },
            'Choose a Filter:'
          ),
          _react2.default.createElement(
            'form',
            null,
            _react2.default.createElement('input', { type: 'checkbox', id: 'delete', name: 'remove', value: 'delete' }),
            _react2.default.createElement(
              'label',
              { className: _OptionsModule2.default.label, 'for': 'delete' },
              'Remove all suggestions older than a day'
            ),
            _react2.default.createElement('br', null),
            _react2.default.createElement('input', { type: 'checkbox', id: 'sort', name: 'sort', value: 'sort' }),
            _react2.default.createElement(
              'label',
              { className: _OptionsModule2.default.label, 'for': 'sort' },
              'Sort suggestions by reasons'
            )
          ),
          _react2.default.createElement(
            'button',
            { className: _OptionsModule2.default.update, onClick: function onClick() {
                return _this2.closeModal();
              } },
            'Update'
          ),
          _react2.default.createElement(
            'button',
            { className: _OptionsModule2.default.cancel, onClick: function onClick() {
                return _this2.closeModal();
              } },
            'Cancel'
          )
        ),
        _react2.default.createElement(
          _semanticUiReact.Button,
          { className: _OptionsModule2.default.iconButton, onClick: function onClick() {
              return _this2.openModal();
            } },
          _react2.default.createElement(_semanticUiReact.Icon, { name: 'filter', size: 'large', className: _OptionsModule2.default.bigIcon }),
          'Filter Options'
        )
      );
    }
  }]);

  return Options;
}(_react.Component);

exports.default = Options;