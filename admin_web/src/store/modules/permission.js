import { asyncRoutes, constantRoutes } from '@/router'

function getRtoueByMenu(menu, routes) {
  for (let index = 0; index < routes.length; index++) {
    const route = routes[index]
    if (route.meta.code === menu.code) {
      if (menu.title) route.meta.title = menu.title
      if (menu.icon) route.meta.icon = menu.icon
      return route
    } else {
      if (route.children) {
        return getRtoueByMenu(menu, route.children)
      }
    }
  }
  return null
}

/**
 * 根据后台menu配置生成route菜单
 * @param {Array} routes 本地route配置
 * @param {Array} menus 后台menus配置
 * @returns {Array}
 */
export function filterAsyncRoutes(routes, menus) {
  const res = []
  menus.forEach(menu => {
    const route = getRtoueByMenu(menu, routes)
    if (route != null) {
      if (menu.children) {
        route.children = filterAsyncRoutes(route.children, menu.children)
      }
      res.push(route)
    }
  })
  return res
}

const state = {
  routes: [],
  addRoutes: []
}

const mutations = {
  SET_ROUTES: (state, routes) => {
    state.addRoutes = routes
    state.routes = constantRoutes.concat(routes)
  }
}

const actions = {
  generateRoutes({ commit }, menus) {
    return new Promise(resolve => {
      const accessedRoutes = filterAsyncRoutes(asyncRoutes, menus)
      commit('SET_ROUTES', accessedRoutes)
      resolve(accessedRoutes)
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
