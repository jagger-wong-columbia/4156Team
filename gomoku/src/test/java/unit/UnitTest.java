package unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import models.GameBoard;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(OrderAnnotation.class) 
public class UnitTest {
  GameBoard board = new GameBoard();
  
  @Test
  @Order(1)
  public void testInit() {
    
    board.initialize();
    
    assertEquals(null, board.getP1());
  }
  
  @Test
  @Order(2)
  public void testStartGame() {
    board.initialize();
    board.startGame('O');
    assertEquals('O', board.getP1().getType());
    assertEquals(false, board.getStartStatus());
  }
  
  @Test
  @Order(3)
  public void testJoinPlayer() {
    board.initialize();   
    board.startGame('O');
    board.joinPlayer();
    
    assertEquals(true, board.getStartStatus());
    
    board.initialize();   
    board.startGame('X');
    board.joinPlayer();
    
    assertEquals(true, board.getStartStatus());
  }
  
  @Test
  @Order(4)
  public void testMakeMove() {
    board.initialize();
    board.startGame('O');
    board.joinPlayer();
    
    assertEquals(true, board.makeMove(0, 0, 1));
    
    board.initialize();
    board.startGame('O');
    board.joinPlayer();
    board.makeMove(0, 0, 1);
    
    assertEquals(false, board.makeMove(0, 0, 2));
    
    board.initialize();
    board.startGame('X');
    board.joinPlayer();
    board.makeMove(0, 0, 1);
    
    assertEquals(false, board.makeMove(0, 0, 2));
    
    board.initialize();
    board.startGame('X');
    board.joinPlayer();
    board.makeMove(0, 0, 1);
    
    assertEquals(true, board.makeMove(1, 1, 2));
    
    board.initialize();
    board.startGame('X');
    board.joinPlayer();
    board.makeMove(0, 0, 1);
    board.makeMove(0, 1, 2);
    
    assertEquals(true, board.makeMove(1, 1, 1));
    
    board.initialize();
    board.startGame('X');
    board.joinPlayer();
    board.makeMove(0, 0, 1);
    board.makeMove(0, 1, 2);
    
    assertEquals(true, board.makeMove(2, 2, 1));
  }
  
  @Test
  @Order(5)
  public void testCheckWin() {
    board.initialize();
    board.startGame('O');
    board.joinPlayer();
    
    board.makeMove(0, 1, 2);
    assertEquals(false, board.checkWin(2, 0, 1));

    board.makeMove(1, 1, 1);
    board.makeMove(0, 2, 2);
    board.makeMove(2, 2, 1);
    assertEquals(false, board.checkWin(1, 2, 2));
    
    board.initialize();
    board.startGame('X');
    board.joinPlayer();
    board.makeMove(0, 0, 1);
    board.makeMove(2, 2, 2);
    board.makeMove(0, 1, 1);
    board.makeMove(1, 0, 2);
    board.makeMove(0, 2, 1);
    assertEquals(true, board.checkWin(1, 0, 2));
    
    board.initialize();
    board.startGame('X');
    board.joinPlayer();
    board.makeMove(0, 0, 1);
    board.makeMove(2, 2, 2);
    board.makeMove(1, 0, 1);
    board.makeMove(1, 1, 2);
    board.makeMove(2, 0, 1);
    assertEquals(true, board.checkWin(1, 2, 0));
    
    board.initialize();
    board.startGame('X');
    board.joinPlayer();
    board.makeMove(0, 0, 1);
    board.makeMove(1, 2, 2);
    board.makeMove(1, 1, 1);
    board.makeMove(1, 0, 2);
    board.makeMove(2, 2, 1);
    assertEquals(true, board.checkWin(1, 2, 2));
    
    board.initialize();
    board.startGame('X');
    board.joinPlayer();
    board.makeMove(0, 2, 1);
    board.makeMove(2, 2, 2);
    board.makeMove(1, 1, 1);
    board.makeMove(2, 1, 2);
    board.makeMove(2, 0, 1);
    assertEquals(true, board.checkWin(1, 2, 0));
    
    board.initialize();
    board.startGame('X');
    board.joinPlayer();
    board.makeMove(0, 0, 1);
    board.makeMove(0, 2, 2);
    board.makeMove(0, 1, 1);
    board.makeMove(1, 0, 2);
    board.makeMove(1, 1, 1);
    board.makeMove(2, 1, 2);
    board.makeMove(1, 2, 1);
    board.makeMove(2, 2, 2);
    board.makeMove(2, 0, 1);
    assertEquals(false, board.checkWin(1, 2, 0));
  }
}
