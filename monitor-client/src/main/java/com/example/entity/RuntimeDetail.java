package com.example.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ClassName RuntimeDetail
 * @Description 运行时系统数据详情
 * @Author su
 * @Date 2024/1/25 15:17
 */
@Data
@Accessors(chain = true)
public class RuntimeDetail {
    long timestamp;
    // cpu使用率
    double cpuUsage;
    // 内存使用率
    double memoryUsage;
    // 磁盘使用率
    double diskUsage;
    // 网络上传速度
    double netUpload;
    // 网络下载速度
    double netDownload;
    // 磁盘读写速度
    double diskRead;
    double diskWrite;
}
