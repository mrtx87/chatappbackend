package com.section9.chatapp.dtos;

import java.util.List;
import java.util.UUID;

import javax.persistence.ElementCollection;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.lang.Nullable;


public class ChatRoomDTO {

	UUID id;
	String title;
	String iconUrl;
	List<UUID> userIds;


	public ChatRoomDTO() {
		// TODO Auto-generated constructor stub
	}

	public List<UUID> getUserIds() {
		return userIds;
	}
	
	public void setUserIds(List<UUID> userIds) {
		this.userIds = userIds;
	}
	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
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
