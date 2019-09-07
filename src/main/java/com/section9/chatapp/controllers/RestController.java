package com.section9.chatapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.section9.chatapp.entities.User;
import com.section9.chatapp.services.ChatService;

@org.springframework.web.bind.annotation.RestController

@RequestMapping({"/data"})
@CrossOrigin(origins = "http://localhost:4200")
public class RestController {

	@Autowired
	ChatService chatService;
	
	/*
	@GetMapping("/health/{secretkey}")
	@CrossOrigin(origins = "http://localhost:4200")
	public String getHealthPage(@PathVariable("secretkey") String key) {
		String healthPage = syncService.getHealthPage(key);
		if(healthPage != null) {	
			return healthPage;
		}
		return "error - not available";
	} */
	
	
	@GetMapping(path = {"/user"})
	public User getUser() {
		return chatService.getUser();
	}
	
	@GetMapping(path = {"/adduser"})
	public void addUser() {
		chatService.addUser();
	}

}
