package com.section9.chatapp.entities;

import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.springframework.lang.Nullable;

@Entity
public class User {

	@Id
	@GeneratedValue
	private UUID id;
	@NotNull
	private String key;
	@NotNull
	private String name;
	@NotNull
	private String password;
	@Nullable
	@Lob 
	@Column(length=1024)
	private String iconUrl;

	@Nullable
	private String info;

	@ElementCollection
	List<UUID> contacts;

	@ElementCollection
	List<UUID> chatRooms;


	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public List<UUID> getContacts() {
		return contacts;
	}

	public void setContacts(List<UUID> contacts) {
		this.contacts = contacts;
	}

	public List<UUID> getChatRooms() {
		return chatRooms;
	}

	public void setChatRooms(List<UUID> chatRooms) {
		this.chatRooms = chatRooms;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public User() {
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

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
