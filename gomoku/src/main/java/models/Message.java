package models;

public class Message {

  private boolean moveValidity;

  private int code;

  private String message;
  
  /** Confirm method of the message.
   */
  // Send valid message;
  public void setValid() {
    this.moveValidity = true;
    this.code = 100;
    this.message = "";
  }
  
  /** Confirm method of the message.
   */
  // Send invalid message;
  public void setInvalid() {
    this.moveValidity = false;
    this.code = 200;
    this.message = "You cannot make invalid move";
  }
  
  /** Success method of the message.
   */
  // Send win message;
  public void setWin(int playerId) {
    this.moveValidity = true;
    this.code = 100;
    this.message = "Player " + playerId + " has won the game!";
  }
}
