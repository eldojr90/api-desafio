package com.desafio.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Medico {

	@Id
	@GeneratedValue
	@Column(name="m_id")
	private int id;
	
	@Column(name="m_crm",unique=true)
	private String crm;
	
	@Column(name="m_nome",unique=true)
	private String nome;
	
	@Column(name="m_idade")
	private int idade;

	public Medico() {
	}
	
	public Medico(int id, String crm, String nome, int idade) {
		super();
		this.id = id;
		this.crm = crm;
		this.nome = nome;
		this.idade = idade;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCrm() {
		return crm;
	}

	public void setCrm(String crm) {
		this.crm = crm;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getIdade() {
		return idade;
	}

	public void setIdade(int idade) {
		this.idade = idade;
	}
			
}
