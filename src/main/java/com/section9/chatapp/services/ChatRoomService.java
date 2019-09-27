package com.section9.chatapp.services;


import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.section9.chatapp.dtos.ChatRoomDTO;
import com.section9.chatapp.dtos.TransferMessage;
import com.section9.chatapp.dtos.UserDTO;
import com.section9.chatapp.entities.ChatMessage;
import com.section9.chatapp.entities.ChatRoom;
import com.section9.chatapp.entities.Contact;
import com.section9.chatapp.entities.User;
import com.section9.chatapp.mapper.ChatRoomMapper;
import com.section9.chatapp.repos.ChatRoomRepository;
import com.section9.chatapp.repos.UserRepository;

@Service
public class ChatRoomService {
	
	@Autowired
	UserRepository userRepository; //DEBUG
	
	@Autowired
	ChatRoomRepository chatRoomRepository;
	
	public ChatRoomService() {
	}

	
	 /*
	  * Nach createRoom() muss im frontend geöffnet werden. Beteiligte(r) user benachrichtigen
	  * 
	  * 
	  * 
	  */
	public List<ChatRoom> getRooms() {
		return chatRoomRepository.findAll();
	}

	public ChatRoom createRoom(TransferMessage transferMessage) {	

		return chatRoomRepository.save(ChatRoomMapper.create(transferMessage.getChatRoom()));	
	}

	public Optional<List<ChatRoomDTO>> getRoomsByUserId(UUID id) {

		return Optional.of(
				chatRoomRepository
				.findAll().stream()
				.filter(chatRoom -> chatRoom.getUserIds()
						.contains(id)).map(ChatRoomMapper::map)
				.collect(Collectors.toList()));
	}
	
}
