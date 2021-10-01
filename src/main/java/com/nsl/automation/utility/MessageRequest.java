package com.nsl.automation.utility;

public class MessageRequest {
	private String roomID;
	private String message;
	private String token;

	public MessageRequest() {
	}

	public MessageRequest(String roomID, String message, String token) {
		this.roomID = roomID;
		this.message = message;
		this.token = token;
	}

	public String getRoomID() {
		return roomID;
	}

	public void setRoomID(String roomID) {
		this.roomID = roomID;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}