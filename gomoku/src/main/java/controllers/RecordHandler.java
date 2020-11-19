package controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import models.GameBoard;
import models.Record;

public class RecordHandler {
  
  
  /**
   * This function createTable in database.
   * @throws SQLException any exception can be caused by the sql connection
   */
  /*
  public static boolean createTable() throws SQLException {
    Connection c = null;
    Statement stmt = null;
    ResultSet rs = null;
    try {
      Class.forName("org.sqlite.JDBC");
      c = DriverManager.getConnection("jdbc:sqlite:record.db");
      System.out.println("Opened database successfully ... create table");

      stmt = c.createStatement();
      rs = stmt.executeQuery("SELECT tbl_name " + "FROM sqlite_master WHERE type = 'table';");
      // SELECT tbl_name FROM sqlite_master WHERE type = 'table';
      String table = null;
      while (rs.next()) {
        table = rs.getString("tbl_name");
      }

      if (table != null) {
        System.out.println("Table record has been created already");
        return true;
      }
      String sql = "CREATE TABLE RECORD " 
          + "(P1ID   TEXT    NOT NULL," 
          + " P2ID           TEXT    NOT NULL, "
          + " RECORDJSON     TEXT    NOT NULL)";
      stmt.executeUpdate(sql);

    } catch (Exception e) {
      if (stmt != null) {
        stmt.close();
      }
      if (c != null) {
        c.close();
      }
      System.out.println("Table created failed");
      
      return false;
    }
    System.out.println("Table created successfully");
    return true;
  }
  */
  
  /** This function inserts the record JSON to table record, which lists all records.
   * @param recordJson the record JSON
   * @throws SQLException any exception can be caused by the sql connection
   */
  public static boolean insertRecord(Record record) throws SQLException {  
    // Connect to database
    Gson gson = new Gson();
    String recordJson = gson.toJson(record);
    Connection c = null;
    PreparedStatement stmt = null;
    try {
      Class.forName("org.sqlite.JDBC");
      c = DriverManager.getConnection("jdbc:sqlite:record.db");
      c.setAutoCommit(false);
      System.out.println("Opened database record successfully");
      // Insert the gameboard JSON to database
      
      stmt = c.prepareStatement("INSERT INTO RECORD (P1ID,P2ID,RECORDJSON) " + "VALUES (?, ?, ?);");
      stmt.setString(1, record.getP1().getId());
      stmt.setString(2, record.getP2().getId());
      stmt.setString(3, recordJson);
      stmt.executeUpdate();
      c.commit();
      stmt.close();
      c.close();
    } catch (Exception e) {
      if (stmt != null) {
        stmt.close();
      }
      if (c != null) {
        c.close();
      }
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      System.exit(0);
      return false;
    }
    System.out.println("Record JSON inserted successfully!");
    return true;
  }
  
  /** Getter function, it gets the Record JSON from table record, which lists all records.
   * @return record JSON
   * @throws SQLException any exception can be caused by the sql connection
   */
  public static List<String> getRecord(String playerId) throws SQLException {
    List<String> recordList = new ArrayList<>();
    
    // Connect to database
    Connection c = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      Class.forName("org.sqlite.JDBC");
      c = DriverManager.getConnection("jdbc:sqlite:record.db");
      c.setAutoCommit(false);
      System.out.println("Opened database record successfully");
      // Select the record JSON from database
      stmt = c.prepareStatement("SELECT * FROM RECORD WHERE P1ID = ? OR P2ID = ?;");
      stmt.setString(1, playerId);
      stmt.setString(2, playerId);
      rs = stmt.executeQuery();
      
      while (rs.next()) {
        recordList.add(rs.getString("recordjson"));
      }
      rs.close();
      stmt.close();
      c.close();
    } catch (Exception e) {
      if (stmt != null) {
        stmt.close();
      }
      if (rs != null) {
        rs.close();
      }
      if (c != null) {
        c.close();
      }
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      System.exit(0);
    }
    System.out.println("Record JSON selected successfully!");
    return recordList;
  }
}
