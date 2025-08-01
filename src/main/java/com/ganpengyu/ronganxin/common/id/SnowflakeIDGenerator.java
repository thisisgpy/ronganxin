package com.ganpengyu.ronganxin.common.id;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * 雪花 ID 生成器
 *
 * @author Pengyu Gan
 * CreateDate 2025/3/11
 */
@Slf4j
@Component
public class SnowflakeIDGenerator {

    /**
     * 起始时间戳
     */
    private final long twepoch;

    /**
     * 机器号位数
     */
    private final long workerIDBits = 10L;

    /**
     * 最大可分配机器 ID = 1023，机器 ID 范围在 0 ~ 1023
     */
    private final long maxWorkerID = ~(-1L << workerIDBits);

    /**
     * 序列号位数，12 位可用范围为 0 ~ 4095
     */
    private final long sequenceBits = 12L;

    /**
     * workerID 左移位数
     */
    private final long workerIDShift = sequenceBits;

    /**
     * 时间戳左移位数
     */
    private final long timestampLeftShift = sequenceBits + workerIDShift;

    /**
     * 序列号掩码
     */
    private final long sequenceMask = ~(-1L << sequenceBits);

    /**
     * 当前实例使用的机器 ID
     */
    private long workerID;

    /**
     * 毫秒内的序列号
     */
    private long sequence = 0L;

    /**
     * 上次生成 ID 的时间戳
     */
    private long lastTimestamp = -1L;

    private static final Random RANDOM = new Random();

    /**
     * 构造雪花算法 ID 生成器实例
     */
    public SnowflakeIDGenerator() {
        // 2024-12-10 09:52:00
        this(1733795520000L);
    }

    /**
     * 构造雪花算法 ID 生成器实例
     *
     * @param twepoch ID 起始时间戳
     */
    public SnowflakeIDGenerator(long twepoch) {
        this.twepoch = twepoch;
        long now = System.currentTimeMillis();
        if (now < twepoch) {
            log.error("当前时间 {} 不允许小于生成器 ID 计算的起始时间 {}", now, twepoch);
            throw new RuntimeException(String.format("当前时间 %s 不允许小于生成器 ID 计算的起始时间 %s", now, twepoch));
        }
        workerID = 1000L;
    }

    public synchronized long getId() {
        long timestamp = System.currentTimeMillis();
        // 发生始终回拨，等待时钟赶上
        if (timestamp < lastTimestamp) {
            long offset = lastTimestamp - timestamp;
            if (offset <= 5) {
                try {
                    wait(offset << 1);
                    timestamp = System.currentTimeMillis();
                    if (timestamp < lastTimestamp) {
                        log.error("发号器出现时钟回拨");
                        throw new RuntimeException("发号器出现时钟回拨");
                    }
                } catch (InterruptedException e) {
                    log.error("等待时钟赶上当前时间时发生异常", e);
                    throw new RuntimeException("等待时钟赶上当前时间时发生异常");
                }
            } else {
                log.error("始终回拨超过 5 毫秒，无法生成 ID。");
                throw new RuntimeException("始终回拨超过 5 毫秒，无法生成 ID。");
            }
        }
        // 同一毫秒内的并发请求计算序列号
        if (timestamp == lastTimestamp) {
            sequence = (sequence + 1) & sequenceMask;
            // sequence 为 0 表示当前毫秒内的 4096 个 ID 已经发完了，需要等到下一毫秒到来才能继续生成 ID
            if (sequence == 0) {
                sequence = RANDOM.nextInt(100);
                timestamp = waitNextMillis(lastTimestamp);
            }
        } else {
            sequence = RANDOM.nextInt(100);
        }
        // 更新 ID 生成时间
        lastTimestamp = timestamp;
        // 生成 ID
        long id = ((timestamp - twepoch) << timestampLeftShift) | (workerID << workerIDShift) | sequence;
        return id;
    }

    private long waitNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }

    public boolean init() {
        return true;
    }

}
