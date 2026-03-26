package com.zhilian.user.vo;

import javax.validation.constraints.NotBlank;

/**
 * 用户登录请求VO
 * 
 * @author 智联票务技术团队
 * @date 2026-03-24
 */
public class UserLoginVO {
    
    @NotBlank(message = "用户名不能为空")
    private String username;
    
    @NotBlank(message = "密码不能为空")
    private String password;

    // getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}