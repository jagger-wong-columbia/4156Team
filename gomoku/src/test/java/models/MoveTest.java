package models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

class MoveTest {
  
  /**
  * This is a test case to evaluate the retrieveMove method.
  */
  @Test
  @Order(1)
  public void retrieveTest() {
    String ctxbody = "x=0&y=13&turn=1";
    
    Player p1 = new Player('X', "1");
    GameBoard testGame = new GameBoard(p1);
    testGame.autoSetP2();

    Move testMove = new Move();
    testMove.retrieveMove(ctxbody, testGame);
    
    // Check assert statement (New Game has started)
    assertEquals(testMove.getPlayer().getId(), "1");
    assertEquals(testMove.getMoveX(), 0);
    assertEquals(testMove.getMoveY(), 13);
    System.out.println("Test retrieve Move");
  }

  /**
  * This is a test case to evaluate the checkMoveValidity method.
  */
  @Test
  @Order(2)
  public void validityTest() {
    String ctxbody = "x=0&y=13&turn=1";
    
    Player p1 = new Player('X', "1");
    GameBoard testGame = new GameBoard(p1);
    testGame.autoSetP2();
    testGame.setGameStarted(true); // update game status

    Move testMove = new Move();
    testMove.retrieveMove(ctxbody, testGame);
    Message testMes = testMove.checkMoveValidity(testGame);
    
    // Check assert statement (New Game has started)
    assertEquals(testMes.getCode(), 200);
    assertEquals(testMes.getMoveValidity(), true);
    System.out.println("Test valid move");
  }

  /**
  * This is a test case to evaluate the checkMoveValidity method in an invalid case.
  */
  @Test
  @Order(3)
  public void invalidityTest() {
    String ctxbody = "x=0&y=13&turn=2";
    
    Player p1 = new Player('X', "1");
    GameBoard testGame = new GameBoard(p1);
    testGame.autoSetP2();
    testGame.setGameStarted(true); // update game status

    Move testMove = new Move();
    testMove.retrieveMove(ctxbody, testGame);
    Message testMes = testMove.checkMoveValidity(testGame);
    
    // Check assert statement (New Game has started)
    assertEquals(testMes.getCode(), 402);
    assertEquals(testMes.getMoveValidity(), false);
    assertEquals(testMes.getMessage(), "It is Player1 turn!");
    System.out.println("Test invalid move");
  }
}
