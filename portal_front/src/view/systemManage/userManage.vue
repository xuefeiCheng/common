<template>
  <div class="div-border">
    <!-- 搜索框 -->
    <Form ref="search" :model="search" :label-width="80">
      <Row>
        <Col span="8">
          <FormItem label="用户名" prop="userName">
            <Input type="text" v-model="search.userName" placeholder="请输入想要查询的用户名" />
          </FormItem>
        </Col>
        <Col span="8">
          <FormItem label="时间段" prop="time">
            <DatePicker type="daterange" placement="bottom-end" :options="options" placeholder="请选择想要查询的时间段"
              style="width: 100%;" show-week-numbers @on-change="timeChange" v-model="search.time"></DatePicker>
          </FormItem>
        </Col>
        <Col span="8">
          <FormItem label="用户状态" prop="state">
            <Select v-model="search.state">
              <Option v-for="item in stateList" :value="item.key" :key="item.key">{{ item.value }}</Option>
            </Select>
          </FormItem>
        </Col>
      </Row>
      <Row>
        <Col span="24" style="text-align: center;">
          <FormItem>
            <Button type="success" icon="ios-search" class="searchBtn" @click="handleSearch">查询</Button>
            <Button type="info"  icon="ios-refresh" class="searchBtn" @click="handleReset">重置</Button>
          </FormItem>
        </Col>
      </Row>
    </Form>

    <Divider/>

    <!-- 操作按钮 -->
    <Button type="info" icon="ios-add-circle-outline" class="opBtn" @click="showAddModal">新增用户</Button>
    <a @click="showDrawer">有疑问？点击查看操作说明</a>
    <!-- 数据表格 -->
    <Table border show-header stripe :columns="columns" :data="data" :loading="loading" @on-sort-change="sortChange"></Table>

    <!-- 分页 -->
    <Page :total="page.total" :current="page.current" :page-size="page.size" show-elevator show-sizer show-total
      @on-change="pageChange" @on-page-size-change="sizeChange" />

    <!-- 操作说明 -->
    <Drawer title="操作说明" :closable="false" v-model="drawer" width="400">
      <Collapse accordion>
        <Panel name="1">
          如何排序
          <p slot="content">用户管理支持根据“<font color="#2db7f5">创建时间</font>”和“<font color="#2db7f5">最后登录时间</font>”进行排序。<br/><br/>
            点击标题栏的“<font color="#2db7f5">创建时间</font>”和“<font color="#2db7f5">最后登录时间</font>”即可按照<font color="red">升序->降序->不排序</font>的顺序进行排序</p>
        </Panel>
        <Panel name="2">
          如何删除用户
            <p slot="content">点击操作栏的“<font color="#2db7f5">注销</font>”按钮即可</p>
        </Panel>
        <Panel name="3">
          如何让“已注销”或“多次登录失败锁定”用户继续使用本系统
          <p slot="content">点击操作栏的“<font color="#2db7f5">解锁</font>”按钮即可</p>
        </Panel>
        <Panel name="4">
          如何让“密码已过期”用户继续使用本系统
          <p slot="content">点击操作栏的“<font color="#2db7f5">编辑</font>”按钮，将<font color="red">密码有效期调大或改为0并点击“解锁”按钮</font>即可</p>
        </Panel>
        <Panel name="5">
          为何不同用户的按钮不一样
          <p slot="content">根据用户状态显示不同的操作按钮。<br/><br/>
            <font color="#2db7f5">“已注销”用户不会显示“注销”按钮</font><br/>
            <font color="#2db7f5">“正常”用户不会显示“解锁”按钮</font></p>
        </Panel>
        <Panel name="6">
          关联角色时为何有些角色不可移除
          <p slot="content">为保证用户可正常使用该系统，系统会为每个用户保留一个基础角色<br/><br/>
            初始用户始终保有“<font color="#2db7f5">超级管理员</font>”和“<font color="#2db7f5">基础操作员</font>”的角色<br/>其他用户始终保有“<font color="#2db7f5">基础操作员</font>”的角色</p>
        </Panel>
        <Panel name="7">
          关联的角色时何时生效
          <p slot="content">关联的角色将在下次登录时生效</p>
        </Panel>
      </Collapse>
    </Drawer>

    <!-- 新增模态框 -->
    <Modal
      v-model="addModal"
      title="新增用户"
      :loading="addLoading"
      @on-ok="addOk"
      @on-cancel="addCancel">
      <Form ref="add" :model="add" :rules="addValidate" :label-width="100">
        <Row>
          <Col span="24">
            <FormItem label="用户名" prop="userName">
              <Input v-model="add.userName" placeholder="请输入用户名"></Input>
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span="24">
            <FormItem label="登录密码" prop="password">
              <Input type="password" v-model="add.password" placeholder="请输入登录密码"></Input>
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span="24">
            <FormItem label="确认密码" prop="pwd">
              <Input type="password" v-model="add.pwd" placeholder="请再次输入登录密码"></Input>
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span="24">
            <FormItem label="真实姓名" prop="realName">
              <Input v-model="add.realName" placeholder="请输入真实姓名"></Input>
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span="24">
            <FormItem label="所属部门" prop="department">
              <Input v-model="add.department" placeholder="请输入所属部门"></Input>
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span="24">
            <FormItem label="联系方式" prop="contact">
              <Input v-model="add.contact" placeholder="请输入联系方式"></Input>
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span="24">
            <FormItem label="密码有效期" prop="pwdValidity">
              <InputNumber v-model="add.pwdValidity" placeholder="请输入密码有效期(0表示永久有效)" :min="0" :max="10000" :step="30"
                :formatter="formatValidity" style="width: 100%;"></InputNumber>
            </FormItem>
          </Col>
        </Row>
      </Form>
    </Modal>

    <!-- 修改模态框 -->
    <Modal
      v-model="updateModal"
      title="修改用户信息"
      :loading="updateLoading"
      @on-ok="updateOk"
      @on-cancel="updateCancel">
      <Form ref="update" :model="update" :rules="updateValidate" :label-width="100">
        <Row>
          <Col span="24">
            <FormItem label="用户名" prop="userName">
              <Input disabled v-model="update.userName" placeholder="请输入用户名"></Input>
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span="24">
            <FormItem label="真实姓名" prop="realName">
              <Input v-model="update.realName" placeholder="请输入真实姓名"></Input>
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span="24">
            <FormItem label="所属部门" prop="department">
              <Input v-model="update.department" placeholder="请输入所属部门"></Input>
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span="24">
            <FormItem label="联系方式" prop="contact">
              <Input v-model="update.contact" placeholder="请输入联系方式"></Input>
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span="24">
            <FormItem label="密码有效期" prop="pwdValidity">
              <InputNumber v-show="pwdDisabled === false" v-model="update.pwdValidity" placeholder="请输入密码有效期(0表示永久有效)" :min="0" :max="10000" :step="30"
                :formatter="formatValidity" style="width: 100%;"></InputNumber>
              <InputNumber v-show="pwdDisabled === true" disabled v-model="update.pwdValidity" placeholder="请输入密码有效期(0表示永久有效)" :min="0" :max="10000" :step="30"
                :formatter="formatValidity" style="width: 100%;"></InputNumber>
            </FormItem>
          </Col>
        </Row>
      </Form>
    </Modal>

    <!-- 角色关联模态框 -->
    <Modal
      v-model="roleModal"
      title="修改用户信息"
      @on-visible-change="recoveryRoles">
      <Transfer
        :data="roleData"
        :titles="['未拥有的角色', '已拥有的角色']"
        :target-keys="ownedRoles"
        @on-change="roleChange"></Transfer>
      <div slot="footer">
        <Button type="primary" @click="roleModal = false">关闭</Button>
      </div>
    </Modal>
  </div>
