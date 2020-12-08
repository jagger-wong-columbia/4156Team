package controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.Gson;
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
    String[] input = new String[1]; // create an empty string array as input
    PlayGame.main(input);
    System.out.println("Before All");
  }
    
  
  /**
  * This method starts a new game before every test run. It will run every time before a test.
  */
  @BeforeEach
  public void startNewGame() {
    // Test if server is running. You need to have an endpoint /
    // If you do not wish to have this end point, it is okay to not have anything in this method.
    Unirest.get("http://localhost:8080/").asString();

    System.out.println("Before Each");
  }
    
  
  /**
  * This is a test case to evaluate the newgame endpoint.
  */
  @Test
  @Order(1)
  public void newGameTest() {
        
    // Create HTTP request and get response
    HttpResponse<?> response = Unirest.get("http://localhost:8080/newgame").asString();
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
    // Remember to use asString() only once for an endpoint call. Every time you call asString(),
    // a new request will be sent to the endpoint. 
    // Call it once and then use the data in the object.
    HttpResponse<String> response = Unirest.post("http://localhost:8080/startgame").body("type=X").asString();
    String responseBody = response.getBody();
    
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
  * This is a test case to see if a player can make a move before both players have joined the game.
  * RULE 1.
  */
  @Test
  @Order(3)
  public void moveBeforeJoinTest() {
        
    // Create HTTP POST request for Move and get response(Message)
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move").body("x=1&y=1&turn=1").asString();
    String responseBody = response.getBody();
    
    // --------------------------- JSONObject Parsing ----------------------------------
    
    System.out.println("Move Before Join Response: " + responseBody);
    
    // Parse the response to JSON object
    JSONObject jsonObject = new JSONObject(responseBody);

    // Check if move validity is false when p2 has not joined: Move should be invalid
    assertEquals(false, jsonObject.get("moveValidity"));
    System.out.println("Test Move Before Join");
  }
  
  
  /**
  * This is a test case to see if p1 always make the first move after game started. 
  * RULE 2
  * Two checks for this problem: 1. if the first "turn" of gameboard is 1;
  *                              2. whether the move if invalid if p2 plays first
  *                              No.2 is moved to method firstMoveTest() for readability
  * This method make use of joingame endpoint, and also checks if joingame endpoint is returned
  * correctly and if the second player is set up correctly.
  */
  @Test
  @Order(4)
  public void joinGameTest() {
        
    // Create HTTP request for joingame and get response
    HttpResponse<?> response = Unirest.get("http://localhost:8080/joingame").asString();
    int restStatus = response.getStatus();
        
    // Check if joingame works well
    //assertEquals(restStatus, 200);
    
    // Create HTTP request and get the gameboard
    HttpResponse<String> response1 = Unirest.get("http://localhost:8080/getgame").asString();
    String responseBody = response1.getBody();

    // --------------------------- JSONObject Parsing ----------------------------------
    
    System.out.println("Join Game Response: " + responseBody);
    
    // Parse the response to JSON object
    JSONObject jsonObject = new JSONObject(responseBody);

    // Check if game started after both players have joined
    assertEquals(true, jsonObject.get("gameStarted"));
    
    // Check if player 1 always makes the first move after game starts
    assertEquals(1, jsonObject.get("turn"));
    
    // ---------------------------- GSON Parsing -------------------------
    
    // GSON use to parse data to object
    Gson gson = new Gson();
    GameBoard gameBoard = gson.fromJson(jsonObject.toString(), GameBoard.class);
    Player player2 = gameBoard.getP2();
    
    // Check if player 2 type is correct
    assertEquals('O', player2.getType());
    System.out.println("Test Join Game");
  }
  
  
  /**
  * This is a test case to second check if p1 always makes the first move after game started. 
  * RULE 2
  * In this test, p2 send "move" command after game started, and the move is supposed to be invalid.
  * And the gameboard position specified in move is supposed to be empty.
  */
  @Test
  @Order(5)
  public void p2FirstMoveTest() {
        
    // Create HTTP POST request for Move and get response(Message)
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move").body("x=0&y=2&turn=2").asString();
    String responseBody = response.getBody();
    
    // --------------------------- JSONObject Parsing ----------------------------------
    
    System.out.println("P2 Move Before P1 Response: " + responseBody);
    
    // Parse the response to JSON object
    JSONObject jsonObject = new JSONObject(responseBody);

    // Check if move validity is false when p2 tries to move first
    assertEquals(false, jsonObject.get("moveValidity"));
    // ---------------------------------------------------------------------------------
    
    // Create HTTP request and get the gameboard
    HttpResponse<String> response1 = Unirest.get("http://localhost:8080/getgame").asString();
    String responseBody1 = response1.getBody();
    
    // --------------------------- JSONObject Parsing ----------------------------------
    
    System.out.println("P2 Move Before P1 Gameboard: " + responseBody1);
    
    // Parse the response to JSON object
    JSONObject jsonObject1 = new JSONObject(responseBody1);
    
    // ---------------------------- GSON Parsing -------------------------
    
    // GSON use to parse data to object
    Gson gson = new Gson();
    GameBoard gameBoard = gson.fromJson(jsonObject1.toString(), GameBoard.class);
    // check if the board position specified for move is filled
    assertEquals('\u0000', gameBoard.getBoardState()[0][2]);
    
    System.out.println("Test P2 First Move");
  }
  
  
  /**
  * This is a test case to check if a player can make 2 moves in their turn.
  * RULE 3
  */
  @Test
  @Order(6)
  public void p1TwoMoveTest() {
    
    // Create two HTTP POST request for Move and get response(Message) of second move
    Unirest.post("http://localhost:8080/move").body("x=0&y=2&turn=1").asString();
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move").body("x=1&y=1&turn=1").asString();
    String responseBody = response.getBody();
    
    // --------------------------- JSONObject Parsing ----------------------------------
    
    System.out.println("P1 Make 2 Continuous Move Response: " + responseBody);
    
    // Parse the response to JSON object
    JSONObject jsonObject = new JSONObject(responseBody);

    // Check if move validity is false when p1 tries to move twice
    assertEquals(false, jsonObject.get("moveValidity"));
    // ---------------------------------------------------------------------------------
    
    // Create HTTP request and get the gameboard
    HttpResponse<String> response1 = Unirest.get("http://localhost:8080/getgame").asString();
    String responseBody1 = response1.getBody();
    
    // --------------------------- JSONObject Parsing ----------------------------------
    
    System.out.println("P1 Make 2 Continuous Move Gameboard: " + responseBody1);
    
    // Parse the response to JSON object
    JSONObject jsonObject1 = new JSONObject(responseBody1);
    
    // ---------------------------- GSON Parsing -------------------------
    
    // GSON use to parse data to object
    Gson gson = new Gson();
    GameBoard gameBoard = gson.fromJson(jsonObject1.toString(), GameBoard.class);
    // check if the board position of first move is filled of X
    assertEquals('X', gameBoard.getBoardState()[0][2]);
    // check if the board position of second move is still empty
    assertEquals('\u0000', gameBoard.getBoardState()[1][1]);
        
    System.out.println("Test Continuous Move");
  }
  
  
  /**
  * This is a test case to check if a player can win the game.
  * RULE 4
  * Too many ways to win. This method will only cover one "corner case".
  * Test a diagonal(top right to bottom left) win of p1.
  */
  @Test
  @Order(7)
  public void winGameTest() {
        
    // Create HTTP requests for multiple moves
    Unirest.post("http://localhost:8080/move").body("x=3&y=0&turn=2").asString();
    Unirest.post("http://localhost:8080/move").body("x=1&y=3&turn=1").asString();
    Unirest.post("http://localhost:8080/move").body("x=3&y=1&turn=2").asString();
    Unirest.post("http://localhost:8080/move").body("x=2&y=4&turn=1").asString();
    Unirest.post("http://localhost:8080/move").body("x=3&y=2&turn=2").asString();
    Unirest.post("http://localhost:8080/move").body("x=3&y=5&turn=1").asString();
    Unirest.post("http://localhost:8080/move").body("x=3&y=3&turn=2").asString();
    Unirest.post("http://localhost:8080/move").body("x=4&y=6&turn=1").asString();
    
    // Create HTTP request and get the gameboard
    HttpResponse<String> response = Unirest.get("http://localhost:8080/getgame").asString();
    String responseBody = response.getBody();
    
    // --------------------------- JSONObject Parsing ----------------------------------
    
    System.out.println("P1 Wins Game Gameboard: " + responseBody);
    
    // Parse the response to JSON object
    JSONObject jsonObject = new JSONObject(responseBody);
    
    // Check if winner is p1
    assertEquals(1, jsonObject.get("winner"));
    
    System.out.println("Test Win Game");
  }
  
  
  /**
  * This is a test case to check if a player can move after the game ends.
  */
  @Test
  @Order(8)
  public void moveAfterEndTest() {
    
    // Create HTTP POST request for Move and get response(Message)
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move").body("x=12&y=12&turn=2").asString();
    String responseBody = response.getBody();
     
    // --------------------------- JSONObject Parsing ----------------------------------
     
    System.out.println("P2 Move After End Response: " + responseBody);
     
    // Parse the response to JSON object
    JSONObject jsonObject = new JSONObject(responseBody);
     
    // Check if move validity is false when p1 tries to move after game ends
    assertEquals(false, jsonObject.get("moveValidity"));
     
    System.out.println("Test Move After End");
  }
  
  
  /**
  * This is a test case to restart the game with p1 using 'O' and test the echo endpoint.
  */
  @Test
  @Order(9)
  public void restartGameTest() {
    
    // Create HTTP POST request for new game, start game and join game
    Unirest.get("http://localhost:8080/newgame").asString();
    Unirest.post("http://localhost:8080/startgame").body("type=O").asString();
    Unirest.get("http://localhost:8080/joingame").asString();
    
    // Testing the echo
    HttpResponse<String> response1 = Unirest.post("http://localhost:8080/echo").body("hi there").asString();
    String responseBody1 = response1.getBody();
     
    // --------------------------- JSONObject Parsing ----------------------------------
     
    System.out.println("Echo Response: " + responseBody1);
     
    // Check if input and output are the same
    assertEquals(responseBody1, "hi there");
     
    System.out.println("Test Restart Game");
  }
  
  
  /**
  * This is a test case to restart a game and check the response if the board position is filled.
  */
  @Test
  @Order(11)
  public void filledBoardTest() {
    
    // Create HTTP POST request for restart of game, and p2's request to move on a filled position
    Unirest.get("http://localhost:8080/newgame").asString();
    Unirest.post("http://localhost:8080/startgame").body("type=X").asString();
    Unirest.get("http://localhost:8080/joingame").asString();
    Unirest.post("http://localhost:8080/move").body("x=0&y=0&turn=1").asString();
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move").body("x=0&y=0&turn=2").asString();
    String responseBody = response.getBody();
    
    // --------------------------- JSONObject Parsing ----------------------------------
    
    System.out.println("Filled Board Response: " + responseBody);
    
    // Parse the response to JSON object
    JSONObject jsonObject = new JSONObject(responseBody);

    // Check if the move is invalid
    assertEquals(false, jsonObject.get("moveValidity"));
     
    System.out.println("Test Filled Board");
  }
  
  
  /**
  * This is a test case to test a horizontal win of p2.
  */
  @Test
  @Order(12)
  public void p2HorizontalWinTest() {
    
    // Create HTTP POST request for a flow of moves that lead to horizontal win of p2
    // then get the gameboard
    Unirest.post("http://localhost:8080/move").body("x=1&y=0&turn=2").asString();
    Unirest.post("http://localhost:8080/move").body("x=0&y=2&turn=1").asString();
    Unirest.post("http://localhost:8080/move").body("x=1&y=1&turn=2").asString();
    Unirest.post("http://localhost:8080/move").body("x=2&y=2&turn=1").asString();
    Unirest.post("http://localhost:8080/move").body("x=1&y=2&turn=2").asString();
    Unirest.post("http://localhost:8080/move").body("x=4&y=4&turn=1").asString();
    Unirest.post("http://localhost:8080/move").body("x=1&y=3&turn=2").asString();
    Unirest.post("http://localhost:8080/move").body("x=6&y=6&turn=1").asString();
    Unirest.post("http://localhost:8080/move").body("x=1&y=4&turn=2").asString();
    Unirest.post("http://localhost:8080/move").body("x=10&y=14&turn=1").asString();
    Unirest.post("http://localhost:8080/move").body("x=1&y=5&turn=2").asString();
    HttpResponse<String> response = Unirest.get("http://localhost:8080/getgame").asString();
    String responseBody = response.getBody();
    
    // --------------------------- JSONObject Parsing ----------------------------------
    
    System.out.println("P2 Horizontal Win Response: " + responseBody);
    
    // Parse the response to JSON object
    JSONObject jsonObject = new JSONObject(responseBody);

    // Check if game winner is p2
    assertEquals(2, jsonObject.get("winner"));
     
    System.out.println("Test P2 Horizontal Win");
  }
  
  
  /**
  * This is a test case to test a vertical win of p1.
  */
  @Test
  @Order(13)
  public void p1VerticalWinTest() {
    
    // Create HTTP POST request for restart of the game,
    // and a flow of requests to get p1 win vertically
    Unirest.get("http://localhost:8080/newgame").asString();
    Unirest.post("http://localhost:8080/startgame").body("type=X").asString();
    Unirest.get("http://localhost:8080/joingame").asString();
    Unirest.post("http://localhost:8080/move").body("x=1&y=0&turn=1").asString();
    Unirest.post("http://localhost:8080/move").body("x=6&y=0&turn=2").asString();
    Unirest.post("http://localhost:8080/move").body("x=2&y=0&turn=1").asString();
    Unirest.post("http://localhost:8080/move").body("x=3&y=1&turn=2").asString();
    Unirest.post("http://localhost:8080/move").body("x=3&y=0&turn=1").asString();
    Unirest.post("http://localhost:8080/move").body("x=3&y=2&turn=2").asString();
    Unirest.post("http://localhost:8080/move").body("x=4&y=0&turn=1").asString();
    Unirest.post("http://localhost:8080/move").body("x=3&y=3&turn=2").asString();
    Unirest.post("http://localhost:8080/move").body("x=5&y=0&turn=1").asString();
    HttpResponse<String> response = Unirest.get("http://localhost:8080/getgame").asString();
    String responseBody = response.getBody();
    
    // --------------------------- JSONObject Parsing ----------------------------------
    
    System.out.println("P1 Vertical Win Response: " + responseBody);
    
    // Parse the response to JSON object
    JSONObject jsonObject = new JSONObject(responseBody);

    // Check if game winner is p1
    assertEquals(1, jsonObject.get("winner"));
     
    System.out.println("Test P1 Vertical Win");
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