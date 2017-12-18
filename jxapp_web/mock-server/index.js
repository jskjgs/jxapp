let mock = require("mockjs")

let urlPrefix = ''
let delay = 0

let apis = require('./json/data.json')

module.exports = (express, app) => {
  for (let item in apis) {
    let module = apis[item]
    module.forEach(api => {
      let url = urlPrefix + api.url
      let response = api.res
      response = api.mock !== false ? mock.mock(response) : response
      app.post(url, (req, res) => {
        setTimeout(() => {
          res.json(response)
        }, delay)
      })
    })
  }
}
