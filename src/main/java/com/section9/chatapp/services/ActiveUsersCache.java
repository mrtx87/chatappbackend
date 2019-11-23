package com.section9.chatapp.services;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.section9.chatapp.entities.Contact;
import com.section9.chatapp.entities.User;
import com.section9.chatapp.mapper.UserMapper;

public class ActiveUsersCache {

	Map<UUID, Contact> cache;
	Map<UUID, Contact> userByCookie;

	public Map<UUID, Contact> getCache() {
		return cache;
	}

	public void setCache(Map<UUID, Contact> cache) {
		this.cache = cache;
	}
	
	public ActiveUsersCache() {
		cache = new HashMap<>();
		userByCookie = new HashMap<>();
	}
	
	public Contact add(User user) {
		return add(UserMapper.reduce(user));
	}
	
	public Contact add(Contact user) {
		return cache.put(user.getId(), user);
	}
	
	public Contact delete(UUID id) {
		return cache.remove(id);
	}
	
	public boolean exists(UUID id) {
		return cache.containsKey(id);
	}
	
	public boolean exists(Contact contact) {
		return exists(contact.getId());
	}
	
	public boolean exists(User user) {
		return exists(UserMapper.reduce(user));
	}
	
	public UUID associateUserByNewCookie(Contact contact) {
		UUID cookie = UUID.randomUUID();
		userByCookie.put(cookie, contact);
		return cookie;
	}
	
	public Contact getContactByCookie(UUID cookie) {
		return userByCookie.get(cookie);
	}
	
	
	
	
	
}
