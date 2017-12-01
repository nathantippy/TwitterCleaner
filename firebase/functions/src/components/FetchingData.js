'use strict';

Object.defineProperty(exports, "__esModule", {
  value: true
});

var _react = require('react');

var _react2 = _interopRequireDefault(_react);

var _FetchingModule = require('../scss/Fetching.module.scss');

var _FetchingModule2 = _interopRequireDefault(_FetchingModule);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var Fetching = function Fetching() {
  return _react2.default.createElement(
    'div',
    { className: _FetchingModule2.default.body },
    _react2.default.createElement('img', { className: _FetchingModule2.default.image, src: 'OCILogo.png' })
  );
};

exports.default = Fetching;