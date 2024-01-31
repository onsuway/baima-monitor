package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.Client;
import com.example.entity.vo.request.*;
import com.example.entity.vo.response.*;

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

    List<ClientSimpleVO> listSimpleClients();

    void renameClient(RenameClientVO vo);

    ClientDetailsVO clientDetails(int clientId);

    void renameNode(RenameNodeVO vo);

    RuntimeHistoryVO clientRuntimeDetailsHistory(int clientId);

    RuntimeDetailVO clientRuntimeDetailsNow(int clientId);

    void deleteClient(int clientId);

    void saveClientSshConnection(SshConnectionVO vo);

    SshSettingsVO sshSettings(int clientId);

}
