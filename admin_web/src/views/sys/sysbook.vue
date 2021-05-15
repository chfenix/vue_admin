<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.typeCode" placeholder="Type Code" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        查询
      </el-button>
      <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="handleCreate">
        增加
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
        class="order-table"
        :header-cell-style="{background:'Gainsboro',color:'#000'}"
      >
        <el-table-column label="Type Code">
          <template slot-scope="scope">
            <template v-if="scope.row.edit">
              <el-form-item :prop="'tableData.' + scope.$index + '.typeCode'" :rules="tableFormRules.typeCode">
                <el-input v-model="scope.row.typeCode" class="edit-input" size="small" />
              </el-form-item>
            </template>
            <span v-else>{{ scope.row.typeCode }}</span>
          </template>
        </el-table-column>
        <el-table-column label="Type Name">
          <template slot-scope="scope">
            <template v-if="scope.row.edit">
              <el-form-item :prop="'tableData.' + scope.$index + '.typeName'" :rules="tableFormRules.typeName">
                <el-input v-model="scope.row.typeName" class="edit-input" size="small" />
              </el-form-item>
            </template>
            <span v-else>{{ scope.row.typeName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="List Code">
          <template slot-scope="scope">
            <template v-if="scope.row.edit">
              <el-form-item :prop="'tableData.' + scope.$index + '.listCode'" :rules="tableFormRules.listCode">
                <el-input v-model="scope.row.listCode" class="edit-input" size="small" />
              </el-form-item>
            </template>
            <span v-else>{{ scope.row.listCode }}</span>
          </template>
        </el-table-column>
        <el-table-column label="List Name">
          <template slot-scope="scope">
            <template v-if="scope.row.edit">
              <el-form-item :prop="'tableData.' + scope.$index + '.listName'" :rules="tableFormRules.listName">
                <el-input v-model="scope.row.listName" class="edit-input" size="small" />
              </el-form-item>
            </template>
            <span v-else>{{ scope.row.listName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="显示顺序">
          <template slot-scope="scope">
            <template v-if="scope.row.edit">
              <el-form-item :prop="'tableData.' + scope.$index + '.pri'" :rules="tableFormRules.pri">
                <el-input v-model="scope.row.pri" class="edit-input" size="small" />
              </el-form-item>
            </template>
            <span v-else>{{ scope.row.pri }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160" align="center" type="index">
          <template slot-scope="scope">
            <template v-if="scope.row.edit">
              <el-button type="success" :loading="btnLoading" size="mini" icon="el-icon-check" @click="scope.row.id == null?confirmCreate(scope.row,scope.$index):confirmEdit(scope.$index,scope.row)" />
              <el-button type="warning" :loading="btnLoading" size="mini" icon="el-icon-close" @click="scope.row.id == null?cancelCreate(scope.$index):cancelEdit(scope.$index, scope.row)" />
            </template>
            <template v-else>
              <el-button
                type="primary"
                size="mini"
                icon="el-icon-edit"
                @click="handleModify(scope.row)"
              >
                修改
              </el-button>
              <el-button
                type="danger"
                size="mini"
                icon="el-icon-delete"
                @click="handleDelete(scope.$index, scope.row.id)"
              />
            </template>
          </template>
        </el-table-column>
      </el-table>
    </el-form>
    <pagination v-show="total>0" :total="total" :page.sync="listQuery.pageNum" :limit.sync="listQuery.pageSize" @pagination="fetchData" />

    <el-dialog :visible.sync="dialogVisible" :title="'数据字典'">
      <el-form :model="sysbook" label-width="80px" label-position="left">
        <el-form-item label="类型编号">
          <el-input v-model="sysbook.typeCode" placeholder="请输入类型编号" />
        </el-form-item>
      </el-form>
      <el-form :model="sysbook" label-width="80px" label-position="left">
        <el-form-item label="类型名称">
          <el-input v-model="sysbook.typeName" placeholder="请输入类型名称" />
        </el-form-item>
      </el-form>
      <el-form :model="sysbook" label-width="80px" label-position="left">
        <el-form-item label="明细编号">
          <el-input v-model="sysbook.listCode" placeholder="请输入明细编号" />
        </el-form-item>
      </el-form>
      <el-form :model="sysbook" label-width="80px" label-position="left">
        <el-form-item label="明细名称">
          <el-input v-model="sysbook.listName" placeholder="请输入明细名称" />
        </el-form-item>
      </el-form>
      <el-form :model="sysbook" label-width="80px" label-position="left">
        <el-form-item label="显示顺序">
          <el-input v-model="sysbook.pri" placeholder="请输入显示顺序" />
        </el-form-item>
      </el-form>
      <div style="text-align:right;">
        <el-button type="danger" @click="dialogVisible=false">取消</el-button>
        <el-button type="primary">保存</el-button>
      </div>
    </el-dialog>
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
      tmp: null,
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
          this.$notify({
            title: '操作成功',
            message: '新增成功',
            type: 'success',
            duration: 2000
          })
          this.tmp = null
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
      this.tmp = Object.assign({}, row) // 保存修改前数据
    },
    confirmEdit(index, row) {
      if (this.validateSubmit(index)) {
        this.btnLoading = true
        const tempData = Object.assign({}, row)
        updateSysbook(tempData).then(() => {
          row.edit = false
          this.btnLoading = false
          this.$notify({
            title: '操作成功',
            message: '修改成功',
            type: 'success',
            duration: 2000
          })
          this.tmp = null
        }).catch(() => {
          console.log('failed!')
          this.btnLoading = false
        })
      }
    },
    cancelEdit(index, row) {
      this.tmp.edit = false
      this.tableForm.tableData.splice(index, 1, this.tmp)
      this.tmp = null
    },
    handleDelete(index, id) {
      this.$confirm('是否删除数据字典?', '注意', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async() => {
        await deleteSysbook(id)
        this.tableForm.tableData.splice(index, 1)
        this.$notify({
          title: '操作成功',
          message: '删除成功',
          type: 'success',
          duration: 2000
        })
      })
        .catch(err => { console.error(err) })
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
