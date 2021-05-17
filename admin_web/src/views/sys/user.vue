<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.userName" placeholder="用户名" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />
      <el-input v-model="listQuery.name" placeholder="姓名" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />
      <el-input v-model="listQuery.email" placeholder="Email" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        查询
      </el-button>
      <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="handleCreate">
        新增
      </el-button>
    </div>
    <el-table
      v-loading="listLoading"
      :data="list"
      element-loading-text="Loading"
      border
      fit
      stripe
      class="order-table"
      style="width: 100%;"
    >
      <el-table-column label="用户名" header-align="center">
        <template slot-scope="{row}">
          <span>{{ row.userName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="姓名" header-align="center">
        <template slot-scope="{row}">
          <span>{{ row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Email" header-align="center">
        <template slot-scope="{row}">
          <span>{{ row.email }}</span>
        </template>
      </el-table-column>
      <el-table-column label="电话" header-align="center">
        <template slot-scope="{row}">
          <span>{{ row.phone }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" header-align="center">
        <template slot-scope="{row}">
          <span>{{ row.status }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" header-align="center">
        <template slot-scope="{row}">
          <span>{{ row.createTime | dateFormat('YYYY-MM-DD HH:mm:ss') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="修改时间" header-align="center">
        <template slot-scope="{row}">
          <span>{{ row.updateTime | dateFormat('YYYY-MM-DD HH:mm:ss') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="160" align="center" type="index">
        <template slot-scope="{row,$index}">
          <el-button
            type="primary"
            size="mini"
            icon="el-icon-edit"
            @click="handleModify(row)"
          >
            修改
          </el-button>
          <el-tooltip class="item" effect="dark" content="删除" placement="top" :open-delay="500" :hide-after="1000">
            <el-button
              type="danger"
              size="mini"
              icon="el-icon-delete"
              @click="handleDelete(row.id,$index)"
            />
          </el-tooltip>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="listQuery.pageNum" :limit.sync="listQuery.pageSize" @pagination="fetchData" />

    <el-dialog :visible.sync="dialogVisible" :title="dialogType==='create'?'新增用户':'修改用户'">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="right" label-width="80px">
        <el-form-item label="用户名" prop="userName">
          <el-input v-model="temp.userName" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="temp.name" placeholder="请输入用户姓名" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="temp.email" placeholder="请输入用户邮箱地址" />
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="temp.phone" placeholder="请输入用户联系电话" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="temp.remark"
            :autosize="{ minRows: 2, maxRows: 4}"
            type="textarea"
            placeholder="请输入备注"
          />
        </el-form-item>
        <el-form-item label="角色" prop="roles">
          <el-checkbox-group v-model="temp.roles">
            <el-checkbox v-for="role in allRoles" :key="role.id" :label="role.id"> {{ role.name }}</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button :loading="btnLoading" @click="dialogVisible = false">
          取消
        </el-button>
        <el-button :loading="btnLoading" type="primary" @click="dialogType==='create'?createData():modifyData()">
          保存
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getUserList, updateUser, createUser, invalidUser, getAllRole, getUserRoles } from '@/api/user'
import Pagination from '@/components/Pagination'

export default {
  name: 'UserManager',
  components: { Pagination },
  data() {
    return {
      listQuery: {
        pageNum: 1,
        pageSize: 10,
        userName: '',
        name: '',
        email: ''
      },
      rules: {
        userName: [{ required: true, message: '用户名不能为空', trigger: 'blur' }],
        name: [{ required: true, message: '姓名不能为空', trigger: 'blur' }],
        email: [{ required: true, message: '邮箱地址不能为空', trigger: 'blur' }, { type: 'email', message: '请输入正确的邮箱地址' }],
        roles: [{ type: 'array', required: true, message: '请至少选择一个角色', trigger: 'change' }]
      },
      list: null,
      temp: {
        id: undefined,
        userName: '',
        name: '',
        email: '',
        Phone: '',
        remark: '',
        roles: []
      },
      allRoles: [],
      total: 0,
      btnLoading: false,
      listLoading: true,
      dialogVisible: false,
      dialogType: 'create'
    }
  },
  created() {
    this.fetchData()
    this.initRoles()
  },
  methods: {
    resetTemp() {
      this.temp = {
        id: undefined,
        userName: '',
        name: '',
        email: '',
        Phone: '',
        remark: '',
        roles: []
      }
    },
    handleFilter() {
      this.listQuery.pageNum = 1
      this.fetchData()
    },
    fetchData() {
      this.listLoading = true
      getUserList(this.listQuery).then(response => {
        this.list = response.data.list
        this.total = response.data.total
        this.listLoading = false
      })
    },
    initRoles() {
      getAllRole().then(response => {
        this.allRoles = response.data
      })
    },
    handleCreate() {
      this.resetTemp()
      this.dialogType = 'create'
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.btnLoading = true
          const tempData = Object.assign({}, this.temp)
          createUser(tempData).then(() => {
            this.list.unshift(tempData)
            this.dialogVisible = false
            this.btnLoading = false
            this.$message({
              message: '新增成功',
              type: 'success'
            })
            this.handleFilter()
          }).catch(() => {
            console.log('failed!')
            this.btnLoading = false
          })
        } else {
          console.log(this.temp.roles)
        }
      })
    },
    handleModify(row) {
      this.resetTemp()
      this.temp = Object.assign({}, row) // copy obj
      this.temp.roles = []
      this.dialogType = 'modify'
      this.dialogVisible = true
      getUserRoles(this.temp.id).then(response => {
        this.temp.roles = response.data
      })
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    modifyData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.btnLoading = true
          const tempData = Object.assign({}, this.temp)
          updateUser(tempData).then(() => {
            this.dialogVisible = false
            this.btnLoading = false
            this.$message({
              message: '修改成功',
              type: 'success'
            })
            this.resetTemp()
            this.fetchData()
          }).catch(() => {
            console.log('failed!')
            this.btnLoading = false
          })
        }
      })
    },
    handleDelete(id, index) {
      this.$confirm('此操作会删除用户数据，是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async() => {
        await invalidUser(id)
        this.$message({
          message: '删除成功',
          type: 'success'
        })
        this.list.splice(index, 1)
      })
        .catch(err => { console.error(err) })
    }
  }
}
</script>
