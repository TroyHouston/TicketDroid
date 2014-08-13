package com.trout.ticketdroid;

public class Account {
	private String user;
	private String password;

	public Account(String user, String password){
		this.user = user;
		this.password = password;
	}


	public String getPassword(){
		return password;
	}
}
