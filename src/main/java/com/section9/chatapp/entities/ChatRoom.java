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
	@Nullable
	String name;
	@Nullable

	@ElementCollection
	List<ChatMessage> chatMessages;

	@ElementCollection
	List<UUID> users;
	@Nullable
	String iconUrl;

	public ChatRoom() {
		// TODO Auto-generated constructor stub
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
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
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ChatMessage> getChatMessages() {
		return chatMessages;
	}

	public void setChatMessages(List<ChatMessage> chatMessages) {
		this.chatMessages = chatMessages;
	}

}
