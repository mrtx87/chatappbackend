package com.section9.chatapp.dtos;

import java.time.Instant;
import java.util.UUID;

public class ChatMessageDTO {

	UUID id;
	UUID roomId;
	String fromId;
	String body;
	Instant createdAt;
	boolean seen; // TODO still used?
	String notSeenBy;
	String deletedFor;

	




	public ChatMessageDTO() {	}

	public String getDeletedFor() {
		return deletedFor;
	}
	
	public void setDeletedFor(String deletedFor) {
		this.deletedFor = deletedFor;
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

	public boolean isSeen() {
		return seen;
	}

	public void setSeen(boolean seen) {
		this.seen = seen;
	}

	public String getNotSeenBy() {
		return notSeenBy;
	}

	public void setNotSeenBy(String notSeenBy) {
		this.notSeenBy = notSeenBy;
	}
	
}
