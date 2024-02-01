package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.dto.Account;
import com.example.entity.vo.request.RenameClientVO;
import com.example.entity.vo.request.RenameNodeVO;
import com.example.entity.vo.request.RuntimeDetailVO;
import com.example.entity.vo.request.SshConnectionVO;
import com.example.entity.vo.response.*;
import com.example.service.AccountService;
import com.example.service.ClientService;
import com.example.utils.Const;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.Supplier;

/**
 * @Author : susu
 * @Date : 2024/1/27 15:25
 * @Description : 发给前端的监控信息接口
 */
@RestController
@RequestMapping("/api/monitor")
@Tag(name = "客户端监控数据管理",description = "客户端相关信息、运行时数据的增删改查")
public class MonitorController {

    @Resource
    ClientService service;

    @Resource
    AccountService accountService;

    @GetMapping("/list")
    public RestBean<List<ClientPreviewVO>> listAllClient(@RequestAttribute(Const.ATTR_USER_ID) int userId,
                                                         @RequestAttribute(Const.ATTR_USER_ROLE) String userRole) {
        List<ClientPreviewVO> clients = service.listClients();
        if (this.isAdminAccount(userRole))
            return RestBean.success(clients);
        else {
            List<Integer> ids = this.accountAccessClients(userId);
            return RestBean.success(clients.stream()
                    .filter(vo -> ids.contains(vo.getId()))
                    .toList()
            );
        }
    }

    @PostMapping("/rename")
    public RestBean<Void> rename(@RequestBody @Valid RenameClientVO vo,
                                 @RequestAttribute(Const.ATTR_USER_ID) int userId,
                                 @RequestAttribute(Const.ATTR_USER_ROLE) String userRole) {
        return handlePermissionCheck(userId, userRole, vo.getId(), () -> {
            service.renameClient(vo);
            return null;
        });
    }

    @PostMapping("/node")
    public RestBean<Void> renameNode(@RequestBody @Valid RenameNodeVO vo,
                                     @RequestAttribute(Const.ATTR_USER_ID) int userId,
                                     @RequestAttribute(Const.ATTR_USER_ROLE) String userRole) {
        return handlePermissionCheck(userId, userRole, vo.getId(), () -> {
            service.renameNode(vo);
            return null;
        });
    }

    @GetMapping("/details")
    public RestBean<ClientDetailsVO> details(int clientId,
                                             @RequestAttribute(Const.ATTR_USER_ID) int userId,
                                             @RequestAttribute(Const.ATTR_USER_ROLE) String userRole) {
        return handlePermissionCheck(userId, userRole, clientId, () -> service.clientDetails(clientId));
    }

    @GetMapping("/runtime-history")
    public RestBean<RuntimeHistoryVO> runtimeDetailsHistory(int clientId,
                                                            @RequestAttribute(Const.ATTR_USER_ID) int userId,
                                                            @RequestAttribute(Const.ATTR_USER_ROLE) String userRole) {
        return handlePermissionCheck(userId, userRole, clientId, () -> service.clientRuntimeDetailsHistory(clientId));
    }

    @GetMapping("/runtime-now")
    public RestBean<RuntimeDetailVO> runtimeDetailsNow(int clientId,
                                                       @RequestAttribute(Const.ATTR_USER_ID) int userId,
                                                       @RequestAttribute(Const.ATTR_USER_ROLE) String userRole) {
        return handlePermissionCheck(userId, userRole, clientId, () -> service.clientRuntimeDetailsNow(clientId));
    }

    @GetMapping("/register")
    public RestBean<String> registerToken(@RequestAttribute(Const.ATTR_USER_ROLE) String userRole) {
        return handleAdminCheck(userRole, () -> service.registerToken());
    }

    @GetMapping("/delete")
    public RestBean<Void> deleteClient(int clientId, @RequestAttribute(Const.ATTR_USER_ROLE) String userRole) {
        return handleAdminCheck(userRole, () -> {
            service.deleteClient(clientId);
            return null;
        });
    }

    @GetMapping("/simple-list")
    public RestBean<List<ClientSimpleVO>> simpleClientList(@RequestAttribute(Const.ATTR_USER_ROLE) String userRole) {
        return handleAdminCheck(userRole, () -> service.listSimpleClients());
    }

    @PostMapping("/ssh-save")
    public RestBean<Void> saveSshConnection(@RequestBody @Valid SshConnectionVO vo,
                                            @RequestAttribute(Const.ATTR_USER_ID) int userId,
                                            @RequestAttribute(Const.ATTR_USER_ROLE) String userRole) {
        return this.handlePermissionCheck(userId, userRole, vo.getId(), () -> {
            service.saveClientSshConnection(vo);
            return null;
        });
    }

    @GetMapping("/ssh")
    public RestBean<SshSettingsVO> sshSettings(int clientId,
                                               @RequestAttribute(Const.ATTR_USER_ID) int userId,
                                               @RequestAttribute(Const.ATTR_USER_ROLE) String userRole) {
        return this.handlePermissionCheck(userId, userRole, clientId, () -> service.sshSettings(clientId));
    }

    // 获得某个用户的可管理的客户端id列表
    private List<Integer> accountAccessClients(int uid) {
        Account account = accountService.getById(uid);
        return account.getClientList();
    }

    // 检查是否是admin
    private boolean isAdminAccount(String role) {
        role = role.substring(5); // spring security的角色前面自带“ROLE_”
        return Const.ROLE_ADMIN.equals(role);
    }

    // 检查是否有权限对“clientId”的客户端修改（是管理员 或者 拥有这个客户端权限的子用户）
    private boolean permissionCheck(int uid, String role, int clientId) {
        if (this.isAdminAccount(role)) return true;
        return this.accountAccessClients(uid).contains(clientId);
    }

    /**
     * @param userId   用户ID
     * @param userRole 用户角色
     * @param clientId 客户端ID
     * @param action   待执行操作
     * @return RestBean.success/noPermission()
     * @description 对用户操作的客户端进行鉴权，成功的话执行action并返回success，否则返回noPermission
     */
    private <T> RestBean<T> handlePermissionCheck(int userId, String userRole, int clientId,
                                                  Supplier<T> action) {
        if (this.permissionCheck(userId, userRole, clientId)) {
            T result = action.get();
            return RestBean.success(result);
        } else {
            return RestBean.noPermission();
        }
    }

    /**
     * @param userRole 用户角色
     * @param action   待执行操作
     * @return RestBean.success/noPermission()
     * @description 鉴定用户是否为管理员，是的话执行action并返回success，否则返回noPermission
     */
    private <T> RestBean<T> handleAdminCheck(String userRole, Supplier<T> action) {
        if (this.isAdminAccount(userRole)) {
            T result = action.get();
            return RestBean.success(result);
        } else {
            return RestBean.noPermission();
        }
    }

}
