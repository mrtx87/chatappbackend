package com.section9.chatapp.mapper;

import java.time.Instant;

import com.section9.chatapp.dtos.ChatMessageDTO;
import com.section9.chatapp.entities.ChatMessage;

public class ChatMessageMapper {

	public static ChatMessage create(ChatMessageDTO origin) {
		ChatMessage message = new ChatMessage();
		message.setRoomId(origin.getRoomId());
		message.setFromId(origin.getFromId());
		message.setBody(origin.getBody());
		message.setCreatedAt(Instant.now());
		return message;
	}
	
	public static ChatMessageDTO map(ChatMessage origin) {
		ChatMessageDTO message = new ChatMessageDTO();
		message.setId(origin.getId());
		message.setRoomId(origin.getRoomId());
		message.setFromId(origin.getFromId());
		message.setBody(origin.getBody());
		message.setCreatedAt(origin.getCreatedAt());
		message.setNotSeenBy(origin.getNotSeenBy());
		return message;
	}
	
}
