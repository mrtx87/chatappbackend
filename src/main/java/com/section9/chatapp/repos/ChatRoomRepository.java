package com.section9.chatapp.repos;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.section9.chatapp.dtos.ChatRoomDTO;
import com.section9.chatapp.entities.ChatRoom;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, UUID>{
	
	@Query(value = "SELECT * FROM CHAT_ROOM cr"
			+ " WHERE cr.userIds LIKE :userId"
			, nativeQuery = true)
	public List<ChatRoom> getRoomsByUserId(@Param("userId") UUID id);

	// TODO
}
