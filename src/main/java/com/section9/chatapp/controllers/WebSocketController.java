package com.section9.chatapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
 

@Controller
public class WebSocketController {

	private final SimpMessagingTemplate messageService;

	@Autowired
	public WebSocketController(final SimpMessagingTemplate template) {
		this.messageService = template;
	}

	
}
