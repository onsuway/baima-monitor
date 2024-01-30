<script setup>
import {reactive, ref} from "vue";
import {logout, post} from "@/net";
import router from "@/router";
import {ElMessage} from "element-plus";
import {Switch} from "@element-plus/icons-vue";

const formRef = ref()
const valid = ref(false)
const onValidate = (prop, isValid) => valid.value = isValid

const form = reactive({
    password: '',
    new_password: '',
    new_password_repeat: '',
})

const validatePassword = (rule, value, callback) => {
    if (value === '') {
        callback(new Error('请再次输入密码'))
    } else if (value !== form.new_password) {
        callback(new Error("两次输入的密码不一致"))
    } else {
        callback()
    }
}

function resetPassword() {
    formRef.value.validate(isValid => {
        if (isValid) {
            post('/api/user/change-password', form, () => {
                ElMessage.success('密码修改成功，请重新登录!')
                logout(() => router.push('/'))
            })
        }
    })
}

const rules = {
    password: [
        {required: true, message: '请输入原来的密码', trigger: 'blur'},
    ],
    new_password: [
        {required: true, message: '请输入新的密码', trigger: 'blur'},
        {min: 6, max: 16, message: '密码的长度必须在6-16个字符之间', trigger: ['blur']}
    ],
    new_password_repeat: [
        {required: true, message: '请重复输入新的密码', trigger: 'blur'},
        {validator: validatePassword, trigger: ['blur', 'change']},
    ],
    email: [
        {required: true, message: '请输入邮件地址', trigger: 'blur'},
        {type: 'email', message: '请输入合法的电子邮件地址', trigger: ['blur', 'change']}
    ]
}
</script>

<template>
    <div style="display: flex;gap: 10px">
        <div style="flex: 50%;">
            <div class="info-card" style="display: flex; justify-content: center">
                <div style="width: 70%;min-width: 500px">
                    <el-form @validate="onValidate" :model="form" :rules="rules"
                             size="large"
                             ref="formRef" style="margin: 20px" label-width="100">
                        <el-form-item label="当前密码" prop="password">
                            <el-input type="password" v-model="form.password"
                                      :prefix-icon="Lock" placeholder="当前密码" maxlength="20"/>
                        </el-form-item>
                        <el-form-item label="新密码" prop="new_password">
                            <el-input type="password" v-model="form.new_password"
                                      :prefix-icon="Lock" placeholder="新密码" maxlength="20"/>
                        </el-form-item>
                        <el-form-item label="重复新密码" prop="new_password_repeat">
                            <el-input type="password" v-model="form.new_password_repeat"
                                      :prefix-icon="Lock" placeholder="重复新密码" maxlength="20"/>
                        </el-form-item>
                        <div style="text-align: center">
                            <el-button :icon="Switch" @click="resetPassword"
                                       type="success" :disabled="!valid">立即重置密码
                            </el-button>
                        </div>
                    </el-form>
                </div>

            </div>
            <div class="info-card" style="margin-top: 10px">

            </div>
        </div>

        <div class="info-card" style="flex: 50%">

        </div>
    </div>
</template>

<style scoped>
.info-card {
    border-radius: 7px;
    padding: 15px 20px;
    background-color: var(--el-bg-color);
}

</style>
