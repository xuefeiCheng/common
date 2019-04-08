<template>
  <div class="div-border">
    <!-- 搜索框 -->
    <Form ref="search" :model="search" :label-width="80">
      <Row>
        <Col span="8">
          <FormItem label="用户名称" prop="userName">
            <Input type="text" v-model="search.userName" placeholder="请输入想要查询的操作用户名称" />
          </FormItem>
        </Col>
        <Col span="8">
          <FormItem label="时间段" prop="time">
            <DatePicker type="daterange" placement="bottom-end" :options="options" placeholder="请选择想要查询的时间段"
              style="width: 100%;" show-week-numbers @on-change="timeChange" v-model="search.time"></DatePicker>
          </FormItem>
        </Col>
        <Col span="8">
          <FormItem label="用户状态" prop="type">
            <Select v-model="search.type">
              <Option v-for="item in resultList" :value="item.key" :key="item.key">{{ item.value }}</Option>
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
    <Button type="info" icon="ios-trash-outline" class="opBtn" @click="clearModal = true">日志清理</Button>
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
          <p slot="content">角色管理支持根据“<font color="#2db7f5">请求时间</font>”进行排序。<br/><br/>
            点击标题栏的“<font color="#2db7f5">请求时间</font>”即可按照<font color="red">升序->降序->不排序</font>的顺序进行排序</p>
        </Panel>
        <Panel name="2">
          “日志清理”作用
            <p slot="content">为防止系统操作日志越来越多，特意提供“<font color="#2db7f5">日志清理</font>”功能，可清理指定时间之前的日志文件
              （<font color="red">30天内的日志不可被清理</font>）</p>
        </Panel>
      </Collapse>
    </Drawer>

    <!-- 日志清理模态框 -->
    <Modal
      v-model="clearModal"
      title="日志清理"
      :loading="clearLoading"
      @on-ok="clearOk"
      @on-cancel="clearCancel">
      <Form ref="clear" :model="clear" :rules="clearValidate" :label-width="100">
        <Row>
          <Col span="24">
            <FormItem label="清理时长" prop="daysAgo">
              <InputNumber v-model="clear.daysAgo" placeholder="请输入要清理多久之前的日志(只能清理30天之前的日志)" :min="30" :max="1000" :step="30"
                :formatter="formatValidity" style="width: 100%;"></InputNumber>
            </FormItem>
          </Col>
        </Row>
      </Form>
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
        userName: null,
        time: [null, null],
        starTime: null,
        endTime: null,
        type: null
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
      resultList: [
        {
          key: '1',
          value: '成功'
        }, {
          key: '2',
          value: '失败'
        }, {
          key: '3',
          value: '异常'
        }
      ],
      columns: [
        {
          type: 'index',
          width: 50,
          align: 'center'
        }, {
          title: '操作用户',
          key: 'operator'
        }, {
          title: '操作描述',
          key: 'description'
        }, {
          title: '处理时长(ms)',
          key: 'execTime'
        }, {
          title: '请求结果',
          key: 'result',
          render: (h, params) => {
            let result = params.row.result
            let resultText = result

            for (let i = 0; i < this.resultList.length; i++) {
              if (this.resultList[i].key === result) {
                resultText = this.resultList[i].value
                break
              }
            }

            return h('span', resultText)
          }
        }, {
          title: '失败原因',
          key: 'error'
        }, {
          title: '请求IP',
          key: 'ip'
        }, {
          title: '请求时间',
          key: 'createTime',
          sortable: true,
          render: (h, params) => {
            return h('span', this.formatTime(params.row.createTime))
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
      clearModal: false,
      clearLoading: true,
      clear: {
        daysAgo: null
      },
      clearValidate: {
        daysAgo: [
          {
            type: 'number',
            required: true,
            message: '清理时长不能为空',
            trigger: 'blur'
          }
        ]
      }
    }
  },
  mounted () {
    this.init()
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
          type: this.search.type
        }
      }

      this.axios.post('/log', param).then(resp => {
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
    clearOk () {
      this.$refs.clear.validate((valid) => {
        if (valid) {
          this.axios.delete('/log/' + this.clear.daysAgo).then(resp => {
            if (resp.code === 200) {
              this.clearModal = false
              this.$Message.success('清理完成')
              this.clearCancel()
              this.init()
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
    clearCancel () {
      this.$refs.clear.resetFields()
    },
    formatValidity (value) { // 向数字输入框输入值格式化
      if (value === '') {
        return ''
      } else {
        return value + '天'
      }
    },
    changeLoading () {
      this.clearLoading = false
      this.$nextTick(() => {
        this.clearLoading = true
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
    }
  }
}
</script>

<style>
@import "../../../static/css/base.css";
</style>
