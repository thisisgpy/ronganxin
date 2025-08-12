package com.ganpengyu.ronganxin.common.component;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * 本地缓存组件，支持设置过期时间
 *
 * @author Pengyu Gan
 * CreateDate 2025/8/11
 */
@Component
public class LocalCache<K, V> implements Cloneable {

    /**
     * 缓存值包装类，包含实际值和过期时间
     */
    private static class CacheValue<V> {
        /**
         * 实际缓存的值
         */
        final V value;

        /**
         * 过期时间戳（毫秒），Long.MAX_VALUE表示永不过期
         */
        final long expireAt;

        /**
         * 构造缓存值包装对象
         *
         * @param value      缓存的实际值
         * @param ttlSeconds 生存时间（秒），<=0表示永不过期
         */
        CacheValue(V value, long ttlSeconds) {
            this.value = value;
            // 计算过期时间戳，当前时间加上ttlSeconds秒
            this.expireAt = ttlSeconds > 0 ? Instant.now().toEpochMilli() + ttlSeconds * 1000 : Long.MAX_VALUE;
        }

        /**
         * 判断缓存值是否已过期
         *
         * @return true表示已过期，false表示未过期
         */
        boolean isExpired() {
            return Instant.now().toEpochMilli() > expireAt;
        }
    }

    /**
     * Google Guava Cache实例，用于存储键值对
     */
    private final Cache<K, CacheValue<V>> cache;

    /**
     * 定时任务执行器，用于定期清理过期缓存
     */
    private final ScheduledExecutorService scheduler;

    /**
     * 构造函数，初始化本地缓存
     * - 设置缓存最大容量为10,000
     * - 创建后台清理线程，每60秒清理一次过期数据
     */
    public LocalCache() {
        // 创建Guava Cache实例，设置最大缓存条目数
        this.cache = CacheBuilder.newBuilder()
                .maximumSize(10_000)
                .build();

        // 创建单线程的定时任务执行器
        this.scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "LocalCache-Cleanup");
            t.setDaemon(true); // 设置为守护线程
            return t;
        });

        // 每 60 秒清理一次过期数据
        scheduler.scheduleAtFixedRate(this::cleanUpExpiredEntries, 60, 60, TimeUnit.SECONDS);
    }

    /**
     * 向缓存中添加键值对
     *
     * @param key        键
     * @param value      值
     * @param ttlSeconds 生存时间（秒），<=0表示永不过期
     */
    public void put(K key, V value, long ttlSeconds) {
        cache.put(key, new CacheValue<>(value, ttlSeconds));
    }

    /**
     * 从缓存中获取指定键的值
     *
     * @param key 键
     * @return 值，如果不存在或已过期则返回null
     */
    public V get(K key) {
        CacheValue<V> wrapper = cache.getIfPresent(key);
        // 如果值不存在或已过期，则从缓存中移除并返回null
        if (wrapper == null || wrapper.isExpired()) {
            cache.invalidate(key);
            return null;
        }
        return wrapper.value;
    }

    /**
     * 从缓存中移除指定键
     *
     * @param key 要移除的键
     */
    public void remove(K key) {
        cache.invalidate(key);
    }

    /**
     * 清空所有缓存数据
     */
    public void clear() {
        cache.invalidateAll();
    }

    /**
     * 获取缓存中的条目数量
     *
     * @return 缓存条目数量
     */
    public long size() {
        return cache.size();
    }

    /**
     * 获取缓存中所有键的集合
     *
     * @return 所有键的集合
     */
    public Set<K> keys() {
        return cache.asMap().keySet();
    }

    /**
     * 清理过期的缓存条目
     * 该方法由定时任务定期调用
     */
    private void cleanUpExpiredEntries() {
        try {
            // 遍历所有缓存条目，检查是否过期
            for (Map.Entry<K, CacheValue<V>> e : cache.asMap().entrySet()) {
                if (e.getValue().isExpired()) {
                    cache.invalidate(e.getKey());
                }
            }
        } catch (Throwable ignore) {
            // 忽略清理过程中可能出现的异常
        }
    }

}
