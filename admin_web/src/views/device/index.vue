<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.imei" placeholder="IMEI" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />
      <el-select v-model="listQuery.userId" class="filter-item" :remote-method="getRemoteUserList" filterable clearable default-first-option remote placeholder="Select Cutomer">
        <el-option v-for="item in custListOptions" :key="item.id" :label="item.fullName" :value="item.id" />
      </el-select>
      <el-select v-model="listQuery.dataPlanId" class="filter-item" filterable clearable default-first-option placeholder="Select DataPlan">
        <el-option v-for="item in dataplanListOptions" :key="item.id" :label="item.name" :value="item.id" />
      </el-select>
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        Search
      </el-button>
      <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="handleCreate">
        Add
      </el-button>
    </div>

    <el-table
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;"
    >
      <el-table-column label="IMEI" align="center">
        <template slot-scope="{row}">
          <span>{{ row.imei }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Vendor" align="center">
        <template slot-scope="{row}">
          <span>{{ showVendor(row.vendor) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Customer" align="center">
        <template slot-scope="{row}">
          <span>{{ row.customerName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Curr. Data Plan" align="center">
        <template slot-scope="{row}">
          <span>{{ row.currPlanName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Next Data Plan" align="center">
        <template slot-scope="{row}">
          <span>{{ row.nextPlanName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Expire Date" align="center">
        <template slot-scope="{row}">
          <span>{{ row.expireDate }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Create Time" align="center">
        <template slot-scope="{row}">
          <span>{{ row.createTime | dateFormat('YYYY-MM-DD HH:mm') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Actions" align="center" class-name="small-padding fixed-width" width="180">
        <template slot-scope="{row,$index}">
          <el-button v-if="row.status!='0'" type="primary" icon="el-icon-edit" size="mini" @click="handleUpdate(row)">
            Edit
          </el-button>
          <el-button v-if="row.status!='0'" size="mini" type="danger" @click="handleDelete(row,$index)">
            Delete
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.pageNum" :limit.sync="listQuery.pageSize" @pagination="getList" />

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="right" label-width="120px" style="width: 400px; margin-left:50px;">
        <el-form-item label="IMEI" prop="imei">
          <el-input v-model="temp.imei" />
        </el-form-item>
        <el-form-item label="Vendor" prop="vendor">
          <el-select v-model="temp.vendor" class="filter-item" placeholder="Please select">
            <el-option v-for="item in vendorOptions" :key="item.key" :label="item.display_name" :value="item.key" />
          </el-select>
        </el-form-item>
        <el-form-item :label="dialogStatus==='create'?'Data Plan':'Curr. Data Plan'" prop="currDataPlan">
          <el-select v-model="temp.currDataPlanId" filterable clearable default-first-option placeholder="Select DataPlan">
            <el-option v-for="item in dataplanListOptions" :key="parseInt(item.id)" :label="item.name" :value="parseInt(item.id)" />
          </el-select>
        </el-form-item>
        <el-form-item v-if="dialogStatus!=='create'" label="Next Data Plan" prop="nextDataPlan">
          <el-select v-model="temp.nextDataPlanId" filterable clearable default-first-option placeholder="Select DataPlan">
            <el-option v-for="item in dataplanListOptions" :key="parseInt(item.id)" :label="item.name" :value="parseInt(item.id)" />
          </el-select>
        </el-form-item>
        <el-form-item label="Customer" prop="customerId">
          <el-select v-model="temp.userId" :remote-method="getRemoteUserList" filterable clearable default-first-option remote placeholder="Select Cutomer">
            <el-option v-for="item in custListOptions" :key="item.id" :label="item.fullName" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="Expire Date" prop="expireDate">
          <el-date-picker v-model="temp.expireDate" type="date" placeholder="Select expire date" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button :loading="btnLoading" @click="dialogFormVisible = false">
          Cancel
        </el-button>
        <el-button :loading="btnLoading" type="primary" @click="dialogStatus==='create'?createData():updateData()">
          Save
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { listDeviceInfo, createDeviceInfo, updateDeviceInfo, deleteDeviceInfo } from '@/api/device'
import waves from '@/directive/waves' // waves directive
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
import { searchUser, searchDataplan } from '@/api/remote-search'

export default {
  name: 'DeviceManager',
  components: { Pagination },
  directives: { waves },
  data() {
    return {
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: true,
      btnLoading: false,
      custListOptions: [],
      dataplanListOptions: [],
      vendorShow: this.$store.getters.vendorOptions,
      listQuery: {
        pageNum: 1,
        pageSize: 10,
        imei: '',
        userId: '',
        vendor: '',
        dataPlanId: '',
        expiredDateStart: '',
        expiredDateEnd: ''
      },
      showReviewer: false,
      temp: {
        id: undefined,
        imei: '',
        userId: '',
        vendor: '',
        model: '',
        currDataPlanId: '',
        nextDataPlanId: '',
        expireDate: ''
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: 'Edit Device',
        create: 'Create Device'
      },
      dialogPvVisible: false,
      rules: {
        imei: [{ required: true, message: 'IMEI is required', trigger: 'change' }],
        vendor: [{ required: true, message: 'please select vendor', trigger: 'change' }]
      },
      downloadLoading: false
    }
  },
  computed: {
    ...mapGetters([
      'vendorOptions'
    ])
  },
  created() {
    this.getList()
    this.getRemoteDataPlanList('')
  },
  methods: {
    showVendor(vendor) {
      const calendarTypeKeyValue = this.$store.getters.vendorOptions.reduce((acc, cur) => {
        acc[cur.key] = cur.display_name
        return acc
      }, {})
      return calendarTypeKeyValue[vendor]
    },
    getList() {
      this.listLoading = true
      listDeviceInfo(this.listQuery).then(response => {
        this.list = response.data.list
        this.total = response.data.total
        this.listLoading = false
      })
    },
    handleFilter() {
      this.listQuery.pageNum = 1
      this.getList()
    },
    resetTemp() {
      this.temp = {
        id: undefined,
        imei: '',
        vendor: '',
        userId: '',
        model: '',
        currDataPlanId: '',
        nextDataPlanId: '',
        expireDate: ''
      }
      this.custListOptions = []
    },
    getRemoteUserList(query) {
      searchUser(query).then(response => {
        this.custListOptions = response.data
      })
    },
    getRemoteDataPlanList(query) {
      searchDataplan(query).then(response => {
        this.dataplanListOptions = response.data
      })
    },
    handleCreate() {
      this.resetTemp()
      this.getRemoteDataPlanList('')
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.btnLoading = true
          createDeviceInfo(this.temp).then(() => {
            // this.list.unshift(this.temp)
            this.dialogFormVisible = false
            this.btnLoading = false
            this.$notify({
              title: 'Success',
              message: 'Created Successfully',
              type: 'success',
              duration: 2000
            })
            this.handleFilter()
          }).catch(() => {
            console.log('failed!')
            this.btnLoading = false
          })
        }
      })
    },
    handleUpdate(row) {
      this.resetTemp()
      this.temp = Object.assign({}, row) // copy obj
      // 初始化下拉框数据
      if (row.userId !== null && row.userId !== '') {
        this.custListOptions.push({ id: row.userId, fullName: row.customerName })
      }
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.btnLoading = true
          const tempData = Object.assign({}, this.temp)
          updateDeviceInfo(tempData).then(() => {
            this.dialogFormVisible = false
            this.btnLoading = false
            this.$notify({
              title: 'Success',
              message: 'Update Successfully',
              type: 'success',
              duration: 2000
            })
            this.resetTemp()
            this.getList()
          }).catch(() => {
            console.log('failed!')
            this.btnLoading = false
          })
        }
      })
    },
    handleDelete(row, index) {
      this.temp = Object.assign({}, row) // copy obj
      this.$confirm('Confirm to Delete the DataPlan?', 'Warning', {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }).then(async() => {
        await deleteDeviceInfo(this.temp.id)
        this.$message({
          title: 'Success',
          message: 'Delete Successfully',
          type: 'success',
          duration: 2000
        })
        this.list.splice(index, 1)
      })
        .catch(err => { console.error(err) })
    }
  }
}
</script>
