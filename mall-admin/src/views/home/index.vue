<template>
  <div class="statistics-layout">
    <div class="layout-title">订单统计</div>
    <el-row>
      <el-col :span="4">
        <div style="padding: 20px">
          <div>
            <div style="color: #909399;font-size: 14px">本月订单总数</div>
            <div style="color: #606266;font-size: 24px;padding: 10px 0">{{ thisMonthOrderCount }}</div>
            <div>
              <span :class="thisMonthOrderChange >= 0 ? 'color-success' : 'color-danger'" style="font-size: 14px">{{ thisMonthOrderChangeStr }}</span>
              <span style="color: #C0C4CC;font-size: 14px">同比上月</span>
            </div>
          </div>

          <div style="margin-top: 20px;">
            <div style="color: #909399;font-size: 14px">本周订单总数</div>
            <div style="color: #606266;font-size: 24px;padding: 10px 0">{{ thisWeekOrderCount }}</div>
            <div>
              <span :class="thisWeekOrderChange >= 0 ? 'color-success' : 'color-danger'" style="font-size: 14px">{{ thisWeekOrderChangeStr }}</span>
              <span style="color: #C0C4CC;font-size: 14px">同比上周</span>
            </div>
          </div>

          <div style="margin-top: 20px;">
            <div style="color: #909399;font-size: 14px">本月销售总额</div>
            <div style="color: #606266;font-size: 24px;padding: 10px 0">{{ thisMonthSales }}</div>
            <div>
              <span :class="thisMonthSalesChange >= 0 ? 'color-success' : 'color-danger'" style="font-size: 14px">{{ thisMonthSalesChangeStr }}</span>
              <span style="color: #C0C4CC;font-size: 14px">同比上月</span>
            </div>
          </div>

          <div style="margin-top: 20px;">
            <div style="color: #909399;font-size: 14px">本周销售总额</div>
            <div style="color: #606266;font-size: 24px;padding: 10px 0">{{ thisWeekSales }}</div>
            <div>
              <span :class="thisWeekSalesChange >= 0 ? 'color-success' : 'color-danger'" style="font-size: 14px">{{ thisWeekSalesChangeStr }}</span>
              <span style="color: #C0C4CC;font-size: 14px">同比上周</span>
            </div>
          </div>
        </div>
      </el-col>

      <el-col :span="20">
        <div style="padding: 10px;border-left:1px solid #DCDFE6">
          <el-date-picker
            style="float: right;z-index: 1"
            size="small"
            v-model="orderCountDate"
            type="daterange"
            align="right"
            unlink-panels
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            @change="handleDateChange"
            :picker-options="pickerOptions">
          </el-date-picker>
          <div>
            <ve-line
              :data="chartData"
              :legend-visible="false"
              :loading="loading"
              :data-empty="dataEmpty"
              :settings="chartSettings">
            </ve-line>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import dayjs from 'dayjs'
import { str2Date } from '@/utils/date'
import img_home_order from '@/assets/images/home_order.png'

const DATA_FROM_BACKEND = {
  columns: ['date', 'orderCount', 'orderAmount'],
  rows: [
    { date: '2025-04-01', orderCount: 10, orderAmount: 1000 },
    { date: '2025-04-02', orderCount: 8, orderAmount: 800 },
    { date: '2025-04-15', orderCount: 20, orderAmount: 2200 },
    { date: '2025-05-01', orderCount: 12, orderAmount: 1200 },
    { date: '2025-05-02', orderCount: 18, orderAmount: 1750 },
    {date: '2025-05-03', orderCount: 25, orderAmount: 2500},
    {date: '2025-05-04', orderCount: 40, orderAmount: 3800},
    {date: '2025-05-05', orderCount: 33, orderAmount: 3200},
    {date: '2025-05-06', orderCount: 22, orderAmount: 2100},
    {date: '2025-05-07', orderCount: 28, orderAmount: 2900},
    {date: '2025-05-08', orderCount: 35, orderAmount: 3600},
    {date: '2025-05-09', orderCount: 30, orderAmount: 3100},
    {date: '2025-05-10', orderCount: 45, orderAmount: 4600}
  ]
}

