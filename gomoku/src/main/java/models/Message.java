package models;

public class Message {

  private boolean moveValidity;

  private int code;

  private String message;
  
  public boolean getMoveValidity() {
    return moveValidity;
  }
  
  public int getCode() {
    return code;
  }
  
  public String getMessage() {
    return message;
  }

  /** Initialize a valid message.
   */
  // Send valid message;
  public void setValid() {
    this.moveValidity = true;
    this.code = 200;
  }
  
  /** Initialize a invalid message when the game is not started.
   */
  // Send valid message;
  public void setNotStarted() {
    this.moveValidity = false;
    this.code = 400;
    this.message = "Game is not started yet!";
  }
  
  /** Initialize a invalid message when the game is ended.
   */
  // Send valid message;
  public void setEnded() {
    this.moveValidity = false;
    this.code = 401;
    this.message = "Game has ended!";
  }

  /** Initialize a invalid message when it is the wrong turn for the player.
   * @param yourTurn either 1 or 2, referring to p1 or p2 of the game.
   */
  // Send valid message;
  public void setWrongTurn(int yourTurn) {
    this.moveValidity = false;
    this.code = 402;
    this.message = "It is Player" + yourTurn + " turn!";
  }
  
  /** Initialize a invalid message when the position is filled.
   */
  // Send valid message;
  public void setPositionFilled() {
    this.moveValidity = false;
    this.code = 403;
    this.message = "This position is filled!";
  }
}