package com.section9.chatapp.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.section9.chatapp.dtos.DataTransferContainer;
import com.section9.chatapp.entities.ChatRoom;
import com.section9.chatapp.mapper.ChatRoomMapper;
import com.section9.chatapp.repos.ChatRoomRepository;
import com.section9.chatapp.repos.UserRepository;

@Service
public class ChatRoomService {

	@Autowired
	UserRepository userRepository; // DEBUG

	@Autowired
	ChatRoomRepository chatRoomRepository;

	public ChatRoomService() {
	}

	/*
	 * Nach createRoom() muss im frontend geöffnet werden. Beteiligte(r) user
	 * benachrichtigen
	 */
	public List<ChatRoom> getRooms() {
		return chatRoomRepository.findAll();
	}

	public ChatRoom createRoom(DataTransferContainer transferMessage) {

		return chatRoomRepository.save(ChatRoomMapper.create(transferMessage.getChatRoom()));
	}

	public Optional<ChatRoom> getRoomById(UUID id) {
		return chatRoomRepository.findById(id);
	}

	public void deleteRoomById(UUID roomId) {
		chatRoomRepository.deleteById(roomId);
	}

	public void updateChatRoom(ChatRoom chatRoom) {
		chatRoomRepository.save(chatRoom);
	}

}
