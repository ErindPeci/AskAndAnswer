package com.askandanswer.client.gui;

import java.util.ArrayList;

import com.askandanswer.client.model.Categoria;
import com.askandanswer.client.service.ClientImplementor;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Categorie extends Composite {
	
	
	private VerticalPanel contentPanel = new VerticalPanel();
	
	private ClientImplementor clientImpl;
	
	// lista delle categorie
	private ArrayList<Categoria> categorie =null;
	
	// text input e lista per il nome e il parent di una nuova categoria 
	private TextBox newNomeTB;
	private ListBox newCatParent;
	
	
	public Categorie(ClientImplementor clientImplInput ,  ArrayList<Categoria> categorie) {
			initWidget(contentPanel);
			this.clientImpl=clientImplInput;
			this.categorie=categorie;
			setupContent();
	}
	
	/**
	 * Riempie l'interfaccia con i dati delle categorie e imposta 
	 * le funzionalità che permettono di creare, modificare o eliminare una sotto/categoria.
	 */
	public void setupContent() {
		if(contentPanel.getWidgetCount() > 0)
			for(int i = 0; i < contentPanel.getWidgetCount(); i++)
				contentPanel.remove(i);
		
		VerticalPanel vPanel = new VerticalPanel();
		
		// salva le categorie predefinite in un'ArrayList
		String[] predf = {"Ambiente", "Animali", "Arte e Cultura","Elettronica e Tecnologia", "Sport", "Svago"};
		ArrayList<String> predefinite =new ArrayList<String>();
		for(int i = 0; i < 6; i++) {
			predefinite.add(predf[i]);
		}
		
		// start printing all categories
		for(Categoria val : categorie) {
			
			if(val.getParent() == null) {
			
				VerticalPanel categBox = new VerticalPanel();
				categBox.setStyleName("categorieBox");
				
				
				HorizontalPanel infoBox = new HorizontalPanel();	
				
				TextBox nomeTB = new TextBox();
				nomeTB.setStyleName("default-tb");
				nomeTB.setTitle(val.getNome());
				nomeTB.setText(val.getNome());
				infoBox.add(nomeTB);
				
				Button editBTN = new Button("Rename");
				editBTN.setStyleName("default-btn edit-btn");
				editBTN.setTitle(val.getNome());
				editBTN.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						InputElement newVal = (InputElement) event.getRelativeElement().getParentElement().getPreviousSiblingElement().getFirstChildElement();
						editCategoria(event.getRelativeElement().getTitle() , newVal.getValue());
						
					}
				});
				
				Button deleteBTN = new Button("Delete");
				deleteBTN.setStyleName("default-btn delete-btn");
				deleteBTN.setTitle(val.getNome());
				deleteBTN.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						eliminaCategoria(event.getRelativeElement().getTitle());
					}
				});
				
				Label pred = new Label("Predefinita");
				pred.setStyleName("default-btn predefinita");
				
				if(predefinite.contains(val.getNome())) {
					infoBox.add(pred);
				}else {
					infoBox.add(editBTN);
					infoBox.add(deleteBTN);
				}
					
				
				categBox.add(infoBox);
				
				// print subcategories of category
				for(Categoria val2 : categorie) {
					if(val2.getParent() != null) {
						if(val2.getParent().getNome() == val.getNome()) {
							HorizontalPanel infoBox2 = new HorizontalPanel();	
							
							TextBox nomeTB2 = new TextBox();
							nomeTB2.setStyleName("default-tb");
							nomeTB2.setText(val2.getNome());
							infoBox2.add(nomeTB2);
							
							Button edit2 = new Button("Rename");
							edit2.setStyleName("default-btn edit-btn");
							edit2.setTitle(val2.getNome());
							edit2.addClickHandler(new ClickHandler() {
								
								@Override
								public void onClick(ClickEvent event) {
									InputElement newVal = (InputElement) event.getRelativeElement().getParentElement().getPreviousSiblingElement().getFirstChildElement();
									editCategoria(event.getRelativeElement().getTitle() , newVal.getValue());
									
								}
							});
							infoBox2.add(edit2);
							
							Button deleteBTN2 = new Button("Delete");
							deleteBTN2.setStyleName("default-btn delete-btn");
							deleteBTN2.setTitle(val2.getNome());
							deleteBTN2.addClickHandler(new ClickHandler() {
								
								@Override
								public void onClick(ClickEvent event) {
									eliminaCategoria(event.getRelativeElement().getTitle());
								}
							});
							infoBox2.add(deleteBTN2);
							
							categBox.add(infoBox2);
							
							// print subcategories of subcategory 
							for(Categoria val3 : categorie) {
								if(val3.getParent() != null) {
									if(val3.getParent().getNome() == val2.getNome()) {
										HorizontalPanel infoBox3 = new HorizontalPanel();	
										
										TextBox nomeTB3 = new TextBox();
										nomeTB3.setStyleName("default-tb");
										nomeTB3.setText(val3.getNome());
										infoBox3.add(nomeTB3);
										
										Button edit3 = new Button("Rename");
										edit3.setStyleName("default-btn edit-btn");
										edit3.setTitle(val3.getNome());
										edit3.addClickHandler(new ClickHandler() {
											
											@Override
											public void onClick(ClickEvent event) {
												InputElement newVal = (InputElement) event.getRelativeElement().getParentElement().getPreviousSiblingElement().getFirstChildElement();
												editCategoria(event.getRelativeElement().getTitle() , newVal.getValue());
												
											}
										});
										infoBox3.add(edit3);
										
										Button deleteBTN3 = new Button("Delete");
										deleteBTN3.setStyleName("default-btn delete-btn");
										deleteBTN3.setTitle(val3.getNome());
										deleteBTN3.addClickHandler(new ClickHandler() {
											
											@Override
											public void onClick(ClickEvent event) {
												eliminaCategoria(event.getRelativeElement().getTitle());
											}
										});
										infoBox3.add(deleteBTN3);
										
										categBox.add(infoBox3);
										
									}
								}
							}
							
						}
					}
				}
				
				vPanel.add(categBox);
			}
		}
		
		// Start insert new category
		HorizontalPanel newCatBox = new HorizontalPanel();
		newCatBox.setStyleName("askBox");
		newNomeTB = new TextBox();
		newNomeTB.setStyleName("default-tb");
		newNomeTB.getElement().setPropertyString("placeholder", "Name of new category...");
		newCatBox.add(newNomeTB);
		
		// listbox of all sub/categories
		newCatParent = new ListBox();
		newCatParent.setStyleName("newCat-LB");
		newCatParent.addItem("No parent", "no");
		for(Categoria cat : categorie) {
			// print super categories
			if(cat.getParent() == null) {
				newCatParent.addItem(cat.getNome(), cat.getNome());
				
				// print subcategory of category
				for(Categoria cat2 : categorie) {
					if(cat2.getParent() != null) {
						if(cat2.getParent().getNome() == cat.getNome()) {
							newCatParent.addItem("---> "+cat2.getNome(), cat2.getNome());
						}
					}
				}
			}
		}
		newCatBox.add(newCatParent);
		
		Button addBTN = new Button("Add");
		addBTN.setStyleName("default-btn");
		addBTN.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				String nome = newNomeTB.getValue();
				String parent = newCatParent.getSelectedValue();
				addNewCategory(nome , parent);
			}
		});
		newCatBox.add(addBTN);
		// End insert new category
		
		vPanel.add(newCatBox);
		
		contentPanel.add(vPanel);
	}
	
	/**
	 * Metodo per modificare(rinominare) una categoria
	 * @param oldVal  vecchio nome della categoria 
	 * @param newVal  nuovo nome della categoria
	 */
	private void editCategoria(String oldVal, String newVal) {
		clientImpl.editCategoria(this, oldVal, newVal.trim());
	}
	
	/**
	 * Metodo per aggiungere una nuova categoria
	 * @param nome  nome della categoria
	 * @param parentName  il parent della categoria
	 */
	private void addNewCategory(String nome, String parentName) {
		
		Categoria parent = null;
		for(Categoria val : categorie) {
			if(val.getNome() == parentName)
				parent = val;
		}
		Categoria newCat = new Categoria(parent, nome);
		clientImpl.salvaCategoria(this, newCat);
		
	}
	
	
	/**
	 * Metodo per eliminare una categoria
	 * @param nome   nome della categoria
	 */
	private void eliminaCategoria(String nome) {
		clientImpl.eliminaCategoria(this, nome);
	}
	
	/**
	 * Metodo per aggiornare la lista delle categorie
	 * @param val  nuova lista aggiornata
	 */
	public void setCatList(ArrayList<Categoria> val) {
		this.categorie = val;
	}
}
