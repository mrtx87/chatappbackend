package com.section9.chatapp.repos;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.section9.chatapp.entities.ChatMessage;
import com.section9.chatapp.entities.ChatRoom;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, UUID>{

}
