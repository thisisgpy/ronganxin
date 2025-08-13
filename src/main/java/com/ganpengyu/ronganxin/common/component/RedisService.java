package com.ganpengyu.ronganxin.common.component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ganpengyu.ronganxin.common.util.JsonUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author Pengyu Gan
 * CreateDate 2025/8/13
 */
@Component
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public <T> void set(String key, T value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    public <T> T get(String key, Class<T> clazz) {
        Object obj = redisTemplate.opsForValue().get(key);
        return clazz.isInstance(obj) ? clazz.cast(obj) : null;
    }

    public <T> T get(String key, TypeReference<T> typeReference) {
        Object obj = redisTemplate.opsForValue().get(key);
        if (obj == null) {
            return null;
        }
        return JsonUtils.getObjectMapper().convertValue(obj, typeReference);
    }

    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    public Boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    public Long incr(String key) {
        return redisTemplate.opsForValue().increment(key);
    }

}
