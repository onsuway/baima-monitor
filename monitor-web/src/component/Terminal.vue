<script setup>
import {onBeforeUnmount, onMounted, ref} from "vue";
import axios from "axios";
import {ElMessage} from "element-plus";
import {Terminal} from "xterm";
import "xterm/css/xterm.css"
import {AttachAddon} from "xterm-addon-attach/src/AttachAddon";

const props = defineProps({
    id: Number,
})
const emits = defineEmits(['dispose'])
const terminalRef = ref()

const socketIp = axios.defaults.baseURL.replace("http://", "").replace("https://", "")
const socket = new WebSocket(`ws://${socketIp}/terminal/${props.id}`)

socket.onclose = evt => {
    if(evt.code !== 1000) {
        ElMessage.warning(`连接失败: ${evt.reason}`)
    } else {
        ElMessage.success('远程SSH连接已断开')
    }
    emits('dispose')
}
const attachAddon = new AttachAddon(socket);
const term = new Terminal({
    lineHeight: 1.2,
    rows: 23,
    fontSize: 13,
    fontFamily: "Monaco, Menlo, Consolas, 'Courier New', monospace",
    fontWeight: "bold",
    theme: {
        background: '#000000'
    },
    // 光标闪烁
    cursorBlink: true,
    cursorStyle: 'underline',
    scrollback: 100,
    tabStopWidth: 4,
});
term.loadAddon(attachAddon);

onMounted(() => {
    term.open(terminalRef.value)
    term.focus()
})

onBeforeUnmount(() => {
    socket.close()
    term.dispose()
})
</script>

<template>
<div ref="terminalRef" class="xterm"></div>
</template>

<style scoped>

</style>