</template>

<script>
import {mapGetters} from 'vuex'
import md5 from 'js-md5'

export default {
  name: 'userManage',
  data () {
    return {
      drawer: false,
      search: {
        userName: null,
        time: [null, null],
        starTime: null,
        endTime: null,
        state: null
      },
      stateList: [
        {
          key: '1',
          value: '正常'
        }, {
          key: '2',
          value: '密码已过期'
        }, {
          key: '3',
          value: '多次登录失败锁定'
        }, {
          key: '4',
          value: '已注销'
        }
      ],
      options: {
        shortcuts: [
          {
            text: '最近一周',
            value () {
              let end = new Date()
              let start = new Date()
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
              return [start, end]
            }
          },
          {
            text: '最近30天',
            value () {
              const end = new Date()
              const start = new Date()
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
              return [start, end]
            }
          }
        ],
        disabledDate (date) {
          return date && date.valueOf() > Date.now()
        }
      },
      columns: [
        {
          type: 'index',
          width: 50,
          align: 'center'
        }, {
          title: '用户名',
          key: 'userName'
        }, {
          title: '真实姓名',
          key: 'realName'
        }, {
          title: '所属部门',
          key: 'department'
        }, {
          title: '联系方式',
          key: 'contact'
        }, {
          title: '创建时间',
          key: 'createTime',
          sortable: true,
          render: (h, params) => {
            return h('span', this.formatTime(params.row.createTime))
          }
        }, {
          title: '最后登录时间',
          key: 'lastLoginTime',
          sortable: true,
          render: (h, params) => {
            return h('span', this.formatTime(params.row.lastLoginTime))
          }
        }, {
          title: '最后修改密码时间',
          key: 'lastPwdTime',
          render: (h, params) => {
            return h('span', this.formatTime(params.row.lastPwdTime))
          }
        }, {
          title: '密码有效期',
          key: 'pwdValidity',
          width: 80,
          render: (h, params) => {
            let validity = params.row.pwdValidity
            let validityText
            if (validity === 0) {
              validityText = '永久'
            } else {
              validityText = validity + '天'
            }
            return h('span', validityText)
          }
        }, {
          title: '状态',
          key: 'state',
          render: (h, params) => {
            let state = params.row.state
            let stateText = ''
            for (let i = 0; i < this.stateList.length; i++) {
              if (this.stateList[i].key === state) {
                stateText = this.stateList[i].value
                break
              }
            }
            return h('span', stateText)
          }
        }, {
          title: '操作',
          width: 260,
          render: (h, params) => {
            let btns = [
              h('Button', {
                props: {
                  type: 'info',
                  size: 'small'
                },
                style: {
                  marginRight: '10px'
                },
                on: {
                  click: () => {
                    this.update.userName = params.row.userName
                    this.update.realName = params.row.realName
                    this.update.department = params.row.department
                    this.update.contact = params.row.contact
                    this.update.pwdValidity = params.row.pwdValidity
                    if (params.row.userName === 'root') {
                      this.pwdDisabled = true
                    } else {
                      this.pwdDisabled = false
                    }
                    this.updateModal = true
                  }
                }
              }, '编辑'),
              h('Button', {
                props: {
                  type: 'success',
                  size: 'small'
                },
                style: {
                  marginRight: '10px'
                },
                on: {
                  click: () => {
                    this.relateRole(params.row.userName)
                  }
                }
              }, '关联角色')
            ]

            if (params.row.userName !== this.name) {
              let state = params.row.state
              if (state !== '1') {
                btns.push(
                  h('Button', {
                    props: {
                      type: 'primary',
                      size: 'small'
                    },
                    style: {
                      marginRight: '10px'
                    },
                    on: {
                      click: () => {
                        this.updateState(params.row.userName, '1', '正常')
                      }
                    }
                  }, '解锁')
                )
              }
              if (state !== '4' && params.row.userName !== 'root') {
                btns.push(
                  h('Button', {
                    props: {
                      type: 'error',
                      size: 'small'
                    },
                    on: {
                      click: () => {
                        this.updateState(params.row.userName, '4', '已注销')
                      }
                    }
                  }, '注销')
                )
              }
            }
            return h('div', btns)
          }
        }
      ],
      data: [],
      loading: true,
      page: {
        total: 0,
        size: 10,
        current: 1,
        order: null,
        orderType: null
      },
      addModal: false,
      addLoading: true,
      add: {
        userName: null,
        password: null,
        pwd: null,
        realName: null,
        department: null,
        contact: null,
        pwdValidity: null
      },
      addValidate: {
        userName: [
          {
            required: true,
            message: '请输入用户名',
            trigger: 'blur'
          }, {
            validator: this.checkLen,
            min: 3,
            max: 50,
            message: '用户名需在3~50个字符之间(一个中文算两个字符)',
            trigger: 'blur'
          }
        ],
        password: [
          {
            required: true,
            message: '请输入密码',
            trigger: 'blur'
          }, {
            validator: this.checkLen,
            min: 6,
            message: '密码不能小于6位'
          }
        ],
        pwd: [
          {
            required: true,
            message: '请输入确认密码',
            trigger: 'blur'
          }, {
            validator: this.pwdEquals,
            compare: 'add.password',
            message: '两次密码输入不一致',
            tigger: 'blur'
          }
        ],
        realName: [
          {
            validator: this.checkLen,
            max: 50,
            message: '真实姓名不能超过50个字符(一个中文算两个字符)',
            trigger: 'blur'
          }
        ],
        department: [
          {
            validation: this.checkLen,
            max: 200,
            message: '所属部门不能超过200个字符(一个中文算两个字符)',
            trigger: 'blur'
          }
        ],
        contact: [
          {
            validation: this.checkLen,
            max: 50,
            message: '联系方式不能超过50个字符(一个中文算两个字符)',
            trigger: 'blur'
          }
        ],
        pwdValidity: [
          {
            type: 'number',
            required: true,
            message: '请输入密码有效期',
            trigger: 'blur'
          }
        ]
      },
      updateModal: false,
      updateLoading: true,
      pwdDisabled: false,
      update: {
        userName: null,
        realName: null,
        department: null,
        contact: null,
        pwdValidity: null
      },
      updateValidate: {
        userName: [
          {
            required: true,
            message: '请输入用户名',
            trigger: 'blur'
          }, {
            validator: this.checkLen,
            min: 3,
            max: 50,
            message: '用户名需在3~50个字符之间(一个中文算两个字符)',
            trigger: 'blur'
          }
        ],
        realName: [
          {
            validator: this.checkLen,
            max: 50,
            message: '真实姓名不能超过50个字符(一个中文算两个字符)',
            trigger: 'blur'
          }
        ],
        department: [
          {
            validation: this.checkLen,
            max: 200,
            message: '所属部门不能超过200个字符(一个中文算两个字符)',
            trigger: 'blur'
          }
        ],
        contact: [
          {
            validation: this.checkLen,
            max: 50,
            message: '联系方式不能超过50个字符(一个中文算两个字符)',
            trigger: 'blur'
          }
        ],
        pwdValidity: [
          {
            type: 'number',
            required: true,
            message: '请输入密码有效期',
            trigger: 'blur'
          }
        ]
      },
      roleModal: false,
      opUser: null,
      roleData: [],
      ownedRoles: []
    }
  },
  computed: {
    ...mapGetters([
      'name'
    ])
  },
  mounted () {
    this.init()

    // 初始化所有角色信息
    this.axios.get('/role').then(resp => {
      if (resp.code === 200) {
        resp.data.map(item => {
          this.roleData.push({
            key: item.roleId,
            label: item.roleName
          })
        })
      }
    })
  },
  methods: {
    showDrawer () {
      this.drawer = true
    },
    init () {
      this.requestSearch()
    },
    handleSearch () {
      this.requestSearch()
    },
    handleReset () {
      this.search.startTime = null
      this.search.endTime = null
      this.page.order = null
      this.page.orderType = null
      this.$refs.search.resetFields()
      this.requestSearch()
    },
    sortChange (column) {
      if (column.order === 'normal') {
        this.page.order = null
        this.page.orderType = null
      } else {
        this.page.order = column.key
        this.page.orderType = column.order
      }
      this.requestSearch()
    },
    pageChange (page) {
      this.page.current = page
      this.requestSearch()
    },
    sizeChange (size) {
      this.page.size = size
      this.requestSearch()
    },
    showAddModal () {
      this.addModal = true
    },
    addOk () {
      this.$refs.add.validate((valid) => {
        if (valid) {
          let salt = this.md5.salt
          let param = {
            userName: this.add.userName,
            password: md5(this.add.userName + salt + this.add.password),
            realName: this.add.realName,
            department: this.add.department,
            contact: this.add.contact,
            pwdValidity: this.add.pwdValidity
          }
          this.axios.post('/user/add', param).then(resp => {
            if (resp.code === 200) {
              this.$Message.success('新增成功')

              this.addModal = false
              this.addCancel()
              this.init()
            } else {
              this.$Message.error(resp.msg)
            }
            this.changeAddLoading()
          }).catch(e => {
            this.changeAddLoading()
          })
        } else {
          this.changeAddLoading()
        }
      })
    },
    addCancel () {
      this.$refs.add.resetFields()
    },
    changeAddLoading () {
      this.addLoading = false
      this.$nextTick(() => {
        this.addLoading = true
      })
    },
    updateOk () {
      this.$refs.update.validate((valid) => {
        if (valid) {
          this.axios.post('/user/update', this.update).then(resp => {
            if (resp.code === 200) {
              this.$Message.success('用户信息更新成功')
              this.updateModal = false
              this.init()
              this.updateCancel()
            } else {
              this.$Message.error(resp.msg)
            }
            this.changeUpdateLoading()
          }).then(e => {
            this.changeUpdateLoading()
          })
        } else {
          this.changeUpdateLoading()
        }
      })
    },
    updateCancel () {
      this.$refs.update.resetFields()
    },
    changeUpdateLoading () {
      this.updateLoading = false
      this.$nextTick(() => {
        this.updateLoading = true
      })
    },
    updateState (userName, state, stateText) {
      let _this = this
      this.$Modal.confirm({
        title: '操作确认',
        content: '确认更新用户状态为[' + stateText + ']吗？',
        onOk: function () {
          let param = {
            userName: userName,
            state: state,
            stateText: stateText
          }
          this.axios.post('/user/updateState', param).then(resp => {
            if (resp.code === 200) {
              this.$Message.success('用户状态更新成功')

              _this.init()
            } else {
              this.$Message.error(resp.msg)
            }
          })
        }
      })
    },
    relateRole (userName) {
      if (this.roleData.length === 0) {
        this.$Message.error('获取角色信息失败')
      } else {
        this.axios.get('/user/role/' + userName).then(resp => {
          if (resp.code === 200) {
            this.ownedRoles = []
            resp.data.map(item => {
              this.ownedRoles.push(item)
            })

            this.roleData.map(item => {
              if ((item.key === 'admin' || item.key === 'base') && userName === 'root') {
                item.disabled = true
              } else if (item.key === 'base' && userName !== 'root') {
                item.disabled = true
              } else {
                item.disabled = false
              }
            })

            this.opUser = userName
            this.roleModal = true
          } else {
            this.$Message.error(resp.msg)
          }
        })
      }
    },
    roleChange (targetKeys, direction, moveKeys) {
      let op = 'add'
      if (direction === 'left') {
        op = 'delete'
      }

      let param = {
        userName: this.opUser,
        op: op,
        roleIds: moveKeys
      }

      this.axios.post('/user/role', param).then(resp => {
        if (resp.code === 200) {
          this.ownedRoles = targetKeys
        } else {
          this.$Message.error(resp.msg)
        }
      })
    },
    recoveryRoles (visible) {
      if (visible === false) {
        this.opUser = null
        this.ownedRole = []
      }
    },
    requestSearch () {
      this.loading = true
      let pageIdx = this.page.current - 1
      let param = {
        pageIdx: pageIdx,
        pageSize: this.page.size,
        order: this.page.order,
        orderType: this.page.orderType,
        search: {
          userName: this.search.userName,
          startTime: this.search.startTime,
          endTime: this.search.endTime,
          state: this.search.state
        }
      }

      this.axios.post('/user', param).then(resp => {
        if (resp.code === 200) {
          let data = resp.data
          this.page.total = data.count
          this.data = data.list.map(item => {
            if (item.userName === this.name) {
              item._disabled = true
            }
            return item
          })
        } else {
          this.$Message.error(resp.msg)
        }
        this.loading = false
      }).catch(e => {
        this.loading = false
      })
    },
    formatTime (time) {
      if (time != null && time.length === 14) {
        return time.substr(0, 4) + '-' + time.substr(4, 2) + '-' + time.substr(6, 2) + ' ' +
          time.substr(8, 2) + ':' + time.substr(10, 2) + ':' + time.substr(12, 2)
      }
      return time
    },
    timeChange (value) {
      let startTime = value[0].substr(0, 4) + value[0].substr(5, 2) + value[0].substr(8, 2) + '000000'
      let endTime = value[1].substr(0, 4) + value[1].substr(5, 2) + value[1].substr(8, 2) + '235959'

      this.search.startTime = startTime
      this.search.endTime = endTime
    },
    formatValidity (value) { // 向数字输入框输入值格式化
      if (value === '') {
        return ''
      } else if (value === 0) {
        return '永久'
      } else {
        return value + '天'
      }
    },
    pwdEquals (rule, value, callback) {
      let pwd = this.add.password
      if (pwd !== value) {
        callback(new Error())
      } else {
        callback()
      }
    },
    checkLen (rule, value, callback) {
      if (value !== null) {
        let len = 0
        for (let i = 0; i < value.length; i++) {
          len = len + ((value.charCodeAt(i) >= 0x4e00 && value.charCodeAt(i) <= 0x9fa5) ? 2 : 1)
        }

        let min = rule.min
        let max = rule.max

        if (min === undefined) {
          min = 0
        }
        if (max === undefined) {
          max = Number.MAX_VALUE
        }

        if (len >= min && len <= max) {
          return callback()
        }
        return callback(new Error())
      }
      return callback()
    }
  }
}
</script>

<style>
@import "../../../static/css/base.css";
</style>
