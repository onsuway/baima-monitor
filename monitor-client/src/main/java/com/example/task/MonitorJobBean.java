package com.example.task;

import com.example.entity.RuntimeDetail;
import com.example.utils.MonitorUtils;
import com.example.utils.NetUtils;
import jakarta.annotation.Resource;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

/**
 * @ClassName MonitorJobBean
 * @Description 定时监控任务
 * @Author su
 * @Date 2024/1/25 16:11
 */
@Component
public class MonitorJobBean extends QuartzJobBean {

    @Resource
    MonitorUtils monitorUtils;

    @Resource
    NetUtils net;

    @Override
    protected void executeInternal(JobExecutionContext context) {
        RuntimeDetail runtimeDetail = monitorUtils.monitorRuntimeDetail();
        net.updateRuntimeDetails(runtimeDetail);
    }
}
