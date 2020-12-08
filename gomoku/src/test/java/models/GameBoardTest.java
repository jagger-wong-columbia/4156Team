package models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.Gson;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;


class GameBoardTest {
  
  /**
  * This is a test case to evaluate the autoSetP2 method.
  */
  @Test
  @Order(1)
  public void autoSetTest() {
    Player p1 = new Player('X', "1");
    GameBoard testGame = new GameBoard(p1);
    testGame.autoSetP2();
    
    // Check assert statement (New Game has started)
    assertEquals(testGame.getP2().getType(), 'O');
    System.out.println("Test auto set");
  }

  /**
  * This is a test case to evaluate the update method.
  */
  @Test
  @Order(2)
  public void updateTest() {
    Player p1 = new Player('X', "1");
    GameBoard testGame = new GameBoard(p1);
    testGame.autoSetP2();
    testGame.setGameStarted(true); // update game status

    Move testMove = new Move();
    testMove.setPlayer(p1);
    testMove.setMoveX(8);
    testMove.setMoveY(12);
    
    testGame.update(testMove);
    
    // Check assert statement (New Game has started)
    assertEquals(testGame.getBoardState()[8][12], 'X');
    assertEquals(testGame.getWinner(), 0);
    System.out.println("Test update");
  }
  

  /**
  * This is a test case to evaluate the gameJudge method.
  */
  @Test
  @Order(3)
  public void judgeTest() {
    Player p1 = new Player('X', "1");
    GameBoard testGame = new GameBoard(p1);
    testGame.autoSetP2();
    testGame.setGameStarted(true); // update game status
    char[][] currentBoard = testGame.getBoardState();
    Gson gson = new Gson();
    String gameBoardJson = gson.toJson(testGame);
    System.out.println(gameBoardJson);
    currentBoard[5][5] = 'X';
    currentBoard[4][6] = 'X';
    currentBoard[3][7] = 'X';
    currentBoard[2][8] = 'X';
    currentBoard[9][9] = 'O';
    currentBoard[9][8] = 'O';
    currentBoard[0][0] = 'O';
    currentBoard[7][9] = 'O';
    assertEquals(testGame.getWinner(), 0);

    Move testMove = new Move();
    testMove.setPlayer(p1);
    testMove.setMoveX(1);
    testMove.setMoveY(9);
    
    testGame.update(testMove);
    
    // Check assert statement (New Game has started)
    assertEquals(testGame.getWinner(), 1);
    gameBoardJson = gson.toJson(testGame);
    System.out.println(gameBoardJson);
    System.out.println("Test update");
  }
}
