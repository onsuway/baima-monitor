package com.example.entity.vo.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @ClassName ClientDetailVO
 * @Description 客户端发来的硬件信息
 * @Author su
 * @Date 2024/1/25 14:37
 */
@Data
public class ClientDetailVO {
    // 操作系统架构
    @NotNull
    String osArch;
    // 操作系统名称
    @NotNull
    String osName;
    // 操作系统版本
    @NotNull
    String osVersion;
    // 操作系统位数
    @NotNull
    int osBit;
    // CPU名称
    @NotNull
    String cpuName;
    // CPU核心数
    @NotNull
    int cpuCore;
    // 内存大小
    @NotNull
    double memory;
    // 硬盘大小
    @NotNull
    double disk;
    // IP地址
    @NotNull
    String ip;
}
