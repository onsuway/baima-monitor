package com.example.entity.vo.request;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @Author : susu
 * @Date : 2024/1/28 13:03
 * @Description : 前端提交的用于更改客户端命名的VO
 */
@Data
public class RenameClientVO {
    int id;
    @Length(min = 1, max = 10)
    String name;
}
