package integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.Gson;
import controllers.PlayGame;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import models.GameBoard;
import models.Player;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;


@TestMethodOrder(OrderAnnotation.class) 
public class GameTest {
	
  /**
  * Runs only once before the testing starts.
  */
  @BeforeAll
  public static void init() {
    // Start Server
    PlayGame.main(null);
    System.out.println("Before All");
  }
	
  /**
  * This method starts a new game before every test run. It will run every time before a test.
  */
  @BeforeEach
  public void startNewGame() {
    // Test if server is running. You need to have an endpoint /
    // If you do not wish to have this end point, it is okay to not have anything in this method.
    HttpResponse response = Unirest.get("http://localhost:8080/").asString();
    int restStatus = response.getStatus();
    System.out.println("Before Each");
  }
	
  /**
  * This is a test case to evaluate the newgame endpoint.
  */
  @Test
  @Order(1)
  public void newGameTest() {
    	
    //Create HTTP request and get response
    HttpResponse response = Unirest.get("http://localhost:8080/newgame").asString();
    int restStatus = response.getStatus();
    
    // Check assert statement (New Game has started)
    assertEquals(restStatus, 200);
    System.out.println("Test New Game");
  }
    
  /**
  * This is a test case to evaluate the startgame endpoint.
  */
  @Test
  @Order(2)
  public void startGameTest() {

    // Create a POST request to startgame endpoint and get the body
    // Remember to use asString() only once for an endpoint call. 
    // Every time you call asString(), a new request will be sent to the endpoint. 
    // Call it once and then use the data in the object.
    HttpResponse response = Unirest.post("http://localhost:8080/startgame").body("type=X").asString();
    String responseBody = (String) response.getBody();
    
    // --------------------------- JSONObject Parsing ----------------------------------
    
    System.out.println("Start Game Response: " + responseBody);
    
    // Parse the response to JSON object
    JSONObject jsonObject = new JSONObject(responseBody);
    
    // Check if game started after player 1 joins: Game should not start at this point
    assertEquals(false, jsonObject.get("gameStarted"));
    
    // ---------------------------- GSON Parsing -------------------------
    
    // GSON use to parse data to object
    Gson gson = new Gson();
    GameBoard gameBoard = gson.fromJson(jsonObject.toString(), GameBoard.class);
    Player player1 = gameBoard.getP1();
    
    // Check if player type is correct
    assertEquals('X', player1.getType());
    
    System.out.println("Test Start Game");
  }
  
  /**
  * This is a test case to evaluate the joingame endpoint.
  */
  @Test
  @Order(3)
  public void joinGameTest() {
     	
    //Create HTTP request and get response
    HttpResponse r1 = Unirest.get("http://localhost:8080/newgame").asString();    
    HttpResponse r2 = Unirest.post("http://localhost:8080/startgame").body("type=X").asString();    
    HttpResponse response = Unirest.get("http://localhost:8080/joingame").asString();
    int restStatus = response.getStatus();
     
    // Check assert statement (New Player has joined)
    assertEquals(restStatus, 200);
    System.out.println("Test Join Game");
  }
  
  /**
  * This is a test case to evaluate the move player 1 endpoint.
  */
  @Test
  @Order(4)
  public void movePlayer1Test() {
      	
    //Create HTTP request and get response
    HttpResponse r1 = Unirest.get("http://localhost:8080/newgame").asString();
    HttpResponse r2 = Unirest.post("http://localhost:8080/startgame").body("type=X").asString();
    HttpResponse r3 = Unirest.get("http://localhost:8080/joingame").asString();
    HttpResponse response = Unirest.post("http://localhost:8080/move/1").body("x=0&y=0").asString();
    String responseBody = (String) response.getBody();
    
    System.out.println("Player1 move Response: " + responseBody);
    
    // Parse the response to JSON object
    JSONObject jsonObject = new JSONObject(responseBody);
    
    // Check if this move is valid: This move should be valid
    assertEquals(true, jsonObject.get("moveValidity"));
    
    System.out.println("Test Player 1 Move");
  }
  
  /**
  * This is a test case to evaluate the move player 2 endpoint.
  */
  @Test
  @Order(5)
  public void movePlayer2Test() {
      	
    //Create HTTP request and get response
    HttpResponse r1 = Unirest.get("http://localhost:8080/newgame").asString();
    HttpResponse r2 = Unirest.post("http://localhost:8080/startgame").body("type=X").asString();
    HttpResponse r3 = Unirest.get("http://localhost:8080/joingame").asString();
    HttpResponse r4 = Unirest.post("http://localhost:8080/move/1").body("x=0&y=0").asString();
    HttpResponse response = Unirest.post("http://localhost:8080/move/2").body("x=1&y=1").asString();
    String responseBody = (String) response.getBody();
    
    System.out.println("Player2 move Response: " + responseBody);
    
    // Parse the response to JSON object
    JSONObject jsonObject = new JSONObject(responseBody);
    
    // Check if this move is valid: This move should be valid
    assertEquals(true, jsonObject.get("moveValidity"));
    
    System.out.println("Test Player 2 Move valid");
  }
  
