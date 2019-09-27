package com.section9.chatapp.dtos;

import java.util.List;
import java.util.UUID;

import com.section9.chatapp.entities.ChatMessage;
import com.section9.chatapp.entities.Contact;

public class TransferMessage {

	private String function;
	private Contact from;
	private UUID chatRoomId;
	private ChatMessage chatMessage;
	private ChatRoomDTO chatRoom;
	private List<Contact> contactsList;
	public String getFunction() {
		return function;
	}
	public void setFunction(String function) {
		this.function = function;
	}
	public Contact getFrom() {
		return from;
	}
	public void setFrom(Contact from) {
		this.from = from;
	}
	public UUID getChatroomId() {
		return chatRoomId;
	}
	public void setChatroomId(UUID chatroomId) {
		this.chatRoomId = chatroomId;
	}
	public ChatMessage getChatMessage() {
		return chatMessage;
	}
	public void setChatMessage(ChatMessage chatMessage) {
		this.chatMessage = chatMessage;
	}
	public ChatRoomDTO getChatRoom() {
		return chatRoom;
	}
	public void setChatroom(ChatRoomDTO chatroom) {
		this.chatRoom = chatroom;
	}
	public List<Contact> getContactsList() {
		return contactsList;
	}
	public void setContactsList(List<Contact> contactsList) {
		this.contactsList = contactsList;
	}

	
	
}
