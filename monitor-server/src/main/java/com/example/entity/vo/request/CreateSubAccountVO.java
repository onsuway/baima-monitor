package com.example.entity.vo.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

/**
 * @Author : susu
 * @Date : 2024/1/30 15:49
 * @Description : 创建子账户VO
 */
@Data
public class CreateSubAccountVO {
    @Length(min = 1, max = 10)
    String username;
    @Email
    String email;
    @Length(min = 6, max = 20)
    String password;
    @Size(min = 1)
    List<Integer> clients;
}
