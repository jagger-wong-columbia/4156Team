package controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.Gson;
import java.sql.SQLException;
import models.GameBoard;
import models.Move;
import models.Player;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;


class DatabaseTest {
  /**
  * This is a test case to evaluate the database clean operation.
   * @throws SQLException nothing
  */
  @Test
  @Order(1)
  public void cleanTest() throws SQLException {
    DatabaseOp.cleanGameBoardJson();
    String testOutput = DatabaseOp.getGameBoardJson();
        
    // Check assert statement (New Game has started)
    assertEquals(testOutput, null);
    System.out.println("Test clean");
  }

  /**
  * This is a test case to evaluate the database insertion operation.
   * @throws SQLException nothing
  */
  @Test
  @Order(2)
  public void insertTest() throws SQLException {
    DatabaseOp.cleanGameBoardJson();
    Player p1 = new Player('X', "1");
    GameBoard testGame = new GameBoard(p1);
    testGame.autoSetP2();
    testGame.setGameStarted(true); // update game status

    Move testMove = new Move();
    testMove.setPlayer(p1);
    testMove.setMoveX(8);
    testMove.setMoveY(12);
    
    testGame.update(testMove);
    
    Gson gson = new Gson();
    String gameBoardJson = gson.toJson(testGame);
    DatabaseOp.insertGameBoardJson(gameBoardJson);
    String testOutput = DatabaseOp.getGameBoardJson();
        
    // Check assert statement (New Game has started)
    assertEquals(gameBoardJson, testOutput);
    System.out.println("Test insert");
  }

  /**
  * This is a test case to evaluate the database insertion operation.
   * @throws SQLException nothing
  */
  @Test
  @Order(3)
  public void updateTest() throws SQLException {
    DatabaseOp.cleanGameBoardJson();
    Player p1 = new Player('X', "1");
    GameBoard testGame = new GameBoard(p1);
    testGame.autoSetP2();
    testGame.setGameStarted(true); // update game status
    Gson gson = new Gson();
    String gameBoardJson = gson.toJson(testGame);
    DatabaseOp.insertGameBoardJson(gameBoardJson);

    Move testMove = new Move();
    testMove.setPlayer(p1);
    testMove.setMoveX(8);
    testMove.setMoveY(12);
    
    testGame.update(testMove);
    
    gameBoardJson = gson.toJson(testGame);
    DatabaseOp.updateGameBoardJson(gameBoardJson);;
    String testOutput = DatabaseOp.getGameBoardJson();
        
    // Check assert statement (New Game has started)
    assertEquals(gameBoardJson, testOutput);
    System.out.println("Test update");
  }


}
