package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.Client;
import com.example.entity.vo.request.ClientDetailVO;
import com.example.entity.vo.request.RenameClientVO;
import com.example.entity.vo.request.RenameNodeVO;
import com.example.entity.vo.request.RuntimeDetailVO;
import com.example.entity.vo.response.ClientDetailsVO;
import com.example.entity.vo.response.ClientPreviewVO;

import java.util.List;

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
    void updateClientDetail(ClientDetailVO vo, Client client);
    void updateRuntimeDetail(RuntimeDetailVO vo, Client client);
    List<ClientPreviewVO> listClients();
    void renameClient(RenameClientVO vo);
    ClientDetailsVO clientDetails(int clientId);
    void renameNode(RenameNodeVO vo);
}
