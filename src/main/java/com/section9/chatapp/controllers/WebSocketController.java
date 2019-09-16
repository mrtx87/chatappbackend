package com.section9.chatapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.section9.chatapp.dtos.TransferMessage;
import com.section9.chatapp.services.ChatService;
 

@Controller
@CrossOrigin(origins = { "http://localhost:4200","https://localhost:4200" })
public class WebSocketController {

	private final ChatService chatService;

	@Autowired
	public WebSocketController(final ChatService chatService) {
		this.chatService = chatService;
	}

	@MessageMapping("/send/chat-message")
	public void onConnectClient(@Nullable final TransferMessage transferMessage) {
		chatService.connectClient(transferMessage);
	}
	
}
