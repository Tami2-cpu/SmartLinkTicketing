package com.zhilian.monitor.controller;

import com.zhilian.core.exception.Result;
import com.zhilian.monitor.service.MonitorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/api/monitor")
@Api(tags = "监控模块")
public class MonitorController {

    @Resource
    private MonitorService monitorService;

    @GetMapping("/health")
    @ApiOperation("健康检查")
    public Result health() {
        Map<String, Object> healthStatus = monitorService.getHealthStatus();
        return Result.success(healthStatus);
    }

    @GetMapping("/info")
    @ApiOperation("获取服务信息")
    public Result info() {
        Map<String, Object> serviceInfo = monitorService.getServiceInfo();
        return Result.success(serviceInfo);
    }

    @PostMapping("/notify/email")
    @ApiOperation("发送邮件通知")
    public Result sendEmail(@RequestBody Map<String, String> emailInfo) {
        String to = emailInfo.get("to");
        String subject = emailInfo.get("subject");
        String content = emailInfo.get("content");

        boolean success = monitorService.sendEmailNotification(to, subject, content);
        if (success) {
            return Result.success("邮件发送成功");
        } else {
            return Result.error("邮件发送失败");
        }
    }
}