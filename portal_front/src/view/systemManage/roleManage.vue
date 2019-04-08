<template>
  <div class="div-border">
    <!-- 搜索框 -->
    <Form ref="search" :model="search" :label-width="80">
      <Row>
        <Col span="8">
          <FormItem label="角色名称" prop="roleName">
            <Input type="text" v-model="search.roleName" placeholder="请输入想要查询的角色名称" />
          </FormItem>
        </Col>
        <Col span="8">
          <FormItem label="时间段" prop="time">
            <DatePicker type="daterange" placement="bottom-end" :options="options" placeholder="请选择想要查询的时间段"
              style="width: 100%;" show-week-numbers @on-change="timeChange" v-model="search.time"></DatePicker>
          </FormItem>
        </Col>
        <Col span="8" style="text-align: center;">
          <FormItem>
            <Button type="success" icon="ios-search" class="searchBtn" @click="handleSearch">查询</Button>
            <Button type="info"  icon="ios-refresh" class="searchBtn" @click="handleReset">重置</Button>
          </FormItem>
        </Col>
      </Row>
    </Form>

    <Divider/>

    <!-- 操作按钮 -->
    <Button type="info" icon="ios-add-circle-outline" class="opBtn" @click="showAddModal">新增角色</Button>
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
          <p slot="content">角色管理支持根据“<font color="#2db7f5">创建时间</font>”进行排序。<br/><br/>
            点击标题栏的“<font color="#2db7f5">创建时间</font>”即可按照<font color="red">升序->降序->不排序</font>的顺序进行排序</p>
        </Panel>
        <Panel name="2">
          为何“admin”和“base”角色不能删除
            <p slot="content">因为这两个角色是基础角色，删除后可能会导致该系统无人能创建用户并且无法正常登录</p>
        </Panel>
        <Panel name="3">
          给角色赋予“用户管理-关联角色”权限后不能正常使用
            <p slot="content">
              “用户管理-关联角色”需与“<font color="red">用户管理-获取所有角色信息</font>”和“<font color="red">用户管理-获取已拥有的角色信息</font>”两个权限配合使用。
              否则会导致拥有该角色的用户无法看到所有角色或指定用户已有的角色，导致无法为用户关联角色或关联的角色异常。<br/><br/>
            </p>
        </Panel>
        <Panel name="4">
          给角色赋予“角色管理-关联权限”权限后不能正常使用
            <p slot="content">
              “角色管理-关联权限”需与“<font color="red">角色管理-获取所有权限信息</font>”和“<font color="red">角色管理-获取已拥有的权限信息</font>”两个权限配合使用。
              否则会导致拥有该角色的用户无法看到所有权限或指定角色已有的权限，导致无法为角色关联权限或关联的权限异常。<br/><br/>
            </p>
        </Panel>
      </Collapse>
    </Drawer>

    <!-- 新增模态框 -->
    <Modal
      v-model="addModal"
      title="新增角色"
      :loading="addLoading"
      @on-ok="addOk"
      @on-cancel="addCancel">
      <Form ref="add" :model="add" :rules="addValidate" :label-width="100">
        <Row>
          <Col span="24">
            <FormItem label="角色ID" prop="roleId">
              <Input v-model="add.roleId" placeholder="请输入角色ID"></Input>
            </FormItem>
          </Col>
        </Row>
        <Row>
          <Col span="24">
            <FormItem label="角色名称" prop="roleName">
              <Input v-model="add.roleName" placeholder="请输入角色名称"></Input>
            </FormItem>
          </Col>
        </Row>
      </Form>
    </Modal>

    <!-- 权限关联模态框 -->
    <Modal
      v-model="authModal"
      title="关联权限"
      :loading="authLoading"
      @on-ok="handleAuth"
      @on-cancel="handleAuthCancel">
      <Tree ref="authTree" :data="authTreeData" show-checkbox multiple check-directly></Tree>
    </Modal>
  </div>
</template>

