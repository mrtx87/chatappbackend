package com.section9.chatapp.dtos;

import java.util.List;
import java.util.UUID;


public class UserDTO {

	private UUID id;
	private String name;
	private String info;
	private List<UUID> chatRooms;
	private List<UUID> contacts;
	
	public List<UUID> getContacts() {
		return contacts;
	}

	public void setContacts(List<UUID> contacts) {
		this.contacts = contacts;
	}

	public UserDTO() {
		// TODO Auto-generated constructor stub
	}

	public List<UUID> getChatRooms() {
		return chatRooms;
	}

	public void setChatRooms(List<UUID> chatRooms) {
		this.chatRooms = chatRooms;
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

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

}
