package com.example.config;

import com.example.task.MonitorJobBean;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName QuartzConfiguration
 * @Description quartz定时任务配置类
 * @Author su
 * @Date 2024/1/25 16:10
 */
@Configuration
@Slf4j
public class QuartzConfiguration {

    @Bean
    public JobDetail jobDetailFactoryBean() {
        return JobBuilder.newJob(MonitorJobBean.class)
                .withIdentity("monitor-task")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger cronTriggerFactoryBean(JobDetail detail) {
        CronScheduleBuilder cron = CronScheduleBuilder.cronSchedule("*/10 * * * * ?");
        return TriggerBuilder.newTrigger()
                .forJob(detail)
                .withIdentity("monitor-trigger")
                .withSchedule(cron)
                .build();
    }
}
