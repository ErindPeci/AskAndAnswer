package com.askandanswer.client.gui;

import com.askandanswer.client.model.Utente;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;



public class Profile extends Composite {

	private VerticalPanel vPanel = new VerticalPanel();

	private Utente actualUser;
	
	public Profile(Utente actualUserInput) {
		initWidget(this.vPanel);
		this.actualUser = actualUserInput;
		VerticalPanel userData=new VerticalPanel();
		
		Label nome = new Label();
		nome.setStyleName("profile-label");
		nome.setText("Nome: " + actualUser.getNome());
		userData.add(nome);
		
		Label cognome = new Label();
		cognome.setStyleName("profile-label");
		cognome.setText("cognome: " + actualUser.getCognome());
		userData.add(cognome);
		
		Label email = new Label();
		email.setStyleName("profile-label");
		email.setText("Email: " + actualUser.getEmail());
		userData.add(email);
		
		Label sesso = new Label();
		sesso.setStyleName("profile-label");
		sesso.setText("Sesso: " + actualUser.getSesso());
		userData.add(sesso);
		
		Label dataNascita = new Label();
		dataNascita.setStyleName("profile-label");
		DateTimeFormat fmt = DateTimeFormat.getFormat("dd/MM/yyyy");
		dataNascita.setText("Data di nascita: " + fmt.format(actualUser.getDataDiNascita()));
		userData.add(dataNascita);
		
		Label luogoNascita = new Label();
		luogoNascita.setStyleName("profile-label");
		luogoNascita.setText("Luogo di nascita: " + actualUser.getLuogoDiNascita());
		userData.add(luogoNascita);
		
		Label luogoResidenza = new Label();
		luogoResidenza.setStyleName("profile-label");
		luogoResidenza.setText("Luogo di residenza: "+actualUser.getLuogoDiResidenza());
		userData.add(luogoResidenza);
		
		String[] split = actualUser.getSocialNetwork(); 
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < split.length; i++) {
		    sb.append(split[i]);
		    if (i != split.length - 1) {
		        sb.append(" ");
		    }
		}
		String joined = sb.toString();
		
		Label socialNetwork = new Label();
		socialNetwork.setStyleName("profile-label");
		socialNetwork.setText("Social network: " +  joined);
		userData.add(socialNetwork);

		vPanel.add(userData);
	}
}