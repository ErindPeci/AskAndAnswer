package com.askandanswer.client.model;


import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class Domanda implements Serializable {
	
	private Integer id = 0;
	private Categoria cat;
	private String text;
	private Utente user;
	private Date data;
	private String[] links;
	
	public Domanda() {}

	public Domanda(Categoria cat, String text, Utente actualUser, String[] links) {
		this.cat = cat;
		this.text = text;
		this.user = actualUser;
		this.data = new Date();
		this.links = links;
	}
	
	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}
	
	/**
	 * @return the user
	 */
	public Utente getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(Utente user) {
		this.user = user;
	}

	/**
	 * @return the data
	 */
	public Date getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(Date data) {
		this.data = data;
	}


	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}


	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the cat
	 */
	public Categoria getCat() {
		return cat;
	}

	/**
	 * @param cat the cat to set
	 */
	public void setCat(Categoria cat) {
		this.cat = cat;
	}
	
	/**
	 * @return the links
	 */
	public String[] getLinks() {
		return links;
	}

	/**
	 * @param links the links to set
	 */
	public void setLinks(String[] links) {
		this.links = links;
	}
}
