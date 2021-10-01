package com.nsl.automation.utility;

public class MessageListenerImpl implements MessageListener{
	boolean connected = false; 
	@Override
	public void onconnect(ConnectionRequest request) {
		connected = true;	
	}

	@Override
	public void onRegister(String response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMessage(String response) {
		// TODO Auto-generated method stub
		
	}
	
	public boolean isConnected() {
		return connected;
	}

}
