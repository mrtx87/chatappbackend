package com.section9.chatapp.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ldap.embedded.EmbeddedLdapProperties.Credential;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.section9.chatapp.dtos.UserDTO;
import com.section9.chatapp.entities.ChatRoom;
import com.section9.chatapp.entities.User;
import com.section9.chatapp.repos.Credentials;
import com.section9.chatapp.services.ChatService;


@org.springframework.web.bind.annotation.RestController
@CrossOrigin(origins = { "http://localhost:4200","https://localhost:4200" })
public class RestController {

	@Autowired
	ChatService chatService;

	/*
	 * @GetMapping("/health/{secretkey}")
	 * 
	 * @CrossOrigin(origins = "http://localhost:4200") public String
	 * getHealthPage(@PathVariable("secretkey") String key) { String healthPage =
	 * syncService.getHealthPage(key); if(healthPage != null) { return healthPage; }
	 * return "error - not available"; }
	 */

	@GetMapping(path = { "/data/users" })
	public List<User> getUsers() {
		return null; //chatService.getUsers();
	}

	@GetMapping(path = { "/data/rooms" })
	public List<ChatRoom> getRooms() {
		//return chatService.getRooms();
		return null;
	}

	@GetMapping(path = { "/data/adduser" })
	public void addUser() {
		//chatService.addUser();
	}

	@GetMapping(path = { "/data/addroom" })
	public void addRoom() {
		//chatService.addRoom();
	}

	/*
	 * @PostMapping("/room/{raumId}/playlist")
	 * 
	 * @CrossOrigin(origins = "http://localhost:4200") public
	 * ResponseEntity<Message> importPlaylist(@PathVariable("raumId") String
	 * raumId, @RequestBody ImportedPlaylist importedPlaylist) {
	 * if(syncService.importPlaylist(raumId, importedPlaylist)) { return
	 * ResponseEntity.ok(new Message()); } return
	 * ResponseEntity.badRequest().build(); }
	 */

	@PostMapping(path = { "/data/register" })
	public ResponseEntity<UserDTO> registerUser(@RequestBody Credentials credentials) {
		Optional<UserDTO> user = chatService.registerUser(credentials);
		if(user.isPresent()) {
			return ResponseEntity.ok().body(user.get());
		}else {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@PostMapping(path = { "/data/login" })
	public ResponseEntity<UserDTO> loginUser(@RequestBody Credentials credentials) {
		Optional<UserDTO> user = chatService.loginUser(credentials);
		if(user.isPresent()) {
			return ResponseEntity.ok().body(user.get());
		}else {
			return ResponseEntity.badRequest().build();
		}
	}


}
