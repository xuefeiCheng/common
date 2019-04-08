import axios from 'axios'
import store from '../store/index.js'
import {Message} from 'iview'

// 请求拦截器
axios.interceptors.request.use(
  config => {
    if (store.getters.token) { // 已登录
      config.headers.token = store.getters.token
    }
    return config
  },
  err => {
    return Promise.reject(err)
  }
)

// 响应拦截器
axios.interceptors.response.use(
  response => {
    let data = response.data
    let code = data.code
    if (code === 402) { // 拦截未登陆请求到登录界面
      store.commit('LOGOUT')
    }
    return data
  },
  error => {
    Message.error('请求异常：' + error.message)
    return Promise.reject(error)
  }
)

export default axios
