package com.section9.chatapp.services;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.section9.chatapp.entities.Contact;
import com.section9.chatapp.entities.Cookie;
import com.section9.chatapp.entities.User;
import com.section9.chatapp.mapper.UserMapper;

@Service
public class OnlineUsers {

	@Autowired
	CookieService cookieService;
	
	
	
	Map<UUID, Contact> cache;
	Map<UUID, Contact> userByCookie;

	public Map<UUID, Contact> getCache() {
		return cache;
	}

	public void setCache(Map<UUID, Contact> cache) {
		this.cache = cache;
	}
	
	public OnlineUsers() {
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
	
	public boolean isOnline(UUID id) {
		return cache.containsKey(id);
	}
	
	public boolean isOnline(Contact contact) {
		return isOnline(contact.getId());
	}
	
	public boolean isOnline(User user) {
		return isOnline(user.getId());
	}
	
	public UUID associateUserByNewCookie(Contact contact) {
		UUID cookieId = UUID.randomUUID();
		Cookie cookie = new Cookie();
		cookie.setCookieId(cookieId);
		cookie.setUserId(contact.getId());
		if(cookieService.saveCookie(cookie) != null) {
			return cookieId;
		}
		return null;
	}
	
	public UUID getContactIdByCookieId(UUID cookieId) {
		return cookieService.getContactIdByCookieId(cookieId);
	}
	
	
	
	
	
}
