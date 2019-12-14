package com.section9.chatapp.services;

public class Constants {
	public static final String SYSTEM_ID = "system";
	public static final String SYSTEM_INIT_ID = "init";
	public static final String CHAT_MESSAGE_DATE_TYPE = "date";
	
	public static final int ADDITIONAL_MESSAGES_SIZE = 30;
	public static final int MESSAGE_BATCH_SIZE = 20;


	// TransferMessage functions
	public static final String TM_FUNCTION_UPDATE_ROOMS_AND_CONTACTS = "update-rooms-and-contacts";
	
	public static final String TM_FUNCTION_UPDATE_SINGLE_USER_PROFILE = "update-single-user-profile";
	public static final String TM_FUNCTION_UPDATE_SINGLE_GROUP_PROFILE = "update-single-group-profile";


	public static final String TM_FUNCTION_LOGIN_AND_COOKIE = "login-and-cookie";

	public static final String TM_FUNCTION_CREATE_ROOM_AND_CONTACT = "create-room";
	public static final String TM_FUNCTION_CREATE_GROUP_ROOM = "create-group-room";
}
