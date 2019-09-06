package com.section9.chatapp.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.lang.Nullable;

@Entity
public class ChatRoom {

	@Id
	String id;
	@Nullable
	String Name;
	
}
