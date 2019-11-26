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
import com.section9.chatapp.repos.ChatRoomRepository;
import com.section9.chatapp.repos.Credentials;

import javassist.expr.NewArray;

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

	OnlineUsers onlineUsers;

	public ChatService() {
		onlineUsers = new OnlineUsers();
	}

	public void processOnlineStatusByUser(TransferMessage transferMessage) {
		if (!onlineUsers.exists(transferMessage.getFrom())) {
			onlineUsers.add(transferMessage.getFrom());
			UUID cookie = onlineUsers.associateUserByNewCookie(transferMessage.getFrom());
			TransferMessage response = new TransferMessage();
			response.setFunction(Constants.TM_FUNCTION_SET_COOKIE);
			response.setCookie(cookie);
			sendMessageToClient(transferMessage.getFrom().getId(), response);
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

	public Optional<Contact> loginUserByCookie(Credentials credentials) {

		Contact loggingInUser = this.onlineUsers.getContactByCookie(credentials.getCookie());
		if (loggingInUser != null) {
			this.onlineUsers.add(loggingInUser);
			return Optional.of(loggingInUser);
		}

		return Optional.empty();
	}

	public Optional<List<Contact>> searchContact(String id, String query) {
		UUID id_ = UUID.fromString(id);
		final User requestingUser = userService.getUserById(id_).get();

		return userService.searchContact(id, query)
				.map(contacts -> contacts.stream()
						.filter(contact -> !contact.getId().equals(id_)
								&& !isOnContactList(contact, requestingUser.getContacts())) // TODO FILTER
						.map(UserMapper::reduce).collect(Collectors.toList()));
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
			if (!chatRoom.isGroupChat()) {
				Optional<User> user1 = userService.getUserById(chatRoom.getUserIds().get(0));
				Optional<User> user2 = userService.getUserById(chatRoom.getUserIds().get(1));
				if (user1.isPresent() && user2.isPresent()) {
					user1.get().getContacts().add(user2.get().getId());
					user2.get().getContacts().add(user1.get().getId());
					user1.get().getChatRooms().add(chatRoom.getId());
					user2.get().getChatRooms().add(chatRoom.getId());
					userService.updateUser(user1.get());
					userService.updateUser(user2.get());
				}
			} else {
				for (UUID userId : chatRoom.getUserIds()) {
					Optional<User> user = userService.getUserById(userId);
					if (user.isPresent()) {
						user.get().getChatRooms().add(chatRoom.getId());
						userService.updateUser(user.get());
					}
				}
			}

			return Optional.of(chatRoomDTO);
		}
		return Optional.empty();
	}

	private void removeChatMessages(UUID roomId) {
		chatMessageService.removeChatMessagesByRoomId(roomId);
	}

	public void removeChatRoom(ChatRoomDTO room, List<User> users) {
		chatRoomService.deleteRoomById(room.getId());
		// delete for each user
		for (User user : users) {
			user.getChatRooms().remove(room.getId());
		}
	}

	private List<User> getUsersById(List<UUID> ids) {
		List<User> userList = new ArrayList<>();
		for (UUID userId : ids) {
			userList.add(userService.getUserById(userId).get());
		}
		return userList;
	}

	private void updateUsers(List<User> users) {
		users.stream().forEach(user -> userService.updateUser(user));
	}

	private void notifyUserIfOnline(UUID userId, TransferMessage transferMessage) {
		if (isOnline(userId)) {
			sendMessageToClient(userId, transferMessage);
		}
	}

	private void notifyUsers(List<User> users) {
		TransferMessage transferMessage = new TransferMessage();
		transferMessage.setFunction(Constants.TM_FUNCTION_UPDATE_ROOMS_AND_CONTACTS);
		users.stream().forEach(user -> notifyUserIfOnline(user.getId(), transferMessage));
	}

	public void processContactRemoving(TransferMessage transferMessage) {
		List<UUID> contactsOfRoom = transferMessage.getChatRoom().getUserIds();
		if (contactsOfRoom.size() == 2) {
			ChatRoomDTO chatRoom = transferMessage.getChatRoom();
			List<User> userList = getUsersById(contactsOfRoom);
			if (unlinkContactRelation(userList)) {
				removeChatRoom(chatRoom, userList);
				removeChatMessages(chatRoom.getId());
				updateUsers(userList);
				notifyUsers(userList);
			} else {
				System.err.println("Contacts could not be removed.");
			}

		} else {
			System.err.println("Contact/ChatRoom could not be removed.");
		}
	}

	public void processChatRoomRemoving(TransferMessage transferMessage) {
		List<User> userList = getUsersById(transferMessage.getChatRoom().getUserIds());
		removeChatRoom(transferMessage.getChatRoom(), userList);
		removeChatMessages(transferMessage.getChatRoom().getId());
		updateUsers(userList);
		notifyUsers(userList);
	}

	private boolean unlinkContactRelation(List<User> userList) {
		User user1 = userList.get(0);
		User user2 = userList.get(1);
		if (user1.getContacts().contains(user2.getId()) && user2.getContacts().contains(user1.getId())) {
			user1.getContacts().remove(user2.getId());
			user2.getContacts().remove(user1.getId());
			return true;
		}
		return false;
	}

	public Boolean isOnline(UUID userId) {
		return onlineUsers.exists(userId);
	}

//	private List<UUID> getOtherUsers(List<UUID> userList, UUID userId) {
//		return userList.stream().filter(other -> !other.equals(userId)).collect(Collectors.toList());
//	}

	private boolean isOnContactList(User contact, List<UUID> contactList) {
		for (UUID id : contactList) {
			if (id.equals(contact.getId())) {
				return true;
			}
		}
		return false;
	}

	private boolean hasSeenChatMessage(UUID userId, ChatMessageDTO chatMessageDTO) {
		if (chatMessageDTO.getNotSeenBy() != null) {
			return !chatMessageDTO.getNotSeenBy().contains(userId.toString());
		}
		return true;
	}

	public Optional<List<ChatRoomDTO>> getRoomsByUserId(UUID id) {
		Optional<UserDTO> user = userService.getUserById(id).map(UserMapper::map);
		if (user.isPresent()) {
			ArrayList<ChatRoomDTO> rooms = new ArrayList<>();
			for (UUID chatRoomId : user.get().getChatRooms()) {
				Optional<ChatRoomDTO> chatRoom = chatRoomService.getRoomById(chatRoomId).map(ChatRoomMapper::map);
				if (chatRoom.isPresent()) {
					rooms.add(chatRoom.get());
				}
			}
			return Optional.of(rooms);
		}
		return Optional.empty();
	}

	public void processChatMessageFromUser(TransferMessage transferMessage) {
		Optional<ChatMessageDTO> chatMessageToBeShared = this.chatMessageService
				.saveChatMessage(buildChatMessage(transferMessage)).map(ChatMessageMapper::map);
		if (chatMessageToBeShared.isPresent()) {

			TransferMessage response = new TransferMessage();
			response.setFunction("chat-message");
			response.setChatMessage(chatMessageToBeShared.get());

			transferMessage.getChatRoom().getUserIds().stream().filter(userId -> onlineUsers.exists(userId))
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
		onlineUsers.delete(transferMessage.getFrom().getId());
	}

	public List<Contact> getContactsByUserId(UUID userId) {
		Optional<User> user = userService.getUserById(userId);
		ArrayList<Contact> contacts = new ArrayList<>();
		if (user.isPresent()) {
			for (UUID id : user.get().getContacts()) {
				contacts.add(userService.getUserById(id).map(UserMapper::reduce).get());
			}
		}
		return contacts;
	}

	public Contact getContactById(UUID contactId) {
		return userService.getUserById(contactId).map(UserMapper::reduce).get();
	}

	public Contact updateUserProfile(UUID userId, TransferMessage transferMessage) {

		User user = userService.getUserById(userId).get();
		user.setInfo(transferMessage.getFrom().getInfo());
		user.setIconUrl(transferMessage.getFrom().getIconUrl());
		user.setName(transferMessage.getFrom().getName());
		userService.updateUser(user);

		return UserMapper.reduce(user);
	}

	/* DEBUG */

	int countGeneratedUsers = 0;
	String[] userNamesList = { "default", "HusterHihi", "goran", "ester", "anna" };

	public void createUsers(int numOfUsers) {
		for (int i = 0; i < numOfUsers && (countGeneratedUsers < userNamesList.length); i++) {
			Credentials creds = new Credentials();
			creds.setPassword("123");
			creds.setUsername(userNamesList[countGeneratedUsers]);
			registerUser(creds);
			countGeneratedUsers++;
		}
	}

	public String displayGeneratedUsers() {
		
		String html = "<table>"
				+ "<tr><td> Name </td><td> angelegt? </td></tr>";
		
		for (int i = 0; i < userNamesList.length; i++) {			
			String tdName = "<td>" + userNamesList[i] + "</td>";
			String tdStatus = "";
			if (i + 1 <= countGeneratedUsers) {
				tdStatus = "<td>" + "ja" + "</td>";
			}else {
				tdStatus = "<td>" + "-" + "</td>";
			}
			String row = "<tr>" + tdName + tdStatus + "</tr>";
			html += row;
		}

		return html + "</table>";
	}

}
