var fs = require('fs')
var path = require('path')

exports.rewriteCss = () => {
  let cssDir = path.resolve(__dirname, '../dist/static/css')
  fs.readdir(cssDir, function(err, files) {
    files.forEach(item => {
      let url = path.join(cssDir, item)
      fs.readFile(url, 'utf8', (err, data) => {
        fs.writeFile(url, data.replace(/url\(static\//g, 'url(../'), (err, data) => {
          console.log('rewrite success')
        })
      })
    })
  })
}
