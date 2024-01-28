package com.example.entity.vo.response;

import lombok.Data;

/**
 * @Author : susu
 * @Date : 2024/1/28 14:31
 * @Description : 服务端发给前端的用于展示客户端详情的VO
 */
@Data
public class ClientDetailsVO {
    int id;
    String name;
    boolean online;
    String node;
    String location;
    String ip;
    String cpuName;
    String osName;
    String osVersion;
    double memory;
    int cpuCore;
    double disk;
}
