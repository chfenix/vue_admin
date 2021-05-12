<template>
  <div class="app-container">
    <el-form ref="passwordForm" :model="form" :rules="submitRules" label-width="140px">
      <el-form-item label="Old Password" prop="oldPassword">
        <el-input v-model="form.oldPassword" type="password" placeholder="Please enter the old password" />
      </el-form-item>
      <el-form-item label="New Password" prop="newPassword">
        <el-input v-model="form.newPassword" type="password" placeholder="Please enter the new password" />
      </el-form-item>
      <el-form-item label="New Password" prop="repeatNewPassword">
        <el-input v-model="form.repeatNewPassword" type="password" placeholder="Please enter the new password again" />
      </el-form-item>
      <el-form-item>
        <el-button :loading="loading" type="primary" @click="onSubmit">Save</el-button>
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
        callback(new Error('Please input the password!'))
      } else if (!validPassword(value)) {
        callback(new Error('Password must be number and letter!'))
      } else {
        callback()
      }
    }
    // 校验新密码是否一致
    const validateRepeatPassword = (rule, value, callback) => {
      if (this.form.newPassword !== this.form.repeatNewPassword) {
        callback(new Error('Two New password must be the same!'))
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
            this.$message.success('New password saved success!')
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

