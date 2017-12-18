/* 代理服务器 */
var opn = require('opn')
var express = require('express')
var app = express()
var proxyTable = require('../config/proxy')
var proxyMiddleware = require('http-proxy-middleware')
var path = require('path')

Object.keys(proxyTable).forEach(function (context) {
  var options = proxyTable[context]
  if (typeof options === 'string') {
    options = { target: options }
  }
  app.use(proxyMiddleware(options.filter || context, options))
})
app.use(express.static(path.join(__dirname, '../')))
app.listen(9999, function () {
  console.log('server start, listening port:9999')
  opn('http://localhost:9999/dist')
})
