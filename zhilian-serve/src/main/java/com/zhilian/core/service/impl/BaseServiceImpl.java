package com.zhilian.core.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhilian.core.service.BaseService;

/**
 * 基础服务实现类
 * 
 * @author 智联票务技术团队
 * @date 2026-03-24
 */
public abstract class BaseServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements BaseService<T> {

    @Override
    public T getById(Long id) {
        return super.getById(id);
    }

    @Override
    public T saveEntity(T entity) {
        save(entity);
        return entity;
    }

    @Override
    public T updateEntity(T entity) {
        updateById(entity);
        return entity;
    }

    @Override
    public void deleteEntity(Long id) {
        removeById(id);
    }
}