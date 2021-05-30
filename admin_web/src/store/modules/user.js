import { login, logout, getInfo } from '@/api/auth'
import { getToken, setToken, removeToken } from '@/utils/auth'
import { resetRouter } from '@/router'

const state = {
  token: getToken(),
  name: '',
  avatar: '',
  introduction: '',
  userinfo: {},
  sysbook: {},
  roles: [],
  menus: [], // 菜单权限
  buttons: [] // 按钮权限
}

const mutations = {
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_USER_INFO: (state, user) => {
    if (user === null) {
      state.avatar = ''
      state.username = ''
      state.name = ''
      state.userinfo = {}
    } else {
      state.avatar = user.avatar
      state.username = user.userName
      state.nickname = user.name
      state.userinfo = user
    }
  },
  SET_SYSBOOK: (state, sysbook) => {
    state.sysbook = sysbook
  },
  SET_ROLES: (state, roles) => {
    state.roles = roles
  },
  SET_MENUS: (state, menus) => {
    state.menus = menus
  },
  SET_BUTTONS: (state, buttons) => {
    state.buttons = buttons
  }
}

const actions = {
  // 用户登录
  login({ commit }, userInfo) {
    const { userName, password, verifyCode, verifyKey } = userInfo
    return new Promise((resolve, reject) => {
      login({ userName: userName.trim(), password: password, verifyCode: verifyCode, verifyKey: verifyKey }).then(response => {
        const respData = response.data
        const tokenValue = respData.prefix + respData.value
        commit('SET_TOKEN', tokenValue)
        setToken(tokenValue)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // 获取用户信息
  getInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      getInfo().then(response => {
        const { data } = response

        if (!data) {
          reject('验证失败，请重新登录')
        }

        // roles must be a non-empty array
        if (!data.roles || data.roles.length <= 0) {
          reject('getInfo: 当前用户无任何权限')
        }
        commit('SET_USER_INFO', data.menus)
        commit('SET_ROLES', data.roles)
        commit('SET_MENUS', data.menus)
        commit('SET_BUTTONS', data.buttons)

        // 数据字典转义内容
        if (data.sysbook) {
          commit('SET_SYSBOOK', data.sysbook)
        }

        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // user logout
  logout({ commit, state }) {
    return new Promise((resolve, reject) => {
      logout(state.token).then(() => {
        removeToken() // must remove  token  first
        commit('SET_USER_INFO', null)
        commit('SET_TOKEN', '')
        commit('SET_ROLES', [])
        commit('SET_MENUS', [])
        commit('SET_BUTTONS', [])
        resetRouter()
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // remove token
  resetToken({ commit }) {
    return new Promise(resolve => {
      removeToken() // must remove  token  first
      commit('SET_USER_INFO', null)
      commit('SET_TOKEN', '')
      commit('SET_ROLES', [])
      commit('SET_MENUS', [])
      commit('SET_BUTTONS', [])
      resolve()
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

