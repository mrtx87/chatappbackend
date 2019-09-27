package com.section9.chatapp.entities;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

@Entity
public class ChatMessage {

	@Id
	@GeneratedValue
	UUID id;
	@NotNull
	UUID roomId;
	String fromId;
	String body;
	Instant createdAt;
	
	//@OneToMany
	//List<UUID> notSeenBy;

	public ChatMessage() {
	}

	/*
	public List<UUID> getNotSeenBy() {
		return notSeenBy;
	}
	
	public void setNotSeenBy(List<UUID> notSeenBy) {
		this.notSeenBy = notSeenBy;
	}*/

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getRoomId() {
		return roomId;
	}

	public void setRoomId(UUID roomId) {
		this.roomId = roomId;
	}

	public String getFromId() {
		return fromId;
	}

	public void setFromId(String fromId) {
		this.fromId = fromId;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

}
