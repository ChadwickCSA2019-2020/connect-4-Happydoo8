import java.util.Random;
/**
 * Describe your basic strategy here.
 * @author <your Github username>
 *
 */
public class MyAgent extends Agent {
  /**
   * A random number generator to randomly decide where to place a token.
   */
  private Random random;

  /**
   * Constructs a new agent, giving it the game and telling it whether it is Red or Yellow.
   *
   * @param game The game the agent will be playing.
   * @param iAmRed True if the agent is Red, False if the agent is Yellow.
   */
  public MyAgent(Connect4Game game, boolean iAmRed) {
    super(game, iAmRed);
    random = new Random();
  }

  /**
   * The move method is run every time it is this agent's turn in the game. You may assume that
   * when move() is called, the game has at least one open slot for a token, and the game has not
   * already been won.
   *
   * <p>By the end of the move method, the agent should have placed one token into the game at some
   * point.</p>
   *
   * <p>After the move() method is called, the game engine will check to make sure the move was
   * valid. A move might be invalid if:
   * - No token was place into the game.
   * - More than one token was placed into the game.
   * - A previous token was removed from the game.
   * - The color of a previous token was changed.
   * - There are empty spaces below where the token was placed.</p>
   *
   * <p>If an invalid move is made, the game engine will announce it and the game will be ended.</p>
   *
   */
  public void move() {
    Connect4Game gameCopy = new Connect4Game(myGame);
	  if(iCanWin() != -1) {
	    moveOnColumn(iCanWin());
	  }
	  else if(iCanWin() == -1 ) {
	    char [][] board = gameCopy.getBoardMatrix();
	    int max = 0;
	    int row = -1;
	    int col = -1;
	    for(int i=0; i<3; i++) {
	      for(int j=0; j<4; j++) {
	        /*if (board[i][j] == 'B') {
	          continue;
	        }*/
	        int count = 1;
	        // Vertical checking
	        if(board[i][j] == board[i+1][j] && board[i+2][j] == 'B') {
	          count++;
	        }
	        else {
	          this.moveOnColumn(this.randomMove());
	        }
	        if(count > max) {
	          max = count;
	          row = i+2;
	          col = j;
	        }
	        
	        // Horizontal checking
	        
	        count = 1;
	        if(board[i][j] == board[i][j+1]) {
	          count++;
	        }
	        else {
	          this.moveOnColumn(this.randomMove());
	        }
	        if(count > max) {
	          if(i==0 || board[i-1][j+2] != 'B' && board[i][j+2] == 'B') {
	            max = count;
	            row = i;
	            col = j+2;
	          }
	        }
	        
	        // Major diagonal checking
	        
	        count = 1;
	        if(board[i][j] == board[i+1][j+1]) {
	          count++;
	        }
	        else {
	          this.moveOnColumn(this.randomMove());
	        }
	        if(count > max) {
	          if(board[i][j+2] != 'B' && board[i+1][j+2] == 'B') {
	            max = count;
	            row = i+1;
	            col = j+2;
	          }
	        }
	      }
	    }
	    
	    // Minor diagonal checking
	    
	    for(int i=0; i<3; i++) {
	      for(int j=3; j<7; j++) {
	        int count = 1;
	        if(board[i][j] == board[i+1][j-1]) {
	          count++;
	        }
	        else {
	          this.moveOnColumn(this.randomMove());
	        }
	        if(count > max) {
	          if(board[i+1][j-2] != 'B' && board[i+2][j-2] == 'B') {
	            max = count;
	            row = i+2;
	            col = j-2;
	          }
	        }
	      }
	    }
	    
	    moveOnColumn(col);
	    // Try to find the position to make 2 in a row to 3 in a row.
	    // If none, then try to change 1 in a row to 2 in a row.
	    // If none, then place randomly
	  }
	  else if(theyCanWin() != -1) {
	    moveOnColumn(randomMove());
	  }
  }

