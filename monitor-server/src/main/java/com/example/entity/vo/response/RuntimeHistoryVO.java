package com.example.entity.vo.response;

import com.alibaba.fastjson2.JSONObject;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * @Author : susu
 * @Date : 2024/1/28 16:43
 * @Description : 用于前端展示运行时历史数据的VO
 */
@Data
public class RuntimeHistoryVO {
    double disk;
    double memory;
    List<JSONObject> list = new LinkedList<>();
}
