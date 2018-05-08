package com.desafio.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desafio.db.ConDB;
import com.desafio.model.Consulta;
import com.desafio.repository.ConsultaRepository;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

@Service
public class ConsultaService {

	@Autowired
	ConsultaRepository cr;
	
	public void addConsulta(Consulta m) {
		cr.save(m);
	}
	
	public List<Consulta> getAllConsultas() {
		List<Consulta> Consultas = new ArrayList<>();
		cr.findAll().forEach(Consultas::add);
		return Consultas;
	}
	
	public List<Consulta> findAll(){
		
		List<Consulta> cs = new ArrayList<>();
		
		String sql = "select ca_id " +
					 "from consulta " +
					 "order by data_hora;";
		
		Connection con = ConDB.getConnection();
		
		try {
			
			Statement st = (Statement) con.createStatement();
			
			ResultSet rs = st.executeQuery(sql);
			
			while(rs.next()) {
				cs.add(this.findOne(rs.getInt(1)));
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return cs;
	}
	
	public List<Consulta> filterByData(String data){
		
		List<Consulta> cs = new ArrayList<>();
		
		String sql = "select ca_id " +
					 "from consulta " +
					 "where date_format(data_hora, '%d/%m/%Y') = "+
					 "date_format(?, '%d/%m/%Y') or" +
					 "where date_format(data_hora, '%h:%i') = "+
					 "date_format(?, '%h:%i') " +
					 "order by data_hora;";
		
		Connection con = ConDB.getConnection();
		
		try {
			
			PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
			ps.setString(1, data);
			ps.setString(2, data);
			
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				cs.add(this.findOne(rs.getInt(1)));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return cs;
	}
	
	public Consulta findOne(int id) {
		
		Iterable<Consulta> mds = cr.findAll();
		
		for(Consulta m:mds) {
			if(m.getId() == id) {
				return m;
			}
		}
		
		return null;
	}
	
	public void deleteConsulta(int id) {
		cr.delete(this.findOne(id));
	}
	
	public boolean existsId(int id) {
		
		return findOne(id) != null;
		
	}
	
	//retorna Consulta que impede o cadastro de uma nova em função do consultório
	public Consulta consultaImped(int idConsultorio, Timestamp horario) {
		
		String sql = "select ca_id " + 
					 "from consulta " + 
					 "where " + 
					 "c_co_id = ? and " + 
					 "data_hora between " + 
					 "timestampadd(MINUTE,-14,?) and " + 
					 "timestampadd(MINUTE,14,?);";
		
		Connection con = ConDB.getConnection();
		System.out.println(sql);
		
		try {
			
			PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
			ps.setInt(1, idConsultorio);
			ps.setTimestamp(2, horario);
			ps.setTimestamp(3, horario);
			
			ResultSet rs = ps.executeQuery();
			
			rs.next();
			
			return this.findOne(rs.getInt(1));
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			
		} finally {
			
			ConDB.closeConnection(con);
			
		}
		
		return null;
		
	}
	
	//retorna a Consulta que impede o cadastro de uma nova em função do paciente
	public Consulta consultaImpedPac(int idPaciente, Timestamp data) {
	
		String sql = 	"select ca_id  " + 
						"from consulta  " + 
						"where p_p_id = ? " + 
						"and date_format(data_hora,'%d/%m/%y') =  " + 
						"date_format(?,'%d/%m/%y');";
		
		Connection con = ConDB.getConnection();
		System.out.println(sql);
		
		try {
			
			PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
			ps.setInt(1,idPaciente);
			ps.setTimestamp(2, data);
			
			ResultSet rs = ps.executeQuery();
			rs.next();
			
			return this.findOne(rs.getInt(1));
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return null;
		
	}
	
	//retorna a Consulta que impede o cadastro de uma nova em função do médico
	public Consulta consultaImpedMed(int idMedico, Timestamp data) {
	
		String sql = 	"select ca_id  " + 
						"from consulta  " + 
						"where m_m_id = ? " + 
						"and date_format(data_hora,'%d/%m/%y') =  " + 
						"date_format(?,'%d/%m/%y');";
		
		Connection con = ConDB.getConnection();
		System.out.println(sql);
		
		try {
			
			PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
			ps.setInt(1,idMedico);
			ps.setTimestamp(2, data);
			
			ResultSet rs = ps.executeQuery();
			rs.next();
			
			return this.findOne(rs.getInt(1));
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return null;
		
	}

	//retorna a Consulta que impede o cadastro de uma nova em função do consultório
	public Consulta consultaImpedConsultorio(int idConsultorio, Timestamp data) {
		
		String sql = 	"select distinct ca_id " + 
						"from consulta " + 
						"where c_co_id = ? and " +
						"e_em_id <> (select em_id " + 
									"from esp_med  " + 
									"where em_desc = 'Cirurgião') " +
						"and  date_format(data_hora,'%d%m%y') =  " + 
						"date_format(?,'%d%m%y');";

		Connection con = ConDB.getConnection();
		System.out.println(sql);
		
		try {
			
			PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
			ps.setInt(1, idConsultorio);
			ps.setTimestamp(2, data);
			
			ResultSet rs = ps.executeQuery();
			
			rs.next();
			
			return this.findOne(rs.getInt(1));
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			
		} finally {
			
			ConDB.closeConnection(con);
			
		}
		
		return null;
		
	}
	
	//valida se há disponibilidade de horário para a consulta obedecendo o intervalo mínimo de 15min
	public boolean validaIntervaloMin(int idConsultorio, Timestamp horario) {
		
		String sql = 	"select (select count(*) " + 
						"from consulta " + 
						"where " + 
						"c_co_id = ? and " + 
						"data_hora between " + 
						"timestampadd(MINUTE,-14,?) and " + 
						"timestampadd(MINUTE,14,?)) <> 1;";
				
		Connection con = ConDB.getConnection(); 
		System.out.println(sql);
		
		try {
			
			PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
			ps.setInt(1, idConsultorio);
			ps.setTimestamp(2, horario);
			ps.setTimestamp(3, horario);
			
			ResultSet rs = ps.executeQuery();
			
			rs.next();
			
			return rs.getBoolean(1);
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			
		} finally {
			
			ConDB.closeConnection(con);
			
		}
		
		
		
		return false;
	}

	//valida se há consulta para o paciente na data a ser marcada de acordo com o limite/dia (1)
	public boolean validaConsultaPaciente(int idPaciente, Timestamp data) {
		
		String sql = 	"select " + 
						"(select count(*) qtd_consultas " + 
						"from consulta " + 
						"where p_p_id = ? and " + 
						"date_format(data_hora, '%d%m%y') = " + 
						"date_format(?, '%d%m%y')) = 0;";
		
		Connection con = ConDB.getConnection(); 
		System.out.println(sql);

		try {
			
			PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
			ps.setInt(1, idPaciente);
			ps.setTimestamp(2, data);
			
			ResultSet rs = ps.executeQuery();
			
			rs.next();
			
			return rs.getBoolean(1);
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			
		} finally {
			
			ConDB.closeConnection(con);
			
		}
		
		return false;
	}
	
	//valida se o consultório tem disponibilidade para consulta conforme o número máximo de consultas/dia (12).
	public boolean validaConsultaConsultorio(int idConsultorio, Timestamp data) {

		String sql = 	"select (select count(*) " + 
								"from consulta " + 
								"where c_co_id = ? and  " + 
								"date_format(data_hora,'%d%m%y') = " +
								"date_format(?,'%d%m%y')) " +
					    "< 12;";
		
		Connection con = ConDB.getConnection(); 
		System.out.println(sql);

		try {
			
			PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
			ps.setInt(1, idConsultorio);
			ps.setTimestamp(2, data);
			
			ResultSet rs = ps.executeQuery();
			
			rs.next();
			
			return rs.getBoolean(1);
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			
		} finally {
			
			ConDB.closeConnection(con);
			
		}
		
		return false;
	}
	
	//verifica se o médico tem consulta para aquela data
	public boolean existeConsultaMedico(int idMedico, Timestamp data) {

		String sql = 	"select (select count(*) " + 
								"from consulta " + 
								"where date_format(data_hora,'%d%m%y') = date_format(?,'%d%m%y') " + 
								"and m_m_id = ?) " + 
						"> 0";
		
		Connection con = ConDB.getConnection(); 
		System.out.println(sql);

		try {
			
			PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
			ps.setTimestamp(1, data);
			ps.setInt(2, idMedico);
			
			ResultSet rs = ps.executeQuery();
			
			rs.next();
			
			return rs.getBoolean(1);
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			
		} finally {
			
			ConDB.closeConnection(con);
			
		}
		
		return false;
	}
	
	//valida se o consultório informado condiz com o consultório que o médico está
	public boolean validaConsultaMedico(int idMedico, int idConsultorio, Timestamp data) {

		String sql = 	"select (select distinct c_co_id " + 
						"from consulta    " + 
						"where date_format(data_hora,'%d%m%y') = " +
						" date_format(?,'%d%m%y')    " + 
						"and m_m_id = ?) = ?;";
		
		Connection con = ConDB.getConnection(); 
		System.out.println(sql);

		try {
			
			PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
			ps.setTimestamp(1, data);
			ps.setInt(2, idMedico);
			ps.setInt(3, idConsultorio);
			
			ResultSet rs = ps.executeQuery();
			
			rs.next();
			
			return rs.getBoolean(1);
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			
		} finally {
			
			ConDB.closeConnection(con);
			
		}
		
		return false;
	}
	
	//valida se o consultório que contém cirurgião tem disponibilidade para mais um cirurgião conforme limite (2)
	public boolean validaConsultaConsultorioCirurgiao(int idMedico, int idConsultorio, Timestamp data) {

			String sql = 	"select " + 
							"(select count(distinct m_m_id)  " + 
							"from consulta  " + 
							"where c_co_id = ? and " +
							"m_m_id <> ? and " +
							"e_em_id = (select em_id " + 
										"from esp_med  " + 
										"where em_desc = 'Cirurgião') " +
							"and  date_format(data_hora,'%d%m%y') =  " + 
							"date_format(?,'%d%m%y'))" +
							"< 2;";
			
			Connection con = ConDB.getConnection();
			System.out.println(sql);

			try {
				
				PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
				ps.setInt(1, idConsultorio);
				ps.setInt(2, idMedico);
				ps.setTimestamp(3, data);
				
				ResultSet rs = ps.executeQuery();
				
				rs.next();
				
				return rs.getBoolean(1);
				
			} catch (SQLException e) {
				
				e.printStackTrace();
				
			} finally {
				
				ConDB.closeConnection(con);
				
			}
			
			return false;
		}
	
	//verifica se o consultorio está vago
	public boolean consultorioDisponivel(int idMedico, int idConsultorio, Timestamp data) {
		
		String sql = 	"select (select count(distinct m_m_id) " + 
								"from consulta   " + 
								"where c_co_id = ? " +
								"and m_m_id <> ? " +
								"and date_format(data_hora,'%d/%m/%Y') = " +
								"date_format(?,'%d/%m/%Y')) " +
						"= 0;";

		Connection con = ConDB.getConnection();
		System.out.println(sql);
		
		try {
			
			PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
			ps.setInt(1, idConsultorio);
			ps.setInt(2, idMedico);
			ps.setTimestamp(3, data);
			
			ResultSet rs = ps.executeQuery();
			
			rs.next();
			
			return rs.getBoolean(1);
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			
		} finally {
			
			ConDB.closeConnection(con);
			
		}
		
		return false;


		
	}
	
	//valida se o cirurgiao pode fazer a consulta no horario determinado (max 2 cirurgioes por horário e 1 por consulta)
	public boolean validaAtdCirurgiao(int idConsultorio, int idMedico, Timestamp data) {
		
		String sql = "select(select count(*) " + 
				" from consulta " + 
				" where " + 
				" c_co_id = ? and " + 
				" data_hora between " + 
				" timestampadd(MINUTE,-14,?) and " + 
				" timestampadd(MINUTE,14,?)) " + 
				" < 2 and " + 
				" (select count(*)    " + 
				" from consulta " + 
				" where " + 
				" c_co_id = ? and " + 
				" m_m_id = ? and " + 
				" data_hora between " + 
				" timestampadd(MINUTE,-14,?) and " + 
				" timestampadd(MINUTE,14,?)) " + 
				" = 0;";
		
		Connection con = ConDB.getConnection();
		
		try {
			
			PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
			ps.setInt(1, idConsultorio);
			ps.setTimestamp(2, data);
			ps.setTimestamp(3, data);
			ps.setInt(4, idConsultorio);
			ps.setInt(5, idMedico);
			ps.setTimestamp(6, data);
			ps.setTimestamp(7, data);
		
			ResultSet rs = ps.executeQuery();
			rs.next();
			
			return rs.getBoolean(1);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
		
	}
}
