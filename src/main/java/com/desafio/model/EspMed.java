package com.desafio.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;

@Entity
@Table(
        name="espMed",
        uniqueConstraints=
            @UniqueConstraint(columnNames={"em_desc"})
    )
public class EspMed {

	@Id
	@GeneratedValue
	@Column(name="em_id")
	private int id;
	
	@Column(name="em_desc")
	private String desc;

	public EspMed() {
	}
	
	public EspMed(int id, String desc) {
		super();
		this.id = id;
		this.desc = desc;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
}
