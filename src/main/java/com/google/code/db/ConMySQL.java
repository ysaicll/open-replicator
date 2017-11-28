package com.google.code.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class ConMySQL {	
	public void conMySQL(String dbName) throws Exception{
		    String driver = "com.mysql.jdbc.Driver";
		    String url = "jdbc:mysql://localhost:3307/" + dbName;	 
		    String query = "select table_name from information_schema.tables "
		    		            + "where table_schema='" + dbName + "'";
		    List<String> Tables = new ArrayList<String>();
		    List<String> CreateTables = new ArrayList<String>(); 
		    try {
		        Class.forName(driver);
		        Connection conn = DriverManager.getConnection(url, Constant.MySQLUser, Constant.MySQLPwd);
		        Statement stmt = conn.createStatement();
		        ResultSet rs = stmt.executeQuery(query);		   
			      while(rs.next()){
			          Tables.add(rs.getString(1));
	           }
			   for(String Table : Tables){
				   ResultSet rs1 = stmt.executeQuery("show create table " + dbName + "." +Table + ";");
				   while(rs1.next()){
					  int index = rs1.getString(2).lastIndexOf(")");
				      System.out.println(rs1.getString(2).substring(0, index + 1) + ";");
			     }
			   }
		    } catch (ClassNotFoundException e) {            
		        e.printStackTrace();
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }	
		    
    }
	
	public static void main(String[] args) {
		ConMySQL cMySQL = new ConMySQL();
		try {
			cMySQL.conMySQL("company");
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
}
