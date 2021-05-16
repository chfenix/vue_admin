<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.typeCode" placeholder="Type Code" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        查询
      </el-button>
      <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="handleCreate">
        新增
      </el-button>
    </div>
    <el-form ref="tableForm" :model="tableForm" :rules="tableFormRules">
      <el-table
        v-loading="listLoading"
        :data="tableForm.tableData"
        element-loading-text="Loading"
        border
        fit
        stripe
        style="width: 100%;"
        class="order-table"
      >
        <el-table-column label="Type Code" header-align="center">
          <template slot-scope="{row,$index}">
            <template v-if="row.edit">
              <el-form-item :prop="'tableData.' + $index + '.typeCode'" :rules="tableFormRules.typeCode">
                <el-input v-model="row.typeCode" class="edit-input" size="small" />
              </el-form-item>
            </template>
            <span v-else>{{ row.typeCode }}</span>
          </template>
        </el-table-column>
        <el-table-column label="Type Name" header-align="center">
          <template slot-scope="{row,$index}">
            <template v-if="row.edit">
              <el-form-item :prop="'tableData.' + $index + '.typeName'" :rules="tableFormRules.typeName">
                <el-input v-model="row.typeName" class="edit-input" size="small" />
              </el-form-item>
            </template>
            <span v-else>{{ row.typeName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="List Code" header-align="center">
          <template slot-scope="{row,$index}">
            <template v-if="row.edit">
              <el-form-item :prop="'tableData.' + $index + '.listCode'" :rules="tableFormRules.listCode">
                <el-input v-model="row.listCode" class="edit-input" size="small" />
              </el-form-item>
            </template>
            <span v-else>{{ row.listCode }}</span>
          </template>
        </el-table-column>
        <el-table-column label="List Name" header-align="center">
          <template slot-scope="{row,$index}">
            <template v-if="row.edit">
              <el-form-item :prop="'tableData.' + $index + '.listName'" :rules="tableFormRules.listName">
                <el-input v-model="row.listName" class="edit-input" size="small" />
              </el-form-item>
            </template>
            <span v-else>{{ row.listName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="显示顺序" header-align="center">
          <template slot-scope="{row,$index}">
            <template v-if="row.edit">
              <el-form-item :prop="'tableData.' + $index + '.pri'" :rules="tableFormRules.pri">
                <el-input v-model="row.pri" class="edit-input" size="small" />
              </el-form-item>
            </template>
            <span v-else>{{ row.pri }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160" align="center" type="index">
          <template slot-scope="{row,$index}">
            <template v-if="row.edit">
              <el-tooltip class="item" effect="dark" :content="row.id == null?'新增':'修改'" placement="top" :open-delay="200">
                <el-button
                  type="success"
                  :loading="btnLoading"
                  size="mini"
                  icon="el-icon-check"
                  @click="row.id == null?confirmCreate(row,$index):confirmModify($index,row)"
                />
              </el-tooltip>
              <el-tooltip class="item" effect="dark" content="取消" placement="top" :open-delay="200">
                <el-button
                  type="warning"
                  :loading="btnLoading"
                  size="mini"
                  icon="el-icon-close"
                  @click="row.id == null?cancelCreate($index):cancelModify($index, row)"
                />
              </el-tooltip>
            </template>
            <template v-else>
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
                  @click="handleDelete($index, row.id)"
                />
              </el-tooltip>
            </template>
          </template>
        </el-table-column>
      </el-table>
    </el-form>
    <pagination v-show="total>0" :total="total" :page.sync="listQuery.pageNum" :limit.sync="listQuery.pageSize" @pagination="fetchData" />
  </div>
</template>

<script>
import { getSysbookList, updateSysbook, createSysbook, deleteSysbook } from '@/api/sysbook'
import Pagination from '@/components/Pagination' // secondary package based on el-pagination

export default {
  name: 'Sysbook',
  components: { Pagination },
  data() {
    return {
      tableForm: {
        tableData: []
      },
      listQuery: {
        typeCode: '',
        pageNum: 1,
        pageSize: 10
      },
      sysbook: {
        id: undefined,
        typeCode: '',
        typeName: '',
        listCode: '',
        listName: '',
        pri: '',
        edit: true
      },
      tableFormRules: {
        typeCode: [{ required: true, message: 'TypeCode不能为空', trigger: 'blur' }],
        typeName: [{ required: true, message: 'TypeName不能为空', trigger: 'blur' }],
        listCode: [{ required: true, message: 'ListCode不能为空', trigger: 'blur' }],
        listName: [{ required: true, message: 'ListName不能为空', trigger: 'blur' }],
        pri: [{ required: true, message: '显示顺序不能为空', trigger: 'blur' }]
      },
      list: null,
      temp: null,
      total: 0,
      btnLoading: false,
      listLoading: true,
      dialogVisible: false,
      dialogType: 'new'
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    handleFilter() {
      this.listQuery.pageNum = 1
      this.fetchData()
    },
    fetchData() {
      this.listLoading = true
      getSysbookList(this.listQuery).then(response => {
        const items = response.data.list
        this.tableForm.tableData = items.map(v => {
          this.$set(v, 'edit', false)
          return v
        })
        this.total = response.data.total
        this.listLoading = false
      })
    },
    validateSubmit(index) {
      // 查找到form中的对应行命名的属性，进行校验
      let result = true
      for (const item of this.$refs['tableForm'].fields) {
        if (parseInt(item.prop.split('.')[1]) === index) {
          this.$refs['tableForm'].validateField(item.prop, (error) => {
            if (error !== '') {
              result = false
            }
          })
        }
      }
      return result
    },
    handleCreate() {
      this.$refs['tableForm'].clearValidate() // 清除报错信息
      const tempData = Object.assign({}, this.sysbook)
      this.tableForm.tableData.unshift(tempData)
    },
    confirmCreate(row, index) {
      if (this.validateSubmit(index)) {
        this.btnLoading = true
        createSysbook(row).then(response => {
          row.edit = false
          row.id = response.data.id
          this.btnLoading = false
          this.$message({
            message: '新增成功',
            type: 'success'
          })
          this.temp = null
        }).catch(() => {
          console.log('failed!')
          this.btnLoading = false
        })
      }
    },
    cancelCreate(index) {
      this.tableForm.tableData.splice(index, 1)
    },
    handleModify(row) {
      row.edit = true
      this.temp = Object.assign({}, row) // 保存修改前数据
    },
    confirmModify(index, row) {
      if (this.validateSubmit(index)) {
        this.btnLoading = true
        const tempData = Object.assign({}, row)
        updateSysbook(tempData).then(() => {
          row.edit = false
          this.btnLoading = false
          this.$message({
            message: '修改成功',
            type: 'success'
          })
          this.temp = null
        }).catch(() => {
          console.log('failed!')
          this.btnLoading = false
        })
      }
    },
    cancelModify(index, row) {
      this.temp.edit = false
      this.tableForm.tableData.splice(index, 1, this.temp)
      this.temp = null
    },
    handleDelete(index, id) {
      this.$confirm('此操作会永久删除此数据，是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async() => {
        await deleteSysbook(id)
        this.tableForm.tableData.splice(index, 1)
        this.$message({
          message: '删除成功',
          type: 'success'
        })
      })
        .catch(err => { console.error(err) })
    }
  }
}
</script>

<style lang="scss" scoped>
.create-button {
  padding-bottom: 20px;
}
</style>
