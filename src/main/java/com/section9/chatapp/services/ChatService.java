package com.section9.chatapp.services;


import java.util.ArrayList;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.section9.chatapp.entities.ChatMessage;
import com.section9.chatapp.entities.ChatRoom;
import com.section9.chatapp.entities.User;
import com.section9.chatapp.repos.ChatRoomRepository;
import com.section9.chatapp.repos.UserRepository;

@Service
public class ChatService {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ChatRoomRepository chatRoomRepository;
	
	public ChatService() {
	}

	public User getUser() {
		return userRepository.findAll().get(0);
	}
	
	int index = 0;
	public void addUser() {
		User user = new User();
		user.setInfo("" + index);
		user.setName("test" + index);
		user.setKey(UUID.randomUUID().toString());

		index +=1;
		
		
		userRepository.save(user);
	}
	
	public void addRoom() {
		ChatRoom chatRoom = new ChatRoom();
		chatRoom.setId(UUID.randomUUID());
		chatRoom.setKey("sdfsdf");
		chatRoom.setName("testroom");
		
		index +=1;
		
		chatRoomRepository.save(chatRoom);
	}
	
}