export default {
  name: 'home',
  data() {
    return {
      pickerOptions: {
        shortcuts: [
          {
            text: '最近一周',
            onClick(picker) {
              const end = new Date()
              const start = new Date(end.getTime() - 1000 * 60 * 60 * 24 * 7)
              picker.$emit('pick', [start, end])
            }
          },
          {
            text: '最近一月',
            onClick(picker) {
              const end = new Date()
              const start = new Date(end.getTime() - 1000 * 60 * 60 * 24 * 30)
              picker.$emit('pick', [start, end])
            }
          }
        ]
      },
      orderCountDate: [],
      chartData: {
        columns: [],
        rows: []
      },
      chartSettings: {
        xAxisType: 'time',
        area: true,
        axisSite: { right: ['orderAmount'] },
        labelMap: { orderCount: '订单数量', orderAmount: '订单金额' }
      },
      loading: false,
      dataEmpty: false,
      img_home_order
    }
  },
  created() {
    this.initOrderCountDate()
    this.getData()
  },
  methods: {
    handleDateChange() {
      this.getData()
    },
    initOrderCountDate() {
      // 为确保图表有数据，将默认时间段设置为包含实际数据的范围
      const start = new Date('2025-05-01')
      const end = new Date('2025-05-10')
      this.orderCountDate = [start, end]
    },
    getData() {
      const [start, end] = this.orderCountDate
      this.loading = true
      setTimeout(() => {
        const filtered = DATA_FROM_BACKEND.rows.filter(item => {
          const curr = new Date(item.date)
          return curr >= start && curr <= end
        })
        this.chartData = {
          columns: ['date', 'orderCount', 'orderAmount'],
          rows: filtered
        }
        this.dataEmpty = filtered.length === 0
        this.loading = false

        console.log('筛选数据范围:', start, end)
        console.log('图表数据:', this.chartData.rows)
      }, 500)
    },
    calcRate(current, previous) {
      if (previous === 0) return 0
      return ((current - previous) / previous * 100).toFixed(1)
    },
    formatRate(val) {
      const num = parseFloat(val)
      return (num > 0 ? '+' : '') + num + '%'
    }
  },
  computed: {
    thisMonthData() {
      return DATA_FROM_BACKEND.rows.filter(item =>
        dayjs(item.date).isSame(dayjs(), 'month')
      )
    },
    lastMonthData() {
      return DATA_FROM_BACKEND.rows.filter(item =>
        dayjs(item.date).isSame(dayjs().subtract(1, 'month'), 'month')
      )
    },
    thisWeekData() {
      return DATA_FROM_BACKEND.rows.filter(item =>
        dayjs(item.date).isSame(dayjs(), 'week')
      )
    },
    lastWeekData() {
      return DATA_FROM_BACKEND.rows.filter(item =>
        dayjs(item.date).isSame(dayjs().subtract(1, 'week'), 'week')
      )
    },
    thisMonthOrderCount() {
      return this.thisMonthData.reduce((sum, item) => sum + item.orderCount, 0)
    },
    lastMonthOrderCount() {
      return this.lastMonthData.reduce((sum, item) => sum + item.orderCount, 0)
    },
    thisMonthOrderChange() {
      return this.calcRate(this.thisMonthOrderCount, this.lastMonthOrderCount)
    },
    thisMonthOrderChangeStr() {
      return this.formatRate(this.thisMonthOrderChange)
    },
    thisWeekOrderCount() {
      return this.thisWeekData.reduce((sum, item) => sum + item.orderCount, 0)
    },
    lastWeekOrderCount() {
      return this.lastWeekData.reduce((sum, item) => sum + item.orderCount, 0)
    },
    thisWeekOrderChange() {
      return this.calcRate(this.thisWeekOrderCount, this.lastWeekOrderCount)
    },
    thisWeekOrderChangeStr() {
      return this.formatRate(this.thisWeekOrderChange)
    },
    thisMonthSales() {
      return this.thisMonthData.reduce((sum, item) => sum + item.orderAmount, 0)
    },
    lastMonthSales() {
      return this.lastMonthData.reduce((sum, item) => sum + item.orderAmount, 0)
    },
    thisMonthSalesChange() {
      return this.calcRate(this.thisMonthSales, this.lastMonthSales)
    },
    thisMonthSalesChangeStr() {
      return this.formatRate(this.thisMonthSalesChange)
    },
    thisWeekSales() {
      return this.thisWeekData.reduce((sum, item) => sum + item.orderAmount, 0)
    },
    lastWeekSales() {
      return this.lastWeekData.reduce((sum, item) => sum + item.orderAmount, 0)
    },
    thisWeekSalesChange() {
      return this.calcRate(this.thisWeekSales, this.lastWeekSales)
    },
    thisWeekSalesChangeStr() {
      return this.formatRate(this.thisWeekSalesChange)
    }
  }
}
</script>

<style scoped>
.layout-title {
  color: #606266;
  padding: 15px 20px;
  background: #F2F6FC;
  font-weight: bold;
}

.statistics-layout {
  margin-top: 20px;
  border: 1px solid #DCDFE6;
}

.color-success {
  color: #67C23A;
}

.color-danger {
  color: #F56C6C;
}
</style>
