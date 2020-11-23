package models;

public class Player {

  private char type;

  private String id;
  
  public Player() {
    this.type = 'X';
    this.id = null;
  }
  
  /** Constructor of Player.
   * @param type the type to set
   * @param id the id to set
   */
  public Player(char type, String id) {
    this.type = type;
    this.id = id;
  }

  public char getType() {
    return type;
  }

  public void setType(char type) {
    this.type = type;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

}
