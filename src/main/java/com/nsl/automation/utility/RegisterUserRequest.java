package com.nsl.automation.utility;

public class RegisterUserRequest {
	private String userName;
	private String email;
	private String department;
	private String token;
	
	public RegisterUserRequest() {}
	
	public RegisterUserRequest(String userName, String email, String department, String token) {
		this.userName = userName;
		this.email = email;
		this.department = department;
		this.token = token;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
