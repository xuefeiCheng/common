// 分离login的 http请求参数设置
import httpFetch from '../utils/http'

// 登录使用的http api方法
export function httpLoginByEmail (email, password) {
  const data = {
    email,
    password
  }
  return httpFetch({
    url: '/user',
    method: 'post',
    data
  })
}

// 登录通过名字
export function httpLoginByName (name, password) {
  const data = {
    username: name,
    password
  }
  return httpFetch({
    url: '/auth/login',
    method: 'post',
    data
  })
}

// 用户退出方法
export function httpLoginOut (token) {
  return httpFetch({
    url: '/auth/logout',
    method: 'get'
  })
}

// 获取用户菜单
export function httpGetMenuData (token) {
  const data = {
    token
  }
  return httpFetch({
    url: '/menu',
    method: 'post',
    data
  })
}
