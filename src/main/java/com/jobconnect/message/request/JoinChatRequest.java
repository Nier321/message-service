package com.jobconnect.message.request;

import lombok.Data;

/**
 * 加入聊天请求DTO
 * 只对key字段加密/解密，userId为明文
 */
@Data
public class JoinChatRequest {
    private String key;
    private String userId;


}
