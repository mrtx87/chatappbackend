package com.section9.chatapp.entities;

import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;

import org.springframework.lang.Nullable;

@Entity
public class ChatRoom {

	@Id
	@GeneratedValue
	UUID id;
	@Nullable
	String title;
	
	@ElementCollection
	List<UUID> userIds;
	
	@Nullable
	@Lob 
	@Column(length=1024)
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

	public List<UUID> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<UUID> users) {
		this.userIds = users;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
