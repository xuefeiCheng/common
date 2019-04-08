export function dateToString (date) {
  let Y = date.getFullYear()
  let M = date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1
  let D = date.getDate() < 10 ? '0' + date.getDate() : date.getDate()
  let h = date.getHours() < 10 ? '0' + date.getHours() : date.getHours()
  let m = date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()
  let s = date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds()
  return Y + '' + M + '' + D + '' + h + '' + m + '' + s
}

export function formatDate (stringdate) {
  let y = stringdate.substr(0, 4)
  let m = stringdate.substr(4, 2)
  let d = stringdate.substr(6, 2)
  return y + '.' + m + '.' + d
}
