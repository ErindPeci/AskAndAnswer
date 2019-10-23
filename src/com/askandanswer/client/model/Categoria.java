package com.askandanswer.client.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Categoria implements Serializable {

	private Categoria parent;
	private String nome;
	
	public Categoria() {
		
	}
	
	public Categoria (Categoria parent, String nome) {
		this.parent = parent;
		this.nome = nome;
	}

	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @param nome the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * @return the parent
	 */
	public Categoria getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(Categoria parent) {
		this.parent = parent;
	}
}
