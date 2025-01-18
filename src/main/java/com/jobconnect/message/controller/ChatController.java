package com.jobconnect.message.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jobconnect.message.dto.ChatDTO;
import com.jobconnect.message.service.ChatService;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api")
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);
 
    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/createChat")
    public ResponseEntity<Map<String, Object>> createChat(@Valid @RequestParam String userId) {
        
        logger.info("Received request to create chat for sessionId: {}", userId);
        ChatDTO chatDTO = chatService.createChat(userId);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("key", chatDTO.getChatKey());
        response.put("username", userId); // 返回username,key
        logger.info("Chat created with key: {} for username: {}", chatDTO.getChatKey(),chatDTO.getCreator() );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/joinChat")
    public ResponseEntity<Map<String, Object>> joinChat(@Valid @RequestParam String key,@Valid @RequestParam String userId) {
        logger.info("joinChat request to validate key: {} ，user:{}", key,userId);
        if (chatService.joinChat(key, userId)) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("key", key); // 返回会话 ID
            logger.info("Chat join with key: {} for username: {}", key, userId);
            return ResponseEntity.ok(response); // 返回有效的响应
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("success", false, "message", "Invalid key.")); // 返回无效的响应
        }
    }


    /**
     * 发送消息到 聊天室
     * @param messageRequest MessageRequest 对象
     */
    @MessageMapping("/sendMessage")
    public void sendMessage(@Payload MessageRequest messageRequest) {
        logger.info("send message from key: {}", messageRequest.getChatkey());
        if (messageRequest.getChatkey() != null) {
            // 发送消息到特定会话的主题
            chatService.sendMessage(messageRequest.getChatkey(),messageRequest.getUserId(),messageRequest.getMessage());
            logger.debug("Message: {} sent to chatroom: {}", messageRequest.getMessage() , messageRequest.getChatkey().toString());
        } else {
            logger.warn("No session found for chatkey: {}", messageRequest.getChatkey());
        }
    }

    /**
     * 处理用户断开连接
     * @param headerAccessor SimpMessageHeaderAccessor 实例
     */
    @MessageMapping("/quitChat")
    public void disconnect(String chatkey) {
        // 清除用户密钥
        chatService.deleteChat(chatkey);
        logger.info("User disconnected and key removed.");
        logger.debug("User disconnected.");
    }

}
