package com.askandanswer.test;

import java.util.ArrayList;
import java.util.Date;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.askandanswer.client.model.Categoria;
import com.askandanswer.client.model.Domanda;
import com.askandanswer.client.model.Risposta;
import com.askandanswer.client.model.Utente;
import com.askandanswer.client.service.StandardService;
import com.askandanswer.client.service.StandardServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StandardServiceImplementorTest extends GWTTestCase {
	
	private StandardServiceAsync standardService;
	private ServiceDefTarget target;

	/**
	 * Setup del server	 
	 */
	private void setUpServer() {
		standardService = GWT.create(StandardService.class);
		target = (ServiceDefTarget) standardService;
		target.setServiceEntryPoint(GWT.getModuleBaseURL() + "askandanswer/standardservice");
		delayTestFinish(10000);
	}
	
	
	// *******  Sprint 1  ********
	
	
	/**
	 * Test per la registrazione di un nuovo utente
	 */
	@Test
	public void test0RegisterUser() {
		setUpServer();
		
		// Clear all data
		standardService.clearDBUsers(new DefaultCallback());
		standardService.clearDBCategorie(new DefaultCallback());
		standardService.clearDBDomande(new DefaultCallback());
		standardService.clearDBRisposte(new DefaultCallback());

		String[]socialNet = {"facebook","twitter"};
		Utente userTest = new Utente("testNickname", "testpswd", "test@a.a", "testnome", 
				"testcognome", "m", new Date(), "testLuogoDiNasc", "testLuogoDiRes", socialNet  );
		standardService.registerUtente(userTest, new TrueCallback());
		standardService.registerUtente(userTest, new FalseCallback());
		
		Utente user1 = new Utente("mario", "mmmmmm", "mario@yahoo.com", "Mario", 
				"rossi", "m", new Date(), "Bologna", "Bologna", socialNet  );
		Utente user2 = new Utente("francesco", "ffffff", "francesco@yahoo.com", "Francesco", 
				"Verdi", "m", new Date(), "Rimini", "Ravenna", socialNet  );		
		Utente user3 = new Utente("Gioia", "gggggg", "gioia@yahoo.com", "Gioia", 
				"Castani", "f", new Date(), "Firenze", "Milano", socialNet  );
		standardService.registerUtente(user1, new TrueCallback());
		standardService.registerUtente(user2, new TrueCallback());
		standardService.registerUtente(user3, new TrueCallback());
	}

	/**
	 * Test per la lettura di un utente dal database
	 */
	@Test
	public void test1GetUser() {
		setUpServer();
		standardService.getUtente("testNickname", new UserCallback());
		standardService.getUtente("Test", new NullCallback());
		standardService.getUtente("", new NullCallback());
	}
	
	/**
	 * Test per il login di un utente
	 */
	@Test
	public void test2Login() {
		setUpServer();
		standardService.login("testNickname", "testpswd", new UserCallback());
		standardService.login("testNickname", "Testpswd", new NullCallback());
		standardService.login("", "", new NullCallback());
	}
	
	
	/**
	 * Test per l'inserimento di una domanda 
	 */
	@Test
	public void test3SaveQuestion() {
		setUpServer();
		
		String[]socialNet = {"facebook","twitter"};
		Utente user1 = new Utente("mario", "mmmmmm", "mario@yahoo.com", "Mario", 
				"rossi", "m", new Date(), "Bologna", "Bologna", socialNet  );
		
		Categoria cat = new Categoria(null, "Svago");
		
		Domanda domanda = new Domanda(cat, "How are you ?", user1, null);
		Domanda domanda1 = new Domanda(cat,"Everything ok ?", user1, null);
		
		standardService.saveQuestion(domanda, new NullCallback());
		standardService.saveQuestion(domanda1,new NullCallback());
	}

	
	/**
	 * Test per la lettura di una domanda 
	 */
	@Test
	public void test4GetDomande() {
		setUpServer();
		
		standardService.getDomande(new DomandeListCallback());
	
	}
	
	/**
	 * Test per la lettura di una categoria 
	 */
	@Test 
	public void test5GetCategoria() {
		setUpServer();
		
		standardService.getCategorie(new CategoriaCallback());
	}
	
	
	/**
	 * Test per l'inserimento di una risposta 
	 */
	@Test
	public void test6SalvaRisposta() {
		setUpServer();
		
		String[]socialNet = {"facebook","twitter"};
		Utente user1 = new Utente("mario", "mmmmmm", "mario@yahoo.com", "Mario", 
				"rossi", "m", new Date(), "Bologna", "Bologna", socialNet  );
		
		Risposta risposta = new Risposta("content of answer", user1, 1, null);
		
		standardService.salvaRisposta(risposta, new NullCallback());
	}
	
	/**
	 * Test per la lettura delle risposte 
	 */
	@Test
	public void test7GetRisposte() {
		setUpServer();
		
		standardService.getRisposte(1, new RisposteCallback());
	}
	
	/**
	 * Test per la lettura delle risposte 
	 */
	@Test
	public void test8GetSessionUser() {
		setUpServer();
		
		standardService.getSessionUser(new UserCallback());
	}
	
	
	// *******  Sprint 2  ********
	
	/**
	 * Test per l'elimina di una domanda
	 */
	@Test 
	public void test90EliminaDomanda() {
		setUpServer();
		
		standardService.eliminaDomanda(1L,new TrueCallback());
	}
	
	
	/**
	 * Test per la lettura della lista degli users
	 */
	@Test
	public void test91GetUsersList() {
		setUpServer();
		
		standardService.getUsers(new UsersListCallback());
	}
	
	
	/**
	 * Test per la modifica di un Utente 
	 */
	@Test
	public void test92EditUtente() {
		setUpServer();
		
		standardService.editUtente("testNickname" ,new TrueCallback());
		standardService.editUtente("Test", new FalseCallback());
	}
	
	
	/**
	 * Test per dare un voto ad una risposta 
	 */
	@Test
	public void test93VotaRisposta() {
		setUpServer();
		standardService.votaRisposta(1, 4, new NullCallback());
	}
	
	/**
	 * Test per l'elimina di una risposta
	 */
	@Test 
	public void test94EliminaRisposta() {
		setUpServer();
		
		standardService.eliminaRisposta(1L,new TrueCallback());
	}
	
	/**
	 * Test per il save di una Categoria
	 */
	@Test
	public void test95SalvaCategoria() {
		setUpServer();
		
		Categoria categoria1 = new Categoria(null,"Filosofia");
		
		standardService.salvaCategoria(categoria1, new NullCallback());
	}
	
	/**
	 * Test per l'edit di una Categoria
	 */
	@Test
	public void test96EditCategoria() {
		setUpServer();
		
		standardService.editCategoria("Filosofia", "Scienze e IT",new NullCallback());
	}
	
	/**
	 * Test per l'edit della categoria di una domanda
	 */
	@Test
	public void test97EditDomanda() {
		setUpServer();
		
		standardService.editDomanda(2L, "Scienze e IT", new NullCallback());
	}
	
	/**
	 * Test per l'elimina di una Categoria
	 */
	@Test 
	public void test98EliminaCategoria() {
		setUpServer();
		
		standardService.eliminaCategoria("Scienze e IT",new NullCallback());
	}
	

	
	@Override
	public String getModuleName() {
		return	"com.askandanswer.AskAndAnswerTest";
	}
	
	/* Callback classes */
	
	private class DefaultCallback implements AsyncCallback<Object> {
		@Override
		public void onFailure(Throwable caught) { }

		@Override
		public void onSuccess(Object result) { finishTest(); }
	}

	private class TrueCallback implements AsyncCallback<Boolean> {
		@Override
		public void onFailure(Throwable caught) {
			fail("Request failure: " + caught.getMessage());
		}

		@Override
		public void onSuccess(Boolean result) {
			assertTrue("Must be true", result);
			finishTest();
		}
	}

	private class FalseCallback implements AsyncCallback<Boolean> {
		@Override
		public void onFailure(Throwable caught) {
			fail("Request failure: " + caught.getMessage());
		}

		@Override
		public void onSuccess(Boolean result) {
			assertFalse("Must be false", result);
			finishTest();
		}
	}

	private class NullCallback implements AsyncCallback<Object> {
		@Override
		public void onFailure(Throwable caught) {
			fail("Request failure: " + caught.getMessage());
		}

		@Override
		public void onSuccess(Object result) {
			assertNull("Must be null", result);
			finishTest();
		}
	}

	private class UserCallback implements AsyncCallback<Object> {
		@Override
		public void onFailure(Throwable caught) {
			fail("Request failure: " + caught.getMessage());
		}

		@Override
		public void onSuccess(Object result) {
			assertTrue("Must be of type User", result instanceof Utente);
			finishTest();
		}
	}
	
	private class DomandeListCallback implements AsyncCallback<ArrayList<Domanda>> {
		@Override
		public void onFailure(Throwable caught) {
			fail("Request failure: " + caught.getMessage());
		}

		@Override
		public void onSuccess(ArrayList<Domanda> result) {
			assertTrue("Must have size equals to 1", result.size()==2);
			finishTest();
		}
	}
	
	private class CategoriaCallback implements AsyncCallback<ArrayList<Categoria>>{

		@Override
		public void onFailure(Throwable caught) {
			fail("Request failure: " + caught.getMessage());
			
		}

		@Override
		public void onSuccess(ArrayList<Categoria> result) {
			assertTrue("Must have size equals to 6" , result.size()==6);
			finishTest();
		}
		
	}
	private class RisposteCallback implements AsyncCallback<ArrayList<Risposta>>{

		@Override
		public void onFailure(Throwable caught) {
			fail("Request failure: " + caught.getMessage());
			
		}

		@Override
		public void onSuccess(ArrayList<Risposta> result) {
			assertTrue("Must have size equals to 1" , result.size()==1);
			finishTest();
		}
		
	}
	
	private class UsersListCallback implements AsyncCallback<ArrayList<Utente>>{

		@Override
		public void onFailure(Throwable caught) {
			fail("Request failure: " + caught.getMessage());
			
		}

		@Override
		public void onSuccess(ArrayList<Utente> result) {
			assertTrue("Must have size equals to 4" , result.size()==4);
			finishTest();
		}
		
	}
}