  /**
   * Drops a token into a particular column so that it will fall to the bottom of the column.
   * If the column is already full, nothing will change.
   *
   * @param columnNumber The column into which to drop the token.
   */
  public void moveOnColumn(int columnNumber) {
    // Find the top empty slot in the column
    // If the column is full, lowestEmptySlot will be -1
    int lowestEmptySlotIndex = getLowestEmptyIndex(myGame.getColumn(columnNumber));
    // if the column is not full
    if (lowestEmptySlotIndex > -1) {
      // get the slot in this column at this index
      Connect4Slot lowestEmptySlot = myGame.getColumn(columnNumber).getSlot(lowestEmptySlotIndex);
      // If the current agent is the Red player...
      if (iAmRed) {
        lowestEmptySlot.addRed(); // Place a red token into the empty slot
      } else {
        lowestEmptySlot.addYellow(); // Place a yellow token into the empty slot
      }
    }
  }

  /**
   * Returns the index of the top empty slot in a particular column.
   *
   * @param column The column to check.
   * @return
   *      the index of the top empty slot in a particular column;
   *      -1 if the column is already full.
   */
  public int getLowestEmptyIndex(Connect4Column column) {
    int lowestEmptySlot = -1;
    for  (int i = 0; i < column.getRowCount(); i++) {
      if (!column.getSlot(i).getIsFilled()) {
        lowestEmptySlot = i;
      }
    }
    return lowestEmptySlot;
  }

  /**
   * Returns a random valid move. If your agent doesn't know what to do, making a random move
   * can allow the game to go on anyway.
   *
   * @return a random valid move.
   */
  public int randomMove() {
    int i = random.nextInt(myGame.getColumnCount());
    while (getLowestEmptyIndex(myGame.getColumn(i)) == -1) {
      i = random.nextInt(myGame.getColumnCount());
    }
    return i;
  }

  /**
   * Returns the column that would allow the agent to win.
   *
   * <p>You might want your agent to check to see if it has a winning move available to it so that
   * it can go ahead and make that move. Implement this method to return what column would
   * allow the agent to win.</p>
   *
   * @return the column that would allow the agent to win.
   */
  public int iCanWin() {
    Connect4Game gameCopy = new Connect4Game(myGame);
    MyAgent currentAgent = new MyAgent(gameCopy, iAmRed);
//    Connect4Game gameCopy2 = new Connect4Game(myGame);
//    MyAgent copyAgent2 = new MyAgent(gameCopy2, !iAmRed);
    for(int i=0; i<7; i++) {
      currentAgent.moveOnColumn(i);
//      copyAgent2.moveOnColumn(i);
      if(iAmRed && gameCopy.gameWon() == 'R') {
        return i;
      }
      else if (!iAmRed && gameCopy.gameWon() == 'Y') {
        return i;
    
      }
      
//      
//      else if(gameCopy2.gameWon() == 'Y') {
//        return i;
//      }
    }
      
    
    return -1;
    
    
    // make a copy of the connect4 game
    // type variable = new type (parameters)
    // Ex. Connect4Game gameCopy = new Connect4Game(myGame);
    // make an agent that is playing that copy
    // Ex. MyAgent agentCopy = new MyAgent(gameCopy, iAmRed);
    // have that agent move on a column
    // check to see if you won
      // if you won, return the column you moved at
    // All of this should be in a for loop for every column
    // If you never win, return -1
  }

  /**
   * Returns the column that would allow the opponent to win.
   *
   * <p>You might want your agent to check to see if the opponent would have any winning moves
   * available so your agent can block them. Implement this method to return what column should
   * be blocked to prevent the opponent from winning.</p>
   *
   * @return the column that would allow the opponent to win.
   */
  public int theyCanWin() {
//    Connect4Game gameCopy = new Connect4Game(myGame);
//    MyAgent copyAgent = new MyAgent(gameCopy, iAmRed);
    Connect4Game gameCopy = new Connect4Game(myGame);
    MyAgent opponentAgent = new MyAgent(gameCopy, !iAmRed);
    for(int i=0; i<7; i++) {
//      copyAgent.moveOnColumn(i);
      opponentAgent.moveOnColumn(i);
      if(iAmRed && gameCopy.gameWon() == 'Y') {
        return i;
      } 
      else if (!iAmRed && gameCopy.gameWon() == 'R') {
        return i;
      }
      
      
//      else if(gameCopy2.gameWon() == 'R') {
//        return i;
//      }

    }
    
    return -1;
  }

  /**
   * Returns the name of this agent.
   *
   * @return the agent's name
   */
  public String getName() {
    return "My Agent";
  }
}
