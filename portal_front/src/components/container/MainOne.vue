<template>
    <div class="layout">
        <app-header v-on:change-side="changeSide"></app-header>
        <Layout :style="{ minHeight: '100vh'}" class="layout">
                <app-siderbar v-bind:headCollapsed="isCollapsed" v-if="isShowSiderbar" ></app-siderbar>
                <Layout :style="{padding: '0 10px 10px',width: '1000px'}" ref='layoutPane'>
                    <Content :style="{flex:'auto', padding: '10px',  minHeight: '200px', background: '#fff', 'overflow': 'auto'}"  >
                        <router-view></router-view>
                    </Content>
                    <app-footer></app-footer>
                </Layout>
        </Layout>
    </div>
</template>
<script>
import AppHeader from './components/Header'
import AppSiderbar from './components/Siderbar'
import AppFooter from './components/Footer'
export default {
  name: 'main-one',
  data () {
    return {
      isCollapsed: false
    }
  },
  created () {
  },
  computed: {
    isShowSiderbar: function () {
      return this.$store.getters.levelTwoMenus.length !== 0
    }
  },
  components: {
    AppHeader,
    AppSiderbar,
    AppFooter
  },
  methods: {
    changeSide () {
      this.isCollapsed = !this.isCollapsed
    }
  }
}
</script>
<style>
.layout{
    border: 1px solid #d7dde4;
    background: #f5f7f9;
    border-radius: 4px;
    overflow-y: hidden;
    min-width: 1210px;
    height: 100%;
    flex-direction: row!important;
}
</style>
