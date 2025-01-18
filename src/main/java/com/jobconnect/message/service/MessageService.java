package com.jobconnect.message.service;

import com.jobconnect.message.event.ChatEvent;

public interface MessageService {
    void sendChatEvent(ChatEvent event, String tag);
}
