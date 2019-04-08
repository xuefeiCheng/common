<template lang="html">
  <div id="app">
    <font style="font-size: 20px">未获取到权限。请到<a :href="user">user系统</a>中分配权限</font>
  </div>
</template>

<script>
export default {
  data () {
    return {
      user: this.thirdSys.user
    }
  }
}
</script>

<style lang="css">
</style>
