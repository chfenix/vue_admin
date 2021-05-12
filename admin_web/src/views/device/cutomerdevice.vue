<template>
  <div class="app-container">
    <el-button type="primary" icon="el-icon-plus" @click="handleBind">Bind Device</el-button>
    <div class="create-button" />
    <el-table
      v-loading="listLoading"
      :data="list"
      element-loading-text="Loading"
      border
      fit
      class="order-table"
      :header-cell-style="{background:'Gainsboro',color:'#000'}"
    >
      <el-table-column label="IMEI">
        <template slot-scope="{row}">
          {{ row.no }}
        </template>
      </el-table-column>
      <el-table-column label="Status">
        <template slot-scope="{row}">
          <el-tag :type="row.orderStatus | statusColor">{{ row.orderStatus | statusMessage }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="Binding Time">
        <template slot-scope="{row}">
          {{ row.createTime }}
        </template>
      </el-table-column>
      <el-table-column label="Action" width="110" align="center">
        <template slot-scope="{row}">
          <el-button :disabled="row.orderStatus != 1" type="text" @click="handleUnbind(row)">Unbind</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :visible.sync="dialogVisible" :title="'Bind New Device'">
      <el-form :model="device" label-width="80px" label-position="left">
        <el-form-item label="IMEI">
          <el-input v-model="device.imei" placeholder="Please input IMEI" />
        </el-form-item>
      </el-form>
      <div style="text-align:right;">
        <el-button type="danger" @click="dialogVisible=false">Cancel</el-button>
        <el-button type="primary" @click="bindDevice">Bind</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { bindDevice, getList, unbindDevice } from '@/api/device'

export default {
  name: 'MyDevice',
  filters: {
    statusMessage(status) {
      const statusMessage = {
        1: 'Waiting for pay',
        2: 'Completed',
        9: 'Canceled'
      }
      return statusMessage[status]
    },
    statusColor(status) {
      const statusMap = {
        1: 'danger',
        2: 'success',
        9: 'info'
      }
      return statusMap[status]
    },
    money(val) {
      val = val.toString().replace(/\$|\,/g, '')
      if (isNaN(val)) {
        val = '0'
      }
      const sign = (Number(val) === Math.abs(val))
      val = Math.floor(val * 100 + 0.50000000001)
      let cents = val % 100
      val = Math.floor(val / 100).toString()
      if (cents < 10) {
        cents = '0' + cents
      }
      for (let i = 0; i < Math.floor((val.length - (1 + i)) / 3); i++) {
        val = val.substring(0, val.length - (4 * i + 3)) + ',' + val.substring(val.length - (4 * i + 3))
      }

      return (((sign) ? '' : '-') + val + '.' + cents)
    }
  },
  data() {
    return {
      form: {
        orderId: ''
      },
      device: {
        imei: ''
      },
      list: null,
      listLoading: true,
      dialogVisible: false,
      dialogType: 'new'
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.listLoading = true
      getList().then(response => {
        this.list = response.data
        this.listLoading = false
      })
    },
    handleBind() {
      this.device.imei = ''
      this.dialogType = 'new'
      this.dialogVisible = true
    },
    handleUnbind(row) {
      this.form.orderId = row.id
      unbindDevice(this.form).then(() => {
        console.log('cancel success!')
        this.$message.success('cancel order success!')
        row.status = 9
        console.log('##########' + row.status)
      }).catch(() => {
        console.log('cancel failed!')
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.app-container {
  padding: 2% 5% 1px;
}
.create-button {
  padding-bottom: 20px;
}
</style>
