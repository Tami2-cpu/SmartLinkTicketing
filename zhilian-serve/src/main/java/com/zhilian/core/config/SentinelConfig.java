package com.zhilian.core.config;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

// @Configuration
public class SentinelConfig {

    @PostConstruct
    public void initFlowRules() {
        List<FlowRule> rules = new ArrayList<>();

        // 配置API流量控制规则
        FlowRule apiRule = new FlowRule();
        apiRule.setResource("/api/**");
        apiRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        apiRule.setCount(100); // 每秒100个请求
        rules.add(apiRule);

        // 配置用户登录接口流量控制
        FlowRule loginRule = new FlowRule();
        loginRule.setResource("/api/user/login");
        loginRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        loginRule.setCount(50); // 每秒50个请求
        rules.add(loginRule);

        // 配置订单创建接口流量控制
        FlowRule orderCreateRule = new FlowRule();
        orderCreateRule.setResource("/api/order/create");
        orderCreateRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        orderCreateRule.setCount(30); // 每秒30个请求
        rules.add(orderCreateRule);

        // FlowRuleManager.loadRules(rules);
    }
}