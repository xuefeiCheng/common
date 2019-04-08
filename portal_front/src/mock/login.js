
const userSuccessData = {
  code: 200,
  msg: 'Ok',
  data: {
    menu: [
      {menuId: 'systemManage', menuName: '系统管理', url: '#'},
      {menuId: 'userManage', menuName: '用户管理', url: '/userManage', parentMenu: 'systemManage'},
      {menuId: 'roleManager', menuName: '角色管理', url: '/role', parentMenu: 'systemManager'},
      {menuId: 'permissionManager', menuName: '权限管理', url: '/permission', parentMenu: 'systemManager'},
      {menuId: 'logManager', menuName: '日志管理', url: '/log', parentMenu: 'systemManager'},
      {menuId: 'business', menuName: '业务管理', url: '#'},
      {menuId: 'black', menuName: '黑名单管理', url: '/black', parentMenu: 'business'},
      {menuId: 'white', menuName: '白名单管理', url: '/white', parentMenu: 'business'},
      {menuId: 'base_info', menuName: '基础数据管理', url: '/council', parentMenu: 'business'},
      {menuId: 'ywfile', menuName: '业务文档管理', url: '/document', parentMenu: 'business'},
      {menuId: 'warning', menuName: '异常告警管理', url: '/warning', parentMenu: 'business'},
      {menuId: 'business_voice', menuName: '语音模板管理', url: '/voice', parentMenu: 'business'},
      {menuId: 'business_review', menuName: '疑似号码取证审核', url: '/review', parentMenu: 'business'},
      {menuId: 'cheatnumber', menuName: '诈骗号码管理', url: '/cheatnumber', parentMenu: 'business'},
      {menuId: 'subscribe', menuName: '订阅管理', url: '/subscribe', parentMenu: 'business'},
      {menuId: 'request', menuName: '历史呼叫查询', url: '/request', parentMenu: 'business'}
    ],
    token: '71afcddc-b480-433e-aaf2-4e6ba0fa3834'
  }
}
const zycSuccessData = {
  code: 200,
  msg: 'Ok',
  data: {
    menu: [
      {menuId: 'listManager', menuName: '名单管理', url: '#'},
      {menuId: 'blackManager', menuName: '黑名单管理', url: '/black', parentMenu: 'listManager'}],
    token: 'zyc'
  }
}
const userErrorData = {
  code: 403,
  msg: '登录失败'
}

export default {
  loginByName: config => {
    // console.log('使用mock，前台传递的参数', config)
    const { username, password } = JSON.parse(config.body)
    if (username === 'root' && password === '123456') {
      return userSuccessData
    } else if (username === 'zyc' && password === '123456') {
      return zycSuccessData
    } else {
      return userErrorData
    }
  },
  loginOut: () => {
    return {
      code: 200,
      msg: 'Ok'
    }
  }
}
