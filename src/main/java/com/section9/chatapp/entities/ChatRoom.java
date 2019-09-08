package com.section9.chatapp.entities;

import java.util.List;
import java.util.UUID;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.springframework.lang.Nullable;

@Entity
public class ChatRoom {

	@Id
	@GeneratedValue
	UUID id;
	@NotNull
	String key;
	@Nullable
	String Name;
	@Nullable

	@ElementCollection
	List<ChatMessage> chatMessages;

	@ElementCollection
	List<UUID> users;

	public ChatRoom() {
		// TODO Auto-generated constructor stub
	}

	public List<UUID> getUsers() {
		return users;
	}

	public void setUsers(List<UUID> users) {
		this.users = users;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public List<ChatMessage> getChatMessages() {
		return chatMessages;
	}

	public void setChatMessages(List<ChatMessage> chatMessages) {
		this.chatMessages = chatMessages;
	}

}
