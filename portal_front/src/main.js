// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.

// 基本组件应用
import Vue from 'vue'
import VueAwesomeSwiper from 'vue-awesome-swiper'
import Viewer from 'v-viewer'
import iview from 'iview'
import 'iview/dist/styles/iview.css'
import echarts from 'echarts'

// 引入配置组件
import App from './App'
import router from './router/index'
import store from './store/index'
// import './mock/index'
import axios from './utils/http.js'

// 使用iview组件
Vue.use(iview)
Vue.use(VueAwesomeSwiper)
Vue.use(Viewer, {
  defaultOptions: {
    navbar: false
  }
})
Vue.config.productionTip = false
Vue.prototype.$echarts = echarts

Vue.prototype.axios = axios

Vue.prototype.getConfigJson = function () {
  axios.get('menuConfig.json').then((res) => {
    let menuConfigData = res.map((item) => {
      let completedItem = {
        path: `/${item.menuId}`,
        menuId: item.menuId,
        name: item.name,
        component: () => import(/* webpackChunkName: "group-foo" */`@/components/${item.componentURL}`)
      }

      let completedChildItem = item.children.map((itemOne) => {
        let childrenItem = {
          path: itemOne.menuId,
          icon: itemOne.icon,
          menuId: itemOne.menuId,
          name: itemOne.name,
          component: () => import(/* webpackChunkName: "group-foo" */`@/view/${itemOne.componentURL}`)
        }

        return childrenItem
      })
      completedItem.children = completedChildItem
      return completedItem
    })

    Vue.prototype.menuConfigData = menuConfigData
  }).catch((e) => {
    console.log('读取前端菜单配置文件失败:' + e)
  })

  axios.get('serverConfig.json').then((res) => {
    axios.defaults.timeout = res.axios.timeout
    axios.defaults.baseURL = res.axios.url
    axios.defaults.withCredentials = true
    Vue.prototype.thirdSys = res.thirdSys
    Vue.prototype.baseURL = res.axios.url
    Vue.prototype.jsonURL = res.axios.json
    Vue.prototype.websocketURL = res.axios.websocket
    Vue.prototype.md5 = res.md5

    /* eslint-disable no-new */
    new Vue({
      el: '#app',
      store,
      router,
      axios,
      components: { App },
      template: '<App/>'
    })
  }).catch((e) => {
    console.log('读取服务器配置文件失败:' + e)
  })
}

Vue.prototype.getConfigJson() // 调用声明的全局方法
