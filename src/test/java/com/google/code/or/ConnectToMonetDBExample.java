package com.google.code.or;

import java.sql.*;

/**
 * This example assumes there exist tables a and b filled with some data.
 * On these tables some queries are executed and the JDBC driver is tested
 * on it's accuracy and robustness against 'users'.
 *
 * @author Fabian Groffen
 */
public class ConnectToMonetDBExample {
    public static void main(String[] args) throws Exception {
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            // make a connection to the MonetDB server using JDBC URL starting with: jdbc:monetdb:// 
            con = DriverManager.getConnection("jdbc:monetdb://localhost:50000/demo?so_timeout=10000", "monetdb", "monetdb");
            // make a statement object
            st = con.createStatement();
            // make a executeand SQL query which returns a ResultSet object
            rs = st.executeQuery("SELECT * from mytext;");
            // get meta data and print column names with their type
            ResultSetMetaData md = rs.getMetaData();
            for (int i = 1; i <= md.getColumnCount(); i++) {
                System.out.print(md.getColumnName(i) + ":" + md.getColumnTypeName(i) + "\t");
            }
            System.out.println("");
            // now print the data: only the first 5 rows, while there probably are
            // a lot more. This shouldn't cause any problems afterwards since the
            // result should get properly discarded when we close it
            for (int i = 0; rs.next() && i < 5; i++) {
                for (int j = 1; j <= md.getColumnCount(); j++) {
                    System.out.print(rs.getString(j) + "\t");
                }
                System.out.println("");
            }
            // close (server) resource as soon as we are done processing resultset data
            rs.close();
            rs = null;

            // tell the driver to only return 5 rows for the next execution
            // it can optimize on this value, and will not fetch any more than 5 rows.
            st.setMaxRows(5);
            // we ask the database for 22 rows, while we set the JDBC driver to
            // 5 rows, this shouldn't be a problem at all...
            rs = st.executeQuery("select * from a limit 22");
            // read till the driver says there are no rows left
            for (int i = 0; rs.next(); i++) {
                System.out.print("[" + rs.getString("var1") + "]");
                System.out.print("[" + rs.getString("var2") + "]");
                System.out.print("[" + rs.getInt("var3") + "]");
                System.out.println("[" + rs.getString("var4") + "]");
            }
            // close (server) resource as soon as we are done processing resultset data
            rs.close();
            rs = null;

            // unset the row limit; 0 means as much as the database sends us
            st.setMaxRows(0);
            // we only ask 10 rows
            rs = st.executeQuery("select * from b limit 10;");
            // and simply print them
            while (rs.next()) {
                System.out.print(rs.getInt("rowid") + ", ");
                System.out.print(rs.getString("id") + ", ");
                System.out.print(rs.getInt("var1") + ", ");
                System.out.print(rs.getInt("var2") + ", ");
                System.out.print(rs.getString("var3") + ", ");
                System.out.println(rs.getString("var4"));
            }
            // close (server) resource as soon as we are done processing resultset data
            rs.close();
            rs = null;

            // perform a ResultSet-less query (with no trailing ; since that should
            // be possible as well and is JDBC standard)
            int updCount = st.executeUpdate("delete from a where var1 = 'zzzz'");
            System.out.println("executeUpdate() returned: " + updCount);
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
