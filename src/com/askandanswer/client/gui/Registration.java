package com.askandanswer.client.gui;

import java.util.ArrayList;
import java.util.Date;

import com.askandanswer.client.model.Utente;
import com.askandanswer.client.service.ClientImplementor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DatePicker;

public class Registration extends Composite implements Foreground {
	
	private VerticalPanel vPanel = new VerticalPanel();
	private Grid gridPanel = new Grid(11, 2);
	
	// Input fields
	private TextBox usernameTB;
	private PasswordTextBox passwordTB;
	private TextBox emailTB;
	private TextBox nomeTB;
	private TextBox cognomeTB;
	private TextBox sessoTB;
	private DatePicker bDateDP;
	private TextBox luogoNascitaTB;
	private TextBox luogoResidenzaTB;
	private TextBox socialNetworkTB;
	
	// Labels
	private Label errorLBL;
	private Label successLBL;
	
	private ClientImplementor clientImpl;
	
	public Registration(ClientImplementor clientImpl) {
		initWidget(this.vPanel);
		this.clientImpl = clientImpl;
		
		// Text boxes
		Label lbl1 = new Label("Username: ");
		usernameTB = new TextBox();
		usernameTB.setStyleName("default-tb register-tb");
		usernameTB.getElement().setPropertyString("placeholder", "Username");
		gridPanel.setWidget(0, 0, lbl1);
		gridPanel.setWidget(0, 1, usernameTB);
		
		
		Label lbl2 = new Label("Password: ");
		passwordTB = new PasswordTextBox();
		passwordTB.setStyleName("default-tb register-tb");
		passwordTB.getElement().setPropertyString("placeholder", "Password");
		gridPanel.setWidget(1, 0, lbl2);
		gridPanel.setWidget(1, 1, passwordTB);
		
		Label lbl3 = new Label("Email: ");
		emailTB = new TextBox();
		emailTB.setStyleName("default-tb register-tb");
		emailTB.getElement().setPropertyString("placeholder", "Email");
		gridPanel.setWidget(2, 0, lbl3);
		gridPanel.setWidget(2, 1, emailTB);
		
		Label lbl4 = new Label("Nome: ");
		nomeTB = new TextBox();
		nomeTB.setStyleName("default-tb register-tb");
		nomeTB.getElement().setPropertyString("placeholder", "Nome");
		gridPanel.setWidget(3, 0, lbl4);
		gridPanel.setWidget(3, 1, nomeTB);
						
		Label lbl5 = new Label("Cognome: ");
		cognomeTB = new TextBox();
		cognomeTB.setStyleName("default-tb register-tb");
		cognomeTB.getElement().setPropertyString("placeholder", "Cognome");
		gridPanel.setWidget(4, 0, lbl5);
		gridPanel.setWidget(4, 1, cognomeTB);
		
		Label lbl6 = new Label("Sesso: ");
		sessoTB = new TextBox();
		sessoTB.setStyleName("default-tb register-tb");
		sessoTB.getElement().setPropertyString("placeholder", "solo un carattere m/f");
		gridPanel.setWidget(5, 0, lbl6);
		gridPanel.setWidget(5, 1, sessoTB);
		
		Label lbl7 = new Label("Data di nascita");
		bDateDP = new DatePicker();
		bDateDP.setValue(new Date(), true);
		gridPanel.setWidget(6, 0, lbl7);
		gridPanel.setWidget(6, 1, bDateDP);
		
		Label lbl8 = new Label("Luogo di nascita: ");
		luogoNascitaTB = new TextBox();
		luogoNascitaTB.setStyleName("default-tb register-tb");
		luogoNascitaTB.getElement().setPropertyString("placeholder", "Luogo di nascita");
		gridPanel.setWidget(7, 0, lbl8);
		gridPanel.setWidget(7, 1, luogoNascitaTB);
		
		Label lbl9 = new Label("Luogo di residenza: ");
		luogoResidenzaTB = new TextBox();
		luogoResidenzaTB.setStyleName("default-tb register-tb");
		luogoResidenzaTB.getElement().setPropertyString("placeholder", "Luogo di residenza");
		gridPanel.setWidget(8, 0, lbl9);
		gridPanel.setWidget(8, 1, luogoResidenzaTB);
		
		Label lbl10 = new Label("Social network usernames: ");
		socialNetworkTB = new TextBox();
		socialNetworkTB.setStyleName("default-tb register-tb");
		socialNetworkTB.getElement().setPropertyString("placeholder", "Split usernames with ','");
		gridPanel.setWidget(9, 0, lbl10);
		gridPanel.setWidget(9, 1, socialNetworkTB);
		
		// Labels
		errorLBL = new Label("");
		errorLBL.setStyleName("error-label");
		successLBL = new Label("");
		successLBL.setStyleName("success-label");
		
		// Buttons
		Button saveBTN = new Button("Sign in");
		saveBTN.setStyleName("default-btn green-btn");
		saveBTN.addClickHandler(new SaveBTNClickHandler());
		gridPanel.setWidget(10, 0, saveBTN);
		
		vPanel.add(gridPanel);
		vPanel.add(successLBL);
		vPanel.add(errorLBL);
		
		Button clearBTN = new Button("Clear");
		clearBTN.setStyleName("default-btn");
		clearBTN.addClickHandler(new ClearBTNClickHandler());
		vPanel.add(clearBTN);
	}
	
