package controllers;

import com.google.gson.Gson;
import io.javalin.Javalin;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Queue;
import models.GameBoard;
import models.Message;
import models.Move;
import models.Player;
import org.eclipse.jetty.websocket.api.Session;

class PlayGame {

  private static final int PORT_NUMBER = 8080;

  private static Javalin app;
  
  private static GameBoard game; // global variable game for all endpoints to access.

  /** Main method of the application.
   * @param args Command line arguments
   */
  public static void main(final String[] args) {
    // Database already initialized with table, leave 500 bytes for gameboard json
    // String sql = "CREATE TABLE GAMEBOARD "
    //    + "(ID INT PRIMARY KEY     NOT NULL,"
    //    + " GAMEBOARDJSON  CHAR(500))";
    
    // Javalin initialization
    app = Javalin.create(config -> {
      config.addStaticFiles("/public");
    }).start(PORT_NUMBER);

    // Test Server Status
    app.get("/", ctx -> {
      ctx.result("Server is ready!");
    });
    
    // Test Echo Server
    app.post("/echo", ctx -> {
      ctx.result(ctx.body());
    });

    
    // newgame Endpoint
    app.get("/newgame", ctx -> {
      // clean the table gameboard of database. This table holds the current game.
      DatabaseOp.cleanGameBoardJson();
      
      ctx.redirect("/gomoku.html"); // TODO: Modify to the new pvp address
    });
    
    
    // startgame Endpoint
    app.post("/startgame", ctx -> {
      // initialize player 1 and a new game board based on that
      Player p1 = new Player(ctx.body().charAt(5), "1");
      game = new GameBoard(p1);
      
      // turn gameboard into json and return
      Gson gson = new Gson();
      String gameBoardJson = gson.toJson(game);
      
      DatabaseOp.insertGameBoardJson(gameBoardJson);
      
      ctx.result(gameBoardJson);
    });

    
    // getgame Endpoint to get the gameboard
    app.get("/getgame", ctx -> {
      // get the gameboardJSON from database
      String gameBoardJson = DatabaseOp.getGameBoardJson();
      ctx.result(gameBoardJson);
    });
    
    
    // joingame Endpoint
    app.get("/joingame", ctx -> {
      ctx.redirect("/gomoku.html?p=2"); // TODO: Modify to the new pvp address

      // get the gameboardJSON from database
      String gameBoardJson = DatabaseOp.getGameBoardJson();
      // GSON use to parse data to object
      Gson gson = new Gson();
      // update the GameBoard to the most current version 
      game = gson.fromJson(gameBoardJson, GameBoard.class);
      
      game.autoSetP2(); // add p2 to current game
      game.setGameStarted(true); // update game status
      
      // update players' view
      // turn gameboard into json and broadcast
      gameBoardJson = gson.toJson(game);
      DatabaseOp.updateGameBoardJson(gameBoardJson);

      sendGameBoardToAllPlayers(gameBoardJson);
      ctx.result(gameBoardJson);
    });


    // move endpoint
    //app.post("/move/:playerId", ctx -> {
    app.post("/move", ctx -> {
      // get the gameboardJSON from database
      String gameBoardJson = DatabaseOp.getGameBoardJson();
      // GSON use to parse data to object
      Gson gson = new Gson();
      // update the GameBoard to the most current version 
      game = gson.fromJson(gameBoardJson, GameBoard.class);
      
      // convert the request into Move class
      Move currentMove = new Move();
      currentMove.retrieveMove(ctx.body(), game);
      
      // decide move validity and return message
      Message myMes = currentMove.checkMoveValidity(game);
      // send out the message
      gson = new Gson();
      String myMesJson = gson.toJson(myMes);
      ctx.result(myMesJson);
      
      // if move is valid update the game board and compute game logic
      if (myMes.getMoveValidity() == true) {
        game.update(currentMove);
      }
      
      // update the players' views
      // turn the gameboard into json and broadcast
      gameBoardJson = gson.toJson(game);
      sendGameBoardToAllPlayers(gameBoardJson);
      
      DatabaseOp.updateGameBoardJson(gameBoardJson);
    });
    
    // Web sockets - DO NOT DELETE or CHANGE
    app.ws("/gameboard", new UiWebSocket());
  }

  /** Send message to all players.
   * @param gameBoardJson Gameboard JSON
   * @throws IOException Websocket message send IO Exception
   */
  private static void sendGameBoardToAllPlayers(final String gameBoardJson) {
    Queue<Session> sessions = UiWebSocket.getSessions();
    for (Session sessionPlayer : sessions) {
      try {
        sessionPlayer.getRemote().sendString(gameBoardJson);
      } catch (IOException e) {
        // Add logger here
      }
    }
  }

  public static void stop() {
    app.stop();
  }
}
