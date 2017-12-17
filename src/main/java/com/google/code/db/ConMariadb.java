package com.google.code.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class ConMariadb {

	public static void execQuery(String sql) throws Exception{
		// TODO Auto-generated method stub
		//create connection for a server installed in localhost, with a user "root" with no password
		//Class.forName("org.mariadb.jdbc.Driver");
		//Connection connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/DB?user=root&password=myPassword");
   try {
		Connection conn = DriverManager.getConnection("jdbc:mariadb://10.77.50.80:3307/test", "root", null);
        // create a Statement
        Statement stmt = conn.createStatement();
        //execute query
        ResultSet rs = stmt.executeQuery(sql);
        //position result to first      
      }catch (Exception e){
    	  System.err.println("Fail to execute SQL statement");
    	  e.printStackTrace();
      }
	}
 }

