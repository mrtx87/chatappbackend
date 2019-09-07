package com.section9.chatapp.services;


import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.section9.chatapp.entities.ChatRoom;
import com.section9.chatapp.entities.User;
import com.section9.chatapp.repos.ChatRoomRepository;
import com.section9.chatapp.repos.UserRepository;

@Service
public class ChatRoomService {
	
	@Autowired
	UserRepository userRepository; //DEBUG
	
	@Autowired
	ChatRoomRepository chatRoomRepository;
	
	public ChatRoomService() {
	}

	int index = 0;
	
	public void addRoom() {
		
		
		
		ChatRoom chatRoom = new ChatRoom();
		chatRoom.setId(UUID.randomUUID());
		chatRoom.setKey("sdfsdf");
		chatRoom.setName("testroom");
		chatRoom.setUsers(userRepository.findAll().stream().map(user -> user.getId()).collect(Collectors.toList()));
		
		index +=1;
		
		
		chatRoomRepository.save(chatRoom);
	}

	public List<ChatRoom> getRooms() {
		// TODO Auto-generated method stub
		return chatRoomRepository.findAll();
	}
	
}
