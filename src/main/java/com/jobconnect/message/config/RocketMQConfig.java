package com.jobconnect.message.config;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jobconnect.message.constant.MQConstants;

/**
 * RocketMQ 配置类
 */
@Configuration // 表示这是一个配置类，用于定义 Spring 的 bean
public class RocketMQConfig {

    /**
     * 从配置文件中读取 RocketMQ 的名称服务器地址
     */
    @Value("${rocketmq.name-server}")
    private String nameService;

    /**
     * 创建 RocketMQTemplate 实例
     * 
     * @return RocketMQTemplate 实例
     */
    @Bean // 表示这是一个 Spring bean
    public RocketMQTemplate chatPocketMQTemplate() {
        // 创建 RocketMQTemplate 实例
        RocketMQTemplate rocketMQTemplate = new RocketMQTemplate();
        // 创建消息生产者实例
        DefaultMQProducer producer = new DefaultMQProducer();

        // 设置生产者组
        producer.setProducerGroup(MQConstants.GROUP_ChAT_PRODUCER);
        // 设置名称服务器地址
        producer.setNamesrvAddr(nameService);
        // 设置发送消息的超时时间（毫秒）
        producer.setSendMsgTimeout(10000);
        // 设置发送失败时的重试次数
        producer.setRetryTimesWhenSendFailed(3);

        // 将生产者设置到 RocketMQTemplate 中
        rocketMQTemplate.setProducer(producer);
        // 返回配置好的 RocketMQTemplate 实例
        return rocketMQTemplate;
    }

}
