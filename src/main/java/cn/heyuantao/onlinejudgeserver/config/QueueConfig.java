package cn.heyuantao.onlinejudgeserver.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author he_yu
 * 队列得配置
 */
@ConfigurationProperties(prefix = "custom.queue")
@Configuration
public class QueueConfig {
    /**
     * 等待队队列得长度
     */
    private Integer pendingQueueSize= 500;

    public Integer getPendingQueueSize() {
        return pendingQueueSize;
    }

    public void setPendingQueueSize(Integer pendingQueueSize) {
        this.pendingQueueSize = pendingQueueSize;
    }
}
