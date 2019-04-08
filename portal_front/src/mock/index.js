import Mock from 'mockjs'
import loginAPI from './login'
import blackNumberAPI from './blackNumber'
import whiteNumberAPI from './whiteNumber'

// 登录相关
Mock.mock(/\/auth\/login/, 'post', loginAPI.loginByName)
Mock.mock(/\/auth\/logout/, 'get', loginAPI.loginOut)

// 黑名单数据管理
Mock.mock(/\/blackNumber/, 'post', blackNumberAPI.addSingleData)
Mock.mock(/\/allBlackNumber/, 'post', blackNumberAPI.getAllData)
Mock.mock(/\/deleteBlackNumber/, 'post', blackNumberAPI.deleteSingleData)
Mock.mock(/\/searchBlackNumber/, 'post', blackNumberAPI.searchData)

// 白名单数据管理
Mock.mock(/\/whiteNumber\/searchData/, whiteNumberAPI.searchData)
Mock.mock(/\/whiteNumber\/deleteSingleData/, whiteNumberAPI.deleteSingleData)
Mock.mock(/\/whiteNumber\/addSingleData/, whiteNumberAPI.addSingleData)

export default Mock
