package com.askandanswer.server;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import javax.servlet.ServletContext;

import org.mapdb.DB;
import org.mapdb.DBMaker;

import com.askandanswer.client.model.Admin;
import com.askandanswer.client.model.Categoria;
import com.askandanswer.client.model.Domanda;
import com.askandanswer.client.model.Risposta;
import com.askandanswer.client.model.Utente;
import com.askandanswer.client.model.UtenteBase;
import com.askandanswer.client.model.Utils;
import com.askandanswer.client.service.StandardService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class StandardServiceImplementor extends RemoteServiceServlet implements StandardService {

	/*
	 * We store the DB in the servlet context
	 * to implement a poor man's singleton
	 */
	private DB getDB() {
		ServletContext context = this.getServletContext();
		synchronized (context) {
			DB db = (DB)context.getAttribute("DB");
			if(db == null) {
				db = DBMaker.newFileDB(new File("db")).closeOnJvmShutdown().make();
				context.setAttribute("DB", db);
			}
			checkAdminInDB(db);
			checkCategoriaInDB(db);
			return db;
		}
	}

	/**
	 * Controlla che l'admin sia presente nel database, in caso contrario lo aggiunge
	 * 
	 * @param db	database
	 */
	private void checkAdminInDB(DB db) {
		Map<Long, UtenteBase> users = db.getTreeMap("users");
		long hashCode = (long) "admin".hashCode();
		if(!users.containsKey(hashCode))
			users.put(hashCode, new Admin("admin", "admin"));
	}

	// *************** Start user methods ***************
	
	/**
	 * Effettua il login di un utente
	 * 
	 * @param username	username utente
	 * @param password	password per il login
	 * @return 	null o l'UtenteBase in caso di successo
	 */
	@Override
	public UtenteBase login(String username, String password) {
		
		UtenteBase user = getUtente(username);
		if(user != null) {
			if(user.getPassword().equals(Utils.MD5(password))) {
				getThreadLocalRequest().getSession().setAttribute("user", user);
				return user;
			}
			else
				return null;
		} else
			return null;
	}

	/**
	 * Registra un utente nel database
	 * 
	 * @param user	utente da inserire
	 * @return 	true in caso di successo nell'inserimento, false altrimenti
	 */
	@Override
	public boolean registerUtente(Utente user) {
		if(!userExists(user)) {
			DB db = getDB();
			Map<Long, UtenteBase> users = db.getTreeMap("users");
			long hash = (long) user.getUsername().hashCode();
			users.put(hash, user);
			db.commit();
			return true;
		} else
			return false;	
	}
	
	/**
	 * Metodo che restituisce un user dal DB
	 * @param username del utente
	 */
	@Override
	public UtenteBase getUtente(String username) {
		DB db = getDB();
		Map<Long, UtenteBase> users = db.getTreeMap("users");
		long hashCode = (long) username.hashCode();
		if(users.containsKey(hashCode))
			return users.get(hashCode);
		else
			return null; 
	}
	
	/**
	 * Modifica un utente salvato nel database
	 * 
	 * @param username	username dell'utente da modificare
	 * @return 	true in caso di successo della modifica, false altrimenti
	 */
	@Override
	public boolean editUtente(String username) {
		Utente user = (Utente)getUtente(username);
		if(userExists(user)) {
			user.setGiudice(true);
			DB db = getDB();
			Map<Long, UtenteBase> users = db.getTreeMap("users");
			long hash = (long) user.getUsername().hashCode();
			users.put(hash, user);
			db.commit();
			return true;
		} else
			return false;
	}
	
	
	/**
	 * Restituisce la presenza o meno di un utente nel database, controllando username
	 * 
	 * @param checkUser	l'utente di cui si vuole verificare l'esistenza
	 * @return	true se l'elemento esiste nel database, false altrimenti
	 */
	private boolean userExists(Utente checkUser) {
		DB db = getDB();
		Map<Long, UtenteBase> users = db.getTreeMap("users");
		for(Map.Entry<Long, UtenteBase> user : users.entrySet())
			if(user.getValue() instanceof Utente && (((Utente)user.getValue()).getUsername().equals(checkUser.getUsername())))
				return true;
		return false;
	}
	
	/**
	 * Restituisce lista degli utenti nel db
	 */
	@Override
	public ArrayList<Utente> getUsers() {
		DB db = getDB();
		ArrayList<Utente> usersList = new ArrayList<>();
		Map<Long, UtenteBase> users = db.getTreeMap("users");
		for(Map.Entry<Long, UtenteBase> user : users.entrySet())
			if(user.getValue() instanceof Utente)
				usersList.add((Utente) user.getValue());
		return usersList;
	}
	
	// *************** End user methods ***************
	
	
	// *************** Start categorie methods *************** 

	/**
	 * Salva una categoria nel database
	 * 
	 * @param categoria	categoria da salvare
	 */
	@Override
	public void salvaCategoria(Categoria categoria) {
			DB db = getDB();
			Map<Long, Categoria> categorie = db.getTreeMap("categorie");
			long hash = (long) categoria.getNome().hashCode();
			categorie.put(hash, categoria);
			db.commit();
	}
	
	/**
	 * Metodo per eliminare una categoria
	 * @param nome   nome della categoria
	 */
	@Override
	public void eliminaCategoria(String nome) {
		DB db = getDB();
		Map<Long, Categoria> categorie = db.getTreeMap("categorie");
		long hash = nome.hashCode();
		categorie.remove(hash);
		db.commit();
	}
	
	
	/**
	 * Metodo per rinominare una categoria
	 * 
	 * @param oldName   il vecchio nome della categoria
	 * @param newName   il nuovo nome della categoria
	 */
	@Override
	public void editCategoria(String oldName, String newName ) {
		DB db = getDB();
		Map<Long, Categoria> categorie = db.getTreeMap("categorie");
		long oldHash = oldName.hashCode();
		long newHash = newName.hashCode();
		Categoria categoria = categorie.get(oldHash); 
		categoria.setNome(newName);
		categorie.remove(oldHash);
		categorie.put(newHash, categoria);
		db.commit();
	}
	
	/**
	 * Metodo che resituisce le categorie salvate nel database
	 */
	@Override
	public ArrayList<Categoria> getCategorie() {
		DB db = getDB();
		ArrayList<Categoria> categorie = new ArrayList<Categoria>();
		Map<Long, Categoria> list = db.getTreeMap("categorie");
		for(Map.Entry<Long, Categoria> categoria : list.entrySet())
			if(categoria.getValue() instanceof Categoria)
				categorie.add((Categoria)categoria.getValue());
		return categorie;
	}

	/**
	 * Controlla  se le categorie predefinite sia presenti nel database, in caso contrario le aggiunge.
	 *
	 *@param db  database  
	 */
	private void checkCategoriaInDB(DB db ){
		String[] catDef = {"Ambiente", "Animali", "Arte e Cultura","Elettronica e Tecnologia", "Sport", "Svago"};
		Map<Long, Categoria> categorie = db.getTreeMap("categorie");
		for(String nomeCat : catDef) {
			long hashcode = (long)nomeCat.hashCode();
			if(!categorie.containsKey(hashcode))
				categorie.put(hashcode, new Categoria(null ,nomeCat));
		}
	}
	
	// *************** End categorie methods ***************


	
	// *************** Start domande methods ***************
	
	/**
	 * Metodo che restituisce lista delle domande presenti nel db
	 */
	@Override
	public ArrayList<Domanda> getDomande() {
		DB db = getDB();
		ArrayList<Domanda> domande = new ArrayList<Domanda>();
		Map<Long, Domanda> list = db.getTreeMap("domande");
		for(Map.Entry<Long, Domanda> domanda : list.entrySet())
			if(domanda.getValue() instanceof Domanda)
				domande.add((Domanda)domanda.getValue());
		return domande;
	}

	/**
	 * Metodo per salvare una nuova domanda
	 * 
	 * @param domanda   la nuova domanda da salvare
	 */
	@Override
	public void saveQuestion(Domanda domanda) {
		int lastID = getLastID()+1;
		domanda.setId(lastID);
		DB db = getDB();
		Map<Long, Domanda> domande = db.getTreeMap("domande");
		long hash = (long) domanda.getId().hashCode();
		domande.put(hash, domanda);
		db.commit();
	}
	
	/**
	 * Metodo che ritorna l'id dell'ultima domanda aggiunta nel db
	 * 
	 * @return id 	id dell'ultima domanda
	 */
	private int getLastID() {
		
		ArrayList<Integer> domandeIdList = new ArrayList<>();
		domandeIdList.add(0);
		DB db = getDB();
		Map<Long, Domanda> domande = db.getTreeMap("domande");
		
		for(Map.Entry<Long, Domanda> domanda : domande.entrySet())
			if(domanda.getValue() instanceof Domanda)
				domandeIdList.add(domanda.getValue().getId());
		
		Integer id = Collections.max(domandeIdList);
		return id;
	}
	
	
	/**
	 * Metodo per eliminare una domanda
	 *  
	 * @param id identificativo della domanda
	 * @return true se la domanda è rimossa, false altrimenti 
	 */
	@Override
	public boolean eliminaDomanda(Long id) {
		DB db = getDB();
		Map<Long, Domanda> domande = db.getTreeMap("domande");
		domande.remove(id);
		db.commit();
		
		if(!domande.containsKey(id))
			return true;
		else 
			return false;
	}
	
	
	/**
	 * Metodo per modificare il nome della categoria di una domanda
	 * 
	 * @param id della domanda
	 * @param newCatName il nuovo nome della categoria
	 */
	@Override
	public void editDomanda(Long id, String newCatName) {
		DB db = getDB();
		Map<Long, Domanda> domande = db.getTreeMap("domande");
		long hash = id.hashCode();
		Domanda domanda = domande.get(hash);
		domanda.getCat().setNome(newCatName);
		domande.put(hash, domanda);
		db.commit();
	}
	
	// *************** End domande methods ***************
	
	
	// *************** Start risposte methods *************** 
	
	/**
	 * Metodo che restituisce lista delle risposte di una domanda
	 * 
	 * @param id   id della domanda
	 */
	@Override
	public ArrayList<Risposta> getRisposte(int id) {
		DB db = getDB();
		ArrayList<Risposta> risposte = new ArrayList<Risposta>();
		Map<Long, Risposta> list = db.getTreeMap("risposte");
		for(Map.Entry<Long, Risposta> risposta : list.entrySet())
			if(risposta.getValue() instanceof Risposta) {
				if(risposta.getValue().getDomandaID() == id)
					risposte.add((Risposta)risposta.getValue());
			}
		return risposte;
	}

	/**
	 * Metodo per salvare una nuova risposta
	 * 
	 * @param risposta   la nuova risposta da salvare
	 */
	@Override
	public void salvaRisposta(Risposta risposta) {
		int lastID = getLastIDRisposte()+1;
		risposta.setId(lastID);
		DB db = getDB();
		Map<Long, Risposta> risposte = db.getTreeMap("risposte");
		long hash = (long) risposta.getId().hashCode();
		risposte.put(hash, risposta);
		db.commit();
	}
	
	/**
	 * Metodo che ritorna l'id dell'ultima risposta aggiunta nel db
	 * 
	 * @return id 	id dell'ultima risposta
	 */
	private int getLastIDRisposte() {

		ArrayList<Integer> risposteIdList = new ArrayList<>();
		risposteIdList.add(0);
		DB db = getDB();		
		Map<Long, Risposta> risposte = db.getTreeMap("risposte");
		
		for(Map.Entry<Long, Risposta> risposta : risposte.entrySet())
			if(risposta.getValue() instanceof Risposta)
				risposteIdList.add(risposta.getValue().getId());
		
		Integer id = Collections.max(risposteIdList);
		return id;
	}
	
	
	/**
	 * Metodo per eliminare una risposta
	 * 
	 * @param id identificativo della risposta 
	 */
	@Override
	public boolean eliminaRisposta(Long id) {
		DB db = getDB();
		Map<Long, Risposta> risposte = db.getTreeMap("risposte");
		risposte.remove(id);
		db.commit();
		
		if(!risposte.containsKey(id))
			return true;
		else 
			return false;
	}
	
	/**
	 * Metodo per votare una risposta
	 * 
	 * @param id della risposta
	 * @param voto dato alla risposta da un giudice
	 */
	@Override
	public void votaRisposta(Integer id, int voto) {
		
		DB db = getDB();
		Map<Long, Risposta> risposte = db.getTreeMap("risposte");
		long hashCode = id.hashCode();
		Risposta risposta = risposte.get(hashCode);
		risposta.setVoto(voto);
		risposte.put(hashCode, risposta);
		db.commit();
	}
	
	// *************** End risposte methods ***************


	// *************** Start session methods ***************
	
	/**
	 * Metodo che restituisce un utente dal session
	 * 
	 * @return user   
	 */
	@Override
	public UtenteBase getSessionUser() {
		UtenteBase user = (UtenteBase) getThreadLocalRequest().getSession().getAttribute("user");
		return user;
	}

	/**
	 * Metodo per chiudere la session
	 */
	@Override
	public void exitSessionUser() {
		getThreadLocalRequest().getSession().invalidate();
		
	}
	
	// *************** End session methods ***************
	
	
	
	/**
	 * Metodo per cancellare tutti gli utenti salvati nel db
	 */
	@Override
	public void clearDBUsers() {
		DB db = getDB();
		Map<Long, Risposta> risposte = db.getTreeMap("users");
		risposte.clear();
		db.commit();
	}

	/**
	 * Metodo per cancellare tutte le categorie salvate nel db
	 */
	@Override
	public void clearDBCategorie() {
		DB db = getDB();
		Map<Long, Risposta> risposte = db.getTreeMap("categorie");
		risposte.clear();
		db.commit();
		
	}
	
	/**
	 * Metodo per cancellare tutte le risposte salvate nel db
	 */
	@Override
	public void clearDBRisposte() {
		DB db = getDB();
		Map<Long, Risposta> risposte = db.getTreeMap("risposte");
		risposte.clear();
		db.commit();
	}
	
	/**
	 * Metodo per cancellare tutte le domande salvate nel db
	 */
	@Override
	public void clearDBDomande() {
		DB db = getDB();
		Map<Long, Domanda> domande = db.getTreeMap("domande");
		domande.clear();
		db.commit();
	}	

	
}
