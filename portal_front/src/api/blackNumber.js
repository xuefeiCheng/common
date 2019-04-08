// 黑名单管理API的使用
import httpFetch from '@/utils/fetch'

// 搜索数据
export function httpSearchData (blackNumberForm) {
  console.log('搜索的黑明大数据', blackNumberForm)
  return httpFetch({
    url: '/searchBlackNumber',
    method: 'post',
    data: blackNumberForm
  })
}

// 添加一条黑名单数据到数据库
export function httpAddSingleData (blackNumberForm) {
  console.log('添加的黑名单数据', blackNumberForm)
  return httpFetch({
    url: '/blackNumber',
    method: 'post',
    data: blackNumberForm
  })
}

// 删除一条数据
export function httpDeleteSingleData (phoneNumber) {
  const data = {
    phoneNumber
  }
  console.log('删除黑名单数据', data)
  return httpFetch({
    url: '/deleteBlackNumber',
    method: 'post',
    data
  })
}

// 获取全部数据
export function httpTableData () {
  return httpFetch({
    url: '/allBlackNumber',
    method: 'post'
  })
}
