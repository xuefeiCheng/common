<template>
  <Header class="base-header">
    <Menu mode="horizontal" theme="dark" width="auto" :active-name="currentPath.split('/')[1]" ref="menuHead">
      <div class="layout-logo">
        <img src="static/img/logo4.png" alt="Paris" width="40" height="40" style="margin-top:0.5rem;">
        <span class="header-logoText">业务管理系统</span>
      </div>
      <Icon @click="changeStateCollapsed" :class="rotateIcon" type="md-menu" size="24"></Icon>
      <div class="layout-nav">
        <MenuItem v-bind:name="levelOneMenu.menuId" v-for="levelOneMenu in levelOneMenus" :to="levelOneMenu.url" :key="levelOneMenu.menuId">
          {{levelOneMenu.name}}
        </MenuItem>
      </div>

      <div class="user-out">
        <Dropdown @on-click="handleClick" placement="bottom-start">
          <a href="javascript:void(0)" style="color:white">
            <Avatar icon="ios-person" /> <span style="padding-left:10px">{{name}}</span>
            <Icon :size="18" type="md-arrow-dropdown"></Icon>
          </a>
          <DropdownMenu slot="list">
            <DropdownItem name="updatePwd">修改密码</DropdownItem>
            <DropdownItem name="logout">退出登录</DropdownItem>
          </DropdownMenu>
        </Dropdown>
      </div>
    </Menu>

    <!-- 修改密码模态框 -->
    <Modal
      v-model="pwdModal"
      title="修改密码"
      @on-ok="ok('pwd')"
      @on-cancel="cancel('pwd')"
      :loading="loading"
      :closable="false"
      :mask-closable="false">
      <Form ref="pwd" :model="pwd" :rules="pwdValidate" :label-width="80">
        <Row>
          <Col span="24">
            <FormItem label="用户名" prop="userName">
              <Input v-model="pwd.userName" disabled placeholder="请输入用户名"></Input>
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span="24">
            <FormItem label="原始密码" prop="oldPwd">
              <Input type="password" v-model="pwd.oldPwd" placeholder="请输入原始密码"></Input>
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span="24">
            <FormItem label="新密码" prop="newPwd">
              <Input type="password" v-model="pwd.newPwd" placeholder="请输入新密码"></Input>
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span="24">
            <FormItem label="确认密码" prop="confirmPwd">
              <Input type="password" v-model="pwd.confirmPwd" placeholder="请再次输入密码"></Input>
            </FormItem>
          </Col>
        </Row>
    </Modal>
  </Header>
</template>
<script>
import { mapGetters } from 'vuex'
import Cookies from 'js-cookie'
import sessionStore from '../../../utils/sessionStore'
import md5 from 'js-md5'

export default {
  data () {
    return {
      loading: true,
      pwdModal: false,
      isCollapsed: false,
      MenuText: '综合业务',
      pwd: {
        userName: '',
        oldPwd: '',
        newPwd: '',
        confirmPwd: ''
      },
      pwdValidate: {
        oldPwd: [
          {
            required: true,
            message: '请输入原始密码',
            trigger: 'blur'
          }
        ],
        newPwd: [
          {
            required: true,
            message: '请输入新密码',
            trigger: 'blur'
          },
          {
            type: 'string',
            min: 6,
            message: '密码长度不能小于6位',
            trigger: 'blur'
          }
        ],
        confirmPwd: [
          {
            required: true,
            message: '请输入确认密码',
            trigger: 'blur'
          },
          {
            validator: this.pwdEquals,
            message: '两次密码输入不一致',
            trigger: 'blur'
          }
        ]
      }
    }
  },
  created () {

  },
  computed: {
    ...mapGetters([
      'name',
      'levelOneMenus',
      'currentPath'
    ]),
    rotateIcon () {
      return ['menu-icon', this.isCollapsed ? 'rotate-icon' : '']
    }
  },
  methods: {
    changeStateCollapsed () {
      this.isCollapsed = !this.isCollapsed
      this.$emit('change-side')
    },
    handleClick (name) {
      switch (name) {
        case 'logout':
          this.axios.get('/auth/logout').then((resp) => {
            this.$store.commit('SET_TOKEN', null)
            this.$store.commit('SET_MENUS', null)
            sessionStore.setItemData('menus', null)
            Cookies.remove('Admin-Token')
            Cookies.remove('user-name')
            this.$store.commit('SET_ADD_ROUTER_MAP', [])
            location.href = '/#/login'
          })
          break
        case 'updatePwd':
          this.pwd.userName = this.name
          this.pwdModal = true
          break
        default:
          break
      }
    },
    ok (name) {
      this.$refs[name].validate((valid) => {
        if (valid) {
          let salt = this.md5.salt
          let param = {
            userName: this.pwd.userName,
            oldPwd: md5(this.pwd.userName + salt + this.pwd.oldPwd),
            newPwd: md5(this.pwd.userName + salt + this.pwd.newPwd)
          }
          this.axios.post('/user/updatePwd', param).then(resp => {
            if (resp.code === 200) {
              this.$Message.success('修改成功')
              this.pwdModal = false
              this.cancel(name)
            } else {
              this.$Message.error(resp.msg)
            }
            this.changeLoading()
          }).catch(e => {
            this.changeLoading()
          })
        } else {
          this.changeLoading()
        }
      })
    },
    cancel (name) {
      this.$refs[name].resetFields()
    },
    changeLoading () {
      this.loading = false
      this.$nextTick(() => {
        this.loading = true
      })
    },
    pwdEquals (rule, value, callback) {
      let pwd = this.pwd.newPwd
      if (pwd !== value) {
        callback(new Error())
      } else {
        callback()
      }
    }
  }
}
</script>
<style>
.base-header .ivu-menu-dark{
  background: #031c55;
}
.base-header.ivu-layout-header {
    background: #031c55 none repeat scroll 0 0;
    height: 62px;
    line-height: 62px;
    padding: 0 20px;
}
.base-header .ivu-menu-item-active {
  background-color: #0A39AF;
}
.base-header .ivu-btn-primary {
    background-color: #FFF;
    border-color: 1px solid #dddee1;
    color: #031c55;
}
.dropdown-button {
    background-color: #FFF;
    border-color: 1px solid #dddee1;
    color: #031c55;
}
.menu-icon {
  transition: all 0.3s;
  font-size: 24px;
  margin: 0px 20px;
  color: white;
  float: left;
  position: relative;
  top: 19px;
  height: 23px;
  cursor: pointer;
}
.rotate-icon {
  transform: rotate(-90deg);
}
.layout-logo {
  width: 12em;
  height: 30px;
  background:#031c55;
  border-radius: 3px;
  float: left;
  position: unset;
  top: 15px;
}
.layout-nav {
  margin: 0 auto;
  float: left;
  margin-left: 0px;
}
.user-out {
  float: right;
  color: white;
}

.header-logoText {
    color: #fff;
    font-size: 20px;
    font-weight: 700;
    position: absolute;
    z-index: 1000;
    padding-left: 0.2rem;
}
</style>
