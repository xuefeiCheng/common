import Cookies from 'js-cookie'
import md5 from 'js-md5'
import { httpLoginByEmail, httpLoginOut, httpLoginByName } from '../../api/login'
import sessionStore from '../../utils/sessionStore'

// 构建用户路由表
function iterateRouteItem (routeTreeNode, routeData) {
  routeData.forEach(menuItem => {
    if (menuItem.parentMenu === routeTreeNode.menuId) {
      routeTreeNode.children.push(menuItem)
      menuItem.children = []
      iterateRouteItem(menuItem, routeData)
    }
  })
}

// 用户状态
const user = {
  state: {
    email: '',
    password: '',
    name: Cookies.get('user-name'), // 用户的名字
    token: Cookies.get('Admin-Token'), // 用户从后端获取的token值,
    // token: '71afcddc-b480-433e-aaf2-4e6ba0fa3834',
    menus: sessionStore.getItemData('menus'), // 保存对应用户的菜单设置
    routeTree: []
  },

  mutations: {
    SET_EMAIL: (state, email) => {
      state.email = email
    },
    SET_PASSWORD: (state, password) => {
      state.password = password
    },
    SET_NAME: (state, name) => {
      state.name = name
    },
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_MENUS: (state, menus) => {
      state.menus = menus
    },
    SET_ROUTE_TREE: (state, routeTree) => {
      state.routeTree = routeTree
    },
    LOGOUT: (state) => {
      Cookies.remove('Admin-Token')
      state.token = null
      location.reload()
    }
  },

  actions: {
    LoginByEmail ({ commit }, userInfo) {
      console.log('在user表里面commit', commit)
      const email = userInfo.email.trim()
      const password = userInfo.password.trim()
      return new Promise((resolve, reject) => {
        httpLoginByEmail(email, password).then(response => {
          // console.log('返回的数据', response)

          if (response.code === 200) {
            let menusData = response.data.menu
            // 保存token数据到cookie ，vuex
            if (userInfo.token !== '') {
              Cookies.set('Admin-Token', userInfo.token)
              commit('SET_TOKEN', userInfo.token)
            } else {
              Cookies.set('Admin-Token', response.data.token)
              commit('SET_TOKEN', response.data.token)
            }

            // 保存菜单数据到vuex ，sessionStorage
            commit('SET_MENUS', menusData)
            sessionStore.setItemData('menus', menusData)

            // 生成routeTree数据
            let routeTree = menusData.filter(menuItem => {
              if (menuItem.parentMenu) {
                return false
              } else {
                menuItem.children = []
                return true
              }
            })
            routeTree.forEach(menuItem => {
              iterateRouteItem(menuItem, menusData)
            })
            commit('SET_EMAIL', email)
            commit('SET_PASSWORD', password)
            commit('SET_NAME', email.split('@')[0])
            Cookies.set('user-name', email.split('@')[0])
            commit('SET_ROUTE_TREE', routeTree)
          }
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 通过用户名登录
    loginByName ({commit, state}, userInfo) {
      let name = userInfo.name.trim()
      let password = userInfo.password.trim()
      let salt = userInfo.salt.trim()
      password = md5(name + salt + password)
      let verifyCode = userInfo.verifyCode.trim()
      return new Promise((resolve, reject) => {
        httpLoginByName(name, password, verifyCode).then(response => {
          // console.log('store user  请求数据', response)
          if (response.code === 200) {
            // 菜单默认
            let menusData = response.data.menu
            // 保存token数据到cookie ，vuex
            Cookies.set('Admin-Token', response.data.token)
            commit('SET_TOKEN', response.data.token)
            // 保存菜单数据到vuex ，sessionStorage
            commit('SET_MENUS', menusData)
            sessionStore.setItemData('menus', menusData)
            commit('SET_PASSWORD', password)
            commit('SET_NAME', name)
            Cookies.set('user-name', name)
          }
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },
    // 退出
    LogOut ({ commit, state }) {
      // console.log('在用户js中的state', state)
      return new Promise((resolve, reject) => {
        httpLoginOut(state.token).then((response) => {
          if (response.code === 200) {
            commit('SET_TOKEN', '')
            Cookies.remove('Admin-Token')
            commit('SET_ADD_ROUTER_MAP', [])
            location.reload()
          }
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    }
  }
}

export default user
