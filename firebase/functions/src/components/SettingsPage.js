'use strict';

Object.defineProperty(exports, "__esModule", {
  value: true
});

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

var _react = require('react');

var _react2 = _interopRequireDefault(_react);

var _semanticUiReact = require('semantic-ui-react');

var _reactRouterDom = require('react-router-dom');

var _Modal = require('./Modal');

var _Modal2 = _interopRequireDefault(_Modal);

var _SettingsPageModule = require('../scss/SettingsPage.module.scss');

var _SettingsPageModule2 = _interopRequireDefault(_SettingsPageModule);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _possibleConstructorReturn(self, call) { if (!self) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return call && (typeof call === "object" || typeof call === "function") ? call : self; }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function, not " + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; }

var SettingsPage = function (_Component) {
  _inherits(SettingsPage, _Component);

  function SettingsPage() {
    var _ref;

    var _temp, _this, _ret;

    _classCallCheck(this, SettingsPage);

    for (var _len = arguments.length, args = Array(_len), _key = 0; _key < _len; _key++) {
      args[_key] = arguments[_key];
    }

    return _ret = (_temp = (_this = _possibleConstructorReturn(this, (_ref = SettingsPage.__proto__ || Object.getPrototypeOf(SettingsPage)).call.apply(_ref, [this].concat(args))), _this), _this.state = {
      activeModal: null,
      languages: ['English', 'Spanish', 'Italian', 'French', 'Mandarin', 'Cantonese', 'Japanese'],
      regions: ['North America', 'South America', 'Europe', 'Asia', 'Africa', 'Australia'],
      nsfws: ['Swearing', 'Nudity', 'Pornography', 'Sexual Content (Talking about sex)', 'Gross'],
      misc: ['Book seller', 'Politics']
    }, _temp), _possibleConstructorReturn(_this, _ret);
  }

  _createClass(SettingsPage, [{
    key: 'openModal',
    value: function openModal(modal) {
      this.setState({ activeModal: modal });
    }
  }, {
    key: 'closeModal',
    value: function closeModal() {
      this.setState({ activeModal: null });
    }
  }, {
    key: 'render',
    value: function render() {
      var _this2 = this;

      return _react2.default.createElement(
        'div',
        { className: _SettingsPageModule2.default.container },
        _react2.default.createElement(
          _reactRouterDom.Link,
          { to: '/', className: _SettingsPageModule2.default.backButton },
          _react2.default.createElement(_semanticUiReact.Icon, { name: 'chevron left', size: 'large' }),
          _react2.default.createElement(
            'span',
            null,
            'Back'
          )
        ),
        _react2.default.createElement(
          _Modal2.default,
          {
            isOpen: this.state.activeModal === 'region',
            onClose: function onClose() {
              return _this2.closeModal();
            } },
          _react2.default.createElement(
            'h1',
            { className: _SettingsPageModule2.default.modalHeader },
            'Region Settings'
          ),
          _react2.default.createElement(
            'h2',
            null,
            'Only Follow'
          ),
          _react2.default.createElement(
            'form',
            null,
            this.state.regions.map(function (region) {
              return _react2.default.createElement(
                'label',
                { key: region, 'for': region, className: _SettingsPageModule2.default.control },
                _react2.default.createElement('input', { type: 'checkbox', id: region }),
                ' ',
                ' ' + region
              );
            })
          ),
          _react2.default.createElement(
            'button',
            { className: _SettingsPageModule2.default.update, onClick: function onClick() {
                return _this2.closeModal();
              } },
            'Update'
          ),
          _react2.default.createElement(
            'button',
            { className: _SettingsPageModule2.default.cancel, onClick: function onClick() {
                return _this2.closeModal();
              } },
            'Cancel'
          )
        ),
        _react2.default.createElement(
          _Modal2.default,
          {
            isOpen: this.state.activeModal === 'language',
            onClose: function onClose() {
              return _this2.closeModal();
            } },
          _react2.default.createElement(
            'h1',
            { className: _SettingsPageModule2.default.modalHeader },
            'Language Settings'
          ),
          _react2.default.createElement(
            'h2',
            null,
            'Only Follow'
          ),
          _react2.default.createElement(
            'form',
            null,
            this.state.languages.map(function (language) {
              return _react2.default.createElement(
                'label',
                { key: language, 'for': language, className: _SettingsPageModule2.default.control },
                _react2.default.createElement('input', { type: 'checkbox', id: language }),
                ' ',
                ' ' + language
              );
            })
          ),
          _react2.default.createElement(
            'button',
            { className: _SettingsPageModule2.default.update, onClick: function onClick() {
                return _this2.closeModal();
              } },
            'Update'
          ),
          _react2.default.createElement(
            'button',
            { className: _SettingsPageModule2.default.cancel, onClick: function onClick() {
                return _this2.closeModal();
              } },
            'Cancel'
          )
        ),
        _react2.default.createElement(
          _Modal2.default,
          {
            isOpen: this.state.activeModal === 'nsfw',
            onClose: function onClose() {
              return _this2.closeModal();
            } },
          _react2.default.createElement(
            'h1',
            { className: _SettingsPageModule2.default.modalHeader },
            'NSFW Settings'
          ),
          _react2.default.createElement(
            'h2',
            null,
            'Unfollow'
          ),
          _react2.default.createElement(
            'form',
            null,
            this.state.nsfws.map(function (nsfw) {
              return _react2.default.createElement(
                'label',
                { key: nsfw, 'for': nsfw, className: _SettingsPageModule2.default.control },
                _react2.default.createElement('input', { type: 'checkbox', id: nsfw }),
                ' ',
                ' ' + nsfw
              );
            })
          ),
          _react2.default.createElement(
            'button',
            { className: _SettingsPageModule2.default.update, onClick: function onClick() {
                return _this2.closeModal();
              } },
            'Update'
          ),
          _react2.default.createElement(
            'button',
            { className: _SettingsPageModule2.default.cancel, onClick: function onClick() {
                return _this2.closeModal();
              } },
            'Cancel'
          )
        ),
        _react2.default.createElement(
          _Modal2.default,
          {
            isOpen: this.state.activeModal === 'misc',
            onClose: function onClose() {
              return _this2.closeModal();
            } },
          _react2.default.createElement(
            'h1',
            { className: _SettingsPageModule2.default.modalHeader },
            'Miscellaneous Settings'
          ),
          _react2.default.createElement(
            'h2',
            null,
            'Unfollow'
          ),
          _react2.default.createElement(
            'form',
            null,
            this.state.misc.map(function (mis) {
              return _react2.default.createElement(
                'label',
                { key: mis, 'for': mis, className: _SettingsPageModule2.default.control },
                _react2.default.createElement('input', { type: 'checkbox', id: mis }),
                ' ',
                ' ' + mis
              );
            })
          ),
          _react2.default.createElement(
            'button',
            { className: _SettingsPageModule2.default.update, onClick: function onClick() {
                return _this2.closeModal();
              } },
            'Update'
          ),
          _react2.default.createElement(
            'button',
            { className: _SettingsPageModule2.default.cancel, onClick: function onClick() {
                return _this2.closeModal();
              } },
            'Cancel'
          )
        ),
        _react2.default.createElement(
          'div',
          { className: _SettingsPageModule2.default.flexContainer },
          _react2.default.createElement(
            'a',
            { className: _SettingsPageModule2.default.region, onClick: function onClick() {
                return _this2.openModal('region');
              } },
            _react2.default.createElement(
              'div',
              null,
              'Region Settings'
            )
          ),
          _react2.default.createElement(
            'a',
            {
              className: _SettingsPageModule2.default.language,
              onClick: function onClick() {
                return _this2.openModal('language');
              } },
            'Language Settings'
          ),
          _react2.default.createElement(
            'a',
            { className: _SettingsPageModule2.default.nsfw, onClick: function onClick() {
                return _this2.openModal('nsfw');
              } },
            'NSFW Settings'
          ),
          _react2.default.createElement(
            'a',
            { className: _SettingsPageModule2.default.misc, onClick: function onClick() {
                return _this2.openModal('misc');
              } },
            'Misc. Unfollows'
          )
        )
      );
    }
  }]);

  return SettingsPage;
}(_react.Component);

exports.default = SettingsPage;