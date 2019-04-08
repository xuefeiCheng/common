// 后台黑明单数据
const whiteNumberData = [
  { streamNumber: '1', phoneNumber: '15909314791', createTime: '20180823000000', delTime: '', state: 1, optName: 'admin' },
  { streamNumber: '2', phoneNumber: '15909314791', createTime: '20180823000000', delTime: '20180823000000', state: 0, optName: 'admin' },
  { streamNumber: '3', phoneNumber: '15909314791', createTime: '20180823000000', delTime: '20180823000000', state: 1, optName: 'admin' },
  { streamNumber: '4', phoneNumber: '15909314791', createTime: '20180823000000', delTime: '20180823000000', state: 0, optName: 'admin' },
  { streamNumber: '5', phoneNumber: '15909314791', createTime: '20180823000000', delTime: '', state: 1, optName: 'admin' },
  { streamNumber: '6', phoneNumber: '15909314791', createTime: '20180823000000', delTime: '20180823000000', state: 0, optName: 'zyc' },
  { streamNumber: '7', phoneNumber: '15909314791', createTime: '20180823000000', delTime: '20180823000000', state: 1, optName: 'admin' },
  { streamNumber: '8', phoneNumber: '15909314791', createTime: '20180823000000', delTime: '20180823000000', state: 1, optName: 'zyc' },
  { streamNumber: '9', phoneNumber: '15909314791', createTime: '20180823000000', delTime: '20180823000000', state: 0, optName: 'admin' },
  { streamNumber: '10', phoneNumber: '15909314791', createTime: '20180823000000', delTime: '', state: 0, optName: 'admin' },
  { streamNumber: '11', phoneNumber: '15909314791', createTime: '20180823000000', delTime: '20180823000000', state: 1, optName: 'admin' },
  { streamNumber: '12', phoneNumber: '15909314791', createTime: '20180823000000', delTime: '20180823000000', state: 1, optName: 'admin' },
  { streamNumber: '13', phoneNumber: '15909314791', createTime: '20180823000000', delTime: '', state: 1, optName: 'admin' },
  { streamNumber: '14', phoneNumber: '15909314791', createTime: '20180823000000', delTime: '20180823000000', state: 1, optName: 'admin' },
  { streamNumber: '15', phoneNumber: '15909314791', createTime: '20180823000000', delTime: '20180823000000', state: 1, optName: 'admin' },
  { streamNumber: '16', phoneNumber: '15909314791', createTime: '20180823000000', delTime: '20180823000000', state: 1, optName: 'admin' },
  { streamNumber: '17', phoneNumber: '15909314791', createTime: '20180823000000', delTime: '', state: 1, optName: 'zyc' },
  { streamNumber: '18', phoneNumber: '15909314791', createTime: '20180823000000', delTime: '20180823000000', state: 1, optName: 'admin' },
  { streamNumber: '19', phoneNumber: '15909314791', createTime: '20180823000000', delTime: '20180823000000', state: 1, optName: 'admin' },
  { streamNumber: '20', phoneNumber: '15909314791', createTime: '20180823000000', delTime: '20180823000000', state: 1, optName: 'admin' },
  { streamNumber: '21', phoneNumber: '15909314791', createTime: '20180823000000', delTime: '20180823000000', state: 1, optName: 'admin' },
  { streamNumber: '22', phoneNumber: '15909314791', createTime: '20180823000000', delTime: '20180823000000', state: 1, optName: 'admin' },
  { streamNumber: '23', phoneNumber: '15909314791', createTime: '20180823000000', delTime: '20180823000000', state: 1, optName: 'admin' },
  { streamNumber: '24', phoneNumber: '15909314791', createTime: '20180823000000', delTime: '20180823000000', state: 1, optName: 'admin' }
]

let streamNumber = 25
export default {
  addSingleData: (request) => {
    const { phoneNumber } = JSON.parse(request.body)
    whiteNumberData.push({ streameNumber: streamNumber++, phoneNumber, createTime: '20180809222222', delTime: '', state: 1, optName: 'admin' })
    return { code: '200', msg: '黑明单添加成功' }
  },
  getAllData: () => {
    return { code: '200', msg: '获取全部黑名单数据成功', data: whiteNumberData }
  },
  // 根据索引删除一条数据
  deleteSingleData: (request) => {
    const { streamNumber } = JSON.parse(request.body)
    let index = whiteNumberData.findIndex(element => {
      if (element.streamNumber === streamNumber) {
        return true
      } else {
        return false
      }
    })
    whiteNumberData.splice(index, 1)
    return { code: '200', msg: '删除成功' }
  },
  // 搜索table数据
  searchData: (request) => {
    let data = JSON.parse(request.body)
    return { code: '200', msg: '搜索黑明单数据成功', data: { total: whiteNumberData.length, whiteNumberTable: whiteNumberData.slice(data.pageIndex * data.pageSize, data.pageIndex * data.pageSize + data.pageSize) } }
  }
}
