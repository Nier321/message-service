package com.jobconnect.message.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jobconnect.message.aop.DecryptMessage;
import com.jobconnect.message.aop.EncryptResponse; // 2025-4江西 新增：用于响应加密的注解
import com.jobconnect.message.dto.ChatDTO;
import com.jobconnect.message.request.DeleteRequest;
import com.jobconnect.message.request.JoinChatRequest; // 2025-4江西 新增
import com.jobconnect.message.request.MessageRequest;
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

    @EncryptResponse(fields = {"key"}) // 2025-4江西 新增：自动加密响应key字段
    @PostMapping("/createChat")
    public ResponseEntity<Map<String, Object>> createChat( @Valid @RequestParam String userId) {

        logger.info("Received request to create chat for sessionId: {}", userId);
        ChatDTO chatDTO = chatService.createChat(userId);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("key", chatDTO.getChatKey()); // 2025-4江西 原始代码，key字段将被AOP自动加密
        response.put("username", userId); // 返回username,key
        logger.info("Chat created with key: {} for username: {}", chatDTO.getChatKey(), chatDTO.getCreator());
        return ResponseEntity.ok(response);
    }

    // 2025-4江西 优化：用DTO+@RequestBody+@DecryptMessage(param="req",fields={"key"})自动解密JoinChatRequest.key字段
    @DecryptMessage(param = "req", fields = {"key"})
    @PostMapping("/joinChat")
    public ResponseEntity<Map<String, Object>> joinChat(@RequestBody JoinChatRequest req) {
        String key = req.getKey();
        String userId = req.getUserId();
        logger.info("Controller joinChat request to validate key: {} ，user:{}", key, userId);
        if (chatService.joinChat(key, userId)) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            logger.info("Chat join with key: {} for username: {}", key, userId);
            return ResponseEntity.ok(response); // 返回有效的响应
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            return ResponseEntity.ok(response);
        }
    }

    /**
     * 发送消息到 聊天室
     * 
     * @param messageRequest MessageRequest 对象
     */
    @DecryptMessage(param = "messageRequest", fields = {"chatkey"}) // 2025-4江西 新增：自动解密messageRequest.chatkey字段
    @MessageMapping("/sendMessage")
    public void sendMessage( @RequestBody MessageRequest messageRequest) {
    try {
        logger.info("send message from key: {}", messageRequest.getChatkey());
    if (messageRequest.getChatkey() != null) {
        chatService.sendMessage(messageRequest.getChatkey(), messageRequest.getUserId(), messageRequest.getMessage());
        logger.debug("Message: {} sent to chatroom: {}", messageRequest.getMessage(), messageRequest.getChatkey());
    } else {
        logger.warn("No session found for chatkey: {}", messageRequest.getChatkey());
    }
    } catch (Exception e) {
        logger.error("消息解密或反序列化失败", e);
        e.printStackTrace();
    }
    }
    /**
     * 处理用户断开连接
     * 
     * @param headerAccessor SimpMessageHeaderAccessor 实例
     */
    @DecryptMessage(param = "deleteRequest", fields = {"chatKey"})// 2025-4江西 新增：自动解密deleteRequest.chatKey字段
    @MessageMapping("/quitChat")
    public void disconnect(@Payload DeleteRequest deleteRequest) {
        String chatKey = deleteRequest.getChatKey();
        String username = deleteRequest.getUsername();
        // 清除用户密钥
        logger.info("controller.清除用户密钥:{}", chatKey);
        chatService.deleteChat(chatKey);
    }

}
