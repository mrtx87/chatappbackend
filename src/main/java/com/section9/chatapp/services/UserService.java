package com.section9.chatapp.services;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.section9.chatapp.entities.User;
import com.section9.chatapp.repos.ChatRoomRepository;
import com.section9.chatapp.repos.Credentials;
import com.section9.chatapp.repos.UserRepository;

@Service
public class UserService {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ChatRoomRepository chatRoomRepository; //DEBUG
	
	public UserService() {
	}

	public List<User> getUsers() {
		
		return userRepository.findAll();
	}
	
	
	public List<User> getByName(String username) {

		return userRepository.findByName(username);
	}
	

	public boolean existsUserByName(String username) {
		return 0 < userRepository.existsByName(username); //findAll().stream().filter(u -> username.equals(u.getName())).collect(Collectors.toList()).size() > 0;
	}
	
	public User getUserByLogin(Credentials credentials) {
		return userRepository.getUserByLogin(credentials.getUsername(), credentials.getPassword());
	}

	public User createUser(User user) {
		return userRepository.save(user);
	}
	
	public User updateUser(User user) {
		return userRepository.save(user);
	}

	public Optional<List<User>> searchContact(String id, String query) {
		return userRepository.searchContact(query.toLowerCase());
	}
	
	public Optional<User> getUserById(UUID id) {
		return userRepository.findById(id);
	}
	
}
