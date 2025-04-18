package com.jobconnect.message.service.impl;


import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.jobconnect.message.constant.MQConstants;
import com.jobconnect.message.event.ChatEvent;
import com.jobconnect.message.service.MessageService;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class MessageServiceImpl implements MessageService {
   
    private final RocketMQTemplate rocketMQTemplate;
    @Autowired
    public MessageServiceImpl(@Qualifier("rocketMQTemplate") RocketMQTemplate rocketMQTemplate) {
        this.rocketMQTemplate = rocketMQTemplate;
    }

    @Override
    public void sendChatEvent(ChatEvent event, String tag) {
        // 构建消息目的地，格式为 "主题:标签"
        String destination = MQConstants.TOPIC_CHAT + ":" + tag;
        // 创建消息对象，携带事件数据
        Message<ChatEvent> message = MessageBuilder.withPayload(event).build();
        rocketMQTemplate.syncSend(destination, message);
        //rocketMQTemplate.syncSendOrderly(destination, message, destination);
        log.info("{}聊天事件发生！: {}",tag, event);
        // 通过 RocketMQ 异步发送消息
       /*  rocketMQTemplate.asyncSend(destination, message, new SendCallback(){

        @Override
        public void onException(Throwable throwable) {
            // 记录发送失败的日志
            log.error("{}聊天事件发生失败！: {}", tag,event, throwable);
            // 可以选择抛出运行时异常，或进行其他处理
            throw new RuntimeException("Failed to send chat event", throwable);
        }

        @Override
        public void onSuccess(SendResult arg0) {
            // 记录发送成功的日志
            log.info("{}聊天事件发生成功！: {}",tag, event);
    }});*/

        
    }

}
