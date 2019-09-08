package com.section9.chatapp.services;


import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.section9.chatapp.entities.ChatRoom;
import com.section9.chatapp.entities.User;
import com.section9.chatapp.repos.ChatRoomRepository;
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
	
	
	public List<User> getAllRalfs() {

		return userRepository.findAll().stream().filter(u -> u.getName().equals("Ralf")).collect(Collectors.toList());
	}
	
	public User getUser() {
		
		Optional<User> user = userRepository.findById(UUID.randomUUID());
		if(user.isPresent()) {
			return user.get();
		}else {
			return new User();
		}
	}
	
	int index = 0;
	public void addUser() {
		User user = new User();
		user.setInfo("" + index);
		user.setName("test" + index);
		user.setKey(UUID.randomUUID().toString());

		index +=1;
		userRepository.save(user);
	}

	public boolean existsUserByName(String username) {
		return userRepository.findAll().stream().filter(u -> username.equals(u.getName())).collect(Collectors.toList()).size() > 0;
	}

	public User createUser(User user) {
		return userRepository.save(user);
	}
	
}
