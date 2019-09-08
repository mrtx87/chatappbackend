package com.section9.chatapp.services;


import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.section9.chatapp.entities.User;
import com.section9.chatapp.repos.Credentials;
import com.section9.chatapp.repos.UserRepository;

@Service
public class ChatService {
	
	@Autowired
	UserService userService;	
	
	@Autowired
	ChatRoomService chatRoomService;
	
	public ChatService() {
	}

	public Optional<User> registerUser(Credentials credentials) {
		if(!userService.existsUserByName(credentials.getUsername())) {
			User user = new User();
			user.setName(credentials.getUsername());
			user.setPassword(credentials.getPassword());
			user.setKey(UUID.randomUUID().toString());
			return Optional.of(userService.createUser(user));
		}
		
		return Optional.empty();
	}
	
}
