<template>
  <div class="app-container">
    <div class="filter-container">
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
      <el-table-column label="Name" align="center">
        <template slot-scope="{row}">
          <span>{{ row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="price" align="center">
        <template slot-scope="{row}">
          <span>{{ row.price | moneyFormatter }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Vendor" align="center">
        <template slot-scope="{row}">
          <span>{{ showVendor(row.vendor) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="planId" align="center">
        <template slot-scope="{row}">
          <span>{{ row.planId }}</span>
        </template>
      </el-table-column>
      <el-table-column label="createTime" align="center">
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
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="70px" style="width: 400px; margin-left:50px;">
        <el-form-item label="Name" prop="name">
          <el-input v-model="temp.name" />
        </el-form-item>
        <el-form-item label="Price" prop="price">
          <el-input v-model="temp.price" onkeyup="this.value = this.value.replace(/[^\d.]/g,'')" />
        </el-form-item>
        <el-form-item label="Vendor" prop="vendor">
          <el-select v-model="temp.vendor" class="filter-item" placeholder="Please select">
            <el-option v-for="item in vendorOptions" :key="item.key" :label="item.display_name" :value="item.key" />
          </el-select>
        </el-form-item>
        <el-form-item label="PlanId" prop="planId">
          <el-input v-model="temp.planId" />
        </el-form-item>
        <el-form-item label="Remark">
          <el-input v-model="temp.remark" :autosize="{ minRows: 2, maxRows: 4}" type="textarea" placeholder="Please input" />
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
import { listDataPlan, createDataPlan, updateDataPlan, deleteDataPlan } from '@/api/dataPlan'
import waves from '@/directive/waves' // waves directive
import Pagination from '@/components/Pagination' // secondary package based on el-pagination

export default {
  name: 'DataPlanManager',
  components: { Pagination },
  directives: { waves },
  data() {
    return {
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: true,
      btnLoading: false,
      vendorShow: this.$store.getters.vendorOptions,
      listQuery: {
        pageNum: 1,
        pageSize: 10,
        name: '',
        vendor: '',
        planId: '',
        price: '',
        remark: ''
      },
      showReviewer: false,
      temp: {
        id: undefined,
        name: '',
        vendor: '',
        planId: '',
        price: '',
        remark: ''
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: 'Edit DataPlan',
        create: 'Crate DataPlan'
      },
      dialogPvVisible: false,
      rules: {
        name: [{ required: true, message: 'name is required', trigger: 'change' }],
        price: [{ required: true, message: 'price is required', trigger: 'change' }],
        vendor: [{ required: true, message: 'please select vendor', trigger: 'change' }]
        // email: [{ required: true, message: 'email is required', trigger: 'change' }, { validator: validateEmail, trigger: 'blur' }]
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
      listDataPlan(this.listQuery).then(response => {
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
        name: '',
        vendor: '',
        planId: '',
        price: '',
        remark: ''
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
          createDataPlan(this.temp).then(() => {
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
          updateDataPlan(tempData).then(() => {
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
    handleDelete(row, index) {
      this.temp = Object.assign({}, row) // copy obj
      this.$confirm('Confirm to Delete the DataPlan?', 'Warning', {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }).then(async() => {
        await deleteDataPlan(this.temp.id)
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
