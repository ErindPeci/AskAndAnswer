package com.askandanswer.client.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Admin extends UtenteBase implements Serializable {

	
	/**
	 * Costruisce un admin vuoto
	 */
	public Admin() {

	}
	
	/**
	 * @param username		username dell'admin per il login
	 * @param password		password dell'utente per il login
	 * */
	public Admin(String username, String password) {
		super(username, password, true);
	}

}