  /**
  * This is a test case to evaluate the move player 2 invalid endpoint.
  */
  @Test
  @Order(6)
  public void movePlayer2InvalidTest() {
      	
    //Create HTTP request and get response
    HttpResponse r0 = Unirest.get("http://localhost:8080/newgame").asString();
    HttpResponse r1 = Unirest.post("http://localhost:8080/startgame").body("type=X").asString();
    HttpResponse r2 = Unirest.get("http://localhost:8080/joingame").asString();
    HttpResponse r3 = Unirest.post("http://localhost:8080/move/1").body("x=0&y=0").asString();
    HttpResponse response = Unirest.post("http://localhost:8080/move/2").body("x=0&y=0").asString();
    String responseBody = (String) response.getBody();
    
    System.out.println("Player2 move Response: " + responseBody);
    
    // Parse the response to JSON object
    JSONObject jsonObject = new JSONObject(responseBody);
    
    // Check if this move is invalid: This move should be invalid
    assertEquals(false, jsonObject.get("moveValidity"));
    
    System.out.println("Test Player 2 Move invalid");
  }
  
  /**
  * This is a test case to evaluate one player cannot make two moves in their turn.
  */
  @Test
  @Order(7)
  public void moveTwoTest() {
      	
    //Create HTTP request and get response
    HttpResponse r0 = Unirest.get("http://localhost:8080/newgame").asString();
    HttpResponse r1 = Unirest.post("http://localhost:8080/startgame").body("type=X").asString();
    HttpResponse r2 = Unirest.get("http://localhost:8080/joingame").asString();
    HttpResponse r3 = Unirest.post("http://localhost:8080/move/1").body("x=0&y=0").asString();
    HttpResponse response = Unirest.post("http://localhost:8080/move/1").body("x=1&y=1").asString();
    String responseBody = (String) response.getBody();
    
    System.out.println("Player1 move Response: " + responseBody);
    
    // Parse the response to JSON object
    JSONObject jsonObject = new JSONObject(responseBody);
    
    // Check if this move is invalid: This move should be invalid
    assertEquals(false, jsonObject.get("moveValidity"));
    
    System.out.println("Test Player 1 Move Twice");
  }
  
  /**
  * This is a test case to evaluate player 2 cannot make first move.
  */
  @Test
  @Order(8)
  public void movePlayer2FirstTest() {
      	
    //Create HTTP request and get response
    HttpResponse r0 = Unirest.get("http://localhost:8080/newgame").asString();
    HttpResponse r1 = Unirest.post("http://localhost:8080/startgame").body("type=X").asString();
    HttpResponse r2 = Unirest.get("http://localhost:8080/joingame").asString();
    HttpResponse response = Unirest.post("http://localhost:8080/move/2").body("x=1&y=1").asString();
    String responseBody = (String) response.getBody();
    
    System.out.println("Player2 move Response: " + responseBody);
    
    // Parse the response to JSON object
    JSONObject jsonObject = new JSONObject(responseBody);
    
    // Check if this move is invalid: This move should be invalid
    assertEquals(false, jsonObject.get("moveValidity"));
    
    System.out.println("Test Player 2 Move First");
  }
  
  /**
  * This is a test case to evaluate two players can draw.
  */
  @Test
  @Order(9)
  public void moveToDrawTest() {
      	
    //Create HTTP request and get response
    HttpResponse r0 = Unirest.get("http://localhost:8080/newgame").asString();
    HttpResponse r1 = Unirest.post("http://localhost:8080/startgame").body("type=X").asString();
    HttpResponse r2 = Unirest.get("http://localhost:8080/joingame").asString();
    HttpResponse r3 = Unirest.post("http://localhost:8080/move/1").body("x=0&y=0").asString();
    HttpResponse r4 = Unirest.post("http://localhost:8080/move/2").body("x=0&y=2").asString();
    HttpResponse r5 = Unirest.post("http://localhost:8080/move/1").body("x=0&y=1").asString();
    HttpResponse r6 = Unirest.post("http://localhost:8080/move/2").body("x=1&y=0").asString();
    HttpResponse r7 = Unirest.post("http://localhost:8080/move/1").body("x=1&y=1").asString();
    HttpResponse r8 = Unirest.post("http://localhost:8080/move/2").body("x=2&y=1").asString();
    HttpResponse r9 = Unirest.post("http://localhost:8080/move/1").body("x=1&y=2").asString();
    HttpResponse r10 = Unirest.post("http://localhost:8080/move/2").body("x=2&y=2").asString();
    HttpResponse response = Unirest.post("http://localhost:8080/move/1").body("x=2&y=0").asString();
    String responseBody = (String) response.getBody();
    
    System.out.println("Player2 move Response: " + responseBody);
    
    // Parse the response to JSON object
    JSONObject jsonObject = new JSONObject(responseBody);
    
    // Check if this move is invalid: This move should be invalid
    assertEquals(true, jsonObject.get("moveValidity"));
    
    System.out.println("Test Player 1 and 2 can draw");
  }
  
