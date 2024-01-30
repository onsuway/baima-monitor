package com.example.entity.vo.request;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @Author : susu
 * @Date : 2024/1/30 15:24
 * @Description : 前端发来的用于重置密码VO
 */
@Data
public class ChangePasswordVO {
    @Length(min = 6, max = 20)
    String password;
    @Length(min = 6, max = 20)
    String new_password;
}
