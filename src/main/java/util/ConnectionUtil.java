package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {
	private static Connection conn = null;
	
	// Prevents us from EVER instantiating this class. Just used for static calls to the method below
	private ConnectionUtil() {
		super();
	}
	
	public static Connection getConnection() {
		/**We will be using DriverManager to get out connection to the DB.
		 * 
		 * We provide it the credential information:
		 * Connection String = "jdbc:oracle:thin:@ENDPOINT:PORT:SID"
		 * So ours would be like:
		 * jdbc:oracle:thin:@ENDPOINT:1521:ORCL
		 * */
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			try {
				conn = DriverManager.getConnection(
						"jdbc:oracle:thin:@trainingdb.c3ynkxcfuk8l.us-east-2.rds.amazonaws.com:1521:ORCL", 
						"root", 
						"password"); // <-- HARD CODED PASSWORD
						// Exceedingly unsafe, you can't push to github. Need environment variables to hide it basically.
			} catch(SQLException e) {
				e.printStackTrace();
			}
		} catch(ClassNotFoundException e) {
			System.out.println("Did not find Oracle JDBC Driver class!");
		}
		
		return conn;
	}
}
