package com.askandanswer.client.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.askandanswer.client.model.Categoria;
import com.askandanswer.client.model.Domanda;
import com.askandanswer.client.model.Utente;
import com.askandanswer.client.model.UtenteBase;
import com.askandanswer.client.service.ClientImplementor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;


public class Main extends Composite {
	
	// Codes to display messages
	public static final int SUCCESS_CODE = 0;
	public static final int ERROR_CODE = 1;
	
	// Codes to recognise the clicked buttons
	private static final String HOME_BTN = "home";
	private static final String REGISTER_BTN = "register";
	private static final String LOGIN_BTN = "login";
	private static final String LOGOUT_BTN = "logout";
	private static final String PROFILE_BTN = "profile";
	private static final String GESTIONE_CATEGORIE_BTN = "gestione-categorie";
	private static final String USERS_LIST_BTN = "users-list";
	private static final String FILTER_BTN = "filter";

	
	// Main UI structure
	private HorizontalPanel wrapper = new HorizontalPanel();
	private VerticalPanel menuPanel = new VerticalPanel();
	private VerticalPanel contentPanel = new VerticalPanel();
	
	// GUI
	private Foreground foreground = null; // The visible fragment
	private Registration registration = null;
	private Login login = null;
	private Profile profile = null;
	private Risposte risposte = null;
	private Users usersGui = null;
	private Categorie categ = null ;
	
	// Client implementor
	private ClientImplementor clientImpl;
	
	// User status variables
	private boolean isAdmin = false;
	private Utente actualUser = null;
	UtenteBase user = null;
	
	// Lists of data
	private ArrayList<Domanda> domande = null;
	private ArrayList<Categoria> categorie = null;
	
	// Variables to insert new question
	private Domanda newQuestion;
	private TextArea askTA;
	private TextArea linksTA;
	private ListBox catList;
	private Label errorLbl;
	
	// List box contains list of categories to filter
	private ListBox categorieLB ;
	
	// Set data in list of categories
	public void setListCategorie(ArrayList<Categoria> value) {
		categorie = value;
	}
	
	// Set data in list of questions
	public void setListDomande(ArrayList<Domanda> quest) {
		domande = quest;
	}
	
	// Set actual user from getSessionUser in the ClientImplementor
	public void setUser(UtenteBase element) {
		user = element;
	}
	
	public Main(ClientImplementor clientImpl) {
		initWidget(wrapper);
		wrapper.add(menuPanel);
		wrapper.add(contentPanel);
		this.clientImpl = clientImpl;
		
		Timer timer = new Timer() {
			public void run() {
				loginUser(user);
			}
		};
		timer.schedule(400);
	}
	
	/**
	 * Richiama il metodo override nel foreground definito, 
	 * rispetto al codice (tipo di messaggio) che gli viene passato come paramentro.
	 * Inoltra un messaggio da stampare nella label.
	 * @param code		tipo di messaggio
	 * @param msg		messaggio
	 */
	public void displayMsg(int code, String msg) {
		switch(code) {
			case SUCCESS_CODE: 
				foreground.updateSuccessLabel(msg);
				break;
			case ERROR_CODE:
				foreground.updateErrorLabel(msg);
				break;
		}
	}
	
	/**
	 * Permette l'accesso alle funzionalita' relative all'utente che effettua il login
	 * @param user		utente generale
	 */
	public void loginUser(UtenteBase user) {
		this.user=user;
		if(user != null) {
			if(user instanceof Utente) {
				actualUser = (Utente) user;
				isAdmin = false;
				
			} else if(user.isAdmin()== true) {
				actualUser = null;
				isAdmin = true;
				
			} else {
				actualUser = null;
				isAdmin = false;
			}
		}
		setupMenuBar();
		setupContent(null);
	}

