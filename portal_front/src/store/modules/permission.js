
// 筛选前端可以显示的路由
function filterRouterMap (routerMap, menuIds) {
  let addRouterMap = routerMap.filter(element => {
    return menuIds.some(menuId => {
      if (menuId === element.menuId) {
        if (element.children && element.children.length > 0) {
          element.children = filterRouterMap(element.children, menuIds)
        }
        return true
      } else {
        return false
      }
    })
  })

  return addRouterMap
}

// 添加的路由的重定向路径添加
function filterRouterMapRedirect (addRouterMap) {
  if (addRouterMap.length === 0) { // 未分配权限
    addRouterMap.push({
      path: '/noAuth',
      menuId: 'noAuth',
      component: () => import('@/components/container/MainOne'),
      children: [
        {path: '', component: () => import('@/view/errorPages/NoAuth'), menuId: 'noAuth'}
      ]
    })
  } else {
    addRouterMap.forEach(element => {
      if (element.children && element.children.length > 0) { // 重定向路由，即点击一次菜单后默认加载哪个二级菜单
        element.redirect = element.path + '/' + element.children[0].path
      } else { // 没有二级菜单，则加载提示界面
        element.redirect = '/noAuth'
        element.children.push({
          path: '',
          component: () => import('@/view/errorPages/NoAuth'),
          menuId: element.menuId
        })
      }
    })
  }
  return addRouterMap
}

// 权限验证
const permission = {
  state: {
    addRouterMap: [], //  添加的路由表
    levelOneMenus: [], // 一级菜单标题
    levelTwoMenus: [] // 二级菜单标题
  },
  mutations: {
    SET_ADD_ROUTER_MAP (state, addRouterMap) {
      state.addRouterMap = addRouterMap
    },
    SET_LEVEL_ONE_MENUS: (state, levelOneMenus) => {
      state.levelOneMenus = levelOneMenus
    },
    SET_LEVEL_TWO_MENUS (state, levelTwoMenus) {
      state.levelTwoMenus = levelTwoMenus
    }

  },
  actions: {
    generateAddRouterMapAndMenu ({commit}, menus) {
      let levelOneMenus = []
      let menuIds = []
      menus.forEach(element => {
        menuIds.push(element.menuId)
      })
      let addRouterMap = filterRouterMapRedirect(filterRouterMap(this._vm.menuConfigData, menuIds))
      commit('SET_ADD_ROUTER_MAP', addRouterMap)
      addRouterMap.forEach(element => {
        let levelOneMenu = {}
        levelOneMenu.name = element.name
        levelOneMenu.menuId = element.menuId
        levelOneMenu.url = element.path
        levelOneMenu.icon = 'ios-bulb'
        levelOneMenus.push(levelOneMenu)
      })
      commit('SET_LEVEL_ONE_MENUS', levelOneMenus)
    },
    generateCurrentSiderMenus ({commit, state}, to) {
      // 保存当前的路径
      commit('SET_CURRENT_PATH', to.fullPath)
      let paths = to.fullPath.split('/')
      let choosedRouterMap = state.addRouterMap.filter(element => {
        if (element.menuId === paths[1]) {
          return true
        } else {
          return false
        }
      })[0]

      if (choosedRouterMap) {
        // 判断是否有二级菜单,没有则赋值为空
        if (choosedRouterMap.menuId === choosedRouterMap.children[0].menuId) {
          commit('SET_LEVEL_TWO_MENUS', [])
        } else {
          commit('SET_LEVEL_TWO_MENUS', choosedRouterMap.children)
        }
      }
    }
  }
}

export default permission
