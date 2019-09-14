package com.section9.chatapp.mapper;

import com.section9.chatapp.dtos.ChatRoomDTO;
import com.section9.chatapp.entities.ChatRoom;

public class ChatRoomMapper {
	public static ChatRoomDTO map(ChatRoom origin) {
		ChatRoomDTO chatRoomDTO = new ChatRoomDTO();	
		chatRoomDTO.setId(origin.getId());
		chatRoomDTO.setTitle(origin.getTitle());
		chatRoomDTO.setUserIds(origin.getUserIds());
		chatRoomDTO.setIconUrl(origin.getIconUrl());
		return chatRoomDTO;
	}
	
	public static ChatRoom map(ChatRoomDTO origin) {
		ChatRoom chatRoom= new ChatRoom();	
		chatRoom.setId(origin.getId());
		chatRoom.setTitle(origin.getTitle());
		chatRoom.setUserIds(origin.getUserIds());
		chatRoom.setIconUrl(origin.getIconUrl());
		return chatRoom;
	}
}
