<template>
  <div class="">
    <div class="content" :class="isShow?'s--signup':''">
      <div class="form">
        <h2>欢迎回来</h2>
        <label>
          <span>学号(纯数字)</span>
          <input type="text" v-model="studentId" oninput="value=value.replace(/[^\d]/g,'')"/>
        </label>
        <label>
          <span>密码</span>
          <input type="password" v-model="password"/>
        </label>
        <label>
          <span style="margin-bottom: 20px">选择学校</span>
        </label>
        <el-select v-model="schoolId" placeholder="请选择" class="school">
          <el-option
              v-for="item in school"
              :key="item.id"
              :label="item.name"
              :value="item.id">
          </el-option>
        </el-select>
        <p class="forgot-pass">
          <router-link to="/forgot-password">忘记密码？</router-link>
        </p>

        <button type="button" class="submit" @click="toLogin('sign')">登 录</button>
      </div>
      <div class="sub-cont">
        <div class="img">
          <div class="img__text m--up">
            <h2>还未注册？</h2>
            <p>立即注册，发现大量机会！</p>
          </div>
          <div class="img__text m--in">
            <h2>已有帐号？</h2>
            <p>有帐号就登录吧，好久不见了！</p>
          </div>
          <div class="img__btn" @click="isShow = !isShow">
            <span class="m--up">注 册</span>
            <span class="m--in">登 录</span>
          </div>
        </div>
        <div class="form sign-up">
          <h2>立即注册</h2>
          <label>
            <span>学号(纯数字)</span>
            <input type="text" v-model="studentId" oninput="value=value.replace(/[^\d]/g,'')"/>
          </label>
          <label>
            <span>密码</span>
            <input type="password" v-model="password"/>
          </label>
          <label>
            <span style="margin-bottom: 20px">选择学校</span>
          </label>
          <el-select v-model="schoolId" placeholder="请选择" class="school" @change="getDomain(school.find(item => item.id === schoolId))">
            <el-option
                v-for="item in school"
                :key="item.id"
                :label="item.name"
                :value="item.id">
            </el-option>
          </el-select>
          <label>
            <!-- 输入邮箱 -->
            <span>邮箱</span>
            <div class="input-button-container">
              <input type="email" v-model="email" style="width: 60%" required/>
              <input style="width:10%;border: none" placeholder="@" disabled/>
              <input type="email" style="width: 30%;border: none" v-model="schoolDomain" disabled/>
            </div>
          </label>
          <label>
            <div class="input-button-container">
              <!-- 输入验证码 -->
              <input type="text" v-model="verificationCode" placeholder="请输入验证码" required/>
              <!-- 发送验证码按钮 -->
              <button type="button" class="send-code" @click="sendCode" :disabled="isCodeSent">
                {{ isCodeSent ? "已发送" : "发送验证码" }}
              </button>
            </div>
          </label>
          <button type="button" class="submit" @click="toLogin('register')">注 册</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>

import {setCookie, getCookie} from "@/util/util";

