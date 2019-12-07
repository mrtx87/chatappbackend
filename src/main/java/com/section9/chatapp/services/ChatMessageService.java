package com.section9.chatapp.services;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
	
	public List<ChatMessage> getAllChatMessagesDesc(UUID roomId){
		//Page<ChatMessage> messages = chatMessageRepository.findByRoomId(roomId, );
		return chatMessageRepository.getAllChatMessagesDesc(roomId);
	}
	
	public List<ChatMessage> getAllChatMessagesAsc(UUID roomId){
		//Page<ChatMessage> messages = chatMessageRepository.findByRoomId(roomId, );
		return chatMessageRepository.getAllChatMessagesAsc(roomId);
	}
	
	private void removeMessageById(UUID messageId) {
		chatMessageRepository.deleteById(messageId);
	}
	
	private void removeMessage(ChatMessage message) {
		chatMessageRepository.delete(message);
	}
	
	public void removeChatMessagesByRoomId(UUID roomId) {
		List<ChatMessage> messagesOfRoom = chatMessageRepository.getAllChatMessagesAsc(roomId);
		messagesOfRoom.stream().forEach(m -> removeMessage(m));
	}
	
}
