package com.zhilian.monitor.service;

import java.util.Map;

public interface MonitorService {
    /**
     * 获取系统健康状态
     * @return 健康状态信息
     */
    Map<String, Object> getHealthStatus();

    /**
     * 获取服务信息
     * @return 服务信息
     */
    Map<String, Object> getServiceInfo();

    /**
     * 发送邮件通知
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     * @return 是否发送成功
     */
    boolean sendEmailNotification(String to, String subject, String content);
}