package com.example.entity.vo.request;

import lombok.Data;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;

/**
 * @Author : susu
 * @Date : 2024/1/31 20:29
 * @Description : 前端发来的保存SSH连接信息的VO
 */
@Data
public class SshConnectionVO {
    int id;
    int port;
    @NonNull
    @Length(min = 1)
    String username;
    @NonNull
    @Length(min = 1)
    String password;
}
