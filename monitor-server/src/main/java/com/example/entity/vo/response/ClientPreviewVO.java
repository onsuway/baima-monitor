package com.example.entity.vo.response;

import lombok.Data;

/**
 * @Author : susu
 * @Date : 2024/1/27 15:27
 * @Description : 发给前端展示的客户端信息
 */
@Data
public class ClientPreviewVO {
    int id;
    boolean online;
    String name;
    String location;
    String osName;
    String osVersion;
    String ip;
    String cpuName;
    int cpuCore;
    double memory;
    double cpuUsage;
    double memoryUsage;
    double networkUpload;
    double networkDownload;
}
