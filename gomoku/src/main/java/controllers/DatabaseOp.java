package controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseOp {
  
  /** This function cleans the table gameboard, which holds current games.
   * @throws SQLException any exception can be caused by the sql connection
   */
  public static void cleanGameBoardJson() throws SQLException {
    // Connect to database
    Connection c = null;
    Statement stmt = null;
    try {
      Class.forName("org.sqlite.JDBC");
      c = DriverManager.getConnection("jdbc:sqlite:gamestate.db");
      c.setAutoCommit(false);
      System.out.println("Opened database successfully");
      // Clean the gameboard JSON
      stmt = c.createStatement();
      String sql = "DELETE from GAMEBOARD where ID=1;";
      stmt.executeUpdate(sql);
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
    }
    System.out.println("Gameboard database cleaned successfully!");
  }
  
  /** This function updates the gameboard JSON to table gameboard, which holds current games.
   * @param gameBoardJson the gameboard JSON
   * @throws SQLException any exception can be caused by the sql connection
   */
  public static void insertGameBoardJson(String gameBoardJson) throws SQLException {  
    // Connect to database
    Connection c = null;
    PreparedStatement stmt = null;
    try {
      Class.forName("org.sqlite.JDBC");
      c = DriverManager.getConnection("jdbc:sqlite:gamestate.db");
      c.setAutoCommit(false);
      System.out.println("Opened database successfully");
      // Insert the gameboard JSON to database
      stmt = c.prepareStatement("INSERT INTO GAMEBOARD (ID,GAMEBOARDJSON) " + "VALUES (1, ?);");
      stmt.setString(1, gameBoardJson);
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
    }
    System.out.println("Gameboard JSON inserted successfully!");
  }
  
  /** Getter function, it gets the gameboard JSON from table gameboard, which holds current games.
   * @return gameboard JSON
   * @throws SQLException any exception can be caused by the sql connection
   */
  public static String getGameBoardJson() throws SQLException {
    String gameBoardJson = null;
    
    // Connect to database
    Connection c = null;
    Statement stmt = null;
    ResultSet rs = null;
    try {
      Class.forName("org.sqlite.JDBC");
      c = DriverManager.getConnection("jdbc:sqlite:gamestate.db");
      c.setAutoCommit(false);
      System.out.println("Opened database successfully");
      // Select the gameboard JSON from database
      stmt = c.createStatement();
      rs = stmt.executeQuery("SELECT * FROM GAMEBOARD;");
      
      while (rs.next()) {
        gameBoardJson = rs.getString("gameboardjson");
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
    System.out.println("Gameboard JSON selected successfully!");
    return gameBoardJson;
  }

  
  /** This function updates the gameboard JSON to table gameboard, which holds current games.
   * @param gameBoardJson the gameboard JSON
   * @throws SQLException any exception can be caused by the sql connection
   */
  public static void updateGameBoardJson(String gameBoardJson) throws SQLException {
    // Connect to database
    Connection c = null;
    PreparedStatement stmt = null;
    try {
      Class.forName("org.sqlite.JDBC");
      c = DriverManager.getConnection("jdbc:sqlite:gamestate.db");
      c.setAutoCommit(false);
      System.out.println("Opened database successfully");
      // Update the gameboard JSON in database
      stmt = c.prepareStatement("UPDATE GAMEBOARD set GAMEBOARDJSON=? where ID=1;");
      stmt.setString(1, gameBoardJson);
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
    }
    System.out.println("Gameboard JSON updated successfully!");
    
  }
}
