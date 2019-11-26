package com.section9.chatapp.entities;

import java.util.UUID;

import javax.annotation.Generated;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Cookie {
	
	@Id
	@GeneratedValue
	Long id;
	UUID cookieId;
	UUID userId;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public UUID getCookieId() {
		return cookieId;
	}
	public void setCookieId(UUID cookieId) {
		this.cookieId = cookieId;
	}
	public UUID getUserId() {
		return userId;
	}
	public void setUserId(UUID userId) {
		this.userId = userId;
	}
	
	
}
