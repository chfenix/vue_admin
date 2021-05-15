<template>
  <div class="app-container">
    <el-form ref="passwordForm" :model="form" :rules="submitRules" label-width="140px">
      <el-form-item label="旧密码" prop="oldPassword">
        <el-input v-model="form.oldPassword" type="password" placeholder="输入旧密码" />
      </el-form-item>
      <el-form-item label="新密码" prop="newPassword">
        <el-input v-model="form.newPassword" type="password" placeholder="输入新密码" />
      </el-form-item>
      <el-form-item label="再次输入新密码" prop="repeatNewPassword">
        <el-input v-model="form.repeatNewPassword" type="password" placeholder="再次输入新密码" />
      </el-form-item>
      <el-form-item>
        <el-button :loading="loading" type="primary" @click="onSubmit">修 改</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { changePwd } from '@/api/auth'
import { validPassword } from '@/utils/validate'

export default {
  data() {
    // 检查输入内容
    const validatePassword = (rule, value, callback) => {
      if (value.length === 0) {
        callback(new Error('密码不能为空!'))
      } else if (!validPassword(value)) {
        callback(new Error('密码必须包含字母、数字和特殊字符!'))
      } else {
        callback()
      }
    }
    // 校验新密码是否一致
    const validateRepeatPassword = (rule, value, callback) => {
      if (this.form.newPassword !== this.form.repeatNewPassword) {
        callback(new Error('两次输入的新密码不一致!'))
      } else {
        callback()
      }
    }
    return {
      form: {
        oldPassword: '',
        newPassword: '',
        repeatNewPassword: ''
      },
      submitRules: {
        oldPassword: [{ required: true, trigger: 'blur', validator: validatePassword }],
        newPassword: [{ required: true, trigger: 'blur', validator: validatePassword }],
        repeatNewPassword: [{ required: true, trigger: 'blur', validator: validatePassword }, { trigger: 'blur', validator: validateRepeatPassword }]
      },
      loading: false
    }
  },
  methods: {
    onSubmit() {
      this.$refs.passwordForm.validate(valid => {
        if (valid) {
          this.loading = true
          changePwd(this.form).then(() => {
            console.log('success!')
            this.loading = false
            this.$message.success('密码修改成功，请使用新密码登录!')
            Object.keys(this.form).forEach(key => (this.form[key] = ''))
          }).catch(() => {
            console.log('failed!')
            this.loading = false
          })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    }
  }
}
</script>

<style scoped>
.line{
  text-align: center;
}
</style>

