package models;

public class Move {

  private Player player;

  private int moveX;

  private int moveY;

  public Player getPlayer() {
    return player;
  }

  public void setPlayer(Player player) {
    this.player = player;
  }

  public int getMoveX() {
    return moveX;
  }

  public void setMoveX(int moveX) {
    this.moveX = moveX;
  }

  public int getMoveY() {
    return moveY;
  }

  public void setMoveY(int moveY) {
    this.moveY = moveY;
  }
  
  /** Retrieve http request body and parse into Move class.
   * @param ctxbody the request body of the http request
   * @param game the GameBoard of the current game
   */
  public void retrieveMove(String ctxbody, GameBoard game) {
    // read all the infos from the request body
    String[] info = ctxbody.split("&");
    String playerId = info[2].split("=")[1];
    if (playerId.equals(game.getP1().getId())) {
      this.setPlayer(game.getP1());
    } else if (playerId.equals(game.getP2().getId())) { 
      this.setPlayer(game.getP2());
    }
    int x = Integer.parseInt(info[0].split("=")[1]);
    int y = Integer.parseInt(info[1].split("=")[1]);
    this.setMoveX(x);
    this.setMoveY(y);
  }
  
  /** Function to check move validity.
   * @param game the GameBoard of the current game
   * @return a Message to be sent back to player.
   */
  public Message checkMoveValidity(GameBoard game) {
    // initialize a message to be sent
    Message myMes = new Message();
    int yourTurn = 2 - game.getTurn() % 2;
    // check for move Validity
    if (game.isGameStarted() == false) {
      // invalid if game is not yet started
      myMes.setNotStarted();
    }  else if (game.isDraw() == true || game.getWinner() != 0) {
      // invalid if game already ended
      myMes.setEnded();
    } else if (yourTurn == 1 && !game.getP1().getId().equals(this.getPlayer().getId())) {
      // invalid if same player keeps playing
      myMes.setWrongTurn(yourTurn);
    } else if (yourTurn == 2 && !game.getP2().getId().equals(this.getPlayer().getId())) {
      // invalid if same player keeps playing
      myMes.setWrongTurn(yourTurn);
    } else if (game.getBoardState()[this.getMoveX()][this.getMoveY()] != '\u0000') {
      // invalid if gameboard position is filled
      myMes.setPositionFilled();
    } else {
      myMes.setValid();
    }
    return myMes;
  }
}
