package com.section9.chatapp.services;


import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.section9.chatapp.entities.ChatMessage;
import com.section9.chatapp.repos.ChatMessageRepository;

@Service
public class ChatMessageService {
	
	@Autowired
	ChatMessageRepository chatMessageRepository;
	
	public ChatMessageService() {
	}

	public ChatMessage saveChatMessage(ChatMessage message) {
		 ChatMessage savedMessage = chatMessageRepository.save(message);
		 
		 return savedMessage;
	}
	
	
	public List<ChatMessage> getChatMessagesByRoomId(UUID roomId){
		return chatMessageRepository.getChatMessagesByRoomId(roomId);
	}
	
}
