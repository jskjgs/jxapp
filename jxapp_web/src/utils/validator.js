/**
 * 验证密码：
 * @param psd
 * @returns {*}
 */
export const validPsd = function (psd) {
  const len = psd.length
  if (len < 8) {
    return new Error('密码不能长度不小于8位')
  } else if (len > 20) {
    return new Error('密码长度不能大于20位')
  } else if (/^\d+$/.test(psd)) {
    return new Error('密码不能为纯数字')
  } else {
    return
  }
}

export const validAccount = (account) => {
  const len = account.length
  if (account === '') {
    return new Error('请输入管理员账号')
  } else if (len < 6 || len > 20) {
    return new Error('字符需为6位以上，20位以下')
  } else if (!(!(/^\d+$/.test(account)) && /^[a-zA-Z0-9]+$/.test(account))) {
    return new Error('管理员账号只能是字母、字母+数字')
  } else {
    return
  }
}
