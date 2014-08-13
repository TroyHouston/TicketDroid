package com.trout.ticketdroid;

import java.util.HashMap;
import java.util.Locale;

public class Database {

	//Username -> Account
	private HashMap<String,Account> database;

	public Database(){
		database = new HashMap<String,Account>();
	}

	/**
	 * Adds account to the local storage database
	 * @param user
	 * @param password
	 * @return True if adds successfully, else false.
	 */
	public boolean addAccount(String user, String password) {
		if(database.containsKey(user)){
			return false;
		}

		//Check max length?
		//No special characters in pw?


		database.put(user.toLowerCase(Locale.ENGLISH), new Account(user.toLowerCase(Locale.ENGLISH),password));
		return true;

	}

	/**
	 * Checks if the users details that are passed to it exist/valid
	 * @param string
	 * @param string2
	 * @return true if exist/valid, else false
	 */
	public boolean validUser(String user, String password) {
		return database.containsKey(user.toLowerCase(Locale.ENGLISH))
				&& database.get(user.toLowerCase(Locale.ENGLISH)).getPassword().equals(password);
	}



}