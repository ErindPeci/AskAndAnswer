package com.askandanswer.client.service;



import java.util.ArrayList;

import com.askandanswer.client.gui.Categorie;
import com.askandanswer.client.gui.Main;
import com.askandanswer.client.gui.Risposte;
import com.askandanswer.client.gui.Users;
import com.askandanswer.client.model.Categoria;
import com.askandanswer.client.model.Domanda;
import com.askandanswer.client.model.Risposta;
import com.askandanswer.client.model.Utente;
import com.askandanswer.client.model.UtenteBase;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;


public class ClientImplementor implements ClientInterface {
	
	private StandardServiceAsync serviceAsync;
	
	// GUI
	private Main main;
	private Risposte risposte;
	private Users usersGui;
	private Categorie categorieGui = null;
	
	// lista delle categorie
	private ArrayList<Categoria> catList = null;
	
	// lista delle domande
	private ArrayList<Domanda> domandaList = null;
	
	// nome della categoria da eliminare/modificare
	private String nome;
	
	// nome della sotto categoria da eliminare
	private String nomeSub;
	
	// nuovo nome di una categoria da modificare
	private String newName;
	
	// id di una domanda per aggiornare la lista delle sue risposte, dopo l'inserimento/votazione/eliminazione di una risposta
	private int idDomanda;

	
	public ClientImplementor(String url) {
		serviceAsync = GWT.create(StandardService.class);
		ServiceDefTarget endpoint = (ServiceDefTarget) serviceAsync;
		endpoint.setServiceEntryPoint(url); // URL is the servlet url
		
		main = new Main(this);
		getSessionUser();
		getDomande();
		getCategorie();
	}
	
	
	/**
	 * Metodo che manda la richiesta al server per effettuare il login di un utente
	 */
	@Override
	public void login(String nickname, String password) {
		serviceAsync.login(nickname, password, new AsyncCallback() {

			@Override
			public void onFailure(Throwable caught) {
				System.out.println("An error has occured.");
			}

			@Override
			public void onSuccess(Object result) {
			if(result instanceof UtenteBase) {
					main.loginUser((UtenteBase) result);
				} else
					main.displayMsg(Main.ERROR_CODE, "Username or password wrong.");
			}
		});
		
	}

	/**
	 * Metodo che manda la richiesta al server per registrare un nuovo utente 
	 */
	@Override
	public void registerUtente(Utente user) {
		serviceAsync.registerUtente(user, new AsyncCallback() {
			@Override
			public void onFailure(Throwable caught) {
				System.out.println("An error has occured.");
			}

			@Override
			public void onSuccess(Object result) {
				if(result instanceof Boolean)
					if((boolean) result)
						main.displayMsg(Main.SUCCESS_CODE, "User successfully registered, go to login now.");
					else
						main.displayMsg(Main.ERROR_CODE, "User already exists.");
			}
		});
		
	}	
	
