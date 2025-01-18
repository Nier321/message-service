package com.jobconnect.message.listener;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import com.jobconnect.message.constant.MQConstants;
import com.jobconnect.message.event.ChatEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RocketMQMessageListener(topic = MQConstants.TOPIC_CHAT, consumerGroup = MQConstants.GROUP_CHAT_CONSUMER)
public class ChatEventListener implements RocketMQListener<ChatEvent>{
    @Override
    public void onMessage(ChatEvent event) {
       log.info("Received chat event: {}", event);
       switch (event.getEventType()) {
        case "CREATE":
            handleChatCreated(event);
            break;
        case "JOIN":
            handleChatJoined(event);
            break;
        case "DELETE":
            handleChatDeleted(event);
            break;
       }

    }
    private void handleChatCreated(ChatEvent event) {
        // 处理聊天创建事件
        //插入数据
        log.info("数据库已加入聊天室 {}", event);
    }
    private void handleChatJoined(ChatEvent event) {
        // 处理聊天加入事件
        // 数据更新
        log.info("聊天室已更新: {}", event);
    }
    private void handleChatDeleted(ChatEvent event) {
        // 处理聊天删除事件
        //删除数据
        log.info("聊天室已删除: {}", event);
    }

}
