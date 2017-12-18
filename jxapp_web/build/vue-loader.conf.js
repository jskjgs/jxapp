var utils = require('./utils')
var config = require('../config')
var isProduction = process.env.NODE_ENV === 'production'

// OPTIMIZE ME
// 生产环境下：autoprexifer 对 @import的scss不起作用
var vueLoaderCfg = {
  loaders: utils.cssLoaders({
    sourceMap: isProduction
      ? config.build.productionSourceMap
      : config.dev.cssSourceMap,
    extract: isProduction
  })
}

if (isProduction) {
  vueLoaderCfg = Object.assign({}, vueLoaderCfg, {
    postcss: [
      require('postcss-import')(),
      require('autoprefixer')({
        browsers: ['last 5 versions']
      })
    ]
  })
}

module.exports = vueLoaderCfg
