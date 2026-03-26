package com.zhilian.core.utils;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 缓存工具类
 * 封装Redis缓存操作
 * 
 * @author 智联票务技术团队
 * @date 2026-03-26
 */
@Component
public class CacheUtil {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 获取缓存
     * 
     * @param key 缓存键
     * @return 缓存值
     */
    public Object get(String key) {
        try {
            return redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取缓存（泛型）
     * 
     * @param key 缓存键
     * @param <T> 泛型类型
     * @return 缓存值
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> clazz) {
        try {
            Object value = redisTemplate.opsForValue().get(key);
            return value != null ? (T) value : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 设置缓存
     * 
     * @param key 缓存键
     * @param value 缓存值
     */
    public void set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置缓存（带过期时间）
     * 
     * @param key 缓存键
     * @param value 缓存值
     * @param expiration 过期时间
     * @param timeUnit 时间单位
     */
    public void set(String key, Object value, long expiration, TimeUnit timeUnit) {
        try {
            redisTemplate.opsForValue().set(key, value, expiration, timeUnit);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除缓存
     * 
     * @param key 缓存键
     */
    public void delete(String key) {
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 批量删除缓存
     * 
     * @param keys 缓存键数组
     */
    public void delete(String... keys) {
        try {
            redisTemplate.delete(java.util.Arrays.asList(keys));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 递增
     * 
     * @param key 缓存键
     * @return 递增后的值
     */
    public Long increment(String key) {
        try {
            return redisTemplate.opsForValue().increment(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * 递增（带过期时间）
     * 
     * @param key 缓存键
     * @param expiration 过期时间
     * @param timeUnit 时间单位
     * @return 递增后的值
     */
    public Long increment(String key, long expiration, TimeUnit timeUnit) {
        try {
            Long value = redisTemplate.opsForValue().increment(key);
            redisTemplate.expire(key, expiration, timeUnit);
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * 检查缓存是否存在
     * 
     * @param key 缓存键
     * @return 是否存在
     */
    public boolean exists(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 设置过期时间
     * 
     * @param key 缓存键
     * @param expiration 过期时间
     * @param timeUnit 时间单位
     * @return 是否设置成功
     */
    public boolean expire(String key, long expiration, TimeUnit timeUnit) {
        try {
            return redisTemplate.expire(key, expiration, timeUnit);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取过期时间
     * 
     * @param key 缓存键
     * @param timeUnit 时间单位
     * @return 过期时间
     */
    public Long getExpire(String key, TimeUnit timeUnit) {
        try {
            return redisTemplate.getExpire(key, timeUnit);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }
}
