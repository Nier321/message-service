package com.jobconnect.message.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatDTO {
    private long id;
    private String chatKey;
    private String creator;
    private String joiner;
    private String message;
    private int peopleCount;

}
