// http 请求配置
import Vue from 'vue'
import axios from 'axios'

let axiosOne = axios.create({
  baseURL: process.env.baseURL,
  timeout: 1000,
  headers: {}
})

// request拦截器
axiosOne.interceptors.request.use(config => {
  // Do something before request is sent
  return config
}, error => {
  // Do something with request error
  console.log(' request 在util fetch', error) // for debug
  Promise.reject(error)
})

// respone拦截器
axiosOne.interceptors.response.use(
  response => response.data,
  error => {
    console.log(' respone 在util fetch', error)// for debug
    Vue.$Message.error({
      message: error.message,
      duration: 5 * 1000,
      closable: true
    })
    return Promise.reject(error)
  }
)

export default axiosOne
