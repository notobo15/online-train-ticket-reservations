package com.trainticketbooking.app.Configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // Register STOMP endpoints
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")  // The WebSocket endpoint
                .setAllowedOrigins("*")  // You can restrict the origins in production for security reasons
                .withSockJS();  // Enable SockJS fallback in case WebSocket is not supported
    }

    // Configure the message broker
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue");  // Simple in-memory broker for messaging
        registry.setApplicationDestinationPrefixes("/app");  // Prefix for the application controller methods
    }
}
