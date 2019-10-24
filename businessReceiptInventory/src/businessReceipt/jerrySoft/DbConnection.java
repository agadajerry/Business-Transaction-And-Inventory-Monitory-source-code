package businessReceipt.jerrySoft;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {

	public static Connection getConnection()
	 {
		 Connection conn = null;
		 try
		 {
			 String db = ".db";

			 String dbURL = "jdbc:sqlite:C:\\BusinessTransaction\\BusinessInventoryAndReceipt.db";
			 
			
			
			 conn = DriverManager.getConnection(dbURL);
			
			System.out.println("database is successfully created");
			 
		 }catch(SQLException ex)
		 {
			 System.out.println(ex.getMessage());
		 }
		 return conn;
	 }
	
}
