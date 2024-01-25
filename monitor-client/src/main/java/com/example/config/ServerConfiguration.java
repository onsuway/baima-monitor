package com.example.config;

import com.alibaba.fastjson2.JSONObject;
import com.example.entity.ConnectConfig;
import com.example.utils.MonitorUtils;
import com.example.utils.NetUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @ClassName ServerConfiguration
 * @Description to服务端配置
 * @Author su
 * @Date 2024/1/24 19:28
 */
@Configuration
@Slf4j
public class ServerConfiguration {

    @Resource
    NetUtils netUtils;

    @Resource
    MonitorUtils monitorUtils;

    @Bean
    public ConnectConfig connectConfig() {
        log.info("正在加载服务端连接配置...");
        ConnectConfig config = this.readConfigFromFile();
        if (config == null)
            config = this.registerToServer();
        System.out.println(monitorUtils.monitorBaseDetails());
        return config;
    }

    /**
     * @return 配置实体类
     * @description 从本地JSON文件中读取配置
     */
    private ConnectConfig readConfigFromFile() {
        File configFile = new File("config/server.json");
        if (configFile.exists()) {
            try (FileInputStream stream = new FileInputStream(configFile)) {
                String raw = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
                return JSONObject.parseObject(raw).to(ConnectConfig.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    private ConnectConfig registerToServer() {
        Scanner scanner = new Scanner(System.in);
        String token, address;
        do {
            log.info("请输入需要注册的服务端访问地址: (例如: http://192.168.0.22:8080)");
            address = scanner.nextLine();
            log.info("请输入服务端生成的用于注册客户端的Token: ");
            token = scanner.nextLine();
        } while (!netUtils.registerToServer(address, token));
        ConnectConfig config = new ConnectConfig(address, token);
        this.saveConfigToFile(config);
        return config;
    }

    private void saveConfigToFile(ConnectConfig config) {
        File dir = new File("config");
        if (!dir.exists() && dir.mkdir())
            log.info("创建用于保存服务端连接配置的目录已成功");
        File file = new File("config/server.json");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(JSONObject.from(config).toJSONString());
        } catch (IOException e) {
            log.error("保存服务端连接配置文件时出现问题", e);
        }
        log.info("服务端连接配置保存至本地已成功");

    }
}
