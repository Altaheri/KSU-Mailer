/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ksu.mailnoti.core.db;
import java.sql.*;

/**
 *
 * @author hamdi
 */
public class SQLITEJDBCInsert {
    
  public static void main( String args[] )
  {
    Connection c = null;
    Statement stmt = null;
    try {
      Class.forName("org.sqlite.JDBC");
      c = DriverManager.getConnection("jdbc:sqlite:db/LIST_CCIS_MAIL.db");
      c.setAutoCommit(false);
      System.out.println("Opened database successfully");

      stmt = c.createStatement();
      String sql = "INSERT INTO CCIS_MAIL (Name,LeadingInit,FirstName,MiddleName,LastName, Suffix, prefix) " +
                   "VALUES ('ms Hamdi T. Altaheri phd1', '', 'Hamdi', 'T.', 'Altaheri', 'ms', 'phd');"; 
      stmt.executeUpdate(sql);

      stmt.close();
      c.commit();
      c.close();
    } catch ( Exception e ) {
      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
      System.exit(0);
    }
    System.out.println("Records created successfully");
  }
}
