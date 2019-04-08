// 黑名单管理API的使用
import httpFetch from '@/utils/fetch'

// 搜索数据
export function httpSearchData (searchFormData) {
  console.log('搜索的黑明大数据', searchFormData)
  return httpFetch({
    url: '/whiteNumber/searchData',
    method: 'post',
    data: searchFormData
  })
}

// 添加一条黑名单数据到数据库
export function httpAddSingleData (addFormData) {
  console.log('添加的黑名单数据', addFormData)
  return httpFetch({
    url: '/whiteNumber/addSingleData',
    method: 'post',
    data: addFormData
  })
}

// 删除一条数据
export function httpDeleteSingleData (deleteIndex) {
  const data = {
    deleteIndex
  }
  console.log('添加的黑名单数据', data)
  return httpFetch({
    url: '/whiteNumber/deleteSingleData',
    method: 'post',
    data
  })
}
