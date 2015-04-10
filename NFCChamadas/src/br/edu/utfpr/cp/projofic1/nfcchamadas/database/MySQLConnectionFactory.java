package br.edu.utfpr.cp.projofic1.nfcchamadas.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnectionFactory {
	
	private static final String HOST = "p3plcpnl0206.prod.phx3.secureserver.net";
	private static final String USER = "nfctime";
	private static final String PASSWORD = "Nfc@time&";
	private static final String DATABASE = "nfctime";
	
	
	public Connection getConnection() throws SQLException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			return DriverManager.getConnection("jdbc:mysql://" + HOST + "/" + DATABASE, USER, PASSWORD);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
}