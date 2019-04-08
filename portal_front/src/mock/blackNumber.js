// 后台黑明单数据
const blackNumberData = [
  { phoneNumber: '15909314791', createTime: '20180823000000', blackNum: 1 },
  { phoneNumber: '15909314791', createTime: '20180823000000', blackNum: 2 },
  { phoneNumber: '15909314791', createTime: '20180823000000', blackNum: 3 },
  { phoneNumber: '15909314791', createTime: '20180823000000', blackNum: 4 },
  { phoneNumber: '15909314791', createTime: '20180823000000', blackNum: 5 },
  { phoneNumber: '15909314791', createTime: '20180823000000', blackNum: 6 },
  { phoneNumber: '15909314791', createTime: '20180823000000', blackNum: 7 },
  { phoneNumber: '15909314791', createTime: '20180823000000', blackNum: 8 },
  { phoneNumber: '15909314791', createTime: '20180823000000', blackNum: 9 },
  { phoneNumber: '15909314791', createTime: '20180823000000', blackNum: 10 },
  { phoneNumber: '15909314791', createTime: '20180823000000', blackNum: 11 },
  { phoneNumber: '15909314791', createTime: '20180823000000', blackNum: 12 },
  { phoneNumber: '15909314791', createTime: '20180823000000', blackNum: 13 },
  { phoneNumber: '15909314791', createTime: '20180823000000', blackNum: 14 },
  { phoneNumber: '15909314791', createTime: '20180823000000', blackNum: 15 },
  { phoneNumber: '15909314791', createTime: '20180823000000', blackNum: 16 },
  { phoneNumber: '15909314791', createTime: '20180823000000', blackNum: 17 },
  { phoneNumber: '15909314791', createTime: '20180823000000', blackNum: 18 },
  { phoneNumber: '15909314791', createTime: '20180823000000', blackNum: 19 },
  { phoneNumber: '15909314791', createTime: '20180823000000', blackNum: 20 },
  { phoneNumber: '15909314791', createTime: '20180823000000', blackNum: 21 },
  { phoneNumber: '15909314791', createTime: '20180823000000', blackNum: 22 },
  { phoneNumber: '15909314791', createTime: '20180823000000', blackNum: 23 },
  { phoneNumber: '15909314791', createTime: '20180823000000', blackNum: 24 },
  { phoneNumber: '15909314791', createTime: '20180823000000', blackNum: 25 },
  { phoneNumber: '15909314791', createTime: '20180823000000', blackNum: 26 },
  { phoneNumber: '15909314791', createTime: '20180823000000', blackNum: 27 },
  { phoneNumber: '15909314791', createTime: '20180823000000', blackNum: 28 },
  { phoneNumber: '15909314791', createTime: '20180823000000', blackNum: 29 },
  { phoneNumber: '15909314791', createTime: '20180823000000', blackNum: 30 } ]

export default {
  addSingleData: (request) => {
    const { phoneNumber } = JSON.parse(request.body)
    blackNumberData.push({ phoneNumber, createTime: '20180809222222', blackNum: 3 })
    return { code: '200', msg: '黑明单添加成功' }
  },
  getAllData: () => {
    return { code: '200', msg: '获取全部黑名单数据成功', data: blackNumberData }
  },
  deleteSingleData: (request) => {
    const { phoneNumber } = JSON.parse(request.body)
    let index = blackNumberData.findIndex(element => {
      if (element.phoneNumber === phoneNumber) {
        return true
      } else {
        return false
      }
    })
    blackNumberData.splice(index, 1)
    return { code: '200', msg: '删除成功' }
  },
  searchData: (request) => {
    let data = JSON.parse(request.body)
    // let startIndex = data.pageIndex * data.pageSize
    // console.log('mock返回数据', blackNumberData.slice(10, 2))
    return { code: '200', msg: '搜索黑明单数据成功', data: { total: blackNumberData.length, blackNumberTable: blackNumberData.slice(data.pageIndex * data.pageSize, data.pageIndex * data.pageSize + data.pageSize) } }
  }
}
