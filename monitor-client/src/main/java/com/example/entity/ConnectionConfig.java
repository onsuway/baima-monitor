package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @ClassName ConnectConfig
 * @Description 连接配置
 * @Author su
 * @Date 2024/1/24 19:29
 */
@Data
@AllArgsConstructor
public class ConnectionConfig {
    // 服务端地址
    String address;
    String token;
}
