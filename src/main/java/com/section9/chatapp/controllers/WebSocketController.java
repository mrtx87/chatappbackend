package com.section9.chatapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

/*
 *   @TODO
 *    FEAT: *    
 *    - switch für privaten modus ob alle gleichberechtig oder nicht   
 *    - öffentliche raum kicken und auf blocklist setzen und cookie setzen bei usern mit userid 
 *    - copy link to clipboard
 *    - controllen für video
 *    - chat schroll down
 *    - playlist fertig bauen (refesh konzept?)
 *    - playlist funktionen implementieren
 *    - löscehn fr jeden playlisteintrag
 *    - playlist funkionalität im backend
 *    - histoey
 *    - make over styling etc
 */

@Controller
public class WebSocketController {

	private final SimpMessagingTemplate messageService;

	@Autowired
	public WebSocketController(final SimpMessagingTemplate template) {
		this.messageService = template;
	}

	
}
