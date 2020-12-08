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
    this.gameID = uuid.toString();
    this.p1 = p1;
    this.gameStarted = false;
    this.turn = 1;
    this.winner = 0;
    this.isDraw = false;
    this.boardState = new char[15][15];
    for (int i = 0; i < 15; i++) {
      char[] temp = new char[15];
      Arrays.fill(temp, '\u0000');
      this.boardState[i] = temp.clone();
    }
    
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

  /** Automatically set P2 based on the type of P1.
   */
  public void autoSetP2() {
    char p2Type;
    if (this.getP1().getType() == 'X') {
      p2Type = 'O';
    } else {
      p2Type = 'X';
    }
    Player p2 = new Player(p2Type, "2");
    this.setP2(p2); // add p2 to current game
  }

  /** Update the GameBoard based on the valid move.
   * @param currentMove the valid move accepted by the game
   */
  public void update(Move currentMove) {
    // update the gameboard
    this.boardState[currentMove.getMoveX()][currentMove.getMoveY()] =
          currentMove.getPlayer().getType();
    // decide game winner or game draw
    this.gameJudge(currentMove);
    this.setTurn(this.getTurn() + 1); // increase the turn counter
  }
  
  /** Function to compute whether the game has ended. The game logic.
   * @param currentMove the valid move accepted by the game
   */
  public void gameJudge(Move currentMove) {
    // determine if any player wins
    char[][] currentBoard = this.getBoardState();
    int yourTurn = 2 - this.getTurn() % 2; // p1 / p2 's turn
    
    // check rows and columns
    for (int i = 0; i < 15; i++) {
      // check rows
      int count = 0;
      if (currentBoard[i][0] != '\u0000') {
        count = 1;
      }
      for (int j = 1; j < 15; j++) {
        if (currentBoard[i][j] != '\u0000' && currentBoard[i][j] == currentBoard[i][j - 1]) {
          count += 1;
        } else if (currentBoard[i][j] != '\u0000') {
          count = 1;
        } else {
          count = 0;
        }
        if (count >= 5) {
          this.setWinner(yourTurn);
          return;
        }
      }
      // check columns
      int countC = 0;
      if (currentBoard[0][i] != '\u0000') {
        countC = 1;
      }
      for (int j = 1; j < 15; j++) {
        if (currentBoard[j][i] != '\u0000' && currentBoard[j][i] == currentBoard[j - 1][i]) {
          countC += 1;
        } else if (currentBoard[j][i] != '\u0000') {
          countC = 1;
        } else {
          countC = 0;
        }
        if (countC >= 5) {
          this.setWinner(yourTurn);
          return;
        }
      }
    }
    
    // check diagonals
    int posx = 10;
    int posy = 0;
    for (int i = 0; i < 21; i++) {
      int x = posx;
      int y = posy;
      int count = 0;
      if (currentBoard[x][y] != '\u0000') {
        count = 1;
      }
      while (x < 14 && y < 14) {
        x++;
        y++;
        if (currentBoard[x][y] != '\u0000' && currentBoard[x][y] == currentBoard[x - 1][y - 1]) {
          count += 1;
        } else if (currentBoard[x][y] != '\u0000') {
          count = 1;
        } else {
          count = 0;
        }
        if (count >= 5) {
          this.setWinner(yourTurn);
          return;
        }
      }
      if (posx > 0) {
        posx--;
      } else {
        posy++;
      }
    }

    // check antidiagonals
    posx = 10;
    posy = 14;
    for (int i = 0; i < 21; i++) {
      int x = posx;
      int y = posy;
      int count = 0;
      if (currentBoard[x][y] != '\u0000') {
        count = 1;
      }
      while (x < 14 && y > 0) {
        x++;
        y--;
        if (currentBoard[x][y] != '\u0000' && currentBoard[x][y] == currentBoard[x - 1][y + 1]) {
          count += 1;
        } else if (currentBoard[x][y] != '\u0000') {
          count = 1;
        } else {
          count = 0;
        }
        if (count >= 5) {
          this.setWinner(yourTurn);
          return;
        }
      }
      if (posx > 0) {
        posx--;
      } else {
        posy--;
      }
    }
    // determine if it is draw if the game board is filled and no one wins
    if (this.getTurn() == 225) {
      this.setDraw(true);
    }
  }
}
