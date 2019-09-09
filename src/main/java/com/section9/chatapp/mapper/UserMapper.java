package com.section9.chatapp.mapper;

import com.section9.chatapp.dtos.UserDTO;
import com.section9.chatapp.entities.User;

public class UserMapper {
	public static UserDTO map(User origin) {
		UserDTO userDTO = new UserDTO();
		userDTO.setId(origin.getId());
		userDTO.setName(origin.getName());
		userDTO.setInfo(origin.getInfo());
		userDTO.setChatRooms(origin.getChatRooms());
		userDTO.setContacts(origin.getContacts());
		return userDTO;
	}
	
	public static User map(UserDTO origin) {
		User user = new User();
		user.setId(origin.getId());
		user.setName(origin.getName());
		user.setInfo(origin.getInfo());
		user.setChatRooms(origin.getChatRooms());
		user.setContacts(origin.getContacts());
		return user;
	}
}
