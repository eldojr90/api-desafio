package com.desafio.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desafio.model.Paciente;
import com.desafio.repository.PacienteRepository;

@Service
public class PacienteService {

	@Autowired
	PacienteRepository pr;
	
	public List<Paciente> findAll(){
		List<Paciente> pts = new ArrayList<>();
		pr.findAll().forEach(pts::add);
		return pts;
	}
	
	public Paciente findOne(int id) {
		
		Iterable<Paciente> pts = findAll();
				
				for(Paciente p:pts) {
					
					if(p.getId() == id) {
						
						return p;
						
					}
					
				}
				
				return null;
				
	}

	public boolean existsId(int id) {
		return findOne(id)!=null;
	}
	
}
