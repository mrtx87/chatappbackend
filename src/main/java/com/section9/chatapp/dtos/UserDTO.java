package com.section9.chatapp.dtos;

import java.util.List;
import java.util.UUID;

public class UserDTO {

	private List<UUID> contacts;
	private List<UUID> chatRooms;

	private UUID id;
	private String name;
	private String iconUrl;
	private String info;

	public UserDTO() {
		// TODO Auto-generated constructor stub
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

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public List<UUID> getChatRooms() {
		return chatRooms;
	}

	public void setChatRooms(List<UUID> chatRooms) {
		this.chatRooms = chatRooms;
	}

	public List<UUID> getContacts() {
		return contacts;
	}

	public void setContacts(List<UUID> contacts) {
		this.contacts = contacts;
	}

}
