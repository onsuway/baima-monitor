package com.example.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.Client;
import com.example.entity.dto.ClientDetail;
import com.example.entity.dto.ClientSsh;
import com.example.entity.vo.request.*;
import com.example.entity.vo.response.*;
import com.example.mapper.ClientDetailMapper;
import com.example.mapper.ClientMapper;
import com.example.mapper.ClientSshMapper;
import com.example.service.ClientService;
import com.example.utils.InfluxDbUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.*;
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

    @Resource
    InfluxDbUtils influx;

    @Resource
    ClientSshMapper sshMapper;

    // 注册token
    private String registerToken = this.generateNewToken();

    // 客户端缓存（根据ID/TOKEN查询） 用ConcurrentHashMap保证并发安全
    private final Map<Integer, Client> clientIdCache = new ConcurrentHashMap<>();
    private final Map<String, Client> clientTokenCache = new ConcurrentHashMap<>();

    @PostConstruct
    public void initCache() {
        clientIdCache.clear();
        clientTokenCache.clear();
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

    // 获取注册用的token
    @Override
    public String registerToken() {
        return registerToken;
    }

    // 根据ID查找客户端
    @Override
    public Client findClientById(int id) {
        return clientIdCache.get(id);
    }

    // 根据token查找客户端
    @Override
    public Client findClientByToken(String token) {
        return clientTokenCache.get(token);
    }

    /**
     * @description 更新客户端硬件信息
     * @param vo 客户端发来的自身硬件信息
     * @param client 客户端实体类
     */
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

    // 客户端运行时数据Map<客户端ID，运行时数据返回VO>
    private final Map<Integer, RuntimeDetailVO> currentRuntime = new ConcurrentHashMap<>();

    /**
     * @description 更新（添加新的）客户端运行时数据
     * @param vo 客户端发来的运行时数据VO
     * @param client 客户端实体类
     */
    @Override
    public void updateRuntimeDetail(RuntimeDetailVO vo, Client client) {
        currentRuntime.put(client.getId(), vo);
        influx.writeRuntimeData(client.getId(), vo);
    }

    /**
     * @description 拿取所有的客户端展示信息发给前端
     * @return 所有的客户端展示VO列表
     */
    @Override
    public List<ClientPreviewVO> listClients() {
        return clientIdCache.values().stream().map(client -> {
            ClientPreviewVO vo = client.asViewObject(ClientPreviewVO.class);
            BeanUtils.copyProperties(detailMapper.selectById(vo.getId()), vo);
            RuntimeDetailVO runtime = currentRuntime.get(client.getId());
            if(this.isOnline(runtime)) {
                BeanUtils.copyProperties(runtime, vo);
                vo.setOnline(true);
            }
            return vo;
        }).toList();
    }

    /**
     * @description 拿取所有的客户端简单展示信息发给前端
     * @return 客户端简单展示VO列表
     */
    @Override
    public List<ClientSimpleVO> listSimpleClients() {
        return clientIdCache.values().stream().map(client -> {
            ClientSimpleVO vo = client.asViewObject(ClientSimpleVO.class);
            BeanUtils.copyProperties(detailMapper.selectById(vo.getId()), vo);
            return vo;
        }).toList();
    }

    /**
     * @description 客户端重命名
     * @param vo 前端发来的客户端重命名VO
     */
    @Override
    public void renameClient(RenameClientVO vo) {
        this.update(
                Wrappers.<Client>update()
                        .eq("id", vo.getId())
                        .set("name", vo.getName())
        );
        this.initCache();
    }

    /**
     * @description 获取客户端详情信息
     * @param clientId 客户端ID
     * @return 客户端详情信息VO
     */
    @Override
    public ClientDetailsVO clientDetails(int clientId) {
        ClientDetailsVO vo = this.clientIdCache.get(clientId).asViewObject(ClientDetailsVO.class);
        BeanUtils.copyProperties(detailMapper.selectById(clientId), vo);
        vo.setOnline(this.isOnline(currentRuntime.get(clientId)));
        return vo;
    }

    /**
     * @description 重命名节点
     * @param vo 重命名节点VO
     */
    @Override
    public void renameNode(RenameNodeVO vo) {
        this.update(
                Wrappers.<Client>update()
                        .eq("id", vo.getId())
                        .set("node", vo.getNode())
                        .set("location", vo.getLocation())
        );
        this.initCache();
    }

    /**
     * @description 获取客户端历史的运行时数据（获取一小时内的历史数据）
     * @param clientId 客户端ID
     * @return 客户端历史的运行时数据VO
     */
    @Override
    public RuntimeHistoryVO clientRuntimeDetailsHistory(int clientId) {
        RuntimeHistoryVO vo = influx.readRuntimeData(clientId);
        ClientDetail detail = detailMapper.selectById(clientId);
        BeanUtils.copyProperties(detail, vo);
        return vo;
    }

    /**
     * @description 获取最新的客户端运行时信息
     * @param clientId 客户端ID
     * @return 客户端运行时信息VO
     */
    @Override
    public RuntimeDetailVO clientRuntimeDetailsNow(int clientId) {
        return currentRuntime.get(clientId);
    }

    // 删除客户端、清除缓存、map
    @Override
    public void deleteClient(int clientId) {
        this.removeById(clientId);
        detailMapper.deleteById(clientId);
        this.initCache();
        currentRuntime.remove(clientId);
    }

    /**
     * @description 保存SSH连接信息
     * @param vo SSH连接信息VO
     */
    @Override
    public void saveClientSshConnection(SshConnectionVO vo) {
        Client client = clientIdCache.get(vo.getId());
        if(client == null) return;
        ClientSsh ssh = new ClientSsh();
        BeanUtils.copyProperties(vo, ssh);
        if(Objects.nonNull(sshMapper.selectById(client.getId()))) {
            sshMapper.updateById(ssh);
        } else {
            sshMapper.insert(ssh);
        }
    }

    /**
     * @description 连接SSH
     * @param clientId 客户端ID
     * @return SSH设置VO
     */
    @Override
    public SshSettingsVO sshSettings(int clientId) {
        ClientDetail detail = detailMapper.selectById(clientId);
        ClientSsh ssh = sshMapper.selectById(clientId);
        SshSettingsVO vo;
        if(ssh == null) {
            vo = new SshSettingsVO();
        } else {
            vo = ssh.asViewObject(SshSettingsVO.class);
        }
        vo.setIp(detail.getIp());
        return vo;
    }

    // 判断客户端是否在线（1分钟之内不发信息就算断线）
    private boolean isOnline(RuntimeDetailVO runtime) {
        return runtime != null && System.currentTimeMillis() - runtime.getTimestamp() < 60 * 1000;
    }

    // 生成新的token
    private String generateNewToken() {
        String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 24; i++)
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        return sb.toString();
    }

    // 生成随机的客户端id
    private int randomClientId() {
        return new Random().nextInt(90000000) + 10000000;
    }
}
