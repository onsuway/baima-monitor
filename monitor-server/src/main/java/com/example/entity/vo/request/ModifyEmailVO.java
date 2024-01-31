package com.example.entity.vo.request;

import jakarta.validation.constraints.Email;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @Author : susu
 * @Date : 2024/1/31 18:59
 * @Description : 前端发来的修改电子邮件VO
 */
@Data
public class ModifyEmailVO {
    @Email
    String email;
    @Length(min = 6, max = 6)
    String code;
}
