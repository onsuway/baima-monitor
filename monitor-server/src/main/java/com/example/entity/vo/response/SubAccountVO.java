package com.example.entity.vo.response;

import com.alibaba.fastjson2.JSONArray;
import lombok.Data;

/**
 * @Author : susu
 * @Date : 2024/1/30 15:52
 * @Description : 前端展示子账户VO
 */
@Data
public class SubAccountVO {
    int id;
    String username;
    String email;
    JSONArray clientList;
}
