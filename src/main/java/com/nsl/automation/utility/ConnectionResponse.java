package com.nsl.automation.utility;

public class ConnectionResponse {
	private String token;
	private String message;
	private String sessionId;
	
	public ConnectionResponse() {}
	
	public ConnectionResponse(String msg, String sId) {
		this.message= msg;
		this.sessionId = sId;
	}

	public String getMessage() {
		return message;
	}

	public String getSessionId() {
		return sessionId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