export default {
  name: "Login",
  data() {
    return {
      isShow: false,
      studentId: "",
      password: "",
      school: [],
      schoolId: '',
      schoolDomain:"xxx.xxx",
      email: "",
      verificationCode: "",
      isCodeSent: false // 防止重复发送验证码
    }
  },
  watch: {
    schoolId() {
      // console.log(this.schoolId)
    }
  },
  created() {
    this.$get("/school")
        .then((res) => {
          this.school = res.data.school
        })

  },
  methods: {
    toLogin(val) {
      if (this.studentId && this.password && this.schoolId) {

        //登录
        if (val == 'sign') {
          {
            this.$get("/user/login",
                {
                  "studentId": this.studentId,
                  "password": this.$md5(this.password),
                  "schoolId": this.schoolId
                }
                //判断是否被冻结
            ).then((res) => {
              if (res.data.status) {
                if (res.data.user.state == 0) {
                  let accountOrName = res.data.user.username ? res.data.user.username : res.data.user.studentId;

                  //存储数据到sessionStorage
                  sessionStorage.setItem("user", JSON.stringify(res.data.user));

                  this.$msg(accountOrName + "，登录成功", "success")
                  this.$router.push("/home")
                } else {
                  this.$msg('该用户已被冻结，请联系管理员', "error")
                }
              } else {
                this.$msg(res.data.msg, "error")
              }
            })
          }
        } else {//注册
          if(!this.isCodeSent){
            this.$msg("还未发送验证码", "error");
            return;
          }
          this.$post("/api/password/register/verifyCodes", {
            email: this.email,
            code: this.verificationCode,
          }).then((isSuccess) => {
            if(!isSuccess)
              this.$msg("验证码错误或已过期", "error");
            else{
              this.$post("/user",
                  {
                    "studentId": this.studentId,
                    "password": this.$md5(this.password),
                    "schoolId": this.schoolId,
                    "email": this.email + "@" + this.schoolDomain,
                  }
              ).then((res) => {
                if (res.data.status) {
                  this.$msg(res.data.msg, "success")
                  this.isShow = false;
                  this.studentId = ""
                  this.password = ""
                  this.schoolId = ""
                } else {
                  this.$msg(res.data.msg, "error")
                }
              })
            }
          });

        }
      } else {
        this.$msg("请填写正确的信息", "error")
      }
    },
    getDomain(value){
      console.log(value);
      this.schoolDomain = value.domain;
    },
    // 发送验证码
    sendCode() {
      if (!this.schoolId || !this.email) {
        this.$msg("请输入邮箱", "error");
        return;
      }
      // 调用后端发送验证码接口，传递学号和邮箱
      this.$post("/api/password/register/send-code", {email: this.email + "@" + this.schoolDomain})
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
    }
  }
}
</script>

<style scoped>
*, *:before, *:after {
  box-sizing: border-box;
}

input, button {
  border: none;
  outline: none;
  background: none;
  font-family: 'Open Sans', Helvetica, Arial, sans-serif;
}

.tip {
  font-size: 20px;
  margin: 40px auto 50px;
  text-align: center;
}

.content {
  overflow: hidden;
  position: absolute;
  left: 50%;
  top: 50%;
  width: 900px;
  height: 550px;
  margin: -300px 0 0 -450px;
  background: #fff;
}

.form {
  position: relative;
  width: 640px;
  height: 100%;
  transition: -webkit-transform 0.6s ease-in-out;
  transition: transform 0.6s ease-in-out;
  transition: transform 0.6s ease-in-out, -webkit-transform 0.6s ease-in-out;
  padding: 50px 30px 0;
}

.sub-cont {
  overflow: hidden;
  position: absolute;
  left: 640px;
  top: 0;
  width: 900px;
  height: 100%;
  padding-left: 260px;
  background: #fff;
  transition: -webkit-transform 0.6s ease-in-out;
  transition: transform 0.6s ease-in-out;
  transition: transform 0.6s ease-in-out, -webkit-transform 0.6s ease-in-out;
}

.content.s--signup .sub-cont {
  -webkit-transform: translate3d(-640px, 0, 0);
  transform: translate3d(-640px, 0, 0);
}

button {
  display: block;
  margin: 0 auto;
  width: 260px;
  height: 36px;
  border-radius: 30px;
  color: #fff;
  font-size: 15px;
  cursor: pointer;
}

.img {
  overflow: hidden;
  z-index: 2;
  position: absolute;
  left: 0;
  top: 0;
  width: 260px;
  height: 100%;
  padding-top: 360px;
}

.img:before {
  content: '';
  position: absolute;
  right: 0;
  top: 0;
  width: 900px;
  height: 100%;
  background-image: url(../../assets/img/test2.jpg);
  background-size: cover;
  transition: -webkit-transform 0.6s ease-in-out;
  transition: transform 0.6s ease-in-out;
  transition: transform 0.6s ease-in-out, -webkit-transform 0.6s ease-in-out;
}

.img:after {
  content: '';
  position: absolute;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.3);
}

.content.s--signup .img:before {
  -webkit-transform: translate3d(640px, 0, 0);
  transform: translate3d(640px, 0, 0);
}

