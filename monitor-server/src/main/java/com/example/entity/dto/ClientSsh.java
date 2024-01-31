package com.example.entity.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.entity.BaseData;
import lombok.Data;

/**
 * @Author : susu
 * @Date : 2024/1/31 20:26
 * @Description : 客户端SSH
 */
@Data
@TableName("db_client_ssh")
public class ClientSsh implements BaseData {
    @TableId
    Integer id;
    Integer port;
    String username;
    String password;
}
