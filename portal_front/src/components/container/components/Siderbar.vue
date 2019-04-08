<template>
    <Sider  hide-trigger collapsible :collapsed-width="78" v-model="headCollapsed" style="height:100%">
        <Menu :active-name="currentPath.split('/')[2]" theme="light" width="auto" :class="menuitemClasses" ref='menuSide'>
            <MenuItem :name="currentSiderMenu.path" v-for="currentSiderMenu in levelTwoMenus" :to="currentSiderMenu.path" :key='currentSiderMenu.menuId'>
            <Icon :type="currentSiderMenu.icon"></Icon>
            <span>{{currentSiderMenu.name}}</span>
            </MenuItem>
        </Menu>
    </Sider>
</template>
<script>
import { mapGetters } from 'vuex'
export default {
  data () {
    return {
    }
  },
  props: {
    headCollapsed: Boolean
  },
  created () {
    // console.log('2222')
  },
  watch: {
    '$route' (to, from) {
      this.$nextTick(() => {
        this.$refs.menuSide.updateOpened()
        this.$refs.menuSide.updateActiveName()
      })
    }
  },
  computed: {
    ...mapGetters([
      'levelTwoMenus',
      'currentPath'
    ]),
    menuitemClasses: function () {
      return ['menu-item', this.headCollapsed ? 'collapsed-menu' : '']
    }
  }
}
</script>
<style>
.ivu-layout-sider-children{
  background-color: #fff;
}
.ivu-layout-sider-children>.ivu-menu-item:hover {
  background-color: #0A39AF;
}

.layout-header-bar {
  background: #fff;
  box-shadow: 0 1px 1px rgba(0, 0, 0, 0.1);
}
.layout-logo-left {
  width: 90%;
  height: 30px;
  background: #5b6270;
  border-radius: 3px;
  margin: 15px auto;
}
.menu-icon {
  transition: all 0.3s;
}
.menu-item span {
  display: inline-block;
  overflow: hidden;
  width: 120px;
  text-overflow: ellipsis;
  white-space: nowrap;
  vertical-align: bottom;
  transition: width 0.2s ease 0.2s;
}
.menu-item i {
  transform: translateX(0px);
  transition: font-size 0.2s ease, transform 0.2s ease;
  vertical-align: middle;
  font-size: 16px;
}
.collapsed-menu span {
  width: 0px;
  transition: width 0.2s ease;
}
.collapsed-menu i {
  transform: translateX(5px);
  transition: font-size 0.2s ease 0.2s, transform 0.2s ease 0.2s;
  vertical-align: middle;
  font-size: 22px;
}
</style>
