package com.section9.chatapp.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.section9.chatapp.services.ChatRoomService;
import com.section9.chatapp.services.ChatService;
import com.section9.chatapp.services.UserService;

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
            .enableSimpleBroker("/client");
        
        registryInstance = registry;
    }
    

    @Bean
    public ChatService chatService() {
     return new ChatService();
     
    }
    
    @Bean
    public UserService userService() {
     return new UserService();
     
    }
    
    @Bean
    public ChatRoomService chatRoomService () {
     return new ChatRoomService();
     
    }
    

}
