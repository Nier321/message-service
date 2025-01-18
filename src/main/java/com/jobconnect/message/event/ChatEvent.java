package com.jobconnect.message.event;

import lombok.AllArgsConstructor; 
import lombok.Builder; 
import lombok.Data; 
import lombok.NoArgsConstructor; 

import java.io.Serializable;

/**
 * 
 */
@Data 
@Builder 
@NoArgsConstructor // 无参构造函数，生成不包含任何参数的构造函数
@AllArgsConstructor // 全参构造函数，生成包含所有参数的构造函数
public class ChatEvent implements Serializable {
    private String eventType;
    private String chatKey;
    private String message;
    private String creator;
    private String joiner;
}
