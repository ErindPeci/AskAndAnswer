package com.askandanswer.client.gui;

import java.util.ArrayList;

import com.askandanswer.client.model.Utente;
import com.askandanswer.client.service.ClientImplementor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Users extends Composite {

	private VerticalPanel contentPanel = new VerticalPanel();

	private ClientImplementor clientImpl;
	
	// lista degli users
	private ArrayList<Utente> usersList;
	
	public Users(ClientImplementor clientImplInput) {
		
		initWidget(this.contentPanel);
		this.clientImpl = clientImplInput;

		openUsersGui();
	}
	
	/**
	 *Metodo che apre/aggiorna la GUI degli Users 
	 */
	public void openUsersGui() {
		clientImpl.getUsersList(this);
		
		Timer timer = new Timer() {
			public void run() {
				setupContent();
			}
		};
		timer.schedule(100);
	}
	
	/**
	 * Stampa tutti gli users
	 */
	private void setupContent() {
		if(contentPanel.getWidgetCount() > 0)
			for(int i = 0; i < contentPanel.getWidgetCount(); i++)
				contentPanel.remove(i);
		
		VerticalPanel vPanel = new VerticalPanel();
		
		for(Utente val : usersList) {
			
			HorizontalPanel userBox = new HorizontalPanel();
			userBox.setStyleName("askBox questionBox");
			
			Label username = new Label();
			username.setStyleName("info-label");
			username.setText(val.getUsername());
			userBox.add(username);
			
			Label stato = new Label();
			stato.setStyleName("info-label data-label");
			stato.setText(val.isGiudice() ? "Utente Giudice" : "Utente Normale");
			if(val.isGiudice() == false)
				stato.setStyleName("info-label data-label status-label");	
			userBox.add(stato);
			
			if(val.isGiudice()== false) {
				Button editUser = new Button("Judge");
				editUser.setStyleName("default-btn");
				editUser.setTitle(val.getUsername());
				editUser.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						String title = event.getRelativeElement().getTitle();
						editUser(title);
					}
				});
				userBox.add(editUser);	
			}
			
			vPanel.add(userBox);
		}
		
		contentPanel.add(vPanel);
	}
	
	/**
	 * Set users data in userList
	 * 
	 * @param val
	 */
	public void setUsersList(ArrayList<Utente> val) {
		this.usersList = val;
	}
	
	/**
	 * Metodo per la modifica di un utente
	 *  
	 * @param username   username dell'utente
	 */
	private void editUser(String username) {
		clientImpl.editUser(this,username);
	}
	
}
