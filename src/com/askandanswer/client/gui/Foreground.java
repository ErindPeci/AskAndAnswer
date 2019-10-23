package com.askandanswer.client.gui;

import com.askandanswer.client.model.Utente;

public interface Foreground {
	/**
	 * Aggiorna il foreground
	 * @param sessionUser		utente loggato
	 */
	void onUpdateForeground(Utente actualUser);
	
	/**
	 * Aggiorna la label degli errori
	 * @param txt		testo da scrivere
	 */
	void updateErrorLabel(String txt);
	
	/**
	 * Aggiorna la label dei successi
	 * @param txt		testo da scrivere
	 */
	void updateSuccessLabel(String txt);
	
}
