package com.section9.chatapp.entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.lang.Nullable;

@Entity
public class User {

	@Id
	public String id;
	@Nullable
	public String name;
	@Nullable
	public List<ChatRoom> chatRooms;
}
