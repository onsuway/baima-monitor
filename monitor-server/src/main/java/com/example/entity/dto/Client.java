package com.example.entity.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName Client
 * @Description 客户端实体类
 * @Author su
 * @Date 2024/1/24 15:32
 */
@Data
@TableName("db_client")
@AllArgsConstructor
public class Client {
    @TableId
    Integer id;
    String name;
    String token;
    Date registerTime;
}
