package com.jobconnect.message.request;

import lombok.Data;

@Data
public class MessageRequest {
    private String chatkey;
    private String userId;
    private String message;
}
