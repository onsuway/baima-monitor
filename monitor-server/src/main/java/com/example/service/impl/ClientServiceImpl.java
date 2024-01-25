package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.Client;
import com.example.entity.dto.ClientDetail;
import com.example.entity.vo.request.ClientDetailVO;
import com.example.entity.vo.request.RuntimeDetailVO;
import com.example.mapper.ClientDetailMapper;
import com.example.mapper.ClientMapper;
import com.example.service.ClientService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName ClientServiceImpl
 * @Description 客户端相关Service实现类
 * @Author su
 * @Date 2024/1/24 15:30
 */
@Service
public class ClientServiceImpl extends ServiceImpl<ClientMapper, Client> implements ClientService {

    @Resource
    ClientDetailMapper detailMapper;

    // 注册token
    private String registerToken = this.generateNewToken();

    // 客户端缓存（根据ID/TOKEN查询） 用ConcurrentHashMap保证并发安全
    private final Map<Integer, Client> clientIdCache = new ConcurrentHashMap<>();
    private final Map<String, Client> clientTokenCache = new ConcurrentHashMap<>();

    @PostConstruct
    public void initCache() {
        // 初始化客户端缓存
        this.list().forEach(this::addClientCache);
    }

    /**
     * @param token 客户端发来的
     * @return boolean 是否验证并注册成功
     * @description 验证并注册客户端
     */
    @Override
    public boolean verifyAndRegister(String token) {
        if (this.registerToken.equals(token)) {
            int id = this.randomClientId();
            Client client = new Client(id, "未命名主机", token, "cn", "未命名节点", new Date());
            if (this.save(client)) {
                registerToken = this.generateNewToken();
                this.addClientCache(client);
                return true;
            }
        }
        return false;
    }

    private void addClientCache(Client client) {
        clientIdCache.put(client.getId(), client);
        clientTokenCache.put(client.getToken(), client);
    }

    // 获取当前的token
    @Override
    public String registerToken() {
        return registerToken;
    }

    @Override
    public Client findClientById(int id) {
        return clientIdCache.get(id);
    }

    @Override
    public Client findClientByToken(String token) {
        return clientTokenCache.get(token);
    }

    @Override
    public void updateClientDetail(ClientDetailVO vo, Client client) {
        ClientDetail detail = new ClientDetail();
        BeanUtils.copyProperties(vo, detail);
        detail.setId(client.getId());
        // saveOrUpdate
        if (Objects.nonNull(detailMapper.selectById(client.getId())))
            detailMapper.updateById(detail);
        else
            detailMapper.insert(detail);
    }

    private Map<Integer, RuntimeDetailVO> currentRuntime = new ConcurrentHashMap<>();

    @Override
    public void updateRuntimeDetail(RuntimeDetailVO vo, Client client) {
        currentRuntime.put(client.getId(), vo);
    }

    // 生成新的token
    private String generateNewToken() {
        String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 24; i++)
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        System.out.println(sb);
        return sb.toString();
    }

    // 生成随机的客户端id
    private int randomClientId() {
        return new Random().nextInt(90000000) + 10000000;
    }
}
