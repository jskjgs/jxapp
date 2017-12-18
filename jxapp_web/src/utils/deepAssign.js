/**
 * 深层assign
 */
export default function () {
  const args = arguments
  return Array.from(args).reduce(function (prev, curr) {
    function typeOf (data) {
      return Object.prototype.toString.call(data).slice(8, -1).toLowerCase()
    }
    let _prev = Object.assign({}, prev)
    let _curr = Object.assign({}, curr)
    ;(function recurse (_prev, _curr) {
      Object.keys(_curr).forEach(function (key) {
        let prevVal = _prev[key]
        let currVal = _curr[key]
        if (typeOf(prevVal) === 'object' && typeOf(currVal) === 'object') {
          recurse(prevVal, currVal)
        } else if (currVal !== undefined) {
          _prev[key] = currVal
        }
      })
    })(_prev, _curr)
    return _prev
  })
}
