<script setup>
import {reactive, watch} from "vue";
import {get} from "@/net";
import {useClipboard} from "@vueuse/core";
import {ElMessage} from "element-plus";

const props = defineProps({
    id: Number,
})

const details = reactive({
    base: {},
    runtime: {}
})
const {copy} = useClipboard()
const copyIp = () => copy(details.base.ip).then(() => ElMessage.success('成功复制IP地址'))

watch(() => props.id, value => {
    if (value !== -1) {
        details.base = {}
        get(`/api/monitor/details?clientId=${value}`, data => Object.assign(details.base, data))
    }
}, {immediate: true})
</script>

<template>
    <div class="client-details" v-loading="Object.keys(details.base).length === 0">
        <div v-if="Object.keys(details.base).length">
            <div class="title">
                <i class="fa-solid fa-server"></i>
                服务器信息
            </div>
            <el-divider style="margin: 10px 0"/>
            <div class="details-list">
                <div>
                    <span>服务器ID</span>
                    <span>{{ details.base.id }}</span>
                </div>
                <div>
                    <span>服务器名称</span>
                    <span>{{ details.base.name }}</span>
                </div>
                <div>
                    <span>运行状态</span>
                    <span>
                        <i style="color: #18cb18" class="fa-solid fa-circle-play" v-if="details.base.online"></i>
                        <i style="color: #18cb18" class="fa-solid fa-circle-stop" v-else></i>
                        {{ details.base.online ? '运行中' : '离线' }}
                    </span>
                </div>
                <div>
                    <span>服务器节点</span>
                    <span :class="`flag-icon flag-icon-${details.base.location}`"></span>&nbsp;
                    <span>未命名节点</span>
                </div>
                <div>
                    <span>公网IP地址</span>
                    <span>
                        {{ details.base.ip }}
                        <i class="fa-solid fa-copy interact-item" @click.stop="copyIp" style="color: dodgerblue"></i>
                    </span>
                </div>
                <div>
                    <span>处理器</span>
                    <span>{{ details.base.cpuName }}</span>
                </div>
                <div>
                    <span>硬件配置信息</span>
                    <span>
                        <i class="fa-solid fa-microchip"></i>&nbsp;
                        <span style="margin-right: 10px;">{{ `${details.base.cpuCore} CPU 核心数 &nbsp;/` }}</span>
                        <i class="fa-solid fa-memory"></i>&nbsp;
                        <span>{{ `${details.base.memory.toFixed(1)} GB 内存容量` }}</span>
                    </span>
                </div>
                <div>
                    <span>操作系统</span>
                    <span>{{ `${details.base.osName} ${details.base.osVersion}` }}</span>
                </div>
            </div>
            <div class="title" style="margin-top: 20px">
                <i class="fa-solid fa-gauge-high"></i>
                实时监控
            </div>
            <el-divider style="margin: 10px 0"/>
            <div style="display: flex">
                <el-progress type="dashboard" :width="100" :percentage="20" status="success">
                    <div style="font-size: 17px;font-weight: bold;color: initial">CPU</div>
                    <div style="font-size: 13px;color: grey;margin-top: 5px">20%</div>
                </el-progress>
                <el-progress style="margin-left: 20px"
                             type="dashboard" :width="100" :percentage="60" status="success">
                    <div style="font-size: 17px;font-weight: bold;color: initial">内存</div>
                    <div style="font-size: 13px;color: grey;margin-top: 5px">28.6 GB</div>
                </el-progress>
                <div style="display: flex;flex: 1;margin-left: 30px; flex-direction: column;height: 80px;">
                    <div style="flex: 1;font-size: 14px">
                        <div>实时网络速度</div>
                        <div>
                            <i style="color: orange" class="fa-solid fa-arrow-up"></i>
                            <span> {{ `20KB/s` }}</span>
                            <el-divider direction="vertical"/>
                            <i style="color: dodgerblue" class="fa-solid fa-arrow-down"></i>
                            <span> {{ `0KB/s` }}</span>
                        </div>
                    </div>
                    <div style="font-size: 13px;display: flex;justify-content: space-between">
                        <div>
                            <i class="fa-solid fa-hard-drive"></i>
                            <span>磁盘总容量</span>
                        </div>
                        <div>6.6 GB / 40.0 GB</div>
                    </div>
                    <el-progress type="line" status="success" :percentage="24" :show-text="false"/>
                </div>

            </div>

        </div>
    </div>
</template>

<style scoped>
.client-details {
    height: 100%;
    padding: 20px;

    .title {
        color: dodgerblue;
        font-size: 18px;
        font-weight: bold;
    }

    .details-list {
        font-size: 14px;

        & div {
            margin-bottom: 10px;

            & span:first-child {
                color: grey;
                font-size: 13px;
                font-weight: normal;
                width: 120px;
                display: inline-block;
            }

            & span {
                font-weight: bold;
            }
        }
    }
}

.interact-item {
    transition: .3s;

    &:hover {
        cursor: pointer;
        scale: 1.1;
        opacity: 0.8;
    }
}

</style>
