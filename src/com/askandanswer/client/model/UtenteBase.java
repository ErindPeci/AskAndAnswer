package com.askandanswer.client.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class UtenteBase implements Serializable {
	
	private String username;
	private String password;
	private boolean isAdmin;
	
	
	/**
	 * Costruisce un utente base vuoto
	 */
	public UtenteBase() {

	}
	
	
	/**
	 * @param username		username dell' utente per il login
	 * @param password		password dell'utente per il login
	 * @param isAdmin		l'utente è un admin o no 
	 * */
	public UtenteBase( String username, String password, boolean isAdmin) {
		this.username = username;
		this.password = Utils.MD5(password);
		this.isAdmin = isAdmin;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = Utils.MD5(password);
	}

	/**
	 * @return the isAdmin
	 */
	public boolean isAdmin() {
		return isAdmin;
	}

	/**
	 * @param isAdmin the isAdmin to set
	 */
	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	
}
