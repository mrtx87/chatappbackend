package com.section9.chatapp.services;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.section9.chatapp.entities.Contact;
import com.section9.chatapp.entities.User;
import com.section9.chatapp.mapper.UserMapper;

public class ActiveUsersCache {

	Map<UUID, Contact> cache;

	public Map<UUID, Contact> getCache() {
		return cache;
	}

	public void setCache(Map<UUID, Contact> cache) {
		this.cache = cache;
	}
	
	public ActiveUsersCache() {
		cache = new HashMap<>();
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
	
	
	
	
}