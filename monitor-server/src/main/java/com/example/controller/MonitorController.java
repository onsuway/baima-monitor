package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.vo.request.RenameClientVO;
import com.example.entity.vo.response.ClientPreviewVO;
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
}
