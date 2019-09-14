package com.section9.chatapp.dtos;

import java.util.List;
import java.util.UUID;

import com.section9.chatapp.entities.Contact;


public class UserDTO extends Contact{

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


}
