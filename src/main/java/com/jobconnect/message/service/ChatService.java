package com.jobconnect.message.service;

import com.jobconnect.message.dto.ChatDTO;

public interface ChatService {
    ChatDTO createChat(String userName);
    Boolean joinChat(String chatKey,String username);
    void sendMessage(String chatKey,String sender,String message);
    void deleteChat(String chatKey);


}
