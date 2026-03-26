package com.zhilian.core.service;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 基础服务接口
 * 
 * @author 智联票务技术团队
 * @date 2026-03-24
 */
public interface BaseService<T> extends IService<T> {
    /**
     * 根据ID获取实体
     * 
     * @param id 实体ID
     * @return 实体对象
     */
    T getById(Long id);
    
    /**
     * 保存实体
     * 
     * @param entity 实体对象
     * @return 保存后的实体
     */
    T saveEntity(T entity);
    
    /**
     * 更新实体
     * 
     * @param entity 实体对象
     * @return 更新后的实体
     */
    T updateEntity(T entity);
    
    /**
     * 删除实体
     * 
     * @param id 实体ID
     */
    void deleteEntity(Long id);
}