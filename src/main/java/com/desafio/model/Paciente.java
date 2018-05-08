package com.desafio.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;

@Entity
@Table(
        name="paciente",
        uniqueConstraints=
            @UniqueConstraint(columnNames={"p_nome"})
		)
public class Paciente {

	@Id
	@GeneratedValue
	@Column(name="p_id")
	private int id;
	
	@Column(name="p_nome")
	private String nome;

	public Paciente() {
	}
	
	public Paciente(int id, String nome) {
		super();
		this.id = id;
		this.nome = nome;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
			
}
