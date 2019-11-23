package com.section9.chatapp.repos;

import java.util.UUID;

public class Credentials {
	private String username;
	private String password;
	private UUID cookie;
	
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public UUID getCookie() {
		return cookie;
	}
	public void setCookie(UUID cookie) {
		this.cookie = cookie;
	}
	
}
