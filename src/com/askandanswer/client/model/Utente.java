package com.askandanswer.client.model;


import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class Utente extends UtenteBase implements Serializable {

	private String email;
	private String nome;
	private String cognome;
	private String sesso;
	private Date dataDiNascita;
	private String luogoDiNascita;
	private String luogoDiResidenza;
	private String [] socialNetwork;
	private boolean giudice;
	
	
	/**
	 * Costruisce un utente vuoto
	 */
	public Utente() {

	}

	/**
	 * Costruisce un utente
	 * @param username	username dell'utente per il login
	 * @param password	password dell'utente per il login
 	 * @param email	email dell'utente		
	 * @param nome	nome reale dell'utente
	 * @param cognome	cognome reale dell'utente
	 * @param sesso		sesso dell'utente	
	 * @param dataDiNascita		data di nascita dell'utente
	 * @param luogoDiNascita	luogo di nascita dell'utente
	 * @param luogoDiResidenza	luogo di residenza dell'utente
	 * @param socialNetwork		uno o più identificativi dell’utente su social network 
	 */
	public Utente(String username, String password, String email, String nome, String cognome, String sesso, 
			Date dataDiNascita, String luogoDiNascita, String luogoDiResidenza, String[] socialNetwork) {
		
		super(username, password, false);
		this.email = email;
		this.nome = nome;
		this.cognome = cognome;
		this.sesso = sesso;
		this.dataDiNascita = dataDiNascita;
		this.luogoDiNascita = luogoDiNascita;
		this.luogoDiResidenza = luogoDiResidenza;
		this.socialNetwork = socialNetwork;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @param nome the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * @return the cognome
	 */
	public String getCognome() {
		return cognome;
	}

	/**
	 * @param cognome the cognome to set
	 */
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	/**
	 * @return the sesso
	 */
	public String getSesso() {
		return sesso;
	}

	/**
	 * @param sesso the sesso to set
	 */
	public void setSesso(String sesso) {
		this.sesso = sesso;
	}

	/**
	 * @return the dataDiNascita
	 */
	public Date getDataDiNascita() {
		return dataDiNascita;
	}

	/**
	 * @param dataDiNascita the dataDiNascita to set
	 */
	public void setDataDiNascita(Date dataDiNascita) {
		this.dataDiNascita = dataDiNascita;
	}

	/**
	 * @return the luogoDiNascita
	 */
	public String getLuogoDiNascita() {
		return luogoDiNascita;
	}

	/**
	 * @param luogoDiNascita the luogoDiNascita to set
	 */
	public void setLuogoDiNascita(String luogoDiNascita) {
		this.luogoDiNascita = luogoDiNascita;
	}

	/**
	 * @return the luogoDiResidenza
	 */
	public String getLuogoDiResidenza() {
		return luogoDiResidenza;
	}

	/**
	 * @param luogoDiResidenza the luogoDiResidenza to set
	 */
	public void setLuogoDiResidenza(String luogoDiResidenza) {
		this.luogoDiResidenza = luogoDiResidenza;
	}

	/**
	 * @return the socialNetwork
	 */
	public String[] getSocialNetwork() {
		return socialNetwork;
	}

	/**
	 * @param socialNetwork the socialNetwork to set
	 */
	public void setSocialNetwork(String[] socialNetwork) {
		this.socialNetwork = socialNetwork;
	}

	/**
	 * @return the giudice
	 */
	public boolean isGiudice() {
		return giudice;
	}

	/**
	 * @param giudice the giudice to set
	 */
	public void setGiudice(boolean giudice) {
		this.giudice = giudice;
	}
	

}
