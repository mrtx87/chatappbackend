package com.section9.chatapp.controllers;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.section9.chatapp.dtos.ChatMessageDTO;
import com.section9.chatapp.dtos.ChatRoomDTO;
import com.section9.chatapp.dtos.DataTransferContainer;
import com.section9.chatapp.dtos.UserDTO;
import com.section9.chatapp.entities.Contact;
import com.section9.chatapp.repos.Credentials;
import com.section9.chatapp.services.ChatMessageService;
import com.section9.chatapp.services.ChatService;

@org.springframework.web.bind.annotation.RestController
@CrossOrigin(origins = {"http://localhost",  "http://localhost:4200", "https://localhost:4200" })
public class RestController {

	@Autowired
	ChatService chatService;

	@PostMapping(path = { "/data/register" })
	public ResponseEntity<UserDTO> registerUser(@RequestBody Credentials credentials) {
		Optional<UserDTO> user = chatService.registerUser(credentials);
		if (user.isPresent()) {
			return ResponseEntity.ok().body(user.get());
		} else {
			return ResponseEntity.badRequest().build();
		}
	}

	@PostMapping(path = { "/data/login" })
	public ResponseEntity<UserDTO> loginUser(@RequestBody Credentials credentials) {
		Optional<UserDTO> user = chatService.loginUser(credentials);
		if (user.isPresent()) {
			return ResponseEntity.ok().body(user.get());
		} else {
			return ResponseEntity.badRequest().build();
		}
	}

	@PostMapping(path = { "/data/login-by-cookie" })
	public ResponseEntity<Contact> loginUserByCookie(@RequestBody Credentials credentials) {
		Optional<Contact> user = chatService.loginUserByCookie(credentials);
		if (user.isPresent()) {
			return ResponseEntity.ok().body(user.get());
		} else {
			return ResponseEntity.badRequest().build();
		}
	}

	@PostMapping(path = { "/data/create-room" })
	public ResponseEntity<ChatRoomDTO> createRoom(@RequestBody DataTransferContainer transferMessage) {
		Optional<ChatRoomDTO> chatRoomDTO = chatService.createRoom(transferMessage);
		if (chatRoomDTO.isPresent()) {
			return ResponseEntity.ok().body(chatRoomDTO.get());
		} else {
			return ResponseEntity.badRequest().build();
		}
	}

	@PostMapping(path = { "/data/remove-contact" })
	public ResponseEntity<Object> removeContact(@RequestBody DataTransferContainer transferMessage) {
		chatService.processContactRemoving(transferMessage);
		return ResponseEntity.ok().build();
	}

	@PostMapping(path = { "/data/remove-chatroom" })
	public ResponseEntity<List<Contact>> removeChatRoom(@RequestBody DataTransferContainer transferMessage) {
		chatService.processChatRoomRemoving(transferMessage);
		return ResponseEntity.ok().build();
	}

	@PostMapping(path = { "/data/update-unseen-messages" })
	public ResponseEntity<ChatRoomDTO> updateUnseenChatMessages(@RequestBody DataTransferContainer transferMessage) {
		boolean success = chatService.updateUnseenChatMessages(transferMessage);
		if (success) {
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.badRequest().build();
		}
	}

	@PostMapping(path = { "/data/update/userId/{userId}" })
	public ResponseEntity<Contact> updateUserProfile(@PathVariable("userId") UUID userId,
			@RequestBody DataTransferContainer transferMessage) {
		Contact contact = chatService.updateUserProfile(userId, transferMessage);
		if (contact != null) {
			return ResponseEntity.ok().body(contact);
		} else {
			return ResponseEntity.badRequest().build();
		}
	}

