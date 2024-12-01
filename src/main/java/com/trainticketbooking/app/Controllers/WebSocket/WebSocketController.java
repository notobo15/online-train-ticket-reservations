package com.trainticketbooking.app.Controllers.WebSocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @MessageMapping("/sendMessage")  // Định tuyến cho các tin nhắn từ client tới endpoint "/app/sendMessage"
    @SendTo("/topic/messages")  // Gửi tin nhắn tới tất cả các client đăng ký tại "/topic/messages"
    public String sendMessage(String message) {
        return message;  // Trả lại tin nhắn tới tất cả các client đã kết nối
    }
}