package com.example.entity.vo.response;

import lombok.Data;

/**
 * @Author : susu
 * @Date : 2024/1/31 20:32
 * @Description : 发给前端的SSH设置的VO
 */
@Data
public class SshSettingsVO {
    String ip;
    int port = 22;
    String username;
    String password;
}
