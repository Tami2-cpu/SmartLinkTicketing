package com.zhilian.base.vo;

import java.util.Date;

/**
 * 地区返回VO
 * 
 * @author 智联票务技术团队
 * @date 2026-03-24
 */
public class RegionVO {
    private Long id;
    private String name;
    private Long parentId;
    private Integer level;
    private Date createTime;

    // getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}