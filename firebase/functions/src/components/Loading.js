'use strict';

Object.defineProperty(exports, "__esModule", {
  value: true
});

var _react = require('react');

var _react2 = _interopRequireDefault(_react);

var _LoadingModule = require('../scss/Loading.module.scss');

var _LoadingModule2 = _interopRequireDefault(_LoadingModule);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var Loading = function Loading() {
  return _react2.default.createElement(
    'div',
    { className: _LoadingModule2.default.body },
    _react2.default.createElement('img', { className: _LoadingModule2.default.image, src: 'OCILogo.png' })
  );
};

exports.default = Loading;