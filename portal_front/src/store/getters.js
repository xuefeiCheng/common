// 根 store中需要从子模块中暴露出来的state
const getters = {
  currentPath: state => state.app.currentPath,
  token: state => state.user.token,
  name: state => state.user.name,
  routeTree: state => state.user.routeTree,
  menus: state => state.user.menus,
  addRouterMap: state => state.permission.addRouterMap,
  levelOneMenus: state => state.permission.levelOneMenus,
  levelTwoMenus: state => state.permission.levelTwoMenus

}
export default getters
