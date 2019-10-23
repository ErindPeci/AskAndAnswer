package com.askandanswer.client.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.askandanswer.client.model.Domanda;
import com.askandanswer.client.model.Risposta;
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
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Risposte extends Composite {
	
	private VerticalPanel contentPanel = new VerticalPanel();

	private ClientImplementor clientImpl;

	// User status variables
	private boolean isAdmin = false;
	private Utente actualUser = null;
	UtenteBase user = null;	

	// lista delle risposte
	private ArrayList<Risposta> risposteList = null;
	
	private Domanda domanda = null;
	private Risposta newAnswer;
	
	private TextArea answerTA;
	private TextArea linksTA;
	
	private Label errorLbl;
	
	public Risposte(ClientImplementor clientImplInput, Domanda domandaInput, UtenteBase userInput) {
		initWidget(this.contentPanel);
		this.clientImpl = clientImplInput;
		this.user = userInput;
		verifyUser(user);
		this.domanda = domandaInput;
		clientImpl.getRisposte(this, domanda.getId());
			
		Timer timer = new Timer() {
			public void run() {
				setupContent();
			}
		};
		timer.schedule(100);
		
	}
	
	/**
	 * Method to verify user status
	 * @param user user to verify
	 */
	private void verifyUser(UtenteBase user) {
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
	}
	
	/**
	 * Stampa tutte le risposte di una certa domanda e un box per aggiungere un nuova risposta
	 */
	public void setupContent() {
		if(contentPanel.getWidgetCount() > 0)
			for(int i = 0; i < contentPanel.getWidgetCount(); i++)
				contentPanel.remove(i);
		
		
		VerticalPanel vPanel = new VerticalPanel();
		
		// start question box
		VerticalPanel questionBox = new VerticalPanel();
		questionBox.setStyleName("askBox questionBox");
		
		HorizontalPanel infoBox = new HorizontalPanel();
		
		Label username = new Label();
		username.setStyleName("info-label");
		username.setText(domanda.getUser().getUsername());
		infoBox.add(username);
		
		Label date = new Label();
		date.setStyleName("info-label data-label");
		DateTimeFormat fmt = DateTimeFormat.getFormat("dd/MM/yyyy HH:mm:ss");
		date.setText(fmt.format(domanda.getData()));
		infoBox.add(date);
		
		Label text = new Label();
		text.setStyleName("info-label text-label");
		text.setText(domanda.getText());
		
		questionBox.add(infoBox);
		questionBox.add(text);
		
		vPanel.add(questionBox);
		// end question box
		
		if(risposteList != null) {
			Collections.sort(risposteList, new Comparator<Risposta>() {
		        @Override
		        public int compare(Risposta ris2, Risposta ris1)
		        {
	
		            return  ris1.getId().compareTo(ris2.getId());
		        }
		    });
		}

		// start print answers
		if(risposteList != null) {
			for(Risposta val : risposteList) {
				VerticalPanel rispostaBox = new VerticalPanel();
				rispostaBox.setStyleName("askBox rispostaBox");
				
				HorizontalPanel infoBox2 = new HorizontalPanel();
				
				Label username2 = new Label();
				username2.setStyleName("info-label");
				username2.setText(val.getUsername());
				infoBox2.add(username2);
				
				Label date2 = new Label();
				date2.setStyleName("info-label data-label");
				DateTimeFormat fmt2 = DateTimeFormat.getFormat("dd/MM/yyyy HH:mm:ss");
				date2.setText(fmt2.format(val.getData()));
				infoBox2.add(date2);
				if(val.getVoto() != -1) {
					Label voto = new Label();
					voto.setStyleName("info-label");
					voto.setText("Voto: "+ String.valueOf(val.getVoto()));
					infoBox2.add(voto);
				}
				rispostaBox.add(infoBox2);
				
				Label text2 = new Label();
				text2.setStyleName("info-label text-label");
				text2.setText(val.getText());
				rispostaBox.add(text2);
				
				
				if(val.getLinks() != null) {
					if(val.getLinks().length > 0) {
						Label title = new Label("links of images :"); 
						title.setStyleName("links-title");
						rispostaBox.add(title);
						
						String[] links = val.getLinks();
						for(String link : links) {
							Label value = new Label();
							value.setStyleName("info-label text-label");
							value.setText(link);
							rispostaBox.add(value);
						}
					}
				}
				
				
				if(user != null) {
					
					// delete button
					if(isAdmin==true || actualUser.isGiudice() == true) {
						Button deleteAns = new Button("Delete");
						deleteAns.setStyleName("default-btn delete-btn");
						deleteAns.setTitle(val.getId().toString());
						deleteAns.addClickHandler(new ClickHandler() {
							
							@Override
							public void onClick(ClickEvent event) {
								String title = event.getRelativeElement().getTitle();
								deleteAnswer(title);
							}
						});
						rispostaBox.add(deleteAns);
					}	
				}
				
				// evaluate buttons
				if(actualUser != null && val.getVoto() == -1) {
					if(actualUser.isGiudice() == true) {
						HorizontalPanel valBox = new HorizontalPanel();
						
						Button zero = new Button("0");
						zero.setStyleName("default-btn");
						zero.setTitle(val.getId().toString());
						zero.addClickHandler(new ClickHandler() {
							
							@Override
							public void onClick(ClickEvent event) {
								String title = event.getRelativeElement().getTitle();
								votaRisposta(Integer.parseInt(title), 0);
							}
						});
						valBox.add(zero);
						
						Button one = new Button("1");
						one.setStyleName("default-btn");
						one.setTitle(val.getId().toString());
						one.addClickHandler(new ClickHandler() {
							
							@Override
							public void onClick(ClickEvent event) {
								String title = event.getRelativeElement().getTitle();
								votaRisposta(Integer.parseInt(title), 1);
							}
						});
						valBox.add(one);
						
						Button two = new Button("2");
						two.setStyleName("default-btn");
						two.setTitle(val.getId().toString());
						two.addClickHandler(new ClickHandler() {
							
							@Override
							public void onClick(ClickEvent event) {
								String title = event.getRelativeElement().getTitle();
								votaRisposta(Integer.parseInt(title), 2);
							}
						});
						valBox.add(two);
						
						Button three = new Button("3");
						three.setStyleName("default-btn");
						three.setTitle(val.getId().toString());
						three.addClickHandler(new ClickHandler() {
							
							@Override
							public void onClick(ClickEvent event) {
								String title = event.getRelativeElement().getTitle();
								votaRisposta(Integer.parseInt(title), 3);
							}
						});
						valBox.add(three);
						
						Button four = new Button("4");
						four.setStyleName("default-btn");
						four.setTitle(val.getId().toString());
						four.addClickHandler(new ClickHandler() {
							
							@Override
							public void onClick(ClickEvent event) {
								String title = event.getRelativeElement().getTitle();
								votaRisposta(Integer.parseInt(title), 4);
							}
						});
						valBox.add(four);
						
						Button five = new Button("5");
						five.setStyleName("default-btn");
						five.setTitle(val.getId().toString());
						five.addClickHandler(new ClickHandler() {
							
							@Override
							public void onClick(ClickEvent event) {
								String title = event.getRelativeElement().getTitle();
								votaRisposta(Integer.parseInt(title), 5);
							}
						});
						valBox.add(five);
						
						rispostaBox.add(valBox);
					}
				}
				
				vPanel.add(rispostaBox);
			}
		}
		
		// start insert answer box
		
		VerticalPanel answerBox = new VerticalPanel();
		answerBox.setStyleName("askBox");
		
		errorLbl = new Label();
		errorLbl.setStyleName("error-label");
		answerBox.add(errorLbl);
		
		answerTA = new TextArea();
		answerTA.setStyleName("default-tb ask-TA");
		answerTA.getElement().setPropertyString("placeholder", "Give an answer ...");
		answerBox.add(answerTA);
		
		// links text area
		linksTA = new TextArea();
		linksTA.setStyleName("links-TA");
		linksTA.getElement().setPropertyString("placeholder", "You can insert one or many links of images separated with comma ','");
		answerBox.add(linksTA);
		
		Button answerBTN = new Button("Answer");
		answerBTN.setStyleName("default-btn");
		answerBTN.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				String errorMsg = "";
				String[] links = null;
				
				
				if(answerTA.getText().length() > 500 ) {
					errorMsg += "The answer must have at most 500 charachters.";
				}
				if(linksTA != null && linksTA.getText() != "") {
					links = linksTA.getText().split(",");
				}
				
				if(errorMsg == "") {
					newAnswer= new Risposta(answerTA.getValue(), actualUser, domanda.getId(),links);
					addAnswer(newAnswer);
				}else {
					errorLbl.setText(errorMsg);
				}
			}
		});
		
		if(actualUser == null)
			answerBTN.setEnabled(false);
		
		answerBox.add(answerBTN);
		
		vPanel.add(answerBox);
		
		// end insert answer box
		
		
		contentPanel.add(vPanel);	 
	}
	
	
	/**
	 * Metodo per la cancellazione delle risposte  
	 * @param title stringa contiene id della risposta da eliminare
	 */
	private void deleteAnswer(String title) {
		long id = Integer.parseInt(title);
		clientImpl.eliminaRisposta(this,id, domanda.getId());
	}
	
	
	/**
	 * metodo che chiama il metodo votaRisposta del client implementor
	 * @param id della risposta
	 * @param voto dato alla risposta
	 */
	private void votaRisposta(Integer id, int voto) {
		clientImpl.votaRisposta(this, id, voto, domanda.getId());
	}
	
	
	/**
	 * metodo per inserire una nuova risposta 
	 * @param newAnswer  la nuova risposta
	 */
	private void addAnswer(Risposta newAnswer) {
	 	clientImpl.salvaRisposta(this, newAnswer);
		Timer timer = new Timer() {
			public void run() {
				setupContent();
			}
		};
		timer.schedule(300);
	}
	
	
	/**
	 * Set list of risposte
	 * @param result
	 */
	public void setRisposte(ArrayList<Risposta> result){
		risposteList = result;
	}
}