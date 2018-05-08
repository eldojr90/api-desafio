package com.desafio.controller;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.desafio.model.Medico;
import com.desafio.service.MedicoService;


@RestController
@RequestMapping("medico") 
public class MedicoController {
	
	@Autowired
	MedicoService ms;

	@RequestMapping(method = RequestMethod.POST, value="/novo")
	public String novoMedico(@RequestBody Medico m) {
		
		String msg = "";
		
		try {
			
			if(!ms.existsCrm(m.getCrm())) {
				
				if(!ms.existsName(m.getNome())) {
				
					ms.addMedico(m);
					msg = "Médico "+m.getNome()+" cadastrado com sucesso!";
				
				}else {
				
					msg = "O nome "+m.getNome()+" já está sendo utilizado!";
				
				}
				
			}else {
				
				msg = "O CRM "+m.getCrm()+" já está sendo utilizado!";
				
			}
			
			
		} catch (Exception e) {
			msg = "Erro ao cadastrar médico! "+e.getMessage();
		}
		
		return this.mensagem(msg);
	}
	
	@RequestMapping(value="/todos", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> todosMedicos() {
		
		List<Medico> mds = ms.getAllMedicos();
		
		try {
			
			if(mds.size()>0) {
				return new ResponseEntity<List<Medico>>(mds,HttpStatus.OK);
			}else {
				return new ResponseEntity<String>(this.mensagem("Sem registros"),HttpStatus.OK);
			}
			
		} catch (Exception e) {
			
			return new ResponseEntity<String>(this.mensagem("Erro interno. "+e.getMessage()),HttpStatus.OK);
			
		}
		
	}
	
	@RequestMapping(value="/pesquisar/id/{id}", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> filtarPelaId(@PathVariable int id) {
		
		try {
		
			Medico m = ms.findOne(id); 
			
			if(m!=null) {
				return new ResponseEntity<Medico>(m,HttpStatus.OK); 
			}else {
				return new ResponseEntity<String>(this.mensagem("Sem registros para a id "+id),HttpStatus.OK);
			}
			
		} catch (Exception e) {
			
			return new ResponseEntity<String>(this.mensagem(e.getMessage()),HttpStatus.OK);
			
		}
		
	}
	
	@RequestMapping(value="/excluir/{id}", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> excluirMedico(@PathVariable int id) {
		
		try {
			
			Medico m = ms.findOne(id);
			
			if(m!=null) {
				
				ms.deleteMedico(id);
				
				return new ResponseEntity<String>(this.mensagem("Médico "+m.getNome()+" excluído com sucesso!"),HttpStatus.OK);
				
			}else {
				return new ResponseEntity<String>(this.mensagem("Id "+id+" inexistente!"),HttpStatus.OK);
			}
			
		} catch (Exception e) {
			return new ResponseEntity<String>(this.mensagem("Erro interno. "+e.getMessage()),HttpStatus.OK);
		}
		
		
	}
	
	public String mensagem(String msg) {
		return "{\"mensagem\":\""+msg+"\"}";
	}
		
}
