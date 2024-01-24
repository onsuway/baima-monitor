package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.Client;

/**
 * @ClassName ClientService
 * @Description 客户端相关Service
 * @Author su
 * @Date 2024/1/24 15:30
 */
public interface ClientService extends IService<Client> {
    boolean verifyAndRegister(String token);
    String registerToken();
    Client findClientById(int id);
    Client findClientByToken(String token);
}
