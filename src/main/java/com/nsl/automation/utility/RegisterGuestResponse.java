package com.nsl.automation.utility;

public class RegisterGuestResponse {
	private String username;
	private long timeStamp;
	private String name;
	private String token;
	private String email;
	private long phone;
	private String lastChatId;
	private long lastChatTimestamp;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public long getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public long getPhone() {
		return phone;
	}
	public void setPhone(long phone) {
		this.phone = phone;
	}
	public String getLastChatId() {
		return lastChatId;
	}
	public void setLastChatId(String lastChatId) {
		this.lastChatId = lastChatId;
	}
	public long getLastChatTimestamp() {
		return lastChatTimestamp;
	}
	public void setLastChatTimestamp(long lastChatTimestamp) {
		this.lastChatTimestamp = lastChatTimestamp;
	}
}
