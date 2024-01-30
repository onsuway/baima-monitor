package com.example.entity.vo.response;

import lombok.Data;

/**
 * @Author : susu
 * @Date : 2024/1/30 20:51
 * @Description : 用于前端创建子用户页面的简单客户端展示VO
 */
@Data
public class ClientSimpleVO {
    int id;
    String name;
    String location;
    String osName;
    String osVersion;
    String ip;
}
