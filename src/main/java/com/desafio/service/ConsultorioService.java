package com.desafio.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desafio.db.ConDB;
import com.desafio.model.Consultorio;
import com.desafio.repository.ConsultorioRepository;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

@Service
public class ConsultorioService {

	@Autowired
	ConsultorioRepository cr;

	public List<Consultorio> findAll(){
		List<Consultorio> cts = new ArrayList<>();
		cr.findAll().forEach(cts::add);
		return cts;
	}
	
	public Consultorio findOne(int id) {
		
		Iterable<Consultorio> cts = findAll();
		
		for(Consultorio c:cts) {
			
			if(c.getId() == id) {
				
				return c;
				
			}
			
		}
		
		return null;
		
	}
	
	public boolean existsId(int id) {
		
		return findOne(id)!=null;
		
	}
	
	public Consultorio getConsultorio(Timestamp data, int idMedico) {
		
		String sql = 	"select distinct c_co_id " + 
						"from consulta " + 
						"where date_format(data_hora,'%d%m%y') = " + 
						" date_format(?,'%d%m%y') " + 
						"and m_m_id = ?";
		
		Connection con = ConDB.getConnection();
		
		try {
			
			PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
			ps.setTimestamp(1, data);
			ps.setInt(2, idMedico);
			
			ResultSet rs = ps.executeQuery();
			rs.next();
			
			return this.findOne(rs.getInt(1));
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
