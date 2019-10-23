package com.askandanswer.client.service;

import java.util.ArrayList;

import com.askandanswer.client.model.Categoria;
import com.askandanswer.client.model.Domanda;
import com.askandanswer.client.model.Risposta;
import com.askandanswer.client.model.Utente;
import com.askandanswer.client.model.UtenteBase;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


@RemoteServiceRelativePath("askservice")
public interface StandardService extends RemoteService {
	
	void clearDBUsers();
	
	void clearDBDomande();
	
	void clearDBRisposte();
	
	void clearDBCategorie();
	
	UtenteBase login(String nickname, String password);	
	
	boolean registerUtente(Utente user);
	
	boolean editUtente(String username);
	
	ArrayList<Utente> getUsers();
	
	UtenteBase getUtente(String username);

	ArrayList<Domanda> getDomande();
	
	void saveQuestion(Domanda domanda);
	
	boolean eliminaDomanda(Long id);
	
	void editDomanda(Long id, String newCatName);

	void salvaCategoria(Categoria categoria);

	ArrayList<Categoria> getCategorie();
	
	void eliminaCategoria(String nome);

	void editCategoria(String oldName, String newName);

	ArrayList<Risposta> getRisposte(int id);

	void salvaRisposta(Risposta risposta);

	boolean eliminaRisposta(Long id);

	void votaRisposta(Integer id, int voto);

	UtenteBase getSessionUser();
	
	void exitSessionUser();
}
