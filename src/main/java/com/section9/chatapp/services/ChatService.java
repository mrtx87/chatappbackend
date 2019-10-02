package com.section9.chatapp.services;

import java.time.Instant;
import java.util.ArrayList;
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
	ChatMessageService chatMessageService;

	@Autowired
	SimpMessagingTemplate messagingService;

	ActiveUsersCache activeUsersCache;

	public ChatService() {
		activeUsersCache = new ActiveUsersCache();
	}

	public void processOnlineStatusByUser(TransferMessage transferMessage) {
		if (!activeUsersCache.exists(transferMessage.getFrom())) {
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
		userService.getUserById(id_).get();

		return userService.searchContact(id, query)
				.map(contacts -> contacts.stream().filter(contact -> !contact.getId().equals(id_)) // TODO FILTER
				.map(user -> UserMapper.reduce(user)).collect(Collectors.toList()));
	}

	public List<ChatMessageDTO> getChatMessagesByRoomId(UUID userId, UUID roomId) {
		return chatMessageService.getChatMessagesByRoomId(roomId).stream().map(chatMessage -> {
			ChatMessageDTO chatMessageDTO = ChatMessageMapper.map(chatMessage);
			chatMessageDTO.setSeen(hasSeenChatMessage(userId, chatMessageDTO));
			return chatMessageDTO;
		}).collect(Collectors.toList());
	}

	public Optional<ChatRoomDTO> createRoom(TransferMessage transferMessage) {
		ChatRoom chatRoom = chatRoomService.createRoom(transferMessage);
		if (chatRoom != null) {
			ChatRoomDTO chatRoomDTO = ChatRoomMapper.map(chatRoom);
			ChatMessage initMessage = buildChatMessage(Constants.SYSTEM_ID, "New chat room created.",
					chatRoomDTO.getId(), chatRoomDTO.getUserIds());
			chatMessageService.saveChatMessage(initMessage);
			if(chatRoom.getUserIds().size() == 2) {
				Optional<User> user1 = userService.getUserById(chatRoom.getUserIds().get(0));
				Optional<User> user2 = userService.getUserById(chatRoom.getUserIds().get(1));
				if(user1.isPresent() && user2.isPresent()) {
					user1.get().getContacts().add(user2.get().getId());
					user2.get().getContacts().add(user1.get().getId());
					userService.updateUser(user1.get());
					userService.updateUser(user2.get());
				}

			}
			
			return Optional.of(chatRoomDTO);
		}
		return Optional.empty();
	}

	private boolean hasSeenChatMessage(UUID userId, ChatMessageDTO chatMessageDTO) {
		if (chatMessageDTO.getNotSeenBy() != null) {
			return !chatMessageDTO.getNotSeenBy().contains(userId.toString());
		}
		return true;
	}

	public Optional<List<ChatRoomDTO>> getRoomsByUserId(UUID id) {
		return chatRoomService.getRoomsByUserId(id);
	}

	public void processChatMessageFromUser(TransferMessage transferMessage) {
		Optional<ChatMessageDTO> chatMessageToBeShared = this.chatMessageService
				.saveChatMessage(buildChatMessage(transferMessage)).map(ChatMessageMapper::map);
		if (chatMessageToBeShared.isPresent()) {

			TransferMessage response = new TransferMessage();
			response.setFunction("chat-message");
			response.setChatMessage(chatMessageToBeShared.get());

			transferMessage.getChatRoom().getUserIds().stream().filter(userId -> activeUsersCache.exists(userId))
					.forEach(userId -> sendMessageToClient(userId, response));
		}
	}

	public boolean updateUnseenChatMessages(TransferMessage transferMessage) {

		for (UUID chatMessageId : transferMessage.getUnseenChatMessageIds()) {
			ChatMessage chatMessage = chatMessageService.getChatMessage(chatMessageId);
			chatMessage
					.setNotSeenBy(chatMessage.getNotSeenBy().replace(transferMessage.getFrom().getId().toString(), ""));
			chatMessageService.saveChatMessage(chatMessage);
		}

		return true;
	}

	private void sendMessageToClient(UUID userId, TransferMessage response) {
		this.messagingService.convertAndSend("/client/" + userId, response);
	}

	private String convertToNotSeenByString(List<UUID> ids) {
		String notSeenBy = "";
		for (int i = 0; i < ids.size(); i++) {
			UUID id = ids.get(i);
			if (i + 1 < ids.size()) {
				notSeenBy += id + ",";
			} else {
				notSeenBy += id;
			}
		}
		return notSeenBy;
	}

	private ChatMessage buildChatMessage(String fromId, String body, UUID chatRoomId, List<UUID> userIds) {
		ChatMessage message = new ChatMessage();
		message.setRoomId(chatRoomId);
		message.setFromId(fromId);
		message.setBody(body);
		message.setCreatedAt(Instant.now());
		message.setNotSeenBy(convertToNotSeenByString(userIds));
		// message.setNotSeenBy(userIds);
		return message;
	}

	private ChatMessage buildChatMessage(TransferMessage transferMessage) {

		return buildChatMessage(transferMessage.getChatMessage().getFromId(),
				transferMessage.getChatMessage().getBody(), transferMessage.getChatRoom().getId(),
				transferMessage.getChatRoom().getUserIds());
	}

	public void processDisconnectFromClient(TransferMessage transferMessage) {
		activeUsersCache.delete(transferMessage.getFrom().getId());
	}

	public List<Contact> getContactsByUserId(UUID userId) {
		Optional<User> user = userService.getUserById(userId);
		ArrayList<Contact> contacts = new ArrayList<>();
		if(user.isPresent()) {
			 for(UUID id : user.get().getContacts()) {
				 contacts.add(userService.getUserById(id).map(UserMapper::reduce).get());
			 }
		}
		return contacts;
	}

}
