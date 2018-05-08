package com.desafio.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.OneToOne;

@Entity
public class Consulta {

	@Id
	@GeneratedValue
	@Column(name="ca_id")
	private int id;
	private Timestamp dataHora;
	
	@OneToOne
	private Paciente p;
	
	@OneToOne
	private Medico m;
	
	@OneToOne
	private EspMed e;
	
	@OneToOne
	private Consultorio c;
	
	public Consulta() {
	}
	
	public Consulta(int id, Timestamp dataHora, Paciente p, Medico m, EspMed e, Consultorio c) {
		this.id = id;
		this.dataHora = dataHora;
		this.p = p;
		this.m = m;
		this.e = e;
		this.c = c;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Timestamp getDataHora() {
		return dataHora;
	}

	public void setDataHora(Timestamp dataHora) {
		this.dataHora = dataHora;
	}

	public Paciente getP() {
		return p;
	}

	public void setP(Paciente p) {
		this.p = p;
	}

	public Medico getM() {
		return m;
	}

	public void setM(Medico m) {
		this.m = m;
	}

	public EspMed getE() {
		return e;
	}

	public void setE(EspMed e) {
		this.e = e;
	}

	public Consultorio getC() {
		return c;
	}

	public void setC(Consultorio c) {
		this.c = c;
	}
	
}
