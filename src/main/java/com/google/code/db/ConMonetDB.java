package com.google.code.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class ConMonetDB {
	public static void conMonetDB (String sql) throws Exception{
	    Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            // make a connection to the MonetDB server using JDBC URL starting with: jdbc:monetdb:// 
            con = DriverManager.getConnection(Constant.MonetURL, Constant.MonetUser, Constant.MonetPwd);
            // make a statement object
            st = con.createStatement();
            // make a executeand SQL query which returns a ResultSet object
            rs = st.executeQuery(sql);
            rs.close();
            rs = null;
    } catch (SQLException se) {
      System.out.println(se.getMessage());
  } finally {
      // when done, close all (server) resources
      if (rs != null) rs.close();
      if (st != null) st.close();
      if (con != null) con.close();
  }
}
}