	@PostMapping(path = { "/data/update/roomId/{roomId}" })
	public ResponseEntity<ChatRoomDTO> updateChatRoomProfile(@PathVariable("roomId") UUID roomId,
			@RequestBody DataTransferContainer transferMessage) {
		ChatRoomDTO chatRoomDTO = chatService.updateChatRoomProfile(roomId, transferMessage);
		if (chatRoomDTO != null) {
			return ResponseEntity.ok().body(chatRoomDTO);
		} else {
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping(path = { "/data/userId/{id}/users/{query}" })
	public ResponseEntity<List<Contact>> searchContact(@PathVariable("id") String id,
			@PathVariable("query") String query) {
		Optional<List<Contact>> users = chatService.searchContact(id, query);
		if (users.isPresent()) {
			return ResponseEntity.ok().body(users.get());
		} else {
			return ResponseEntity.ok().body(new ArrayList<Contact>());
		}
	}

	@GetMapping(path = { "/data/userId/{id}/rooms" })
	public ResponseEntity<List<ChatRoomDTO>> getRoomsByUserId(@PathVariable("id") UUID id) {
		Optional<List<ChatRoomDTO>> chatRooms = chatService.getRoomsByUserId(id);
		if (chatRooms.isPresent()) {
			return ResponseEntity.ok().body(chatRooms.get());
		} else {
			return ResponseEntity.ok().body(new ArrayList<ChatRoomDTO>());
		}

	}

	@GetMapping(path = { "/data/inital-messages/userId/{userId}/roomId/{roomId}" })
	public ResponseEntity<List<ChatMessageDTO>> getInitialChatMessagesByRoomId(@PathVariable("userId") UUID userId,
			@PathVariable("roomId") UUID roomId) {
		List<ChatMessageDTO> chatMessages = chatService.getInitialChatMessages(userId, roomId);
		if (chatMessages != null) {
			return ResponseEntity.ok().body(chatMessages);
		} else {
			return ResponseEntity.ok().build();
		}
	}
	
	@GetMapping(path = { "/data/userId/{userId}/roomId/{roomId}/token/{lastMessageToken}" })
	public ResponseEntity<List<ChatMessageDTO>> getChatMessagesBatchByRoomId(@PathVariable("userId") UUID userId,
			@PathVariable("roomId") UUID roomId, @PathVariable("lastMessageToken") UUID lastMessageToken) {
		List<ChatMessageDTO> chatMessages = chatService.getChatMessagesBatch(userId, roomId, lastMessageToken);
		if (chatMessages != null) {
			return ResponseEntity.ok().body(chatMessages);
		} else {
			return ResponseEntity.ok().build();
		}
	}
	
	

	@GetMapping(path = { "/data/contactId/{contactId}" })
	public ResponseEntity<Contact> getContactById(@PathVariable("contactId") UUID contactId) {
		Contact contact = chatService.getContactById(contactId);
		if (contact != null) {
			return ResponseEntity.ok().body(contact);
		} else {
			return ResponseEntity.ok().body(null);
		}
	}

	@GetMapping(path = { "/data/userId/{userId}/contacts" })
	public ResponseEntity<List<Contact>> getContactsByUserId(@PathVariable("userId") UUID userId) {
		/*
		 * List<Contact> contacts = chatService.getContactsByUserId(userId); if(contacts
		 * != null) { return ResponseEntity.ok().body(contacts); }else { return
		 * ResponseEntity.ok().body(new ArrayList<Contact>()); }
		 */
		return getOnlineStatusOfContacts(userId);

	}

	@GetMapping(path = { "/data/online/status/contacts/userId/{userId}" })
	public ResponseEntity<List<Contact>> getOnlineStatusOfContacts(@PathVariable("userId") UUID userId) {
		List<Contact> contacts = chatService.getContactsWithOnlineStatus(userId);
		if (contacts != null) {
			return ResponseEntity.ok().body(contacts);
		} else {
			return ResponseEntity.badRequest().build();
		}
	}

	/* DEBUG */

	@GetMapping(path = { "/data/create-users/{number}" })
	public ResponseEntity<Object> createUsers(@PathVariable("number") int count) {

		chatService.createUsers(count);
		return ResponseEntity.ok().build();
	}

	@GetMapping(path = { "/data/display-generated-users" })
	public ResponseEntity<String> displayGeneratedUsers() {

		return ResponseEntity.ok().body(chatService.displayGeneratedUsers());
	}

}
