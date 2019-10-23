package com.askandanswer.client.service;


import java.util.ArrayList;

import com.askandanswer.client.model.Categoria;
import com.askandanswer.client.model.Domanda;
import com.askandanswer.client.model.Risposta;
import com.askandanswer.client.model.Utente;
import com.google.gwt.user.client.rpc.AsyncCallback;


public interface StandardServiceAsync {
	
	void clearDBUsers(AsyncCallback callback);

	void clearDBDomande(AsyncCallback callback);

	void clearDBRisposte(AsyncCallback callback);

	void clearDBCategorie(AsyncCallback callback);
	
	void login(String nickname, String password, AsyncCallback callback);

	void registerUtente(Utente user, AsyncCallback  callback);

	void getDomande(AsyncCallback<ArrayList<Domanda>> callback);

	void saveQuestion(Domanda domanda, AsyncCallback asyncCallback);

	void getUtente(String username, AsyncCallback callback);

	void getSessionUser(AsyncCallback callback);

	void exitSessionUser(AsyncCallback callback);

	void salvaCategoria(Categoria categoria, AsyncCallback callback);

	void getCategorie(AsyncCallback<ArrayList<Categoria>> callback);

	void salvaRisposta(Risposta risposta, AsyncCallback callback);

	void getRisposte(int id, AsyncCallback<ArrayList<Risposta>> asyncCallback);

	void eliminaDomanda(Long id, AsyncCallback callback);

	void getUsers(AsyncCallback<ArrayList<Utente>> callback);

	void editUtente(String username, AsyncCallback<Boolean> callback);

	void eliminaRisposta(Long id, AsyncCallback<Boolean> callback);

	void votaRisposta(Integer id, int voto, AsyncCallback callback);

	void eliminaCategoria(String nome, AsyncCallback callback);

	void editCategoria(String oldName, String newName, AsyncCallback callback);

	void editDomanda(Long id, String newCatName, AsyncCallback callback);



}
