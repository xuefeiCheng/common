const app = {
  state: {
    currentPath: '' // 保存当前对应的路由路径
  },
  mutations: {
    SET_CURRENT_PATH (state, currentPath) {
      state.currentPath = currentPath
    }
  },
  actions: {}
}

export default app
