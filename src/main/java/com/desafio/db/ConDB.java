package com.desafio.db;

import java.sql.DriverManager;
import java.sql.SQLException;
import com.mysql.jdbc.Connection;


public class ConDB {

	public static Connection getConnection() {
		
		String url = "jdbc:mysql://localhost:3306/desafio";
		String user = "root";
		String password = "";
		
		Connection con = null;
		
		try {
			con = (Connection) DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return con;
	}
	
	public static void closeConnection(Connection c) {

		try {
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
