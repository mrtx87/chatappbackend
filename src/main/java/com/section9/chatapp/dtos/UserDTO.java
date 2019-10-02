package com.section9.chatapp.dtos;

import java.util.List;
import java.util.UUID;

import com.section9.chatapp.entities.Contact;
import com.section9.chatapp.entities.User;


public class UserDTO {

	private List<UUID> contacts;
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
	

	public List<UUID> getContacts() {
		return contacts;
	}

	public void setContacts(List<UUID> contacts) {
		this.contacts = contacts;
	}

	

}
