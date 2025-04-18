// package com.jobconnect.message.config;

// import com.jobconnect.message.constant.MQConstants;
// import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
// import org.apache.rocketmq.client.exception.MQClientException;
// import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;

// @Configuration
// public class RocketMQConsumerConfig {

//     @Value("${rocketmq.name-server}")
//     private String nameServer;

//     @Bean
//     public DefaultMQPushConsumer chatConsumer() throws MQClientException {
//         DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(MQConstants.GROUP_CHAT_CONSUMER);
//         consumer.setNamesrvAddr(nameServer);
//         consumer.setMessageModel(MessageModel.CLUSTERING);
//         consumer.subscribe(MQConstants.TOPIC_CHAT, "*");
       
//         return consumer;
//     }
// }
