package com.section9.chatapp.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.section9.chatapp.dtos.UserDTO;
import com.section9.chatapp.entities.User;
import com.section9.chatapp.mapper.UserMapper;
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

	public Optional<UserDTO> registerUser(Credentials credentials) {
		if (!userService.existsUserByName(credentials.getUsername())) {
			User user = new User();
			user.setName(credentials.getUsername());
			user.setPassword(credentials.getPassword());
			user.setKey(UUID.randomUUID().toString());
			return Optional.of(UserMapper.map(userService.createUser(user)));
		}

		return Optional.empty();
	}

	public Optional<UserDTO> loginUser(Credentials credentials) {
		User user = userService.getUserByLogin(credentials);
		if (user != null) {
			return Optional.of(UserMapper.map(user));
		}

		return Optional.empty();
	}

}