	/**
	 * Imposta la menu bar: insieme di buttons che esprimono le funzionalita a cui può accedere l'utente
	 */
	private void setupMenuBar() {
		
		// Clean old menu bar
		menuPanel.setStyleName("menuPanel");
		if(menuPanel.getWidgetCount() > 0)
			for(int i = 0; i < menuPanel.getWidgetCount(); i++)
				menuPanel.remove(i);
		
		VerticalPanel vPanel = new VerticalPanel();

		Button homeBTN = new Button("Home");
		homeBTN.setStyleName("default-btn");
		homeBTN.setTitle(HOME_BTN);
		homeBTN.addClickHandler(new ButtonClickHandler());
		vPanel.add(homeBTN);
		
		if(actualUser == null && isAdmin == false) {
			
			Button registerBTN = new Button("Register");
			registerBTN.setStyleName("default-btn");
			registerBTN.setTitle(REGISTER_BTN);
			registerBTN.addClickHandler(new ButtonClickHandler());
			vPanel.add(registerBTN);
			
			Button loginBTN = new Button("Login");
			loginBTN.setStyleName("default-btn");
			loginBTN.setTitle(LOGIN_BTN);
			loginBTN.addClickHandler(new ButtonClickHandler());
			vPanel.add(loginBTN);
		}

		if(actualUser != null) {
			
			Button profileBTN = new Button("My Profile");
			profileBTN.setStyleName("default-btn");
			profileBTN.setTitle(PROFILE_BTN);
			profileBTN.addClickHandler(new ButtonClickHandler());
			vPanel.add(profileBTN);
		}
		
		if(actualUser != null || isAdmin == true) {
			
			HorizontalPanel catPanel = new HorizontalPanel();
			
			Label categories = new Label("Categorie");
			categories.setStyleName("catLabel");
			catPanel.add(categories);
			
		 
			if(isAdmin==true) {
				Button edit = new Button("Edit");
				edit.setStyleName("default-btn edit-btn edit-cat-btn");
				edit.setTitle(GESTIONE_CATEGORIE_BTN);
				edit.addClickHandler(new ButtonClickHandler());
				catPanel.add(edit);
			}
			vPanel.add(catPanel);
			
			// categories listBox
			categorieLB = new ListBox();
			categorieLB.setStyleName("default-LB");
			categorieLB.addItem("");

			// add categories in list box
			for(Categoria cat : categorie) {
				if(cat.getParent() == null) {
					categorieLB.addItem(cat.getNome(), cat.getNome());
					
					// subcategory of category
					for(Categoria cat2 : categorie) {
						if(cat2.getParent() != null) {
							if(cat2.getParent().getNome() == cat.getNome()) {
								categorieLB.addItem("---> "+cat2.getNome(), cat2.getNome());
								
								// subcategory of subcategory 
								for(Categoria cat3 : categorie) {
									if(cat3.getParent() != null) {
										if(cat3.getParent().getNome() == cat2.getNome()) {
											categorieLB.addItem("--------> "+cat3.getNome(), cat3.getNome());
										}
									}
								}
							}
						}
					}
				}
			}
			vPanel.add(categorieLB);
			
			Button filter = new Button("Filter");
			filter.setStyleName("default-btn");
			filter.setTitle(FILTER_BTN);
			filter.addClickHandler(new ButtonClickHandler());
			vPanel.add(filter);
			
			//view all users button 
			if(isAdmin==true) {
				Button viewAllUsers = new Button("User List");
				viewAllUsers.setStyleName("default-btn");
				viewAllUsers.setTitle(USERS_LIST_BTN);
				viewAllUsers.addClickHandler(new ButtonClickHandler() );
				vPanel.add(viewAllUsers);
			}
			
			
			// logout button
			Button logoutBTN = new Button("Logout");
			logoutBTN.setStyleName("default-btn logout-btn delete-btn");
			logoutBTN.setTitle(LOGOUT_BTN);
			logoutBTN.addClickHandler(new ButtonClickHandler());
			vPanel.add(logoutBTN);	
			
		}
		
		menuPanel.add(vPanel);
	}
	/**
	 * Stampa AskBox e tutte le domande 
	 * Stampa AskBox e tutte le domande per una certa categoria
	 * @param nomeCateg  nome di una categoria per il filter
	 */
	public void setupContent(String nomeCateg) {
		
		contentPanel.setStyleName("contentPanel");
		// Clean old content
		if(contentPanel.getWidgetCount() > 0)
			for(int i = 0; i < contentPanel.getWidgetCount(); i++)
				contentPanel.remove(i);
		
		VerticalPanel vPanel = new VerticalPanel();
		
		if(nomeCateg != null && nomeCateg != "") {
			Categoria cat = new Categoria();
			String path = "";
			for(Categoria val : categorie) {
				if(val.getNome() == nomeCateg)
					cat = val;
			}
			if(cat.getParent() == null) {
				path = cat.getNome();
			}else {
				if(cat.getParent().getParent() == null)
					path = cat.getParent().getNome() +" > "+cat.getNome();
				else
					path = cat.getParent().getParent().getNome() + " > " +cat.getParent().getNome() + " > " + cat.getNome();
			}
			Label categoriesPath = new Label();
			categoriesPath.setText(path);
			categoriesPath.setStyleName("cat-path");
			vPanel.add(categoriesPath);
		}
		
		if(nomeCateg == null || nomeCateg == "") {
			VerticalPanel askBox = new VerticalPanel();
			askBox.setStyleName("askBox");
			
			errorLbl = new Label();
			errorLbl.setStyleName("error-label");
			askBox.add(errorLbl);
			
			// ask text area
			askTA = new TextArea();
			askTA.setStyleName("default-tb ask-TA");
			askTA.getElement().setPropertyString("placeholder", "Make a question...");
			askBox.add(askTA);
			
			
			// links text area
			linksTA = new TextArea();
			linksTA.setStyleName("links-TA");
			linksTA.getElement().setPropertyString("placeholder", "You can insert one or many links of images separated with comma ','");
			askBox.add(linksTA);
			
			
			// lista per scegliere la categoria della domanda
			catList = new ListBox();
			catList.setStyleName("default-LB");
			
			// print category in list box
			for(Categoria cat : categorie) {
				if(cat.getParent() == null) {
					catList.addItem(cat.getNome(), cat.getNome());
					
					// subcategory of category
					for(Categoria cat2 : categorie) {
						if(cat2.getParent() != null) {
							if(cat2.getParent().getNome() == cat.getNome()) {
								catList.addItem("---> "+cat2.getNome(), cat2.getNome());
								
								// subcategory of subcategory 
								for(Categoria cat3 : categorie) {
									if(cat3.getParent() != null) {
										if(cat3.getParent().getNome() == cat2.getNome()) {
											catList.addItem("--------> "+cat3.getNome(), cat3.getNome());
										}
									}
								}
							}
						}
					}
				}
			}
			askBox.add(catList);
			
			// ask button per inserire la nuova domanda
			Button askBTN = new Button("Ask");
			askBTN.setStyleName("default-btn");
			askBTN.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					
					String errorMsg = "";
					String[] links = null;
					
					Categoria categ = new Categoria();
					for(Categoria cat : categorie) {
						if(cat.getNome() == catList.getSelectedValue())
							categ = cat;
					}
					
					if(askTA.getText().length() < 10) {
						errorMsg += "The question must have at least 10 charachters.";
					}else if(askTA.getText().length() > 300 ) {
						errorMsg += "The question must have at most 300 charachters.";
					}
					
					if(linksTA != null && linksTA.getText() != "") {
						links = linksTA.getText().split(",");
					}

					if(errorMsg == "") {
						newQuestion = new Domanda(categ, askTA.getValue(), actualUser, links);
						addQuestion(newQuestion);
						
						Timer timer = new Timer() {
							public void run() {
								loginUser(user);
							}
						};
						timer.schedule(200);
					}else {
						errorLbl.setText(errorMsg);
					}
				}
			});
			
			if(actualUser == null)
				askBTN.setEnabled(false);
			
			askBox.add(askBTN);
			
			vPanel.add(askBox);
		}
		
		if(domande != null) {
			
			Collections.sort(domande, new Comparator<Domanda>() {
		        @Override
		        public int compare(Domanda dom2, Domanda dom1)
		        {
	
		            return  dom1.getId().compareTo(dom2.getId());
		        }
		    });
		
		
		
			for(Domanda val : domande) {
				
				if(val.getCat().getNome() == nomeCateg || nomeCateg == null || nomeCateg == "") {
					
					VerticalPanel questionBox = new VerticalPanel();
					questionBox.setStyleName("askBox questionBox");
					
					HorizontalPanel infoBox = new HorizontalPanel();
					
					Label username = new Label();
					username.setStyleName("info-label");
					username.setText(val.getUser().getUsername());
					infoBox.add(username);
					
					Label date = new Label();
					date.setStyleName("info-label data-label");
					DateTimeFormat fmt = DateTimeFormat.getFormat("dd/MM/yyyy HH:mm:ss");
					date.setText(fmt.format(val.getData()));
					infoBox.add(date);
					
					questionBox.add(infoBox);
					
					Label text = new Label();
					text.setStyleName("info-label text-label");
					text.setText(val.getText());
					questionBox.add(text);
					
					
					if(val.getLinks() != null) {
						if(val.getLinks().length > 0) {
							Label title = new Label("links of images :"); 
							title.setStyleName("links-title");
							questionBox.add(title);
							
							String[] links = val.getLinks();
							for(String link : links) {
								Label value = new Label();
								value.setStyleName("info-label text-label");
								value.setText(link);
								questionBox.add(value);
							}
						}
					}
					
					Button viewAns = new Button("View Answers");
					viewAns.setStyleName("default-btn");
					viewAns.setTitle(val.getId().toString());
					viewAns.addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							String title = event.getRelativeElement().getTitle();
							setupRisposte(title);
						}
					});
					questionBox.add(viewAns);
					
					if(isAdmin==true) {
						Button deleteQues = new Button("Delete");
						deleteQues.setStyleName("default-btn delete-btn");
						deleteQues.setTitle(val.getId().toString());
						deleteQues.addClickHandler(new ClickHandler() {
							
							@Override
							public void onClick(ClickEvent event) {
								String title = event.getRelativeElement().getTitle();
								deleteQuestion(title);
							}
						});
						questionBox.add(deleteQues);
					}
					vPanel.add(questionBox);
				}	
			}
		}
		
		if(nomeCateg != "" && nomeCateg != null) {
			ArrayList<Domanda> dm = new ArrayList<Domanda>();
			for(Domanda val : domande) {
				if(val.getCat().getNome() == nomeCateg)
					dm.add(val);
			}
			if(dm.size() == 0) {
				Label msg = new Label("Non esistono domande che  appartengono a questa categoria.");
				vPanel.add(msg);
			}
		}
		
		contentPanel.add(vPanel);
	}
	
	/**
	 * Metodo per la cancellazione delle domande
	 * @param title   stringa contiene id della domanda da eliminare 
	 */
	private void deleteQuestion(String title) {
		long id = Integer.parseInt(title);
		clientImpl.eliminaDomanda(id);
	}
	
	/**
	 * Metodo per aggiungere nuova domanda
	 * @param domanda  nuova domanda da inserire
	 */
	private void addQuestion(Domanda domanda) {
		clientImpl.addQuestion(domanda);
	}
	
	/**
	 * Creazione/Aggiornamento Gui Registrazione
	 */
	private void setupRegistration() {
		if(registration == null) 
			registration = new Registration(clientImpl);
		else
			registration.onUpdateForeground(actualUser);
		
		foreground = registration;
		setupForeground(registration);
	}
	
	/**
	 * Creazione/Aggiornamento Gui Login
	 */
	private void setupLogin() {
		if(login == null)
			login = new Login(clientImpl);	
		else
			login.onUpdateForeground(actualUser);
		
		foreground = login;
		setupForeground(login);
	}
	
	/**
	 * Logout utente
	 */
	private void setupLogout() {
		actualUser = null;
		isAdmin = false;
		clientImpl.exitSession();
		setupMenuBar();
		setupContent(null);
	}

	
	/**
	 * Creazione Gui Profilo utente
	 */
	private void setupProfile() {
		
		profile = new Profile(actualUser);
		setupForeground(profile);
	}

	/**
	 * Creazione gui che permette all'admin di gestire le categorie 
	 */
	private void setupCategorieManag() {
		categ = new Categorie(clientImpl,categorie);
		setupForeground(categ);
	}
	
	
	/**
	 * Creazione gui Risposte di una domanda
	 * @param title id della domanda
	 */
	private void setupRisposte(String title) {
		Domanda domanda = null;
		int id  = Integer.parseInt(title);
		for(Domanda val : domande) {
			if(val.getId() == id)
				domanda = val;
		}
		risposte = new Risposte(clientImpl, domanda, user);
		setupForeground(risposte);
	}
	
	/**
	 * Creazione Gui Users contiene la lista degli users
	 */
	private void setupUsersView() {
		usersGui = new Users(clientImpl);
		setupForeground(usersGui);
	}
	
	/**
	 * Rimuove tutti gli elementi nel content panel e aggiunge l'elemento composto specificato come parametro
	 * @param element	insieme di elementi grafici
	 */
	private void setupForeground(Composite element) {
		int widgets = contentPanel.getWidgetCount();
		if(widgets > 0)
			for(int i = 0; i < widgets; i++)
				contentPanel.remove(i); 
		if(element != null)
			contentPanel.add(element);
	}
	
	/**
	 * Gestisce il click dei buttons nella barra di navigazione: richiama il metodo setup relativo al button cliccato
	 *
	 */
	private class ButtonClickHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			Button clicked = (Button) event.getSource();
			switch(clicked.getTitle()) {
				case HOME_BTN: 
					setupMenuBar();
					setupContent(null);
					break;
				case REGISTER_BTN: 
					setupRegistration();
					break;
				case LOGIN_BTN: 
					setupLogin();
					break;
				case LOGOUT_BTN: 
					setupLogout();
					break;
				case PROFILE_BTN: 
					setupProfile();
					break;
				case FILTER_BTN:
					setupContent(categorieLB.getSelectedValue());
					break;
				case GESTIONE_CATEGORIE_BTN:
					setupCategorieManag();
					break;
				case USERS_LIST_BTN:
					setupUsersView();
					break;
			}
		}
	}
	
}

