package com.section9.chatapp.services;


import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.section9.chatapp.entities.ChatRoom;
import com.section9.chatapp.entities.User;

@Service
public class ChatService {
	
	@Autowired
	UserService userService;	
	
	@Autowired
	ChatRoomService chatRoomService;
	
	public ChatService() {
	}

	public List<User> getUsers() {
		
		return userService.getUsers();
	}
	
	public void addUser() {
		userService.addUser();
	}
	
	public void addRoom() {
		chatRoomService.addRoom();
		
	}

	public List<ChatRoom> getRooms() {
		return chatRoomService.getRooms();
	}
	
}
