package com.trainticketbooking.app.Services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void notifyClients(String message) {
        messagingTemplate.convertAndSend("/topic/messages", message);
    }
}