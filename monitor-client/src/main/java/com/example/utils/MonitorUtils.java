package com.example.utils;

import com.example.entity.BaseDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;
import oshi.software.os.OperatingSystem;

import java.io.File;
import java.net.NetworkInterface;
import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;

/**
 * @ClassName MonitorUtils
 * @Description 监控客户端硬件的工具类
 * @Author su
 * @Date 2024/1/25 14:06
 */
@Component
@Slf4j
public class MonitorUtils {

    private final SystemInfo info = new SystemInfo();
    private final Properties properties = System.getProperties();

    public BaseDetail monitorBaseDetails() {
        OperatingSystem os = info.getOperatingSystem();
        HardwareAbstractionLayer hardware = info.getHardware();
        double memory = hardware.getMemory().getTotal() / 1024.0 / 1024 / 1024;
        double diskSize = Arrays.stream(File.listRoots()).mapToLong(File::getTotalSpace).sum() / 1024.0 / 1024 / 1024;
        String ip = Objects.requireNonNull(findNetworkInterface(hardware)).getIPv4addr()[0];

        return new BaseDetail()
                .setOsArch(properties.getProperty("os.arch"))
                .setOsName(os.getFamily())
                .setOsVersion(os.getVersionInfo().getVersion())
                .setOsBit(os.getBitness())
                .setCpuName(hardware.getProcessor().getProcessorIdentifier().getName())
                .setCpuCore(hardware.getProcessor().getLogicalProcessorCount())
                .setMemory(memory)
                .setDisk(diskSize)
                .setIp(ip);
    }

    private NetworkIF findNetworkInterface(HardwareAbstractionLayer hardware) {
        try {
            for (NetworkIF network : hardware.getNetworkIFs()) {
                String[] ipv4Addr = network.getIPv4addr();
                NetworkInterface ni = network.queryNetworkInterface();
                if (!ni.isLoopback() && !ni.isPointToPoint() && !ni.isVirtual() && ni.isUp()
                    && (ni.getName().startsWith("eth") || ni.getName().startsWith("en"))
                    && ipv4Addr.length > 0) {
                    return network;
                }
            }
        } catch (Exception e) {
            log.error("在获取网络接口信息时出现问题", e);
        }
        return null;
    }
}
