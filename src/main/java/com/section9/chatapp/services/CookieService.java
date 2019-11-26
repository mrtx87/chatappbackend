package com.section9.chatapp.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.section9.chatapp.entities.Cookie;
import com.section9.chatapp.repos.CookieRepository;


@Service
public class CookieService {
	
	@Autowired
	CookieRepository cookieRepository;
	
	public UUID getContactIdByCookieId(UUID cookieId) {
		Cookie cookie = cookieRepository.findByCookieId(cookieId);
		return cookie.getUserId();
	}
	
	public List<Cookie> getCookiesByContactId(UUID contactId) {
		return cookieRepository.findByContactId(contactId);
				
	}
	
	public boolean hasCookie(UUID id) {
		List<Cookie> cookies = getCookiesByContactId(id);
		for(Cookie cookie : cookies) {
			if(cookie.getUserId().equals(id)) {
				return true;
			}
		}
		return false;
	}

	
	
	public Cookie saveCookie(Cookie cookie) {
		return cookieRepository.save(cookie);
	}
	
}
