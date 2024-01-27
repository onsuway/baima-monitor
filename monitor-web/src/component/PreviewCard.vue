<script setup>
import {fitByUnit} from "@/tools";

const props = defineProps({
    data: Object,
})
</script>

<template>
    <div class="instance-card">
        <div style="display: flex; justify-content: space-between">
            <div>
                <div class="name">
                    <span :class="`flag-icon flag-icon-${data.location}`"></span>
                    <span style="margin: 0 10px">{{ data.name }}</span>
                    <i class="fa-solid fa-pen-to-square"></i>
                </div>
                <div class="os">
                    操作系统：{{ data.osName }} {{ data.osVersion }}
                </div>
            </div>
            <div class="status" v-if="data.online">
                <i style="color: #18cb18" class="fa-solid fa-circle-play"></i>
                <span style="margin-left: 5px">运行中</span>
            </div>
            <div class="status" v-else>
                <i style="color: #8a8a8a" class="fa-solid fa-circle-stop"></i>
                <span style="margin-left: 5px">离线</span>
            </div>
        </div>
        <el-divider style="margin: 10px 0"/>
        <div class="network">
            <span style="margin-right: 5px">公网IP：{{ data.ip }}</span>
            <i class="fa-solid fa-copy" style="color: dodgerblue"></i>
        </div>
        <div class="cpu">
            <span style="margin-right: 10px">处理器：{{ data.cpuName }}</span>
        </div>
        <div class="hardware">
            <i class="fa-solid fa-microchip"></i>
            <span style="margin-right: 10px"> {{ data.cpuCore }} CPU</span>
            <i class="fa-solid fa-memory"></i>
            <span> {{ data.memory.toFixed(1) }} GB</span>
        </div>
        <div class="progress">
            <span>{{ `CPU：${(data.cpuUsage * 100).toFixed(1)}%` }}</span>
            <el-progress status="success"
                         :percentage="data.cpuUsage * 100"
                         :stroke-width="5" :show-text="false"
            />
        </div>
        <div class="progress">
            <span>内存：<b>{{ data.memoryUsage.toFixed(1) }}</b>GB</span>
            <el-progress
                status="success"
                :percentage="data.memoryUsage / data.memory * 100"
                :stroke-width="5"
                :show-text="false"/>
        </div>
        <div class="network-flow">
            <div>网络流量</div>
            <div>
                <i class="fa-solid fa-arrow-up"></i>
                <span> {{`${fitByUnit(data.networkUpload, 'KB')}/s`}}</span>
                <el-divider direction="vertical"/>
                <i class="fa-solid fa-arrow-down"></i>
                <span> {{`${fitByUnit(data.networkDownload, 'KB')}/s`}}</span>
            </div>
        </div>
    </div>
</template>

<style scoped>
:deep(.el-progress-bar__outer) {
    background-color: #18cb1822;
}

:deep(.el-progress-bar__inner) {
    background-color: #18cb18;
}

.dark .instance-card {
    color: #d9d9d9;
}

.instance-card {
    padding: 15px;
    width: 320px;
    background-color: var(--el-bg-color);
    border-radius: 5px;
    box-sizing: border-box;
    color: #606060;

    .name {
        font-size: 15px;
        font-weight: bold;
    }

    .status {
        font-size: 14px;
    }

    .os {
        font-size: 13px;
        color: grey;
    }

    .network {
        font-size: 13px;
    }

    .cpu {
        font-size: 13px;
    }

    .hardware {
        margin-top: 5px;
        font-size: 13px;
    }

    .progress {
        margin-top: 10px;
        font-size: 12px;
    }

    .network-flow {
        margin-top: 10px;
        font-size: 12px;
        display: flex;
        justify-content: space-between;
    }
}
</style>
