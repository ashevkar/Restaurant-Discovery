package model;
import java.sql.*; 
  
public class DBConnect {
	
//	static final String DB_URL = "jdbc:mysql://localhost:3306/restarant";
//	static final String USER = "root", PASS = "root";
	
	static final String DB_URL = "jdbc:mysql://www.papademas.net:3307/510fp?autoReconnect=true&useSSL=false";
	static final String USER = "fp510", PASS = "510";

  public Connection connect() throws SQLException {
	  try {
		Class.forName("com.mysql.cj.jdbc.Driver");
		return DriverManager.getConnection(DB_URL, USER, PASS); 
	} catch (ClassNotFoundException e) {
		e.printStackTrace();
	}
	  return null;
  }
}