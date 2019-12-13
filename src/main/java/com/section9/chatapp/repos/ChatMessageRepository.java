package com.section9.chatapp.repos;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.section9.chatapp.entities.ChatMessage;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, UUID> {

	@Query(value = "SELECT * FROM CHAT_MESSAGE m" + " WHERE m.room_id = :roomId" + " ORDER BY CREATED_AT ASC"
			+ " LIMIT :limit", nativeQuery = true)
	public List<ChatMessage> getChatMessagesByRoomId(@Param("roomId") UUID roomId, @Param("limit") int limit);

	@Query(value = "SELECT * FROM CHAT_MESSAGE m" + " WHERE m.room_id = :roomId"
			+ " ORDER BY CREATED_AT DESC", nativeQuery = true)
	public List<ChatMessage> getAllChatMessagesDesc(@Param("roomId") UUID roomId);
	
	@Query(value = "SELECT * FROM CHAT_MESSAGE m" + " WHERE m.room_id = :roomId"
			+ " ORDER BY CREATED_AT ASC", nativeQuery = true)
	public List<ChatMessage> getAllChatMessagesAsc(@Param("roomId") UUID roomId);

	@Query(value = "SELECT * FROM CHAT_MESSAGE m" + " WHERE m.room_id = :roomId" + " ORDER BY CREATED_AT ASC"
			+ " LIMIT 1", nativeQuery = true)
	public ChatMessage getLatestChatMessageByRoomId(@Param("roomId") UUID roomId);

}
