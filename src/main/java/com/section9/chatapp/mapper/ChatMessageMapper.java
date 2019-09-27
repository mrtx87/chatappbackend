package com.section9.chatapp.mapper;

import com.section9.chatapp.dtos.ChatMessageDTO;
import com.section9.chatapp.entities.ChatMessage;

public class ChatMessageMapper {

	public static ChatMessage create(ChatMessageDTO origin) {
		ChatMessage message = new ChatMessage();
		message.setId(origin.getId());
		message.setRoomId(origin.getRoomId());
		message.setFromId(origin.getFromId());
		message.setBody(origin.getBody());
		message.setCreatedAt(origin.getCreatedAt());
		return message;
	}
	
	public static ChatMessageDTO map(ChatMessage origin) {
		ChatMessageDTO message = new ChatMessageDTO();
		message.setId(origin.getId());
		message.setRoomId(origin.getRoomId());
		message.setFromId(origin.getFromId());
		message.setBody(origin.getBody());
		message.setCreatedAt(origin.getCreatedAt());
		return message;
	}
	
}
