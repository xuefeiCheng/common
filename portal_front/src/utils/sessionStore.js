// 工具类，数据存储在sessionStorage里面

const sessionStore = {
  setItemData: (keyName, data) => {
    let jsonData = JSON.stringify(data)
    // console.log('sessionStorage里面存数据', jsonData)
    sessionStorage.setItem(keyName, jsonData)
  },
  getItemData: (keyName) => {
    let data = JSON.parse(sessionStorage.getItem(keyName))
    // console.log('sessinStorage里面取数据', data)
    return data
  }
}

export default sessionStore
