package com.askandanswer.client.gui;

import java.util.ArrayList;

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

public class Login extends Composite implements Foreground {
	
	private VerticalPanel vPanel = new VerticalPanel();
	private Grid gridPanel = new Grid(3, 2);
	
	private TextBox usernameTB;
	private PasswordTextBox passwordTB;
	
	private Label errorLBL;
	private Label successLBL;
	
	private ClientImplementor clientImpl;
	
	public Login(ClientImplementor clientImpl) {
		initWidget(this.vPanel);
		this.clientImpl = clientImpl;
		
		// Text boxes
		Label username = new Label("Username: ");
		usernameTB = new TextBox();
		usernameTB.setStyleName("default-tb");
		usernameTB.getElement().setPropertyString("placeholder", "Username");
		gridPanel.setWidget(0, 0, username);
		gridPanel.setWidget(0, 1, usernameTB);
		
		Label password = new Label("Password: ");
		passwordTB = new PasswordTextBox();
		passwordTB.setStyleName("default-tb");
		passwordTB.getElement().setPropertyString("placeholder", "Password");
		gridPanel.setWidget(1, 0, password);
		gridPanel.setWidget(1, 1, passwordTB);
		
		// Labels
		errorLBL = new Label("");
		errorLBL.setStyleName("error-label");
		successLBL = new Label("");
		successLBL.setStyleName("success-label");
		
		// Buttons
		Button loginBTN = new Button("Login");
		loginBTN.setStyleName("default-btn");
		loginBTN.addClickHandler(new LoginBTNClickHandler());
		gridPanel.setWidget(2, 0, loginBTN);
		
		gridPanel.setStyleName("padding-table");
		vPanel.add(gridPanel);
		vPanel.add(successLBL);
		vPanel.add(errorLBL);
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
	 * Gestisce il click del button Login: esegue l'accesso dell'utente
	 */
	private class LoginBTNClickHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			resetLabels();
			
			String username = usernameTB.getText().trim();
			String password = passwordTB.getText().trim();
			
			String errorMsg = "";
			
			ArrayList<String> emptyFields = new ArrayList<>();
			if(username.equals(""))
				emptyFields.add("username");
			if(password.equals(""))
				emptyFields.add("password");
			
			if(emptyFields.size() > 0) {
				errorMsg += "The following fields must be filled: ";
				for(String field : emptyFields)
					errorMsg += field + " ";
			} else if((password.length() < 6)&&(username!="admin")) {
				errorMsg += "Password length must be at least 6 characters.";
			} else {
				clientImpl.login(username, password);
			}
			updateErrorLabel(errorMsg);
		}
	}
	
}

