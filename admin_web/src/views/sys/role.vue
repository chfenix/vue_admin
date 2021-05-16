<template>
  <div class="app-container">
    <div class="filter-container">
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
      <el-table-column label="CODE" header-align="center">
        <template slot-scope="{row}">
          <span>{{ row.code }}</span>
        </template>
      </el-table-column>
      <el-table-column label="名称" header-align="center">
        <template slot-scope="{row}">
          <span>{{ row.name }}</span>
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

    <el-dialog :visible.sync="dialogVisible" :title="dialogType==='create'?'新增角色':'修改角色'">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="right" label-width="80px">
        <el-form-item label="CODE" prop="code">
          <el-input v-model="temp.code" placeholder="请输入角色CODE" />
        </el-form-item>
        <el-form-item label="名称" prop="name">
          <el-input v-model="temp.name" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="temp.remark" placeholder="请输入备注" />
        </el-form-item>
        <el-form-item label="权限">
          <el-tree
            ref="tree"
            :data="menuData"
            :props="menuProps"
            :default-checked-keys="temp.checkedMenus"
            show-checkbox
            node-key="id"
            class="permission-tree"
          />
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
import { getRoleList, updateRole, createRole, invalidRole, getMenuTree, getRoleMenus } from '@/api/role'
import Pagination from '@/components/Pagination' // secondary package based on el-pagination

export default {
  name: 'Role',
  components: { Pagination },
  data() {
    return {
      listQuery: {
        pageNum: 1,
        pageSize: 10
      },
      rules: {
        code: [{ required: true, message: '角色CODE不能为空', trigger: 'blur' }],
        name: [{ required: true, message: '角色名称不能为空', trigger: 'blur' }]
      },
      list: null,
      temp: {
        id: undefined,
        code: '',
        name: '',
        remark: '',
        checkedMenus: []
      },
      menuData: [],
      menuProps: {
        id: 'id',
        label: 'title',
        children: 'children'
      },
      authority: {
        list: [],
        props: {
          label: 'title',
          children: 'children'
        },
        checkedKeys: [],
        rid: 0,
        visible: false,
        title: ''
      },
      total: 0,
      btnLoading: false,
      listLoading: true,
      dialogVisible: false,
      dialogType: 'create'
    }
  },
  created() {
    this.fetchData()
    this.initMenuTree()
  },
  methods: {
    resetTemp() {
      this.temp = {
        id: undefined,
        code: '',
        name: '',
        remark: '',
        checkedMenus: []
      }
    },
    handleFilter() {
      this.listQuery.pageNum = 1
      this.fetchData()
    },
    fetchData() {
      this.listLoading = true
      getRoleList(this.listQuery).then(response => {
        this.list = response.data.list
        this.total = response.data.total
        this.listLoading = false
      })
    },
    initMenuTree() {
      getMenuTree().then(response => {
        this.menuData = response.data
      })
    },
    handleCreate() {
      this.resetTemp()
      // FIXME 无法清空之前树的选择
      // this.$refs.tree.setCheckedKeys([])
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
          // 复制选中id至tempData中
          this.$refs.tree.getCheckedNodes(false, true)
            .forEach((node) => {
              tempData.checkedMenus.push(node.id)
            })
          createRole(tempData).then(() => {
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
        }
      })
    },
    handleModify(row) {
      this.resetTemp()
      this.temp = Object.assign({}, row) // copy obj
      getRoleMenus(this.temp.id).then(response => {
        this.$refs.tree.setCheckedNodes(response.data)
      })
      this.dialogType = 'modify'
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    modifyData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.btnLoading = true
          const tempData = Object.assign({}, this.temp)
          tempData.checkedMenus = []
          // 复制选中id至tempData中
          this.$refs.tree.getCheckedNodes(false, true)
            .forEach((node) => {
              tempData.checkedMenus.push(node.id)
            })
          updateRole(tempData).then(() => {
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
      this.$confirm('此操作会删除角色数据，是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async() => {
        await invalidRole(id)
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
