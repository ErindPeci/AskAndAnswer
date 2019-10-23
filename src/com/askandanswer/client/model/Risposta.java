package com.askandanswer.client.model;



import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class Risposta implements Serializable {

	private Integer id;
	private String text;
	private String username;
	private Date data;
	private int domandaID;
	private int voto;
	private String[] links;
	
	public Risposta()  {}
	
	public Risposta(String text, Utente actualUser, int domandaID, String[] links) {
		this.text = text;
		this.username = actualUser.getUsername();
		this.domandaID = domandaID;
		this.data = new Date();
		this.voto = -1;
		this.links = links;
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
	 * @return the user
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param user the user to set
	 */
	public void setUsername(String user) {
		this.username = user;
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
	 * @return the domandaID
	 */
	public int getDomandaID() {
		return domandaID;
	}

	/**
	 * @param domandaID the domandaID to set
	 */
	public void setDomandaID(int domandaID) {
		this.domandaID = domandaID;
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
	 * @return the voto
	 */
	public int getVoto() {
		return voto;
	}

	/**
	 * @param voto the voto to set
	 */
	public void setVoto(int voto) {
		this.voto = voto;
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
