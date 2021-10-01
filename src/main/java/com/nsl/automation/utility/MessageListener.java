package com.nsl.automation.utility;

public interface MessageListener {
	public void onconnect(ConnectionRequest connectionResponse);
	public void onRegister(String response);
	public void onMessage(String response);
	public boolean isConnected();
}
