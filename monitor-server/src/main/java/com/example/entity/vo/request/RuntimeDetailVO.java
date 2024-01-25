package com.example.entity.vo.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @ClassName RuntimeDetailVO
 * @Description 接受客户端运行时数据详情
 * @Author su
 * @Date 2024/1/25 16:04
 */
@Data
public class RuntimeDetailVO {
    @NotNull
    long timestamp;
    // cpu使用率
    @NotNull
    double cpuUsage;
    // 内存使用率
    @NotNull
    double memoryUsage;
    // 磁盘使用率
    @NotNull
    double diskUsage;
    // 网络上传速度
    @NotNull
    double netUpload;
    // 网络下载速度
    @NotNull
    double netDownload;
    // 磁盘读写速度
    @NotNull
    double diskRead;
    @NotNull
    double diskWrite;
}