  /**
  * This is a test case to evaluate player 1 can win.
  */
  @Test
  @Order(10)
  public void movePlayer1WinTest() {
      	
    //Create HTTP request and get response
    HttpResponse r0 = Unirest.get("http://localhost:8080/newgame").asString();
    HttpResponse r1 = Unirest.post("http://localhost:8080/startgame").body("type=X").asString();
    HttpResponse r2 = Unirest.get("http://localhost:8080/joingame").asString();
    HttpResponse r3 = Unirest.post("http://localhost:8080/move/1").body("x=0&y=0").asString();
    HttpResponse r4 = Unirest.post("http://localhost:8080/move/2").body("x=1&y=2").asString();
    HttpResponse r5 = Unirest.post("http://localhost:8080/move/1").body("x=0&y=1").asString();
    HttpResponse r6 = Unirest.post("http://localhost:8080/move/2").body("x=1&y=0").asString();
    HttpResponse response = Unirest.post("http://localhost:8080/move/1").body("x=0&y=2").asString();
    String responseBody = (String) response.getBody();
    
    System.out.println("Player1 move Response: " + responseBody);
    
    // Parse the response to JSON object
    JSONObject jsonObject = new JSONObject(responseBody);
    
    // Check if this move is invalid: This move should be invalid
    assertEquals(true, jsonObject.get("moveValidity"));
    
    System.out.println("Test Player 1 can win");
  }
  
  /**
  * This is a test case to evaluate player 2 can win.
  */
  @Test
  @Order(11)
  public void movePlayer2WinTest() {
      	
    //Create HTTP request and get response
    HttpResponse r0 = Unirest.get("http://localhost:8080/newgame").asString();
    HttpResponse r1 = Unirest.post("http://localhost:8080/startgame").body("type=X").asString();
    HttpResponse r2 = Unirest.get("http://localhost:8080/joingame").asString();
    HttpResponse r3 = Unirest.post("http://localhost:8080/move/1").body("x=0&y=0").asString();
    HttpResponse r4 = Unirest.post("http://localhost:8080/move/2").body("x=1&y=0").asString();
    HttpResponse r5 = Unirest.post("http://localhost:8080/move/1").body("x=0&y=1").asString();
    HttpResponse r6 = Unirest.post("http://localhost:8080/move/2").body("x=1&y=1").asString();
    HttpResponse r7 = Unirest.post("http://localhost:8080/move/1").body("x=2&y=0").asString();
    HttpResponse response = Unirest.post("http://localhost:8080/move/2").body("x=1&y=2").asString();
    String responseBody = (String) response.getBody();
    
    System.out.println("Player2 move Response: " + responseBody);
    
    // Parse the response to JSON object
    JSONObject jsonObject = new JSONObject(responseBody);
    
    // Check if this move is invalid: This move should be invalid
    assertEquals(true, jsonObject.get("moveValidity"));
    
    System.out.println("Test Player 2 can win");
  }
  
  /**
  * This is a test case to evaluate the move player 1 invalid endpoint.
  */
  @Test
  @Order(12)
  public void movePlayer1InvalidTest() {
      	
    //Create HTTP request and get response
    HttpResponse r0 = Unirest.get("http://localhost:8080/newgame").asString();
    HttpResponse r1 = Unirest.post("http://localhost:8080/startgame").body("type=X").asString();
    HttpResponse r2 = Unirest.get("http://localhost:8080/joingame").asString();
    HttpResponse r3 = Unirest.post("http://localhost:8080/move/1").body("x=0&y=0").asString();
    HttpResponse r4 = Unirest.post("http://localhost:8080/move/2").body("x=1&y=1").asString();
    HttpResponse response = Unirest.post("http://localhost:8080/move/1").body("x=0&y=0").asString();
    String responseBody = (String) response.getBody();
    
    System.out.println("Player1 move Response: " + responseBody);
    
    // Parse the response to JSON object
    JSONObject jsonObject = new JSONObject(responseBody);
    
    // Check if this move is invalid: This move should be invalid
    assertEquals(false, jsonObject.get("moveValidity"));
    
    System.out.println("Test Player 1 Move invalid");
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
    // Stop Server
    PlayGame.stop();
    System.out.println("After All");
  }
}
