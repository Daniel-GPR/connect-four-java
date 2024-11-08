package src;

import src.Util.ConstraintsFunctions;

public class ConnectFour {

  private static final int minDimensions = 4;
  private static final int maxDimensions = 16;
  private static final int boardPadding = 5;

  private int rows;
  private int columns;
  private Player playerA;
  private Player playerB;

  private Tile[][] gameBoard;

  ConnectFour() {
    super();
  }

  private static ConstraintsFunctions.intConstraint dimensionConstraintFunc = (input) -> input >= minDimensions
      && input <= maxDimensions;

  private void initializeBoard() throws Exception {
    if (rows == 0 || columns == 0) {
      throw new Exception("No board constraints provided");
    }
    gameBoard = new Tile[columns][rows];

    // initialize empty board
    for (int i = 0; i < columns; i++) {
      for (int j = 0; j < rows; j++) {
        gameBoard[i][j] = Tile.EMPTY;
      }
    }

  }

  private void printBoard() {
    // print some padding
    for (int i = 0; i < boardPadding; i++) {
      Util.println("");
    }

    for (int i = 0; i < rows; i++) {
      Util.print(String.format("%s|  ", " ".repeat(boardPadding)));
      for (int j = 0; j < columns; j++) {
        Util.print(gameBoard[j][i].toString());
        Util.print("  ");
      }
      Util.println("|");
    }

    // print bottom divider
    Util.print(" ".repeat(boardPadding));
    int boardWidth = (columns) + (columns + 1) * 2 + 2;
    for (int k = 0; k < boardWidth; k++) {
      Util.print(Tile.EMPTY.toString());
    }
    Util.println("");

    // print column numbers
    Util.print(" ".repeat(boardPadding));
    Util.print("   ");
    for (int k = 0; k < columns; k++) {
      Util.print(String.valueOf(k+1) + "  ");
    }
    Util.println("");

  }

  public void startGameSetup() {

    // prompt player names
    playerA = new Player(Util.readLine("Please enter the name of the 1st player"));
    playerB = new Player(Util.readLine("Please enter the name of the 2nd player"));

    // prompt chips
    playerA.setPlayerChip(Util.readChip(String.format("%s, please select your chip", playerA.name),
        "Invalid input, please enter 'x' or 'o'"));
    playerB.setPlayerChip(playerA.chip == Chip.O ? Chip.X : Chip.O);
    Util.println(String.format("%s selected chip: %s", playerA.name, playerA.chip));
    Util.println(String.format("%s, your chip is: %s", playerB.name, playerB.chip));

    // prompt board dimensions
    rows = Util.readInt("Please enter the number of rows",
        String.format("Incorrect input. Please enter the number of rows [%s, %s]", minDimensions, maxDimensions),
        dimensionConstraintFunc);
    columns = Util.readInt("Please enter the number of columns",
        String.format("Incorrect input. Please enter the number of columns [%s, %s]", minDimensions, maxDimensions),
        dimensionConstraintFunc);

    // init board
    gameBoard = new Tile[columns][rows];

    try {
      initializeBoard();
      printBoard();
    } catch (Exception e) {
      System.err.println(e.getMessage());
      return;
    }

  }

}