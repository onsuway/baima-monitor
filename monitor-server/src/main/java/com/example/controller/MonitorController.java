package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.vo.request.RenameClientVO;
import com.example.entity.vo.request.RenameNodeVO;
import com.example.entity.vo.request.RuntimeDetailVO;
import com.example.entity.vo.response.ClientDetailsVO;
import com.example.entity.vo.response.ClientPreviewVO;
import com.example.entity.vo.response.RuntimeHistoryVO;
import com.example.service.ClientService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author : susu
 * @Date : 2024/1/27 15:25
 * @Description : 发给前端的监控信息接口
 */
@RestController
@RequestMapping("/api/monitor")
public class MonitorController {

    @Resource
    ClientService service;

    @GetMapping("/list")
    public RestBean<List<ClientPreviewVO>> listAllClient() {
        return RestBean.success(service.listClients());
    }

    @PostMapping("/rename")
    public RestBean<Void> rename(@RequestBody @Valid RenameClientVO vo) {
        service.renameClient(vo);
        return RestBean.success();
    }

    @PostMapping("/node")
    public RestBean<Void> renameNode(@RequestBody @Valid RenameNodeVO vo) {
        service.renameNode(vo);
        return RestBean.success();
    }

    @GetMapping("/details")
    public RestBean<ClientDetailsVO> details(int clientId) {
        return RestBean.success(service.clientDetails(clientId));
    }

    @GetMapping("/runtime-history")
    public RestBean<RuntimeHistoryVO> runtimeDetailsHistory(int clientId) {
        return RestBean.success(service.clientRuntimeDetailsHistory(clientId));
    }

    @GetMapping("/runtime-now")
    public RestBean<RuntimeDetailVO> runtimeDetailsNow(int clientId) {
        return RestBean.success(service.clientRuntimeDetailsNow(clientId));
    }

    @GetMapping("/register")
    public RestBean<String> register() {
        return RestBean.success(service.registerToken());
    }

}
