<template>
  <div class="login-container">
    <el-card class="login-card">
      <el-form :model="loginForm" :rules="loginRules" ref="loginForm" label-position="left">
        <div class="logo-box">
          <svg-icon icon-class="login-mall" class="logo-icon" />
          <h2 class="login-title">mall-admin-web</h2>
        </div>

        <el-form-item prop="username">
          <el-input v-model="loginForm.username" placeholder="请输入用户名" autocomplete="on">
            <template #prefix>
              <svg-icon icon-class="user" />
            </template>
          </el-input>
        </el-form-item>

        <el-form-item prop="password">
          <el-input
            :key="pwdType"
            :type="pwdType"
            v-model="loginForm.password"
            placeholder="请输入密码"
            autocomplete="on"
            @keyup.enter.native="handleLogin">
            <template #prefix>
              <svg-icon icon-class="password" />
            </template>
            <template #suffix>
              <svg-icon :icon-class="pwdType === 'password' ? 'eye' : 'eye-open'" @click="showPwd" style="cursor: pointer" />
            </template>
          </el-input>
        </el-form-item>

        <el-form-item class="btn-box">
          <el-button type="primary" :loading="loading" @click.native.prevent="handleLogin" class="btn-login">
            登录
          </el-button>
          <el-button type="text" @click.native.prevent="handleTry">注册账号</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 注册界面 -->
    <el-dialog title="注册账号" :visible.sync="dialogVisible" center width="400px">
      <el-form :model="registerForm" :rules="registerRules" ref="registerForm" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="registerForm.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input type="password" v-model="registerForm.password" placeholder="请输入密码" />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitRegister">注册</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { isvalidUsername } from '@/utils/validate';
import { setSupport, getSupport, setCookie, getCookie } from '@/utils/support';
import login_center_bg from '@/assets/images/login_center_bg.png';

export default {
  name: 'login',
  data() {
    const validateUsername = (rule, value, callback) => {
      if (!isvalidUsername(value)) {
        callback(new Error('请输入正确的用户名'))
      } else {
        callback()
      }
    };
    const validatePass = (rule, value, callback) => {
      if (value.length < 3) {
        callback(new Error('密码不能小于3位'))
      } else {
        callback()
      }
    };
    return {
      loginForm: {
        username: '',
        password: '',
      },
      loginRules: {
        username: [{ required: true, trigger: 'blur', validator: validateUsername }],
        password: [{ required: true, trigger: 'blur', validator: validatePass }]
      },
      loading: false,
      pwdType: 'password',
      login_center_bg,
      dialogVisible: false,
      registerForm: {
        username: '',
        password: ''
      },
      registerRules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.loginForm.username = getCookie("username");
    this.loginForm.password = getCookie("password");
    if (!this.loginForm.username) {
      this.loginForm.username = 'admin';
    }
    if (!this.loginForm.password) {
      this.loginForm.password = '';
    }
  },
  methods: {
    showPwd() {
      this.pwdType = this.pwdType === 'password' ? 'text' : 'password';
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true;
          this.$store.dispatch('Login', this.loginForm).then(() => {
            this.loading = false;
            setCookie("username", this.loginForm.username, 15);
            setCookie("password", this.loginForm.password, 15);
            this.$router.push({ path: '/' });
          }).catch(() => {
            this.loading = false;
          })
        } else {
          console.log('参数验证不合法！');
          return false;
        }
      })
    },
    handleTry() {
      this.dialogVisible = true;
    },
    submitRegister() {
      this.$refs.registerForm.validate((valid) => {
        if (valid) {
          // 模拟注册成功逻辑（此处应接调用注册 API）
          this.$message.success('注册成功！请使用新账户登录');
          this.dialogVisible = false;

          // 可选：自动填充到登录表单
          this.loginForm.username = this.registerForm.username;
          this.loginForm.password = this.registerForm.password;
        }
      });
    }
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: #f5f7fa;
}

.login-card {
  width: 360px;
  padding: 20px 30px;
  border-top: 6px solid #409EFF;
  border-radius: 8px;
}

.logo-box {
  text-align: center;
  margin-bottom: 20px;
}

.logo-icon {
  font-size: 48px;
  color: #409EFF;
  margin-bottom: 8px;
}

.login-title {
  margin: 0;
  font-size: 20px;
  color: #409EFF;
}

.btn-box {
  text-align: center;
}

.btn-login {
  width: 100%;
}
</style>