.img__text {
  z-index: 2;
  position: absolute;
  left: 0;
  top: 50px;
  width: 100%;
  padding: 0 20px;
  text-align: center;
  color: #fff;
  transition: -webkit-transform 0.6s ease-in-out;
  transition: transform 0.6s ease-in-out;
  transition: transform 0.6s ease-in-out, -webkit-transform 0.6s ease-in-out;
}

.img__text h2 {
  margin-bottom: 10px;
  font-weight: normal;
}

.img__text p {
  font-size: 14px;
  line-height: 1.5;
}

.content.s--signup .img__text.m--up {
  -webkit-transform: translateX(520px);
  transform: translateX(520px);
}

.img__text.m--in {
  -webkit-transform: translateX(-520px);
  transform: translateX(-520px);
}

.content.s--signup .img__text.m--in {
  -webkit-transform: translateX(0);
  transform: translateX(0);
}

.img__btn {
  overflow: hidden;
  z-index: 2;
  position: relative;
  width: 100px;
  height: 36px;
  margin: 0 auto;
  background: transparent;
  color: #fff;
  text-transform: uppercase;
  font-size: 15px;
  cursor: pointer;
}

.img__btn:after {
  content: '';
  z-index: 2;
  position: absolute;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  border: 2px solid #fff;
  border-radius: 30px;
}

.img__btn span {
  position: absolute;
  left: 0;
  top: 0;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  transition: -webkit-transform 0.6s;
  transition: transform 0.6s;
  transition: transform 0.6s, -webkit-transform 0.6s;
}

.img__btn span.m--in {
  -webkit-transform: translateY(-72px);
  transform: translateY(-72px);
}

.content.s--signup .img__btn span.m--in {
  -webkit-transform: translateY(0);
  transform: translateY(0);
}

.content.s--signup .img__btn span.m--up {
  -webkit-transform: translateY(72px);
  transform: translateY(72px);
}

h2 {
  width: 100%;
  font-size: 26px;
  text-align: center;
}

label {
  display: block;
  width: 260px;
  margin: 25px auto 0;
  text-align: center;
}

label span {
  font-size: 12px;
  color: #909399;
  text-transform: uppercase;
}

input {
  display: block;
  width: 100%;
  margin-top: 5px;
  padding-bottom: 5px;
  font-size: 14px;
  border-bottom: 1px solid rgba(0, 0, 0, 0.4);
  text-align: center;
}

.forgot-pass {
  margin-top: 15px;
  text-align: center;
  font-size: 12px;
  color: #cfcfcf;
}

.forgot-pass a {
  color: #cfcfcf;
}

.submit {
  margin-top: 40px;
  margin-bottom: 20px;
  background: #d4af7a;
  text-transform: uppercase;
}

.input-button-container {
  display: flex;
  gap: 2%;
}

.send-code{
  background: #d4af7a;
  text-transform: uppercase;
  border-radius: 5px;
}

.fb-btn {
  border: 2px solid #d3dae9;
  color: #8fa1c7;
}

.fb-btn span {
  font-weight: bold;
  color: #455a81;
}

.sign-in {
  transition-timing-function: ease-out;
}

.content.s--signup .sign-in {
  transition-timing-function: ease-in-out;
  transition-duration: 0.6s;
  -webkit-transform: translate3d(640px, 0, 0);
  transform: translate3d(640px, 0, 0);
}

.sign-up {
  -webkit-transform: translate3d(-900px, 0, 0);
  transform: translate3d(-900px, 0, 0);
  overflow: auto; /* 开启滚动 */
  scrollbar-width: none; /* 隐藏滚动条（Firefox） */
  -ms-overflow-style: none; /* 隐藏滚动条（IE/Edge） */
}

.content.s--signup .sign-up {
  -webkit-transform: translate3d(0, 0, 0);
  transform: translate3d(0, 0, 0);
}

/deep/ .el-select {
  display: block !important;
  margin: 0 auto !important;
  width: 260px !important;
}

/deep/ .el-menu {
  border-right: NONE !important;
}

/deep/ .el-input__inner {
  border: none !important;
  border-bottom: 1px solid rgba(0, 0, 0, 0.4) !important;
  border-radius: 0px !important;
}
</style>