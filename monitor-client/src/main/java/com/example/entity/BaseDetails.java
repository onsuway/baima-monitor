package com.example.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ClassName BaseDetails
 * @Description TODO
 * @Author su
 * @Date 2024/1/25 14:07
 */
@Data
@Accessors(chain = true)
public class BaseDetails {
    // 操作系统架构
    String osArch;
    // 操作系统名称
    String osName;
    // 操作系统版本
    String osVersion;
    // 操作系统位数
    int osBit;
    // CPU名称
    String cpuName;
    // CPU核心数
    int cpuCore;
    // 内存大小
    double memory;
    // 硬盘大小
    double disk;
    // IP地址
    String ip;
}
