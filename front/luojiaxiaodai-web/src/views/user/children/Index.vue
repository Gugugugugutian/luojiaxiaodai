<template>
    <div class="content">
        <el-row>
        <el-col :span="24"><div class="grid-content bg-purple-dark">珞珈小代：代取快递系统欢您！</div></el-col>
        </el-row>
        <el-alert
                :title="user.school.name + '当前学校共有' + tasks.length + '个任务'"
                :closable="false"
                type="info">
        </el-alert>
        <div class="title">
            <div class="title_center">
                <h2>{{tasks.length}}</h2>
                <p>任务数量</p>
            </div>
            <div class="title_right">
                <h2>{{users.length}}</h2>
                <p>总用户</p>
            </div>
        </div>

        <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>使用帮助</span>
            </div>
          <p>欢迎使用珞珈小代，希望下面内容能助您更好的使用系统！</p>
          <el-collapse v-model="activeNames" @change="handleChange" style="margin-top: 5px">
            <el-collapse v-model="activeNames">
              <el-collapse-item title="用户信息维护说明" name="1">
                <div>
                  用户信息维护功能包括：
                  <ul>
                    <li>用户注册、登录、注销</li>
                    <li>用户信息修改（如昵称、性别等）</li>
                    <li>用户密码、手机号、邮箱、头像修改</li>
                    <li>用户角色切换（如学生、代取者等）</li>
                    <li>用户状态修改（如激活、冻结等）</li>
                  </ul>
                </div>
                <div>提示：</div>
                <ol>
                  <li>如需切换用户角色，请先注销当前用户，再重新登录选择角色。</li>
                  <li>如需修改密码、手机号，请在页面左上角的“安全设置”中进行修改。</li>
                  <li>修改其他用户信息，请在左侧菜单栏选择“用户信息设置”进行操作。</li>
                </ol>
              </el-collapse-item>

              <el-collapse-item title="任务发布说明" name="2">
                <div>
                  <p>任务发布功能允许学生用户发布取件需求，具体操作步骤如下：</p>
                  <ol>
                    <li>点击页面上的“发布任务”按钮。</li>
                    <li>填写取件地点、快递公司、取件码、报酬金额等信息。</li>
                    <li>点击“确认发布”按钮，任务将进入任务列表中。</li>
                  </ol>
                </div>
              </el-collapse-item>

              <el-collapse-item title="任务接取说明" name="3">
                <div>
                  <p>任务接取功能允许代取者浏览和接取任务，具体操作步骤如下：</p>
                  <ol>
                    <li>在任务列表中浏览可接任务，查看任务详情。</li>
                    <li>点击“接取任务”按钮，任务将被锁定为代取者所有。</li>
                    <li>按要求完成任务后，确认送达并获取报酬。</li>
                  </ol>
                </div>
              </el-collapse-item>

              <el-collapse-item title="任务管理说明" name="4">
                <div>
                  <p>任务管理功能包括发布者和代取者的任务查看与管理：</p>
                  <ul>
                    <li>发布者可以查看已发布的任务状态（待接单、进行中、已完成）。</li>
                    <li>代取者可以查看已接取的任务状态，进行任务确认或取消操作。</li>
                  </ul>
                </div>
              </el-collapse-item>

              <el-collapse-item title="发表评价说明" name="5">
                <div>
                  <p>任务完成后，双方可对彼此的服务进行评价，操作步骤如下：</p>
                  <ol>
                    <li>在任务完成页面，点击“发表评价”按钮。</li>
                    <li>填写评价内容，选择星级评分（1-5星）。</li>
                    <li>点击“提交评价”，评价结果将显示在对方的个人信息中。</li>
                  </ol>
                </div>
              </el-collapse-item>

              <el-collapse-item title="注意事项" name="6">
                <div>
                  <ul>
                    <li>请确保填写的任务信息准确，以便代取者能顺利取件。</li>
                    <li>在确认收件前，请仔细核对物品，防止丢失或错误领取。</li>
                    <li>平台管理员有权对用户的评价进行管理，包括删除、屏蔽等操作。请您遵守平台规则，避免被封号。</li>
                    <li>使用平台过程中，请注意确保快递安全。如有疑问，请及时联系客服。</li>
                  </ul>
                </div>
              </el-collapse-item>
            </el-collapse>

          </el-collapse>
        </el-card>
    </div>

</template>

<script>
    import {mapMutations, mapState} from "vuex";

    export default {
        name: "Index",
        data() {
            return {
                tasks: [],
                users: [],
            }
        },
        methods: {
        },
        computed: {
            ...mapState('user', ['user'])
        },
        created() {
            // console.log(this.user)
            this.$get("/task", {id: this.user.id})
                .then((res) => {
                    this.tasks = res.data.task
            })

            this.$get("/user")
            .then((rs) => {
                this.users = rs.data.user
            })

        },

    }
</script>

<style scoped lang="less">

    .el-row {
        margin-bottom: 20px;
        &:last-child {
        margin-bottom: 0;
        }
    }
    .el-col {
        border-radius: 4px;
    }

      .bg-purple {
    background: #d3dce6;
  }

    .content {
        background: #FFf;
        margin: 0 15px;
        padding: 15px;

        .card h2 {
            font-weight: normal;
            font-size: 18px;

            span {
                font-size: 12px;
                display: inline-block;
                border: 1px solid red;
                padding: 1px 3px;
            }
        }

        .title {
            margin: 15px 0;
            width: 100%;
            display: flex;
            height: 100px;
            justify-content: space-between;

            div {
                border-radius: 5px;
                padding: 15px;
                color: #fff;
            }

            div p {
                font-size: 12px;
                margin-top: 10px;
            }

            .title_center {
                width: 50%;
                background: #d8b34d;
            }

            .title_right {
                width: 50%;
                background: #1f68bb;
            }
        }

    }

</style>