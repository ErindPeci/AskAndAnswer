package com.askandanswer.client.service;


import com.askandanswer.client.gui.Categorie;
import com.askandanswer.client.gui.Risposte;
import com.askandanswer.client.gui.Users;
import com.askandanswer.client.model.Categoria;
import com.askandanswer.client.model.Domanda;
import com.askandanswer.client.model.Risposta;
import com.askandanswer.client.model.Utente;


public interface ClientInterface {
	
	void login(String nickname, String password);
	
	void registerUtente(Utente user);
	
	void addQuestion(Domanda domanda);
	
	void getDomande();
	
	void getSessionUser();

	void exitSession();
	
	void getCategorie();

	void getRisposte(Risposte risposte, int id);

	void salvaRisposta(Risposte val, Risposta risposta);

	void getUsersList(Users pUsersGui);

	void editUser(Users userGUI,String username);

	void eliminaDomanda(Long id);

	void votaRisposta(Risposte risposteIN, Integer id, int voto, int idDom);

	void salvaCategoria(Categorie catGui, Categoria cat);

	void eliminaCategoria(Categorie catGui, String nome);

	void editCategoria(Categorie catGui, String nome, String newName);
	
}
