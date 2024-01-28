package com.example.entity.vo.request;

import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @Author : susu
 * @Date : 2024/1/28 16:10
 * @Description : 前端发来的用于重命名节点名称的VO
 */
@Data
public class RenameNodeVO {
    int id;
    @Length(min = 1, max = 10)
    String node;
    @Pattern(regexp = "(cn|hk|jp|us|sg|kr|de)")
    String location;
}
