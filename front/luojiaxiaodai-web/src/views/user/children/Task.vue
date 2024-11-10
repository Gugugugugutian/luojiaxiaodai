<template>
    <div class="content">
        <el-alert
                title="发布新任务"
                :closable="false"
                type="info">
        </el-alert>
        <el-card class="box-card card">
            <div slot="header" class="clearfix">
                <span>发布任务</span>
                <el-button style="float: right; padding: 3px 0;font-size: 16px" icon="el-icon-s-promotion" type="text"
                           @click="submitTask">发布
                </el-button>
            </div>
            <div class="input">
                <div class="append">任务标题</div>
                <el-input placeholder="请输入任务标题" v-model="taskTitle"></el-input>
            </div>
            <div class="input">
                <div class="append">奖励</div>
                <el-input placeholder="请输入奖励（单位：元）" v-model="reward" oninput="value=value.replace(/[^\d]/g,'')"></el-input>
                <div class="append" style="border-right: 1px solid #DCDFE6;border-left: none;">余额：{{user.balance}}元
                </div>
            </div>
            <el-card class="box-card">
                <div slot="header" class="clearfix">
                    <span>任务详情</span>
                </div>
              <div class="input">
                <div class="append">快递点</div>
                <el-input placeholder="请输入快递点" v-model="expressPoint"></el-input>
              </div>
              <div class="input">
                <div class="append">取件码</div>
                <el-input placeholder="请输入取件码" v-model="code"></el-input>
              </div>
              <div class="input">
                <div class="append">物品重量</div>
                <el-radio-group v-model="weight" style="flex: 5;">
                  <el-radio label="轻">轻（1kg-）</el-radio>
                  <el-radio label="常规">常规（1kg-10kg）</el-radio>
                  <el-radio label="重">重（10kg+）</el-radio>
                </el-radio-group>
              </div>
              <div class="input">
                <div class="append">快递公司</div>
                <el-input placeholder="请输入快递公司" v-model="expressCompany"></el-input>
              </div>
              <div class="input">
                <div class="append">快递单号</div>
                <el-input placeholder="请输入快递单号" v-model="expressNumber"></el-input>
              </div>
              <div class="input">
                <div class="append">收件人姓名</div>
                <el-input placeholder="请输入收件人姓名" v-model="name"></el-input>
              </div>
              <div class="input">
                <div class="append">收件人电话</div>
                <el-input placeholder="请输入收件人电话" v-model="phone"></el-input>
              </div>
              <div class="input">
                <div class="append">收件地址</div>
                <el-input placeholder="您可以输入收件地址" v-model="taskContext"></el-input>
              </div>
            </el-card>
        </el-card>
        <el-drawer
                title="任务信息"
                :visible.sync="drawer"
                direction="rtl">
            <div class="content_drawer">
                <el-card class="box-card">
                    <div slot="header" class="clearfix">
                        <span>发布内容</span>
                    </div>
                    <el-collapse v-model="activeNames">
                        <el-collapse-item title="任务标题" name="1">
                            <div>{{taskTitle}}</div>
                        </el-collapse-item>
                        <el-collapse-item title="任务内容" name="2">
<!--                            <div>{{taskContext}}</div>-->
                          <div>请到任务详情页面查看</div>
                        </el-collapse-item>
                        <el-collapse-item title="任务奖励" name="3">
                            <div><i class="el-icon-money" style="color: red;"> {{reward}}元</i></div>
                        </el-collapse-item>
                        <el-collapse-item title="发布时间" name="4">
                            <div>{{createTime | formatDate}}</div>
                        </el-collapse-item>
                    </el-collapse>
                </el-card>
            </div>
        </el-drawer>
    </div>
</template>

<script>
    import {mapMutations, mapState} from "vuex";
    import {formatDate} from '@/util/date';

    export default {
        name: "Task",
        data() {
            return {
                // 任务奖励
                reward: "",
                // 任务标题
                taskTitle: "代取快递",
                // 任务内容
                taskContext: "",
                // 快递点
                expressPoint: "",
                // 取件码
                code: "",
                // 物品重量
                weight: "轻",
                // 快递公司
                expressCompany: "",
                // 快递单号
                expressNumber: "",
                // 收件人姓名
                name: "",
                // 收件人电话
                phone: "",
                // 发布时间
                createTime: "",
                // 零钱
                balance: 0,
                // 是否弹出抽屉
                drawer: false,
                activeNames: ['1', '2', '3', '4']

            }
        },
        computed: {
            ...mapState("user", ['user'])
        },
        methods: {
            ...mapMutations('user', ['setUser']),
            
            submitTask() {
              // 快递任务的格式：kd&加任务内容，以&%&分开
              this.taskContext = [
                  '快递',
                  this.expressPoint,
                  this.code,
                  this.weight,
                  this.expressCompany,
                  this.expressNumber,
                  this.name,
                  this.phone,
                  this.taskContext
              ].join('%&')

                if (this.taskTitle && this.reward > 0 && this.taskContext) {
                    // console.log(this.user)
                    this.$post("/task",
                        {
                            "publishId": this.user.id,
                            "schoolId": this.user.school.id,
                            "reward": this.reward,
                            "taskTitle": this.taskTitle,
                            "taskContext": this.taskContext
                        }
                    ).then(res => {
                        if (res.data.status) {
                            this.createTime = new Date().getTime()
                            this.drawer = true;
                            this.renew()
                            this.$msg(res.data.msg, "success")
                        } else {
                            this.$msg(res.data.msg, "error")
                        }
                    })
                } else {
                    this.$msg("请正确填写信息", "error")
                }
            },
            renew() {
                this.$get("user/" + this.user.id)
                    .then(response => {
                        sessionStorage.setItem('user', JSON.stringify(response.data.user))
                        this.setUser(JSON.parse(sessionStorage.getItem('user')))
                    })
            },
        },
        created() {
            this.renew()
        },
        filters: {
            formatDate(time) {
                let date = new Date(time);
                return formatDate(date, 'yyyy-MM-dd hh:mm');
            }
        }
    }
</script>

<style scoped lang="less">
    .content {
        background: #FFf;
        margin: 0 15px;
        padding: 15px;

        .card {
            margin-top: 20px;

            .input {
                margin-top: 10px;

                width: 100%;
                height: 40px;
                display: flex;
                justify-content: space-between;
                align-items: center;

                .append {
                    border: 1px solid #DCDFE6;
                    border-right: none;
                    width: 150px;
                    font-size: 14px;
                    line-height: 40px;
                    height: 100%;
                    text-align: center;
                }

                .select {
                    flex: 1;
                }

                /deep/ .el-input {
                    flex: 1;
                }


            }

            .box-card {

                /deep/ .el-textarea__inner {
                    font-family: '微软雅黑' !important;
                }

                margin-top: 10px;

                /deep/ .el-card__header {
                    border-bottom: none;
                }

                /deep/ .el-card__body {
                    padding: 0 !important;
                }
            }
        }

        .content_drawer {
            padding: 0 20px;

            p {
                margin: 10px 0;
            }

            span {
                font-size: 14px;
            }
        }

    }
</style>