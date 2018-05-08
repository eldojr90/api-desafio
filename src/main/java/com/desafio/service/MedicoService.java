package com.desafio.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desafio.db.ConDB;
import com.desafio.model.Medico;
import com.desafio.repository.MedicoRepository;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

@Service
public class MedicoService {

	@Autowired
	MedicoRepository mr;
	
	public void addMedico(Medico m) {
		mr.save(m);
	}
	
	public List<Medico> getAllMedicos() {
		List<Medico> medicos = new ArrayList<>();
		mr.findAll().forEach(medicos::add);
		return medicos;
	}
	
	public Medico findOne(int id) {
		
		Iterable<Medico> mds = mr.findAll();
		
		for(Medico m:mds) {
			if(m.getId() == id) {
				System.out.println("ID AQUI!");
				return m;
			}
		}
		
		return null;
	}
	
	public void deleteMedico(int id) {
		mr.delete(this.findOne(id));
	}
	
	public boolean existsName(String name) {
		
		Iterable<Medico> mds = mr.findAll();
		
		for(Medico m:mds) {
			if(m.getNome().equals(name)) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean existsCrm(String crm) {
		
		Iterable<Medico> mds = mr.findAll();
		
		for(Medico m:mds) {
			if(m.getCrm().equals(crm)) {
				return true;
			}
		}
		
		return false;
		
	}
	
	public List<Medico> cirurgioesPorConsultorio(int idConsultorio, Timestamp data){
		List<Medico> cirurgioes = new ArrayList<>();
		
		String sql = 	"select distinct(m.m_id)\n" + 
						"from consulta c " + 
						"inner join medico m " + 
						"on (c.m_m_id = m.m_id) " + 
						"inner join esp_med em  " + 
						"on (c.e_em_id = em.em_id) " + 
						"where c_co_id = ? " + 
						"and em.em_desc = 'Cirurgião' " + 
						"and date_format(data_hora,'%d/%m/%y') =  " + 
						"date_format(?,'%d/%m/%y') " + 
						"order by m_nome;";
		
		Connection con = ConDB.getConnection();
		
		try {
			
			PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
			ps.setInt(1,idConsultorio);
			ps.setTimestamp(2, data);
			
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				
				Medico m = findOne(rs.getInt(1));
				
				cirurgioes.add(m);
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return cirurgioes;
		
	}
	
	
	
}
