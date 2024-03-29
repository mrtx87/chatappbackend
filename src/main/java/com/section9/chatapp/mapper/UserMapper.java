package com.section9.chatapp.mapper;

import com.section9.chatapp.dtos.UserDTO;
import com.section9.chatapp.entities.Contact;
import com.section9.chatapp.entities.User;

public class UserMapper {
	public static UserDTO map(User origin) {
		UserDTO userDTO = new UserDTO();
		userDTO.setId(origin.getId());
		userDTO.setName(origin.getName());
		userDTO.setInfo(origin.getInfo());
		userDTO.setContacts(origin.getContacts());
		userDTO.setChatRooms(origin.getChatRooms());
		userDTO.setIconUrl(origin.getIconUrl());

		return userDTO;
	}
	
	public static User map(UserDTO origin) {
		User user = new User();
		user.setId(origin.getId());
		user.setName(origin.getName());
		user.setInfo(origin.getInfo());
		user.setIconUrl(origin.getIconUrl());
		return user;
	}
	
	public static Contact reduce(User origin) {
		Contact contact = new Contact();
		contact.setId(origin.getId());
		contact.setName(origin.getName());
		contact.setInfo(origin.getInfo());
		contact.setIconUrl(origin.getIconUrl());
		return contact;
	}
}
