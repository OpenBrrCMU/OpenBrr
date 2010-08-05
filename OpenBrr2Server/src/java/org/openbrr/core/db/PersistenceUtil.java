package org.openbrr.core.db;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 
 * @author Admin
 * In this utility class specify the DB details that the server needs to connect to. 
 * All the parsed Flossmole data files are stored in this database. 
 */

public class PersistenceUtil {
	
	private static String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
	private static String JDBC_URL = "jdbc:mysql://localhost:3306/openbrr2_dev";
	private static String DB_USER = "root";
	private static String DB_PASSWORD = "root";
	
	private static SessionFactory sessionFactory;
	
	static {
		try {
			//sessionFactory = new Configuration().configure().buildSessionFactory();
			sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
		} catch(Throwable t) {
			t.printStackTrace();
		}
	}
	
	public static Session getSession() {
		Session session = null;
		session = sessionFactory.openSession(); 
		return session;
	}
	
	public static Session getCurrentSession() {
		Session session = null;
		session = sessionFactory.getCurrentSession(); 
		return session;
	}
	
	public static Connection getConnection() throws SQLException {
		return getC3p0PooledConnection();
	}
	
	public static Connection getC3p0PooledConnection() throws SQLException {
		Connection dbConn = null;
		
		ComboPooledDataSource ds = new ComboPooledDataSource();
		try {
			ds.setDriverClass(MYSQL_DRIVER);
			ds.setJdbcUrl(JDBC_URL);
			ds.setUser(DB_USER);
			ds.setPassword(DB_PASSWORD);
			
			dbConn = ds.getConnection();
		} catch (PropertyVetoException e) {
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
			
			Session session = getSession();
			System.out.println("session = "+session);
			session.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
