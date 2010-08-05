package org.openbrr.core.db;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * 
 * @author Admin
 * This Utility class connects the server with the specified database
 */

public class AppServerUtil {
	
	public static Connection getConnection() throws SQLException {
		
		Connection dbConn = null;
		try {
			InitialContext ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup("jdbc/obrr_data");
			dbConn = ds.getConnection();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return dbConn;
	}
	
	public static void main(String[] args) {
		Connection dbConn;
		try {
			dbConn = getConnection();
			System.out.println("conn = "+dbConn);
			dbConn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
