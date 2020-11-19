package models;

public class Record {
  private String gameID;

  private Player p1;

  private Player p2;

  private char[][] boardState;

  private int winner; // 0 draw, 1 & 2 two players

  /**
   * Constructor with all the preconditions set.
   */
  public Record(String gameID, Player p1, Player p2, char[][] boardState, int winner) {
    this.gameID = gameID;
    this.p1 = p1;
    this.p2 = p2;
    this.boardState = boardState.clone();
    this.winner = winner;
  }
  
  /**
   * Constructor with all the preconditions set.
   */
  public Record(GameBoard gameBoard) {
    this.gameID = gameBoard.getGameID();
    this.p1 = gameBoard.getP1();
    this.p2 = gameBoard.getP2();
    this.boardState = gameBoard.getBoardState().clone();
    this.winner = gameBoard.getWinner();
  }

  public String getGameID() {
    return gameID;
  }

  public void setGameID(String gameID) {
    this.gameID = gameID;
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

}
