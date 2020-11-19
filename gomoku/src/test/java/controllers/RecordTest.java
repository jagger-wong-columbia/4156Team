package controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.SQLException;
import java.util.List;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import models.Player;
import models.Record;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(OrderAnnotation.class) 
public class RecordTest {
  
  static RecordHandler recordHandler;
  /**
  * Runs only once before the testing starts.
  */
  
  @BeforeAll
  public static void init() {
    
    System.out.println("Before All");
  }
    
  
  /**
  * This is a test case to create record table.
   * @throws SQLException for possible exception
  */
  /*
  @Test
  @Order(1)
  public void createTableTest() throws SQLException {
        
    // Create Table
    boolean isSuccessful = RecordHandler.createTable();
    
    
    // Check assert statement 
    assertEquals(isSuccessful, true);
    System.out.println("Test Create Table");
  }
  */
  
  /**
   * This is a test case to insert record.
   * @throws SQLException for possible exception.
   */
  @Test
  @Order(1)
  public void insertRecordTest() throws SQLException {

    // Create one record
    Player p1 = new Player('X', "1");
    Player p2 = new Player('O', "2");
    char[][] boardState = new char[15][15];
    Record record = new Record("qwe", p1, p2, boardState, 0);

    // insert into table
    boolean isSuccessful = RecordHandler.insertRecord(record);

    // Check assert statement
    assertEquals(isSuccessful, true);
    System.out.println("Test Insert Record");
  }
  
  /**
   * This is a test case to get record.
   * @throws SQLException for possible exception.
   */
  @Test
  @Order(2)
  public void getRecordTest() throws SQLException {

    // Get all records for Player whose id = 1
    List<String> recordList = RecordHandler.getRecord("1");
    for (int i = 0; i < recordList.size(); i++) {
      System.out.println(recordList.get(i));
    }
    
    // Check assert statement
    assertEquals(recordList.size() > 0, true);
    System.out.println("Test get Record");
  }
  
  /**
   * This is a test case for getrecord endpoint.
   * @throws SQLException for possible exception.
   */
  @Test
  @Order(3)
  public void getRecordEndPointTest() throws SQLException {

    // start the server
    String[] input = new String[1]; // create an empty string array as input
    PlayGame.main(input);
    
    // Get all records for Player whose id = 1
    HttpResponse<?> response = Unirest.get("http://localhost:8080/getrecord/:1").asString();
    int restStatus = response.getStatus();
    String recordList = (String) response.getBody();
    System.out.println(recordList);
    
    
    // Check assert statement
    assertEquals(restStatus, 200);
    System.out.println("Test get Record End Point");
  }
  
  /**
  * This will run every time after a test has finished.
  */
  
  @AfterEach
  public void finishGame() {
    System.out.println("After Each");
  }
  
  
  /**
   * This method runs only once after all the test cases have been executed.
   */
  
  @AfterAll
  public static void close() {
    // 
    System.out.println("After All");
  }
}