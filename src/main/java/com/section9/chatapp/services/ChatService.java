package com.section9.chatapp.services;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
import com.section9.chatapp.dtos.DataTransferContainer;
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

	@Autowired
	OnlineUsers onlineUsers;

	@Autowired
	CookieService cookieService;

	public ChatService() {
		onlineUsers = new OnlineUsers();
		userNamesList = toList();
	}

	public void finalizeWebSocketConnectionAndLogin(DataTransferContainer transferMessage) {
		Contact contact = transferMessage.getFrom();
		onlineUsers.add(contact);
		DataTransferContainer response = new DataTransferContainer();
		response.setFunction(Constants.TM_FUNCTION_LOGIN_AND_COOKIE);
		response.setFrom(contact);

		if (transferMessage.getCookie() == null) {
			UUID cookie = onlineUsers.associateUserByNewCookie(contact);
			response.setCookie(cookie);
		} else {
			response.setCookie(transferMessage.getCookie());
		}

		notifyClient(contact.getId(), response);

	}

	public Optional<UserDTO> registerUser(Credentials credentials) {
		if (!userService.existsUserByName(credentials.getUsername())) {
			User user = new User();
			user.setName(credentials.getUsername());
			user.setPassword(credentials.getPassword());
			user.setKey(UUID.randomUUID().toString());
			user.setChatRooms(new ArrayList<>());
			user.setContacts(new ArrayList<>());
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

		UUID loggingInUserId = this.onlineUsers.getContactIdByCookieId(credentials.getCookie());
		if (loggingInUserId != null) {
			Contact loggingInUser = this.getContactById(loggingInUserId);
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

	public List<ChatMessageDTO> getInitialChatMessages(UUID userId, UUID roomId) {
		List<ChatMessage> allChatMessages = chatMessageService.getAllChatMessagesDesc(roomId);
		List<ChatMessageDTO> initMessages = new ArrayList<>();
		int additionalMessagesCount = Constants.ADDITIONAL_MESSAGES_SIZE; // TODO in constants?
		for (ChatMessage chatMessage : allChatMessages) {
			ChatMessageDTO chatMessageDTO = ChatMessageMapper.map(chatMessage);
			if (hasSeenChatMessage(userId, chatMessageDTO)) {
				chatMessageDTO.setSeen(true);
				additionalMessagesCount -= 1;
			} else {
				chatMessageDTO.setSeen(false);
			}

			initMessages.add(0, chatMessageDTO);
			if (additionalMessagesCount == 0) {
				break;
			}
		}
		return initMessages;

		// return null;

	}

	public List<ChatMessageDTO> getChatMessagesBatch(UUID userId, UUID roomId, UUID lastMessageToken) {

		List<ChatMessage> allChatMessages = chatMessageService.getAllChatMessagesDesc(roomId);
		List<ChatMessageDTO> batchOfMessages = new ArrayList<>();
		int batchSize = Constants.MESSAGE_BATCH_SIZE;
		boolean found = false;
		for (ChatMessage message : allChatMessages) {
			if (found) {
				batchOfMessages.add(0, ChatMessageMapper.map(message));
				batchSize -= 1;
				if (batchSize == 0) {
					break;
				}
				continue;
			}

			if (!found && message.getId().equals(lastMessageToken)) {
				found = true;
			}
		}

		return batchOfMessages;
	}

	public Optional<ChatRoomDTO> createRoom(DataTransferContainer transferMessage) {
		ChatRoom chatRoom = chatRoomService.createRoom(transferMessage);
		if (chatRoom != null) {
			ChatRoomDTO chatRoomDTO = ChatRoomMapper.map(chatRoom);
			ChatMessage initDateMessage = buildChatMessage(Constants.CHAT_MESSAGE_DATE_TYPE,
					Instant.now().truncatedTo(ChronoUnit.DAYS).toString(), chatRoomDTO.getId(),
					chatRoomDTO.getUserIds(), true, 0);
			chatMessageService.saveChatMessage(initDateMessage);

			ChatMessage initMessage = buildChatMessage(Constants.SYSTEM_INIT_ID, "New chat room created.",
					chatRoomDTO.getId(), chatRoomDTO.getUserIds(), true, 100);
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

					DataTransferContainer response = new DataTransferContainer();
					response.setFrom(transferMessage.getFrom());
					response.setChatroom(chatRoomDTO);
					response.setFunction(Constants.TM_FUNCTION_CREATE_ROOM_AND_CONTACT);
					notifyUserIfOnline(user1.get().getId(), response);
					notifyUserIfOnline(user2.get().getId(), response);
				}
			} else {
				DataTransferContainer response = new DataTransferContainer();
				response.setFrom(transferMessage.getFrom());
				response.setChatroom(chatRoomDTO);
				response.setFunction(Constants.TM_FUNCTION_CREATE_GROUP_ROOM);
				for (UUID userId : chatRoom.getUserIds()) {
					Optional<User> user = userService.getUserById(userId);
					if (user.isPresent()) {
						user.get().getChatRooms().add(chatRoom.getId());
						userService.updateUser(user.get());
						notifyUserIfOnline(user.get().getId(), response);
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

	private void notifyUserIfOnline(UUID userId, DataTransferContainer transferMessage) {
		if (isOnline(userId)) {
			notifyClient(userId, transferMessage);
		}
	}

	private void notifyUsersIfOnline(List<User> users, DataTransferContainer transferMessage) {
		users.stream().forEach(user -> notifyUserIfOnline(user.getId(), transferMessage));
	}

	private void notifyContactsIfOnline(List<Contact> users, DataTransferContainer transferMessage) {
		users.stream().forEach(user -> notifyUserIfOnline(user.getId(), transferMessage));
	}

	private void notifyUsersByIdIfOnline(List<UUID> userIds, DataTransferContainer transferMessage) {
		userIds.stream().forEach(userId -> notifyUserIfOnline(userId, transferMessage));
	}

	private void notifyUsersAboutCallForAction(List<User> users, String function) {
		DataTransferContainer transferMessage = new DataTransferContainer();
		transferMessage.setFunction(function);
		users.stream().forEach(user -> notifyUserIfOnline(user.getId(), transferMessage));
	}

	private void notifyUsersById(List<UUID> userIds, String function) {
		DataTransferContainer transferMessage = new DataTransferContainer();
		transferMessage.setFunction(function);
		userIds.stream().forEach(userId -> notifyUserIfOnline(userId, transferMessage));
	}

	public void processContactRemoving(DataTransferContainer transferMessage) {
		List<UUID> contactsOfRoom = transferMessage.getChatRoom().getUserIds();
		if (contactsOfRoom.size() == 2) {
			ChatRoomDTO chatRoom = transferMessage.getChatRoom();
			List<User> userList = getUsersById(contactsOfRoom);
			if (unlinkContactRelation(userList)) {
				removeChatRoom(chatRoom, userList);
				removeChatMessages(chatRoom.getId());
				updateUsers(userList);
				notifyUsersAboutCallForAction(userList, Constants.TM_FUNCTION_UPDATE_ROOMS_AND_CONTACTS);
			} else {
				System.err.println("Contacts could not be removed.");
			}

		} else {
			System.err.println("Contact/ChatRoom could not be removed.");
		}
	}

	public void processChatRoomRemoving(DataTransferContainer transferMessage) {
		List<User> userList = getUsersById(transferMessage.getChatRoom().getUserIds());
		removeChatRoom(transferMessage.getChatRoom(), userList);
		removeChatMessages(transferMessage.getChatRoom().getId());
		updateUsers(userList);
		notifyUsersAboutCallForAction(userList, Constants.TM_FUNCTION_UPDATE_ROOMS_AND_CONTACTS);
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
		return onlineUsers.isOnline(userId);
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

	public ChatMessageDTO getLatestChatMessageByRoomId(UUID roomId) {
		return chatMessageService.getLatestChatMessageByRoomId(roomId);
	}

	private void insertDateMessageIfNeeded(DataTransferContainer dtc, List<ChatMessageDTO> store) {
		ChatMessageDTO latestChatMessage = getLatestChatMessageByRoomId(dtc.getChatMessage().getRoomId());
		Instant latestChatMessageDay = latestChatMessage.getCreatedAt().truncatedTo(ChronoUnit.DAYS);
		Instant today = Instant.now().truncatedTo(ChronoUnit.DAYS);

		if (latestChatMessageDay.isBefore(today)) {
			ChatMessage dateMessage = buildChatMessage(Constants.CHAT_MESSAGE_DATE_TYPE,
					Instant.now().truncatedTo(ChronoUnit.DAYS).toString(), dtc.getChatMessage().getRoomId(),
					dtc.getUnseenChatMessageIds(), true, 0);
			this.chatMessageService.saveChatMessage(dateMessage);
			store.add(ChatMessageMapper.map(dateMessage));
		}
	}

	public void processChatMessageFromUser(DataTransferContainer transferMessage) {
		ChatMessageDTO receivedMessage = transferMessage.getChatMessage();
		List<ChatMessageDTO> responseMessages = new ArrayList<ChatMessageDTO>();

		insertDateMessageIfNeeded(transferMessage, responseMessages);

		Optional<ChatMessageDTO> chatMessageToBeShared = this.chatMessageService
				.saveChatMessage(buildChatMessage(receivedMessage.getFromId(), receivedMessage.getBody(),
						receivedMessage.getRoomId(), transferMessage.getUnseenChatMessageIds(), false, 100))
				.map(ChatMessageMapper::map);
		if (chatMessageToBeShared.isPresent()) {
			DataTransferContainer response = new DataTransferContainer();
			response.setFunction("chat-message");
			response.setChatRoomId(receivedMessage.getRoomId());
			responseMessages.add(chatMessageToBeShared.get());

			response.setChatMessages(responseMessages);

			transferMessage.getUnseenChatMessageIds().stream().filter(userId -> onlineUsers.isOnline(userId))
					.forEach(userId -> notifyClient(userId, response));
		}
	}

	public boolean updateUnseenChatMessages(DataTransferContainer transferMessage) {

		for (UUID chatMessageId : transferMessage.getUnseenChatMessageIds()) {
			ChatMessage chatMessage = chatMessageService.getChatMessage(chatMessageId);
			chatMessage
					.setNotSeenBy(chatMessage.getNotSeenBy().replace(transferMessage.getFrom().getId().toString(), ""));
			chatMessageService.saveChatMessage(chatMessage);
		}

		return true;
	}

	private void notifyClient(UUID userId, DataTransferContainer response) {
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

	private ChatMessage buildChatMessage(String fromId, String body, UUID chatRoomId, List<UUID> userIds,
			boolean randomizeId, int timeOffset) {
		ChatMessage message = new ChatMessage();
		message.setFromId(randomizeId ? fromId + Instant.now().toEpochMilli() : fromId);
		message.setRoomId(chatRoomId);
		message.setBody(body);
		message.setCreatedAt(Instant.now().plusMillis(timeOffset));
		message.setNotSeenBy(convertToNotSeenByString(
				userIds.stream().filter(userId -> !fromId.equals(userId)).collect(Collectors.toList())));
		// message.setNotSeenBy(userIds);
		return message;
	}

	public void processDisconnectFromClient(DataTransferContainer transferMessage) {
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

	public Contact updateUserProfile(UUID userId, DataTransferContainer transferMessage) {
		Optional<User> user_ = userService.getUserById(userId);

		if (user_.isPresent()) {
			User user = user_.get();
			user.setInfo(transferMessage.getFrom().getInfo());
			user.setIconUrl(transferMessage.getFrom().getIconUrl());
			user.setName(transferMessage.getFrom().getName());
			userService.updateUser(user);

			Contact updatedContact = UserMapper.reduce(user);
		  /*DataTransferContainer dtc = new DataTransferContainer();
			dtc.setFunction(Constants.TM_FUNCTION_UPDATE_SINGLE_USER_PROFILE);
			dtc.setFrom(updatedContact);
			notifyUsersByIdIfOnline(user.getContacts(), dtc);*/
			
			return updatedContact;
		}
		return null;
	}

	public ChatRoomDTO updateChatRoomProfile(UUID roomId, DataTransferContainer transferMessage) {

		Optional<ChatRoom> chatRoom_ = chatRoomService.getRoomById(roomId);

		if (chatRoom_.isPresent()) {
			ChatRoom chatRoom = chatRoom_.get();
			chatRoom.setTitle(transferMessage.getChatRoom().getTitle());
			chatRoom.setIconUrl(transferMessage.getChatRoom().getIconUrl());
			chatRoom.setDescription(transferMessage.getChatRoom().getDescription());
			chatRoomService.updateChatRoom(chatRoom);

			ChatRoomDTO updatedChatRoomDTO = ChatRoomMapper.map(chatRoom);
			DataTransferContainer dtc = new DataTransferContainer();
			dtc.setFunction(Constants.TM_FUNCTION_UPDATE_SINGLE_GROUP_PROFILE);
			dtc.setChatRoom(updatedChatRoomDTO);
			notifyUsersByIdIfOnline(chatRoom.getUserIds(), dtc);
			return updatedChatRoomDTO;
		}
		return null;
	}

	/* DEBUG */

	static int countGeneratedUsers = 0;
	List<String> userNamesList;

	private static List<String> toList() {
		String[] userNamesArr = { "default", "HusterHihi", "goran", "ester", "anna", "sven", "tom", "markus" };

		List<String> temp = new ArrayList<String>();
		for (int i = 0; i < userNamesArr.length; i++) {
			temp.add(userNamesArr[i]);
		}
		return temp;
	}

	public void createUsers(int numOfUsers) {
		int rest = Math.min(numOfUsers, userNamesList.size() - countGeneratedUsers);
		for (int i = 0; i < rest; i++) {
			Credentials creds = new Credentials();
			creds.setPassword("123");
			creds.setUsername(userNamesList.get(countGeneratedUsers));
			registerUser(creds);
			countGeneratedUsers++;
		}
	}

	public String displayGeneratedUsers() {

		String html = "<table>" + "<tr><td> Name </td><td> angelegt? </td></tr>";

		for (int i = 0; i < userNamesList.size(); i++) {
			String tdName = "<td>" + userNamesList.get(i) + "</td>";
			String tdStatus = "";
			if (i + 1 <= countGeneratedUsers) {
				tdStatus = "<td>" + "ja" + "</td>";
			} else {
				tdStatus = "<td>" + "-" + "</td>";
			}
			String row = "<tr>" + tdName + tdStatus + "</tr>";
			html += row;
		}

		return html + "</table>";
	}

	public List<Contact> getContactsWithOnlineStatus(UUID userId) {
		List<Contact> contacts = getContactsByUserId(userId);
		contacts.forEach(contact -> {
			if (onlineUsers.isOnline(contact.getId())) {
				contact.setOnline(true);
			} else {
				contact.setOnline(false);
			}
		});
		return contacts;
	}

}
