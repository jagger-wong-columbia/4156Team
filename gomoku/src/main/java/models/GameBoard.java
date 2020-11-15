package models;

public class GameBoard {

  private Player p1;

  private Player p2;

  private boolean gameStarted;

  private int turn;

  private char[][] boardState;

  private int winner;

  private boolean isDraw;
  
  private int[] rows;

  private int[] cols;
  
  private int diagonal;
  
  private int antidiagonal;
  
  private int count;
  
  /** Initialize method of the board.
   */
  // Initialize game;
  public void initialize() {
    this.winner = 0;
    this.isDraw = false;
    this.rows = new int[3];
    this.cols = new int[3];
    this.diagonal = 0;
    this.antidiagonal = 0;
    this.boardState = new char[3][3];
    this.count = 0;
  }
  
  /** Start method of the board.
   */
  // Start game;
  public void startGame(char type) {
    this.p1 = new Player();
    this.p1.setPlayer(type, 1);
    this.turn = 1;
  }
  
  /** Join method of the board.
   */
  // Player2 join;
  public void joinPlayer() {
    this.p2 = new Player();
    char type = this.p1.getType();
    if (type == 'O') {
      this.p2.setPlayer('X', 2);
    } else {
      this.p2.setPlayer('O', 2);
    }
    this.gameStarted = true;
  }
  
  // get current turn;
  public int getTurn() {
    return this.turn;
  }
  
  /** Move method of the board.
   */
  // Make a move;
  public boolean makeMove(int x, int y, int playerId) {
    // check if this move is valid;
    if (this.boardState[x][y] == 'O' || this.boardState[x][y] == 'X') {
      return false;
    } else {
      ++count;
      if (playerId == 1) {
        this.boardState[x][y] = this.p1.getType();
        this.rows[x] += 1;
        this.cols[y] += 1;
        if (x == y) {
          this.diagonal += 1;
        }
        if (x + y == 2) {
          this.antidiagonal += 1;
        }
      } else {
        this.boardState[x][y] = this.p2.getType();
        this.rows[x] -= 1;
        this.cols[y] -= 1;
        if (x == y) {
          this.diagonal -= 1;
        }
        if (x + y == 2) {
          this.antidiagonal -= 1;
        }
      }
      return true;
    }
  }
  
  // switch turn;
  public void setTurn(int playerId) {
    this.turn = playerId;
  }
  
  /** Check method of the board.
   */
  // check if one player win or if two player draw;
  public boolean checkWin(int playerId, int x, int y) {
    if (Math.abs(this.rows[x]) == 3 || Math.abs(this.cols[y]) == 3
        || Math.abs(this.diagonal) == 3 || Math.abs(this.antidiagonal) == 3) {
      this.gameStarted = false;
      this.winner = playerId;
      return true;
    }
    if (count == 9) {
      this.isDraw = true;
    }
    return false;
  }

  public Player getP1() {
    return this.p1;
  }

  public Player getP2() {
    return this.p2;
  }

  public Object getStartStatus() {
    return this.gameStarted;
  }

  public int getCount() {
    return this.count;
  }
  
  /** get board state.
   */
  public String getboardState() {
    String res = "";
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        if (this.boardState[i][j] == '\u0000') {
          res += "0";
        } else {
          res += this.boardState[i][j];
        }
      }
      if (i != 2) {
        res += ";";
      }
    }
    return res;
  }

  public int getWinner() {
    return this.winner;
  }
  
  public boolean getisDraw() {
    return this.isDraw;
  }
  
  public String getRows() {
    return this.rows[0] + ";" + this.rows[1] + ";" + this.rows[2];
  }
  
  public String getCols() {
    return this.cols[0] + ";" + this.cols[1] + ";" + this.cols[2];
  }
  
  public int getDiagonal() {
    return this.diagonal;
  }
  
  public int getAntidiagonal() {
    return this.antidiagonal;
  }
  
  /** set board state.
   */  
  public void setBoard(char p1, char p2, boolean gameStarted, int turn, String boardState,
       int winner, String rows, String cols, int diagonal, int antidiagonal, int count) {
    this.p1 = new Player();
    this.p1.setPlayer(p1, 1);
    if (p2 != '0') {
      this.p2 = new Player();
      this.p2.setPlayer(p2, 2);
    } else {
      this.p2 = null;
    }
    this.gameStarted = gameStarted;
    this.turn = turn;
    String[] info = boardState.split(";");
    this.boardState = new char[3][3];
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        this.boardState[i][j] = info[i].charAt(j) == '0' ? '\u0000' : info[i].charAt(j);
      }
    }
    this.winner = winner;
    this.rows = new int[3];
    this.cols = new int[3];
    for (int i = 0; i < 3; i++) {
      this.rows[i] = Integer.valueOf(rows.charAt(i));
      this.cols[i] = Integer.valueOf(cols.charAt(i));
    }
    this.diagonal = diagonal;
    this.antidiagonal = antidiagonal;
    this.count = count;
  }
}
