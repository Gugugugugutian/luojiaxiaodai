<template>
  <div class="forgot-password-container">
    <div class="form">
      <div class="return-container">
        <span class="icon" @click="goBack">
          <i class="fas fa-arrow-left"></i>
        </span>
        <h2 style="text-align: center">找回密码</h2>
      </div>
      <!-- 输入账号 (student_id) -->
      <label>
          <span>学号</span>
          <input type="text" v-model="studentId" placeholder="请输入学号" required />
      </label>
      <!-- 输入邮箱 -->
      <label>
        <span>邮箱</span>
        <input type="email" v-model="email" placeholder="请输入注册邮箱" required />
      </label>
      <!-- 发送验证码按钮 -->
      <button type="button" class="send-code" @click="sendCode" :disabled="isCodeSent">
        {{ isCodeSent ? "验证码已发送" : "发送验证码" }}
      </button>
      <!-- 输入验证码 -->
      <label>
        <span>验证码</span>
        <input type="text" v-model="verificationCode" placeholder="请输入验证码" required />
      </label>
      <!-- 输入新密码 -->
      <label>
        <span>新密码</span>
        <input type="password" v-model="newPassword" placeholder="请输入新密码" required />
      </label>
      <!-- 提交按钮 -->
      <button type="button" class="submit" @click="resetPassword">提交</button>
    </div>
  </div>
</template>

<script>
export default {
  name: "ForgotPassword",
  data() {
    return {
      studentId: "", // 学号字段
      email: "",
      verificationCode: "",
      newPassword: "",
      isCodeSent: false, // 防止重复发送验证码
    };
  },
  methods: {
    // 返回上一级
    goBack() {
      this.$router.go(-1);
    },
    // 发送验证码
    sendCode() {
      if (!this.studentId || !this.email) {
        this.$msg("请输入学号和邮箱", "error");
        return;
      }

      // 调用后端发送验证码接口，传递学号和邮箱
      this.$post("/api/password/send-code", { email: this.email, studentId: this.studentId })
        .then((res) => {
          if (res.data.status) {
            this.$msg("验证码发送成功，请查收邮箱", "success");
            this.isCodeSent = true;
            setTimeout(() => (this.isCodeSent = false), 60000); // 一分钟后可重新发送
          } else {
            this.$msg(res.data.msg || "验证码发送失败", "error");
          }
        })
        .catch(() => {
          this.$msg("验证码发送失败，请重试", "error");
        });
    },
    // 重置密码
    resetPassword() {
      if (!this.studentId || !this.email || !this.verificationCode || !this.newPassword) {
        this.$msg("请填写完整信息", "error");
        return;
      }

      // 调用后端重置密码接口，传递学号、邮箱、验证码、新密码
      this.$post("/api/password/reset", {
        studentId: this.studentId,
        email: this.email,
        code: this.verificationCode,
        newPassword: this.$md5(this.newPassword),
      })
        .then((res) => {
          if (res.data.status) {
            this.$msg("密码重置成功，请使用新密码登录", "success");
            this.$router.push("/login"); // 跳转回登录页面
          } else {
            this.$msg(res.data.msg || "密码重置失败", "error");
          }
        })
        .catch(() => {
          this.$msg("密码重置失败，请稍后再试", "error");
        });
    },
  },
};
</script>

<style scoped>
.forgot-password-container {
  width: 400px;
  margin: 50px auto;
  padding: 20px;
  background-color: #fff;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.return-container {
  position: relative;
}

.icon {
  position: absolute;
  top: 50%;
  font-size: 18px;
  transform: translate(0, -50%);
}

title {
  text-align: center;
  margin-bottom: 20px;
  font-size: 24px;
  color: #333;
}

label {
  display: block;
  margin-bottom: 15px;
}

label span {
  font-size: 14px;
  color: #909399;
}

input {
  display: block;
  width: 100%;
  padding: 8px;
  margin-top: 5px;
  font-size: 14px;
  border: 1px solid #ddd;
  border-radius: 4px;
  outline: none;
}

input:focus {
  border-color: #d4af7a;
}

button {
  display: block;
  width: 100%;
  margin-top: 20px;
  padding: 10px;
  font-size: 16px;
  color: #fff;
  background-color: #d4af7a;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:disabled {
  background-color: #bbb;
  cursor: not-allowed;
}

button:hover:not(:disabled) {
  background-color: #b8956a;
}
</style>
