/**
 * Created by PanJiaChen on 16/11/18.
 */

/**
 * @param {string} path
 * @returns {Boolean}
 */
export function isExternal(path) {
  return /^(https?:|mailto:|tel:)/.test(path)
}

/**
 * 密码规则校验
 * @param {string} password
 * @returns {Boolean}
 */
export function validPassword(password) {
  var passwordreg = /(?=.*\d)(?=.*[a-zA-Z])(?=.*[^a-zA-Z0-9]).{6,16}/
  return passwordreg.test(password)
}

/**
 * Email规则校验
 * @param {string} email
 * @returns {Boolean}
 */
export function validEmail(email) {
  var emailreg = /^([a-zA-Z]|[0-9])(\w|\-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/
  return emailreg.test(email)
}
