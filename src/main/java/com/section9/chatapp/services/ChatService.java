package com.section9.chatapp.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.section9.chatapp.dtos.ChatMessageDTO;
import com.section9.chatapp.dtos.ChatRoomDTO;
import com.section9.chatapp.dtos.TransferMessage;
import com.section9.chatapp.dtos.UserDTO;
import com.section9.chatapp.entities.ChatMessage;
import com.section9.chatapp.entities.ChatRoom;
import com.section9.chatapp.entities.Contact;
import com.section9.chatapp.entities.User;
import com.section9.chatapp.mapper.ChatMessageMapper;
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
	
	@Autowired
	ChatMessageService chatMessageService;
	
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
		UUID id_ = UUID.fromString(id);
		User requestingUser = userService.getUserById(id_).get();
		
		return userService
				.searchContact(id, query)
				.map(contacts -> contacts.stream()
						.filter(contact -> !contact.getId().equals(id_)) //TODO FILTER CONTACTS
						.map(user -> UserMapper.reduce(user))
						.collect(Collectors.toList())
						);
	}
	
	private ChatMessage buildChatMessage(String fromId, String body,  UUID chatRoomId, List<UUID> userIds) {
		ChatMessage message = new ChatMessage();
		message.setRoomId(chatRoomId);
		message.setFromId(fromId);
		message.setBody(body);
		//message.setNotSeenBy(userIds);
		return message;
	}
	
	public List<ChatMessageDTO> getChatMessagesByRoomId(UUID roomId){
		return chatMessageService
				.getChatMessagesByRoomId(roomId)
				.stream()
				.map(ChatMessageMapper::map)
				.collect(Collectors.toList());
	}

	public Optional<ChatRoomDTO> createRoom(TransferMessage transferMessage) {
		ChatRoom chatRoom = chatRoomService.createRoom(transferMessage);
		if(chatRoom != null) {
			
			
			ChatRoomDTO chatRoomDTO = ChatRoomMapper.map(chatRoom);
			ChatMessage initMessage = buildChatMessage(Constants.SYSTEM_ID, "New chat room created.", chatRoomDTO.getId(), chatRoomDTO.getUserIds());
			ChatMessage savedInitMessage = chatMessageService.saveChatMessage(initMessage);
			
			return Optional.of(chatRoomDTO);
		}
		
		return Optional.empty();

	}
	
	
	public Optional<List<ChatRoomDTO>> getRoomsByUserId(UUID id) {
		return chatRoomService.getRoomsByUserId(id);
	}

}
