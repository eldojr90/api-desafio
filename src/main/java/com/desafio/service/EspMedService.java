package com.desafio.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desafio.model.EspMed;
import com.desafio.repository.EspMedRepository;

@Service
public class EspMedService {

	@Autowired
	EspMedRepository emr;
	
	public List<EspMed> findAll(){
		List<EspMed> pts = new ArrayList<>();
		emr.findAll().forEach(pts::add);
		return pts;
	}
	
	public EspMed findOne(int id) {
		
		Iterable<EspMed> ems = findAll();
				
				for(EspMed em:ems) {
					
					if(em.getId() == id) {
						
						return em;
						
					}
					
				}
				
				return null;
				
	}
	
	public boolean existsId(int id) {
		return findOne(id)!=null;
	}
	
}
