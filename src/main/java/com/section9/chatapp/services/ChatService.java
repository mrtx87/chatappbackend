package com.section9.chatapp.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.section9.chatapp.dtos.ChatRoomDTO;
import com.section9.chatapp.dtos.TransferMessage;
import com.section9.chatapp.dtos.UserDTO;
import com.section9.chatapp.entities.Contact;
import com.section9.chatapp.entities.User;
import com.section9.chatapp.mapper.ChatRoomMapper;
import com.section9.chatapp.mapper.UserMapper;
import com.section9.chatapp.repos.Credentials;

@Service
public class ChatService {

	@Autowired
	UserService userService;

	@Autowired
	ChatRoomService chatRoomService;
	
	@Autowired
	SimpMessagingTemplate messageService;
	
	ActiveUsersCache activeUsersCache;

	public ChatService() {
		activeUsersCache = new ActiveUsersCache();
	}
	
	public void connectClient(TransferMessage transferMessage) {
		if(!activeUsersCache.exists(transferMessage.getFrom())){
			activeUsersCache.add(transferMessage.getFrom());
		}
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

	public Optional<List<Contact>> searchContact(String id, String query) {
		return userService
				.searchContact(id, query)
				.map(contacts -> contacts.stream()
						.map(user -> UserMapper.reduce(user))
						.collect(Collectors.toList())
						);
	}

	public Optional<ChatRoomDTO> createRoom(TransferMessage transferMessage) {
		return chatRoomService.createRoom(transferMessage).map(ChatRoomMapper::map);
	}

	public Optional<List<ChatRoomDTO>> getRoomsByUserId(UUID id) {
		return chatRoomService.getRoomsByUserId(id);
	}

}
