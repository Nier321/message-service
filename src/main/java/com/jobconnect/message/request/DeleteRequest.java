package com.jobconnect.message.request;

import lombok.Data;

@Data
public class DeleteRequest {
    private String chatKey;
    private String username;
}
