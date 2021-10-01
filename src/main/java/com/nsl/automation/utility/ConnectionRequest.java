package com.nsl.automation.utility;

public class ConnectionRequest {
	private String roomId;
	private String token;
	
	public ConnectionRequest() {}
	
	public ConnectionRequest(String token, String roomId) {
		this.token = token;
		this.roomId = roomId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
}
