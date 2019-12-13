package com.section9.chatapp.dtos;

import java.util.List;
import java.util.UUID;

import com.section9.chatapp.entities.ChatMessage;
import com.section9.chatapp.entities.Contact;

public class DataTransferContainer {

	private String function;
	private Contact from;
	private UUID chatRoomId;
	private ChatMessageDTO chatMessage;
	private List<ChatMessageDTO> chatMessages;
	private ChatRoomDTO chatRoom;
	private List<Contact> contactsList;
    List<UUID> unseenChatMessageIds; 
    UUID cookie;

	
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
	public ChatMessageDTO getChatMessage() {
		return chatMessage;
	}
	public void setChatMessage(ChatMessageDTO chatMessage) {
		this.chatMessage = chatMessage;
	}

	public List<ChatMessageDTO> getChatMessages() {
		return chatMessages;
	}
	public void setChatMessages(List<ChatMessageDTO> chatMessages) {
		this.chatMessages = chatMessages;
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
	public UUID getChatRoomId() {
		return chatRoomId;
	}
	public void setChatRoomId(UUID chatRoomId) {
		this.chatRoomId = chatRoomId;
	}
	public List<UUID> getUnseenChatMessageIds() {
		return unseenChatMessageIds;
	}
	public void setUnseenChatMessageIds(List<UUID> unseenChatMessageIds) {
		this.unseenChatMessageIds = unseenChatMessageIds;
	}
	public void setChatRoom(ChatRoomDTO chatRoom) {
		this.chatRoom = chatRoom;
	}
	public UUID getCookie() {
		return cookie;
	}
	public void setCookie(UUID cookie) {
		this.cookie = cookie;
	}
	
	

	
	
}
