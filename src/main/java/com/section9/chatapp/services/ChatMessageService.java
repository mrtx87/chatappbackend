package com.section9.chatapp.services;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.section9.chatapp.dtos.ChatMessageDTO;
import com.section9.chatapp.entities.ChatMessage;
import com.section9.chatapp.mapper.ChatMessageMapper;
import com.section9.chatapp.repos.ChatMessageRepository;

@Service
public class ChatMessageService {

	@Autowired
	ChatMessageRepository chatMessageRepository;

	public ChatMessageService() {
	}

	public Optional<ChatMessage> saveChatMessage(ChatMessage message) {
		ChatMessage savedMessage = chatMessageRepository.save(message);
		return Optional.of(savedMessage);
	}

	public ChatMessage getChatMessage(UUID chatMessageId) {
		return chatMessageRepository.getOne(chatMessageId);
	}

	public List<ChatMessage> getAllChatMessagesDesc(UUID roomId) {
		// Page<ChatMessage> messages = chatMessageRepository.findByRoomId(roomId, );
		return chatMessageRepository.getAllChatMessagesDesc(roomId);
	}

	public List<ChatMessage> getAllChatMessagesAsc(UUID roomId) {
		// Page<ChatMessage> messages = chatMessageRepository.findByRoomId(roomId, );
		return chatMessageRepository.getAllChatMessagesAsc(roomId);
	}

	private void removeMessageById(UUID messageId) {
		chatMessageRepository.deleteById(messageId);
	}

	private void removeMessage(ChatMessage message) {
		chatMessageRepository.delete(message);
	}

	public void removeChatMessagesByRoomId(UUID roomId) {
		List<ChatMessage> messagesOfRoom = chatMessageRepository.getAllChatMessagesAsc(roomId);
		messagesOfRoom.stream().forEach(m -> removeMessage(m));
	}

	public ChatMessageDTO getLatestChatMessageByRoomId(UUID roomId) {
		ChatMessage chatMessage = chatMessageRepository.getLatestChatMessageByRoomId(roomId);
		if (chatMessage != null) {
			return ChatMessageMapper.map(chatMessage);
		}
		return null;
	}

}
