package com.desafio.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;

@Entity
@Table(name="consultorio",
		uniqueConstraints=
				@UniqueConstraint(columnNames= {"co_numero"})
		)
public class Consultorio {

	@Id
	@GeneratedValue
	@Column(name="co_id")
	private int id;
	
	@Column(name="co_numero")
	private int numero;

	public Consultorio() {
		
	}
	
	public Consultorio(int id, int numero) {
		super();
		this.id = id;
		this.numero = numero;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNumero(){
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}
	
}
