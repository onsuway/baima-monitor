package com.example.entity.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @ClassName ClientDetail
 * @Description 客户端硬件详细信息
 * @Author su
 * @Date 2024/1/25 14:35
 */
@Data
@TableName("db_client_detail")
public class ClientDetail {
    @TableId
    Integer id;
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
