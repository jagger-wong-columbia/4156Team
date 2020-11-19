package models;

import java.util.Arrays;
import java.util.UUID;

public class GameBoard {

  private String gameID;

  private Player p1;

  private Player p2;

  private boolean gameStarted;

  private int turn;

  private char[][] boardState;

  private int winner;

  private boolean isDraw;
  
  /** Constructor with all the preconditions set. 
   * @param p1 Player 1 of the game
   */
  public GameBoard(Player p1) {
    UUID uuid = UUID.randomUUID();
    this.setGameID(uuid.toString());
    this.p1 = p1;
    this.gameStarted = false;
    this.turn = 1;
    this.winner = 0;
    this.isDraw = false;
    char[][] bs = new char[15][15];
    char[] temp = new char[15];
    Arrays.fill(temp, '\u0000');
    Arrays.fill(bs, temp);
    this.boardState = bs;
    
  }

  public Player getP1() {
    return p1;
  }

  public void setP1(Player p1) {
    this.p1 = p1;
  }

  public Player getP2() {
    return p2;
  }

  public void setP2(Player p2) {
    this.p2 = p2;
  }

  public boolean isGameStarted() {
    return gameStarted;
  }

  public void setGameStarted(boolean gameStarted) {
    this.gameStarted = gameStarted;
  }

  public int getTurn() {
    return turn;
  }

  public void setTurn(int turn) {
    this.turn = turn;
  }

  public char[][] getBoardState() {
    return boardState.clone();
  }

  public void setBoardState(char[][] boardState) {
    this.boardState = boardState.clone();
  }

  public int getWinner() {
    return winner;
  }

  public void setWinner(int winner) {
    this.winner = winner;
  }

  public boolean isDraw() {
    return isDraw;
  }

  public void setDraw(boolean isDraw) {
    this.isDraw = isDraw;
  }

  public String getGameID() {
    return gameID;
  }

  public void setGameID(String gameID) {
    this.gameID = gameID;
  }

}