	/**
	 * Metodo che manda la richiesta al server per la lista degli users nel db
	 */
	@Override
	public void getUsersList(Users pUsersGui) {
		setUsersGui(pUsersGui);
		
		serviceAsync.getUsers(new AsyncCallback<ArrayList<Utente>>() {
			
			@Override
			public void onSuccess(ArrayList<Utente> result) {
				usersGui.setUsersList(result);
				GWT.log("get users list success");
			}
			
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("get users list failed");
			}
		});
	}

	/**
	 * Metodo che manda la richiesta al server per modificare un utente
	 */
	@Override
	public void editUser(Users userGUI ,String username) {
		setUsersGui(userGUI);
		serviceAsync.editUtente(username, new AsyncCallback<Boolean>() {
			
			@Override
			public void onSuccess(Boolean result) {
				if(result==true) {
					GWT.log("Utente modificato");
					usersGui.openUsersGui();
				}
				else
					GWT.log("Utente non esiste");
			}
			
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Metodo editUser fallito");
				
			}
		});
	}

	/**
	 * Metodo che manda la richiesta al server per l'inserimento di una domanda
	 */
	@Override
	public void addQuestion(Domanda domanda) {
		serviceAsync.saveQuestion(domanda, new AsyncCallback() {

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("add question failed");
				
			}

			@Override
			public void onSuccess(Object result) {
				GWT.log("add question success");
				getDomande();
				
				Timer timer = new Timer() {
					public void run() {
						main.setupContent(null);
					}
				};
				timer.schedule(100);
			}
		});	
	}
	
	/**
	 * Manda la richiesta al server per la lista delle domande 
	 */
	@Override
	public void getDomande() {
		serviceAsync.getDomande(new AsyncCallback<ArrayList<Domanda>>() {
			
			@Override
			public void onSuccess(ArrayList<Domanda> result) {
				GWT.log("get domande method success");
				domandaList = result;
				main.setListDomande(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("get domande method failed");
			}
		});

	}
	
	/**
	 * Manda la richiesta al server per l'eliminazione della domanda
	 */
	@Override
	public void eliminaDomanda(Long id) {
		serviceAsync.eliminaDomanda(id,new AsyncCallback() {

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Metodo fallito");
				
			}

			@Override
			public void onSuccess(Object result) {
				if((boolean)result) {
					GWT.log("Domanda eliminata");
					getDomande();
					
					Timer timer = new Timer() {
						public void run() {
							main.setupContent(null);
						}
					};
					timer.schedule(100);
				}
			}
		});
	}
	

	/**
	 * Metodo che manda la richiesta al server per la lista delle categorie
	 */
	@Override
	public void getCategorie() {
		serviceAsync.getCategorie(new AsyncCallback<ArrayList<Categoria>>() {

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Get Categorie failed");
			}

			@Override
			public void onSuccess(ArrayList<Categoria> result) {
				GWT.log("Get Categorie Success");
				catList = result;
				main.setListCategorie(result);
				if(categorieGui != null) {
					categorieGui.setCatList(result);
					Timer timer = new Timer() {
						public void run() {
							categorieGui.setupContent();
						}
					};
					timer.schedule(300);
				}
				
			}
		});
		
	}
	
	/**
	 * Metodo che manda la richiesta al server per salvare una categoria
	 */
	@Override
	public void salvaCategoria(Categorie catGui, Categoria cat) {
		setCategorieGui(catGui);
		serviceAsync.salvaCategoria(cat, new AsyncCallback() {

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("salva categoria failed");
				
			}

			@Override
			public void onSuccess(Object result) {
				GWT.log("salva categoria success");
				getCategorie();
				
				Timer timer = new Timer() {
					public void run() {
						categorieGui.setupContent();
					}
				};
				timer.schedule(100);
			}
		});
	}
	
	/**
	 * Metodo che manda la richiesta al server per eliminare una categoria
	 */
	@Override
	public void eliminaCategoria(Categorie catGui, String nomeIn) {
		nome  = nomeIn;
		setCategorieGui(catGui);
		serviceAsync.eliminaCategoria(nome, new AsyncCallback() {

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("elimina categoria failed");
				
			}

			@Override
			public void onSuccess(Object result) {
				GWT.log("elimina categoria success");
				getCategorie();
				eliminaDomandeOfCat(nome);
			}
		});
		
		for(Categoria val : catList) {
			if(val.getParent() != null) {
				if(val.getParent().getNome() == nome) {
					nomeSub = val.getNome();
					serviceAsync.eliminaCategoria(val.getNome(), new AsyncCallback() {
	
						@Override
						public void onFailure(Throwable caught) {
							GWT.log("remove sub categories failed");	
						}
						
						@Override
						public void onSuccess(Object result) {
							GWT.log("sub categories removed");
							
							eliminaDomandeOfCat(nomeSub);
							getCategorie();
							
							
						}
					});
				}
			}
		}

		
	}
	/**
	 * Metodo per eliminare domande di una categoria eliminata
	 * @param nomeCat
	 */
	private void eliminaDomandeOfCat(String nomeCat) {
		for(Domanda val : domandaList) {
			if(val.getCat().getNome() == nomeCat) {
				serviceAsync.eliminaDomanda((long)val.getId(), new AsyncCallback() {

					@Override
					public void onFailure(Throwable caught) {
						GWT.log("remove domande of the category removed failed");
					}

					@Override
					public void onSuccess(Object result) {
						GWT.log("remove domande of the category removed success");
						getDomande();
					}
				});
			}
		}
	}
	
	/**
	 * Metodo che manda la richiesta al server pero rinominare una categoria
	 * @param catGui  categorie gui
	 * @param pOldName   vecchio nome della categoria
	 * @param pNewName   nuovo nome della categoria
	 */
	@Override
	public void editCategoria(Categorie catGui, String pOldName, String pNewName) {
		setCategorieGui(catGui);
		nome = pOldName;
		newName = pNewName;
		serviceAsync.editCategoria(nome, newName, new AsyncCallback() {

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("edit categoria failed");
			}

			@Override
			public void onSuccess(Object result) {
				GWT.log("edit categoria success");
				getCategorie();
				Window.alert("Category's name has been changed to : "+newName);
				editDomandeOfCat(nome, newName);
			}
		});
	}
	
	/**
	 * Metodo per modificare il nome di una categoria nelle domande 
	 * @param oldName vecchio nome della categoria rinominata
	 * @param newName nuovo nome della categoria rinominata
	 */
	private void editDomandeOfCat(String oldName, String newName) {
		for(Domanda val : domandaList) {
			if(val.getCat().getNome() == oldName) {
				serviceAsync.editDomanda((long)val.getId(), newName, new AsyncCallback() {

					@Override
					public void onFailure(Throwable caught) {
						GWT.log("edit domanda of the category modified failed");
						
					}

					@Override
					public void onSuccess(Object result) {
						GWT.log("edit domanda of the category modified success");
						
					}
				});
			}
		}
		getDomande();
	}
	
	/**
	 * Metodo che ritorna le risposte di una domanda specificata
	 * @param val Risposte Gui 
	 * @param id della domanda
	 */
	@Override
	public void getRisposte(Risposte val, int id) {
		setRisposteGui(val);
		serviceAsync.getRisposte(id, new AsyncCallback<ArrayList<Risposta>>() {
			
			@Override
			public void onSuccess(ArrayList<Risposta> result) {
				risposte.setRisposte(result);
				GWT.log(String.valueOf(result.size()));
			}
			
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Get Risposte failed");	
			}
		});
	}
	
	/**
	 * Metodo per salvare una nuova risposta
	 */
	@Override
	public void salvaRisposta(Risposte val, Risposta risposta) {
		idDomanda = risposta.getDomandaID();
		setRisposteGui(val);
		serviceAsync.salvaRisposta(risposta, new AsyncCallback() {
			
			@Override
			public void onSuccess(Object result) {		
				GWT.log("answer inserted");
				getRisposte(risposte, idDomanda);
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("salva risposta method failed");
			}
		});
	}
	

	/**
	 * Metodo che manda la richiesta al server per votare una risposta
	 * @param risposteIN  risposte gui
	 * @param id  id della risposta
	 * @param voto  voto dato alla risposta
	 * @param idDom  id della domanda a cui appartiene la risposta
	 */
	@Override
	public void votaRisposta(Risposte risposteIN, Integer id, int voto, int idDom) {
		setRisposteGui(risposteIN);
		idDomanda = idDom;
		serviceAsync.votaRisposta(id, voto, new AsyncCallback() {
			
			@Override
			public void onSuccess(Object result) {
				getRisposte(risposte, idDomanda);
				Timer timer = new Timer() {
					public void run() {
						risposte.setupContent();
					}
				};
				timer.schedule(100);
				GWT.log("method votaRisposta success");
			}
			
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("method votaRisposta failed");
				
			}
		});
	}

	/**
	 * Manda la richiesta al server per l'eliminazione della risposta
	 */
	public void eliminaRisposta(Risposte risposta,Long id, int domandaID) {
		setRisposteGui(risposta);
		idDomanda = domandaID;
		serviceAsync.eliminaRisposta(id, new AsyncCallback<Boolean>() {
			
			@Override
			public void onSuccess(Boolean result) {
				if((boolean)result) {
					
					GWT.log("Risposta eliminata");
					getRisposte(risposte, idDomanda);
					
					Timer timer = new Timer() {
						public void run() {
							risposte.setupContent();
						}
					};
					timer.schedule(100);
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Metodo fallito");
				
			}
		});
	}
	
	
	

	/**
	 * Manda richiesta al server per il valore del utente salvato nel session
	 */
	@Override
	public void getSessionUser() {
		serviceAsync.getSessionUser(new AsyncCallback() {

			@Override
			public void onFailure(Throwable caught) {
				
			}

			@Override
			public void onSuccess(Object result) {
				main.setUser((UtenteBase) result);
			}
		});
	}
	
	/**
	 * Manda richiesta al server per chiudere la session
	 */
	@Override
	public void exitSession() {
		serviceAsync.exitSessionUser(new AsyncCallback() {

			@Override
			public void onFailure(Throwable caught) {
				System.out.println("exit session failed");
			}

			@Override
			public void onSuccess(Object result) {
				GWT.log("exit session success");
			}
		});
	}
	
	/**
	 * Restituisce il main gui
	 * @return  main gui
	 */
	public Main getMainGUI() {
		return main;
	}
	
	/**
	 * Modifica la risposte gui
	 * @param pRisposte  risposte gui
	 */
	private void setRisposteGui(Risposte pRisposte) {
		risposte = pRisposte;
	}
	
	
	/**
	 * Modifica la users gui
	 * @param val  users gui
	 */
	private void setUsersGui(Users val) {
		usersGui = val;
	}
	
	
	/**
	 * Modifica la Categorie gui
	 * @param val  Categorie gui
	 */
	private void setCategorieGui(Categorie val) {
		categorieGui = val;
	}
	


}