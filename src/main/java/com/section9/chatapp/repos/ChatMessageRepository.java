package com.section9.chatapp.repos;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.section9.chatapp.entities.ChatMessage;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, UUID>{
	
	@Query(value = "SELECT * FROM CHAT_MESSAGE m"
			+ " WHERE m.room_id = :roomId"
			+ " ORDER BY CREATED_AT ASC"
			+ " LIMIT 100", nativeQuery = true)
	public List<ChatMessage> getChatMessagesByRoomId( @Param("roomId") UUID roomId);
	
	@Query(value = "DELETE FROM CHAT_MESSAGE m"
			+ "WHERE m.room_id = :roomId", nativeQuery = true)
	public void removeChatMessagesByRoomId(@Param("roomId") UUID roomId);

}