	/**
	 * Pulisce il contenuto delle labels
	 */
	private void resetLabels() {
		updateErrorLabel("");
		updateSuccessLabel("");
	}
	
	/**
	 * Ripristina i valori di default
	 */
	private void resetAll() {
		resetLabels();
		usernameTB.setText("");
		passwordTB.setText("");
		emailTB.setText("");
		nomeTB.setText("");
		cognomeTB.setText("");
		sessoTB.setText("");
		bDateDP.setValue(new Date());
		luogoNascitaTB.setText("");
		luogoResidenzaTB.setText("");
		socialNetworkTB.setText("");
	}
	
	@Override
	public void onUpdateForeground(Utente actualUser) {
		resetAll();
	}
	
	@Override
	public void updateErrorLabel(String txt) {
		errorLBL.setText(txt);
	}
	
	@Override
	public void updateSuccessLabel(String txt) {
		successLBL.setText(txt);
	}
	
	/**
	 * Gestisce il click del button Save: dopo una serie di controlli, permette la registrazione dell'utente base al sito
	 *
	 */
	private class SaveBTNClickHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			resetLabels();
			
			String username = usernameTB.getText().trim();
			String password = passwordTB.getText().trim();
			String email = emailTB.getText().trim();
			String nome = nomeTB.getText().trim();
			String cognome = cognomeTB.getText().trim();
			String sesso = sessoTB.getText().trim();
			Date bDate = (Date) bDateDP.getValue();
			String luogoNascita = luogoNascitaTB.getText().trim();
			String luogoResidenza = luogoResidenzaTB.getText().trim();
			String [] social = socialNetworkTB.getText().split(",");
			
			String errorMsg = "";
			
			ArrayList<String> emptyFields = new ArrayList<>();
			if(username.equals(""))
				emptyFields.add("username");
			if(password.equals(""))
				emptyFields.add("password");
			if(email.equals(""))
				emptyFields.add("email");
			
			
			if(emptyFields.size() > 0) {
				errorMsg += "The following fields must be filled: ";
				for(String field : emptyFields)
					errorMsg += field + " ";
			} else if(password.length() < 6) {
				errorMsg += "Password length must be at least 6 characters.";
			} else if(username.length() < 4) {
				errorMsg += "Username must be at least 4 characters.";
			} else if(!email.contains("@") || !email.contains(".") || email.length() < 8) {
				errorMsg += "Email format not valid.";
			} else if(sesso.length() > 1) {
				errorMsg += "Sesso must be one character m or f.";
			} else {
				Utente user = new Utente(username, password, email, nome, cognome, sesso, bDate, luogoNascita, luogoResidenza, social);
				clientImpl.registerUtente(user);
			}
			updateErrorLabel(errorMsg);
		}
	}
	
	
	/**
	 * Pulisce i dati inseriti nei campi
	 */
	private class ClearBTNClickHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			resetAll();
		}
	}
	
}