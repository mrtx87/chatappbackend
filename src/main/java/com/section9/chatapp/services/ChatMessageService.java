package com.section9.chatapp.services;


import java.util.List;
import java.util.Optional;
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

	public Optional<ChatMessage> saveChatMessage(ChatMessage message) {
		 ChatMessage savedMessage = chatMessageRepository.save(message);
		 return Optional.of(savedMessage);
	}
	
	public ChatMessage getChatMessage(UUID chatMessageId) {
		return chatMessageRepository.getOne(chatMessageId);
	}
	
	public List<ChatMessage> getChatMessagesByRoomId(UUID roomId){
		
		return chatMessageRepository.getChatMessagesByRoomId(roomId);
	}
	
}
