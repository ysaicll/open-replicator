package com.google.code.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ConMySQL {	
	public List<String> getCreateSQL(String dbName) throws Exception{
		    String driver = "com.mysql.jdbc.Driver";
		    String url = "jdbc:mysql://120.24.98.91:3306/" + dbName;	 
		    String query = "select table_name from information_schema.tables "
		    		            + "where table_schema='" + dbName + "'";
		    List<String> Tables = new ArrayList<String>();
		    List<String> CreateTables = new ArrayList<String>();
		    StringBuffer sql = new StringBuffer("");
		    try {
		        Class.forName(driver);
		        Connection conn = DriverManager.getConnection(url, Constant.MySQLUser, Constant.MySQLPwd);
		        Statement stmt = conn.createStatement();
		        ResultSet rs = stmt.executeQuery(query);		   
			      while(rs.next()){
			          Tables.add(rs.getString(1));
	           }
			   for(String Table : Tables){
				   ResultSet rs1 = stmt.executeQuery("desc " + dbName + "." + Table + ";");
				   while(rs1.next()){				   
					   if(rs1.isLast()){
						   String field = rs1.getString(1);
						   String datatype = rs1.getString(2);
						   if(!datatype.contains("(") || datatype.contains("int") ){      						 
       						   sql.append(field + " " + TypeMatch.getType(datatype.toUpperCase()) + "\n");
						   }else{
							   String[] tmp = datatype.split("\\(");
							   //"(" need Escape			  
							   sql.append(field + " " + TypeMatch.getType(tmp[0].toUpperCase()) + "(" + tmp[1] + "\n");
						   }
					   } else{
						   String field = rs1.getString(1);
						   String datatype = rs1.getString(2);
						   if(!datatype.contains("(") || datatype.contains("int") ){
       						   sql.append(field + " " + TypeMatch.getType(datatype.toUpperCase()) + "," + "\n");
						   }else{
							   String []tmp = datatype.split("\\(");
							   sql.append(field + " " + TypeMatch.getType(tmp[0].toUpperCase()) + "(" + tmp[1] + "," + "\n");
						   }
			            } 
				   }
				   CreateTables.add("CREATE TABLE " + Table + "(" + sql.toString() + ");");
				   sql.delete(0, sql.length());	
			   }
		    } catch (ClassNotFoundException e) {            
		        e.printStackTrace();
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }	
		    return CreateTables;
    }
	
	public static void main(String[] args) {
		ConMySQL cMySQL = new ConMySQL();
		List<String>teStrings = new ArrayList<String>();
		try {
			teStrings = cMySQL.getCreateSQL("employees");
			for(String te : teStrings)
				System.out.println(te);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
}
