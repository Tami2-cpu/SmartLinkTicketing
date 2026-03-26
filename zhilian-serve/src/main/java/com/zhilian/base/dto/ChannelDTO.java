package com.zhilian.base.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
/**
 * 渠道请求DTO
 * 
 * @author 智联票务技术团队
 * @date 2026-03-24
 */
public class ChannelDTO {
    
    @NotBlank(message = "渠道名称不能为空")
    @Size(max = 50, message = "渠道名称长度不能超过50个字符")
    private String name;
    
    @Size(max = 200, message = "渠道描述长度不能超过200个字符")
    private String description;

    // getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}