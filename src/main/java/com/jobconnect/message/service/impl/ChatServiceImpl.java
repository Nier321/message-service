package com.jobconnect.message.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Future;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jobconnect.message.constant.MQConstants;
import com.jobconnect.message.controller.ChatController;
import com.jobconnect.message.dto.ChatDTO;
import com.jobconnect.message.event.ChatEvent;
import com.jobconnect.message.service.ChatService;
import com.jobconnect.message.service.MessageService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    // SimpMessagingTemplate 是用于发送消息给客户端的模板
    @Autowired
    private final SimpMessagingTemplate messagingTemplate;
    @Autowired
    private final MessageService messageService;

    // 用户密钥 与用户列表的映射线程安全hashmap
    private final ConcurrentHashMap<String, List<String>> chatSessions = new ConcurrentHashMap<>();

     /**
     * 构造函数，用于初始化 SimpMessagingTemplate
     * @param messagingTemplate SimpMessagingTemplate 实例
     */
   
    /**
     * 生成一个随机密钥
     * 
     * @return 一个随机密钥
     */
    private String generateKey() {
        return UUID.randomUUID().toString();
    }

    /**
     * 生成一个 token
     * 
     * @param userName  用户 ID
     * @param secretKey 秘钥
     * @return 生成的 token
     */
    private String generateToken(String userName, String secretKey) {

        return userName + secretKey;
    }

    // 创建动态线程池
    private final ThreadPoolExecutor executorPool = new ThreadPoolExecutor(
            4, // 核心线程数
            8, // 最大线程数
            60, // 空闲线程存活时间（秒）
            TimeUnit.SECONDS, // 时间单位
            new LinkedBlockingQueue<>() // 队列，用于存放任务
    );

    @Override
    public ChatDTO createChat(String userName) {
        Future<ChatDTO> future = executorPool.submit(() -> {
            logger.info("Received request to create chat for sessionId: {}", userName);
            String key = generateKey(); // 生成密钥
            List<String> userlist = new ArrayList<>(); // 聊天人员列表
            userlist.add(userName);
            chatSessions.put(key, userlist);
            ChatDTO chat = ChatDTO.builder()
                    .chatKey(key)
                    .creator(userName)
                    .build();
            // 发送创建聊天事件
            ChatEvent ChatEvent = com.jobconnect.message.event.ChatEvent.builder()
                    .chatKey(key)
                    .creator(userName)
                    .eventType("CREATE")
                    .build();
            messageService.sendChatEvent(ChatEvent, MQConstants.TAG_CHAT_NEW);
            return chat;
        });
        try {
            return future.get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Boolean joinChat(String chatKey, String username) {
        logger.info("joinChat request to validate key: {} ，user:{}", chatKey, username);
        Future<Boolean> future = executorPool.submit(() -> {
            if (chatSessions.containsKey(chatKey)) {
                chatSessions.get(chatKey).add(username); // 将用户加入聊天室
                sendMessageToWebSocket(chatKey, username, "你好！我是" + username);// 发送加入消息
                ChatEvent chatEvent = ChatEvent.builder()
                .chatKey(chatKey)
                .joiner(username)
                .eventType("JOIN")
                .build();    
                messageService.sendChatEvent(chatEvent, MQConstants.TAG_CHAT_JOIN);
                return true;
            } else {
                return false;
            }
        });
        try {
            return future.get();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void sendMessage(String chatKey, String sender, String message) {
        sendMessageToWebSocket(chatKey, sender, message); 
        
    }


    @Override
    public Void deleteChat(String chatKey) {
        throw new UnsupportedOperationException("Unimplemented method 'deleteChat'");
    }

    private void sendMessageToWebSocket(String chatKey, String sender, String message) {
        // 发送消息到特定会话的主题
        try {
            messagingTemplate.convertAndSend("/topic/messages/" + chatKey,
                    "{\"userId\":\"" + sender + "\", \"message\":\"" + message + "\"}");
            logger.debug("Message: {} sent to chatroom: {}", message, chatKey.toString());
        } catch (MessagingException e) {
            e.printStackTrace();
           logger.error("Error sending message发送消息失败: {}", e.getMessage()); 
        }
       

    }

    // 关闭线程池
    public void shutdown() {
        executorPool.shutdown();
    }

   

}
