package com.section9.chatapp.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.section9.chatapp.services.ChatService;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSocketMessageBroker

@EnableWebSocket
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

	public static MessageBrokerRegistry registryInstance;
	
	
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/socket").setAllowedOrigins("*").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app")
            .enableSimpleBroker("/chat");
        
        registryInstance = registry;
    }
    

    @Bean
    public ChatService syncService() {
     return new ChatService();
     
    }

}