<script>
export default {
  name: 'roleManage',
  data () {
    return {
      drawer: false,
      search: {
        roleName: null,
        time: [null, null],
        starTime: null,
        endTime: null
      },
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
          title: '角色ID',
          key: 'roleId'
        }, {
          title: '角色名称',
          key: 'roleName'
        }, {
          title: '创建时间',
          key: 'createTime',
          sortable: true,
          render: (h, params) => {
            return h('span', this.formatTime(params.row.createTime))
          }
        }, {
          title: '操作',
          width: 260,
          render: (h, params) => {
            let btns = [
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
                    this.relateAuth(params.row.roleId)
                  }
                }
              }, '关联权限')
            ]
            if (params.row.roleId !== 'admin' && params.row.roleId !== 'base') {
              btns.push(h('Button', {
                props: {
                  type: 'error',
                  size: 'small'
                },
                on: {
                  click: () => {
                    this.deleteRole(params.row.roleId, params.row.roleName)
                  }
                }
              }, '删除'))
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
        roleId: null,
        roleName: null
      },
      addValidate: {
        roleId: [
          {
            required: true,
            message: '请输入角色ID',
            trigger: 'blur'
          }, {
            pattern: '^[0-9a-zA-Z]*$',
            message: '用户ID只能输入英文字母和数字',
            trigger: 'blur'
          }, {
            validator: this.checkLen,
            min: 3,
            max: 50,
            message: '用户ID需在3~50个字符之间',
            trigger: 'blur'
          }
        ],
        roleName: [
          {
            required: true,
            message: '请输入角色名称',
            trigger: 'blur'
          }, {
            validator: this.checkLen,
            min: 3,
            max: 50,
            message: '角色名称需在3~50个字符之间(一个中文算两个字符)',
            trigger: 'blur'
          }
        ]
      },
      authModal: false,
      authLoading: true,
      authData: [],
      authTreeData: [],
      ownedAuth: [],
      opRole: null
    }
  },
  mounted () {
    this.init()

    // 初始化所有角色信息
    this.axios.get('/menu').then(resp => {
      if (resp.code === 200) {
        this.authData = resp.data
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
          let param = {
            roleId: this.add.roleId,
            roleName: this.add.roleName
          }
          this.axios.post('/role/add', param).then(resp => {
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
    deleteRole (roleId, roleName) {
      let _this = this
      this.$Modal.confirm({
        title: '操作确认',
        content: '确认删除角色[' + roleName + ']吗？',
        onOk: function () {
          this.axios.delete('/role/' + roleId).then(resp => {
            if (resp.code === 200) {
              this.$Message.success('删除成功')

              _this.init()
            } else {
              this.$Message.error(resp.msg)
            }
          })
        }
      })
    },
    relateAuth (roleId) {
      this.axios.get('/role/auth/' + roleId).then(resp => {
        if (resp.code === 200) {
          this.ownedAuth = resp.data
          this.opRole = roleId
          this.authTreeData = this.createTreeData(null, this.ownedAuth)
        }
        this.authModal = true
      })
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
          roleName: this.search.roleName,
          startTime: this.search.startTime,
          endTime: this.search.endTime
        }
      }

      this.axios.post('/role', param).then(resp => {
        if (resp.code === 200) {
          let data = resp.data
          this.page.total = data.count
          this.data = data.list
        } else {
          this.$Message.error(resp.msg)
        }
        this.loading = false
      }).catch(e => {
        this.loading = false
      })
    },
    createTreeData (parentNode, data) {
      let treeData = []
      for (let i = 0; i < this.authData.length; i++) {
        let parentMenu = this.authData[i].parentMenu
        if (parentMenu === parentNode) {
          let checked = false
          for (let j = 0; j < data.length; j++) {
            if (this.authData[i].menuId === data[j] && this.authData[i].menuType === '2') {
              checked = true
              break
            }
          }

          // 不可操作的权限
          let disabled = false
          if (this.opRole === 'admin' && (parentMenu === 'systemManage' || parentMenu === 'userManage' ||
            parentMenu === 'roleManage' || parentMenu === 'logManage' || this.authData[i].menuId === 'systemManage')) {
            disabled = true
          } else if (this.opRole === 'base' && this.authData[i].menuId === 'updatePwd') {
            disabled = true
          }

          treeData.push({
            id: this.authData[i].menuId,
            title: this.authData[i].menuName,
            expand: true,
            checked: checked,
            disabled: disabled,
            children: this.createTreeData(this.authData[i].menuId, data)
          })
        }
      }
      return treeData
    },
    handleAuth (visible) {
      let nodes = this.$refs.authTree.getCheckedAndIndeterminateNodes() // 获取当前已选中或半选中节点
      let prevNodes = this.ownedAuth

      let addNodes = []
      for (let i = 0; i < nodes.length; i++) {
        let add = true
        for (let j = 0; j < prevNodes.length; j++) {
          if (prevNodes[j] === nodes[i].id) {
            add = false
            break
          }
        }

        if (add) {
          addNodes.push(nodes[i].id)
        }
      }

      let removeNodes = []
      for (let i = 0; i < prevNodes.length; i++) {
        let remove = true
        for (let j = 0; j < nodes.length; j++) {
          if (prevNodes[i] === nodes[j].id) {
            remove = false
          }
        }

        if (remove) {
          removeNodes.push(prevNodes[i])
        }
      }

      if (addNodes.length > 0 || removeNodes.length > 0) {
        let param = {
          roleId: this.opRole,
          addNodes: addNodes,
          removeNodes: removeNodes
        }
        this.axios.post('/role/auth', param).then(resp => {
          if (resp.code === 200) {
            this.$Message.success('关联权限成功')
            this.authModal = false
          } else {
            this.$Message.error(resp.msg)
          }
          this.changeAuthLoading()
          this.handleAuthCancel()
        }).catch(e => {
          this.changeAuthLoading()
          this.handleAuthCancel()
        })
      } else {
        this.authModal = false
        this.changeAuthLoading()
        this.handleAuthCancel()
      }
    },
    handleAuthCancel () {
      this.authTreeData = []
      this.ownedAuth = []
      this.opRole = null
    },
    formatTime (time) {
      if (time != null && time.length === 14) {
        return time.substr(0, 4) + '-' + time.substr(4, 2) + '-' + time.substr(6, 2) + ' ' +
          time.substr(8, 2) + ':' + time.substr(10, 2) + ':' + time.substr(12, 2)
      }
      return time
    },
    changeAuthLoading () {
      this.authLoading = false
      this.$nextTick(() => {
        this.authLoading = true
      })
    },
    timeChange (value) {
      let startTime = value[0].substr(0, 4) + value[0].substr(5, 2) + value[0].substr(8, 2) + '000000'
      let endTime = value[1].substr(0, 4) + value[1].substr(5, 2) + value[1].substr(8, 2) + '235959'

      this.search.startTime = startTime
      this.search.endTime = endTime
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
