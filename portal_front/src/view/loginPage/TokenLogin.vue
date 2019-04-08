<template lang="html">
  <div id="app">
  </div>
</template>

<script>
import Cookies from 'js-cookie'
import sessionStore from '../../utils/sessionStore'

export default {
  created () {
    this.$Spin.show()
    let token = this.$route.query.token
    this.axios.get('/auth/login/' + token).then((resp) => {
      let code = resp.code
      if (code === 200) {
        let menu = resp.data.menu
        menu.map((item) => {
          if (item.parentMenu) { // 存在父节点
            item.url = '/' + item.menuId
          } else { // 不存在父节点
            item.url = '#'
          }
        })
        Cookies.set('Admin-Token', token)
        this.$store.commit('SET_TOKEN', resp.data.token)
        // 保存菜单数据到vuex ，sessionStorage
        this.$store.commit('SET_MENUS', menu)
        sessionStore.setItemData('menus', menu)
        this.$store.commit('SET_NAME', resp.data.user)
        Cookies.set('user-name', resp.data.user)
        this.$router.push({ path: '/' })
      } else {
        this.$Message.error(resp.msg)
        location.href = this.thirdSys.logout
      }
      this.$Spin.hide()
    }).catch((e) => {
      this.$Message.error('登录失败')
      location.href = this.thirdSys.logout
      this.$Spin.hide()
    })
  }
}
</script>

<style lang="css">
</style>
