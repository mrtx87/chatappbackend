package com.section9.chatapp.entities;

import java.time.Instant;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class ChatMessage {

	@Id
	@GeneratedValue
	UUID id;
	@NotNull
	UUID roomId;
	@NotNull
	String fromId;
	String body;
	@NotNull
	Instant createdAt;
	String notSeenBy;
	
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


	public String getNotSeenBy() {
		return notSeenBy;
	}

	public void setNotSeenBy(String notSeenBy) {
		this.notSeenBy = notSeenBy;
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
