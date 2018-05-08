package com.desafio.controller;

import java.sql.Timestamp;
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

import com.desafio.model.Consulta;
import com.desafio.model.Consultorio;
import com.desafio.model.EspMed;
import com.desafio.model.Medico;
import com.desafio.model.Paciente;
import com.desafio.service.ConsultaService;
import com.desafio.service.ConsultorioService;
import com.desafio.service.EspMedService;
import com.desafio.service.MedicoService;
import com.desafio.service.PacienteService;


@RestController
@RequestMapping("consulta") 
public class ConsultaController {
	
	@Autowired
	ConsultaService cs;
	
	@Autowired
	MedicoService ms;
	
	@Autowired
	PacienteService ps;
	
	@Autowired
	ConsultorioService cts;
	
	@Autowired
	EspMedService ems;

	@RequestMapping(method = RequestMethod.POST, value="/nova", 
					produces = MediaType.APPLICATION_JSON)
	public String novoConsulta(@RequestBody Consulta c) {
		
		String msg = "";
		
		try {
			msg = validacoes(c);
		} catch (Exception e) {
			msg = "Erro ao cadastrar consulta!";
		}
		
		return this.mensagem(msg);
		
	}
	
	@RequestMapping(value="/todas", method=RequestMethod.GET, 
					produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> todosConsultas() {
		
		List<Consulta> consultas = cs.getAllConsultas();
		
		if(consultas.size()>0) {
			return new ResponseEntity<List<Consulta>>(consultas,HttpStatus.OK);
		}else {
			return new ResponseEntity<String>(this.mensagem("Sem registros"),HttpStatus.OK);
		}
		
	}
	
	@RequestMapping(value="/pesquisar/id/{id}", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> filtarPelaId(@PathVariable int id) {
		
		try {
		
			if(cs.existsId(id)) {
				return new ResponseEntity<Consulta>(cs.findOne(id),HttpStatus.OK); 
			}else {
				return new ResponseEntity<String>(this.mensagem("Sem registros para a id "+id),HttpStatus.OK);
			}
			
		} catch (Exception e) {
			
			return new ResponseEntity<String>(this.mensagem(e.getMessage()),HttpStatus.OK);
			
		}
		
	}
	
	@RequestMapping(value="/excluir/{id}", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> excluirConsulta(@PathVariable int id) {
		
		try {
			
			if(cs.existsId(id)) {
				
				cs.deleteConsulta(id);
				
				return new ResponseEntity<String>(this.mensagem("Consulta excluída com sucesso!"),HttpStatus.OK);
				
			}else {
				return new ResponseEntity<String>(this.mensagem("Id "+id+" inexistente!"),HttpStatus.OK);
			}
			
		} catch (Exception e) {
			return new ResponseEntity<String>(this.mensagem("Erro interno. "+e.getMessage()),HttpStatus.OK);
		}
		
		
	}
	
	@RequestMapping(value="/pesquisar/datahora/{data}", method=RequestMethod.GET,
					produces=MediaType.APPLICATION_JSON)
	public ResponseEntity<?> filtrarPelaDataOuHora(@PathVariable String data){
		
		List<Consulta> consultas = cs.filterByData(data);
		
		if(consultas.size()>0) {
			return new ResponseEntity<List<Consulta>>(consultas,HttpStatus.OK);
		}else {
			return new ResponseEntity<String>(this.mensagem("Sem registros para a data: "+data),HttpStatus.OK);
		}
		
	}
	
	@RequestMapping(value="/test1/{idConsultorio}/{horario}",method=RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<String> teste1(@PathVariable int idConsultorio, @PathVariable Timestamp horario){
		
		String msg = "Horário de consulta "+(cs.validaIntervaloMin(idConsultorio, horario)?"":"in")+"disponível";

		return new ResponseEntity<String>(this.mensagem(msg),HttpStatus.OK);
		
	}
	
	@RequestMapping(value="/test2/{idPaciente}/{data}",method=RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<String> teste2(@PathVariable int idPaciente, 
										 @PathVariable Timestamp data){
		
		String msg = "Este paciente "+(cs.validaConsultaPaciente(idPaciente,data)?"não":"")+" tem consulta nesta data. ";

		return new ResponseEntity<String>(this.mensagem(msg),HttpStatus.OK);
		
	}
	
	@RequestMapping(value="/test3/{idMedico}/{data}",method=RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<String> teste3(@PathVariable int idMedico, 
										 @PathVariable Timestamp data){
		
		String msg = (cs.existeConsultaMedico(idMedico, data)?"T":"Não t")+"em consulta pra essa data";

		return new ResponseEntity<String>(this.mensagem(msg),HttpStatus.OK);
		
	}
	
	@RequestMapping(value="/test4/{idMedico}/{idConsultorio}/{data}",method=RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<String> teste4(@PathVariable int idMedico, 
										 @PathVariable int idConsultorio,							 
										 @PathVariable Timestamp data){
		
		String msg = "";
		
		if(cs.existeConsultaMedico(idMedico, data)) {
			
			if(cs.validaConsultaMedico(idMedico, idConsultorio, data)) {
				msg = "Ok! Consultório correto!";
			}else {
				
				Consulta c = cs.consultaImpedMed(idMedico, data);
				Consultorio con = c.getC();
				
				msg = 	"Consultório Ocupado por outro médico! "+ 
						"O médico especificado está atendendo no consultório id:"+con.getId()+
						" nº:00"+con.getNumero();
				
			}
			
		}else {
			if(cs.consultorioDisponivel(idMedico, idConsultorio, data)) {
				msg = "Esse consultório pode ser reservado para este médico nesta data.";
			}else {
				msg = "Consultório Ocupado! Tente outro";
			}
		}
		
		return new ResponseEntity<String>(this.mensagem(msg),HttpStatus.OK);
		
	}
	
	@RequestMapping(value="/test5/{idConsultorio}/{data}",method=RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<String> teste5(@PathVariable int idConsultorio,							 
										 @PathVariable Timestamp data){
		
		String msg = "O consultório "+
					  (cs.validaConsultaConsultorio(idConsultorio, data)?"ainda não":"")+
					  " excedeu o limite de consultas diárias";
		
		return new ResponseEntity<String>(this.mensagem(msg),HttpStatus.OK);
		
	}
	
	public String validacoes(Consulta c) {
		
		Medico med = ms.findOne(c.getM().getId());

		if(med != null) {
			
			EspMed em = ems.findOne(c.getE().getId());
			 
			if(em != null) {
		
				Paciente p = ps.findOne(c.getP().getId());
				
				if(p != null) {
					  
					Consultorio cons = cts.findOne(c.getC().getId());
					
					if(cons != null) {
						// validação de acordo com o limite de consultas/dia para o mesmo paciente
						if(cs.validaConsultaPaciente(p.getId(), c.getDataHora())) {
							// validação de acordo com o limite consultas/dia para o mesmo consultório (12)
							if(cs.validaConsultaConsultorio(cons.getId(),c.getDataHora())) {
								// verificação da existência de consulta para médico 	
								if(cs.existeConsultaMedico(med.getId(),c.getDataHora())) {
									// validação de consulta para o médico de acordo com o consultório
									if(cs.validaConsultaMedico(med.getId(), cons.getId(), c.getDataHora())) {
										return ultimoFluxoNovaConsulta(c);
									}else {
										Consultorio ci = cts.getConsultorio(c.getDataHora(), med.getId());
										return "O Dr. "+med.getNome()+" irá atender no consultório id:"+ci.getId() +
												" nº:00"+ci.getNumero();
									}
								}else {
									return ultimoFluxoNovaConsulta(c);
								}
								
							}else {
																
								Consultorio cnc = cts.findOne(c.getC().getId());
								
								return "Não foi possível marcar consulta. O constório id:"+cnc.getId()+" nº:00"+cnc.getNumero() +
										" excedeu o limite de consultas/dia (12).";
								
							}
							
						}else {
							
							Consulta cip = cs.consultaImpedPac(p.getId(), c.getDataHora());
							String horario = cip.getDataHora().toString(); 
							horario = horario.substring(horario.length()-11, horario.length()-5);
							
							return "Não foi possível marcar consulta. O paciente não pode marcar duas consultas pro mesmo dia. O mesmo possui uma consulta "+
									"marcada para a data informada às "+horario;
							
						}
						
					}else {
						
						return "Não foi possível agendar consulta. Não existe consultório com o id informado";
						
					}
				
				}else {
					
					return "Não foi possível agendar consulta. Não existe paciente com o id informado";
					
				}
				
			}else {
				
				return "Não foi possível agendar consulta. Não existe especialidade com o id informado";
				
			}
		
		}else {
			
			return "Não foi possível agendar consulta. Não existe médico com o id informado";
			
		}
		
	}
	
	public String ultimoFluxoNovaConsulta(Consulta c) {
		
		Consultorio cons = cts.findOne(c.getC().getId());
		EspMed em = ems.findOne(c.getE().getId());
		
		if(cs.consultorioDisponivel(c.getM().getId(),cons.getId(), c.getDataHora())) {
			
			if(!em.getDesc().equals("Cirurgião")) {
				if(!cs.validaIntervaloMin(cons.getId(), c.getDataHora())) {
					return "Não foi possível agendar consulta. Não é possível marcar no mesmo" +
							" horário para a especialidade "+em.getDesc()+".";
				}
			}else {
				if(!cs.validaAtdCirurgiao(cons.getId(), c.getM().getId(), c.getDataHora())) {
					return "Não foi possível agendar consulta. Selecione outro horário ou outro Cirurgião. Cirurgião com atendimento pra "+
							"esse horário ou este horário já possui 2 cirurgiões alocados.";
				}
			}
			
			cs.addConsulta(c);
			
			return "Consulta salva com sucesso!";
			
		}else {
			
			if(em.getDesc().equals("Cirurgião")) {
				
				if(cs.validaConsultaConsultorioCirurgiao(c.getM().getId(),cons.getId(), c.getDataHora())) {
					
					if(!cs.validaAtdCirurgiao(cons.getId(), c.getM().getId(), c.getDataHora())) {
						return "Não foi possível agendar consulta. Já existem 2 atendimentos pra esse horário.";
					}
					
					cs.addConsulta(c);
					
					return "Consulta salva com sucesso!";
					
				}else {
				
					List<Medico> cirurgioes = ms.cirurgioesPorConsultorio(cons.getId(), c.getDataHora());
					Medico m1 = cirurgioes.get(0);
					Medico m2 = cirurgioes.get(1);
					
					return "Não foi possível marcar consulta. Limite de Cirurgiões atingido para consultório id:"+cons.getId()+
							" nº:00"+cons.getNumero()+" Dr. "+m1.getNome()+" - CRM: "+m1.getCrm()+
							" e Dr. "+m2.getNome()+" - CRM: "+m2.getCrm();
					
				}
				
			}else {
				
				return "Não foi possível marcar consulta. O consultório id:"+cons.getId() + " nº:00"+cons.getNumero()+
						" não está disponível para esta data.";
			}
			
		}
		
	}
	
	public String mensagem(String msg) {
		return "{\"mensagem\":\""+msg+"\"}";
	}
	
	
}
