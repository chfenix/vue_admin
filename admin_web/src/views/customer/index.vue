<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.email" placeholder="Email" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />
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
      <el-table-column label="Email" align="center">
        <template slot-scope="{row}">
          <span>{{ row.email }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Full Name" align="center">
        <template slot-scope="{row}">
          <span>{{ row.fullName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Phone" align="center">
        <template slot-scope="{row}">
          <span>{{ row.phone }}</span>
        </template>
      </el-table-column>
      <el-table-column label="CreateTime" align="center">
        <template slot-scope="{row}">
          <span>{{ row.createTime | dateFormat('YYYY-MM-DD HH:mm') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="LastLoginTime" align="center">
        <template slot-scope="{row}">
          <span>{{ row.lastLoginTime | dateFormat('YYYY-MM-DD HH:mm:ss') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Status" class-name="status-col" width="120">
        <template slot-scope="{row}">
          <el-tag :type="row.status | statusStyleFilter">
            {{ row.status | statusFilter }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="Actions" align="center" class-name="small-padding fixed-width" width="180">
        <template slot-scope="{row,$index}">
          <el-button v-if="row.status!='0'" type="primary" icon="el-icon-edit" size="mini" @click="handleUpdate(row)">
            Edit
          </el-button>
          <el-button v-if="row.status!='0'" size="mini" type="danger" @click="handleInvalid(row,$index)">
            Invalid
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.pageNum" :limit.sync="listQuery.pageSize" @pagination="getList" />

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="right" label-width="100px" style="width: 400px; margin-left:50px;">
        <el-form-item label="Email" prop="email">
          <el-input v-model="temp.email" type="email" />
        </el-form-item>
        <el-form-item label="Full Name" prop="fullName">
          <el-input v-model="temp.fullName" />
        </el-form-item>
        <el-form-item label="Phone" prop="phone">
          <el-input v-model="temp.phone" />
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
import { getCustomerList, createCustomer, updateCustomer, invalidCustomer } from '@/api/user'
import waves from '@/directive/waves' // waves directive
// import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
import { validEmail } from '@/utils/validate'

export default {
  name: 'CustomerManager',
  components: { Pagination },
  directives: { waves },
  filters: {
    statusStyleFilter(status) {
      const statusStyleMap = {
        1: 'success',
        0: 'info'
      }
      return statusStyleMap[status]
    },
    statusFilter(status) {
      const statusMap = {
        1: 'Normal',
        0: 'Invalid'
      }
      return statusMap[status]
    }
  },
  data() {
    const validateEmail = (rule, value, callback) => {
      if (!validEmail(value)) {
        callback(new Error('Invalid Email format!'))
      } else {
        callback()
      }
    }
    return {
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: true,
      btnLoading: false,
      listQuery: {
        pageNum: 1,
        pageSize: 10,
        userName: '',
        email: ''
      },
      showReviewer: false,
      temp: {
        id: undefined,
        email: '',
        fullName: '',
        phone: ''
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: 'Edit Customer',
        create: 'Crate Customer'
      },
      dialogPvVisible: false,
      pvData: [],
      rules: {
        email: [{ required: true, message: 'email is required', trigger: 'change' }, { validator: validateEmail, trigger: 'blur' }]
      },
      downloadLoading: false
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      getCustomerList(this.listQuery).then(response => {
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
        email: '',
        fullName: '',
        phone: ''
      }
    },
    handleCreate() {
      this.resetTemp()
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
          createCustomer(this.temp).then(() => {
            this.list.unshift(this.temp)
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
      this.temp = Object.assign({}, row) // copy obj
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
          updateCustomer(tempData).then(() => {
            const index = this.list.findIndex(v => v.id === this.temp.id)
            this.list.splice(index, 1, this.temp)
            this.dialogFormVisible = false
            this.btnLoading = false
            this.$notify({
              title: 'Success',
              message: 'Update Successfully',
              type: 'success',
              duration: 2000
            })
          }).catch(() => {
            console.log('failed!')
            this.btnLoading = false
          })
        }
      })
    },
    handleInvalid(row, index) {
      this.temp = Object.assign({}, row) // copy obj
      this.$confirm('Confirm to Invalid the customer?', 'Warning', {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }).then(async() => {
        await invalidCustomer(this.temp.id)
        this.temp.status = 0
        const index = this.list.findIndex(v => v.id === this.temp.id)
        this.list.splice(index, 1, this.temp)
        this.$message({
          title: 'Success',
          message: 'Invalid customer Successfully',
          type: 'success',
          duration: 2000
        })
      })
        .catch(err => { console.error(err) })
    }
  }
}
</script>
