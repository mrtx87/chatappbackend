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
public class UserService {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ChatRoomRepository chatRoomRepository; //DEBUG
	
	public UserService() {
	}

	public List<User> getUsers() {
		
		return userRepository.findAll();
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
	
}
