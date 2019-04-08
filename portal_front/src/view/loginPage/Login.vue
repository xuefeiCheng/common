<template>
  <div>
    <Form ref="login" :model="login" :rules="ruleValidate" :label-width="80">
      <Row>
        <Col span="23">
          <FormItem label="用户名" prop="userName">
              <Input v-model="login.userName" placeholder="请输入用户名"></Input>
          </FormItem>
        </Col>
      </Row>
      <Row>
        <Col span="23">
          <FormItem label="密  码" prop="password">
              <Input type="password" v-model="login.password" placeholder="请输入密码"></Input>
          </FormItem>
        </Col>
      </Row>
      <Row>
        <Col span="21">
          <FormItem label="验证码" prop="verifyCode">
              <Input v-model="login.verifyCode" placeholder="请输入验证码"></Input>
          </FormItem>
        </Col>
        <Col span="2" style="margin-left: 10px;">
          <img :src="codeSrc" style="width: 100%; minWidth: 50px; maxWidth: 110px; cursor: pointer;" @click="getCode()"/>
        </Col>
      </Row>
      <Row>
        <Col span="23">
          <FormItem>
              <Button type="primary" @click="handleSubmit('login')">登录</Button>
              <Button @click="handleReset('login')" style="margin-left: 8px">重置</Button>
          </FormItem>
        </Col>
      </Row>
    </Form>

    <!-- 修改密码提示模态框 -->
    <Modal v-model="alertModal" width="360" :closable="false" :mask-closable="false">
        <p slot="header" style="color:#f60;text-align:center">
            <Icon type="ios-information-circle"></Icon>
            <span>密码过期提示</span>
        </p>
        <div style="text-align:center">
            <p v-html="tipMsg"></p>
        </div>
        <div slot="footer">
            <Button type="error" size="large" long @click="confirm()">确定</Button>
        </div>
    </Modal>

    <!-- 修改密码模态框 -->
    <Modal
      v-model="pwdModal"
      title="修改密码"
      @on-ok="ok('pwd')"
      @on-cancel="cancel('pwd')"
      :closable="false"
      :mask-closable="false">
      <Alert type="success" show-icon>
        <Icon type="ios-bulb-outline" slot="icon"></Icon>
        <font color="red">{{tipMsg}}</font>
      </Alert>

      <Form ref="pwd" :model="pwd" :rules="pwdValidate" :label-width="80">
        <Row>
          <Col span="23">
            <FormItem label="用户名" prop="userName">
                <Input v-model="pwd.userName" disabled placeholder="请输入新密码"></Input>
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span="23">
            <FormItem label="原始密码" prop="oldPwd">
                <Input type="password" v-model="pwd.oldPwd" disabled placeholder="请输入新密码"></Input>
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span="23">
            <FormItem label="新密码" prop="newPwd">
                <Input type="password" v-model="pwd.newPwd" placeholder="请输入新密码"></Input>
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span="23">
            <FormItem label="确认密码" prop="confirmPwd">
                <Input type="password" v-model="pwd.confirmPwd" placeholder="请再次输入密码"></Input>
            </FormItem>
          </Col>
        </Row>
    </Modal>
  </div>
</template>
<script>
import Cookies from 'js-cookie'
import sessionStore from '../../utils/sessionStore'
import md5 from 'js-md5'

export default {
  data () {
    return {
      alertModal: false,
      pwdModal: false,
      tipMsg: '',
      codeSrc: this.baseURL + '/auth/code?timestamp=' + new Date().getTime(),
      login: {
        userName: '',
        password: '',
        verifyCode: ''
      },
      ruleValidate: {
        userName: [
          {
            required: true,
            message: '请输入用户名',
            trigger: 'blur'
          }
        ],
        password: [
          {
            required: true,
            message: '请输入密码',
            trigger: 'blur'
          },
          {
            type: 'string',
            min: 6,
            message: '密码位数不小于六位',
            trigger: 'blur'
          }
        ],
        verifyCode: [
          {
            required: true,
            message: '请输入验证码',
            trigger: 'blur'
          }
        ]
      },
      pwd: {
        userName: '',
        oldPwd: '',
        newPwd: '',
        confirmPwd: ''
      },
      pwdValidate: {
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
  methods: {
    handleSubmit (name) {
      this.$refs[name].validate((valid) => {
        if (valid) {
          let salt = this.md5.salt
          let pwd = md5(this.login.userName + salt + this.login.password)
          let param = {
            userName: this.login.userName,
            password: pwd,
            verifyCode: this.login.verifyCode
          }
          this.axios.post('/auth/login', param).then(resp => {
            if (resp.code === 200) {
              // 判断是否需要提示
              let expired = resp.data.expired
              let firstLogin = resp.data.firstLogin

              // 登录信息写入
              this.loginInfo(resp.data, this.login.userName)

              if (expired !== undefined) {
                // 密码即将过期提示
                this.alertModal = true
                this.tipMsg = '您的密码将在' + expired + '天后过期，过期后将不能继续使用本系统。<br/>请尽快修改密码'
              } else if (firstLogin) {
                // 第一次登录提示
                this.tipMsg = '由于您为第一次登录，为保证安全，需修改密码后方可使用本系统'
                this.pwd.userName = param.userName
                this.pwd.oldPwd = pwd
                this.pwdModal = true
              } else {
                this.$router.push({path: '/'})
              }
            } else {
              this.$Message.error(resp.msg)
            }
          })
        }
      })
    },
    handleReset (name) {
      this.$refs[name].resetFields()
    },
    getCode () {
      this.codeSrc = this.baseURL + '/auth/code?timestamp=' + (new Date()).getTime()
    },
    confirm () {
      this.$router.push({path: '/'})
    },
    ok (name) {
      this.$refs[name].validate((valid) => {
        if (valid) {
          let salt = this.md5.salt
          let param = {
            userName: this.pwd.userName,
            oldPwd: this.pwd.oldPwd,
            newPwd: md5(this.pwd.userName + salt + this.pwd.newPwd)
          }
          this.axios.post('/user/updatePwd', param).then(resp => {
            if (resp.code !== 200) {
              this.$Message.error(resp.msg)
              this.cacel(name)
            } else {
              this.$router.push({path: '/'})
            }
          }).catch(e => {
            this.cancel(name)
          })
        }
      })
    },
    cancel (name) {
      this.$refs[name].resetFields()
      this.logout()
    },
    pwdEquals (rule, value, callback) {
      let pwd = this.pwd.newPwd
      if (pwd !== value) {
        callback(new Error())
      } else {
        callback()
      }
    },
    loginInfo (data, name) {
      // 菜单默认
      let menusData = data.menu
      // 保存token数据到cookie ，vuex
      Cookies.set('Admin-Token', data.token)
      this.$store.commit('SET_TOKEN', data.token)
      // 保存菜单数据到vuex ，sessionStorage
      this.$store.commit('SET_MENUS', menusData)
      sessionStore.setItemData('menus', menusData)
      this.$store.commit('SET_NAME', name)
      Cookies.set('user-name', name)
    },
    logout () {
      this.axios.get('/auth/logout').then((resp) => {
        Cookies.remove('Admin-Token')
        this.$store.commit('SET_TOKEN', null)
        this.$store.commit('SET_MENUS', null)
        sessionStore.setItemData('menus', null)
        this.$store.commit('SET_NAME', null)
        Cookies.remove('user-name')
        this.$store.commit('SET_ADD_ROUTER_MAP', [])
      })
    }
  }
}
</script>
