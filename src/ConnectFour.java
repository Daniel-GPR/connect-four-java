package src;

import src.Util.ConstraintsFunctions;

public class ConnectFour {

  // Enter one digit or two digit numbers only, otherwise printBoard() function
  // will print 3-digit numbers off-column
  private static final int minDimensions = 4;
  private static final int maxDimensions = 15;
  private static final int boardPadding = 5;
  private static final int lineForWin = 4; // defines the "length" of a winning line

  private int rows;
  private int columns;
  private Player playerA;
  private Player playerB;
  private Player currentPlayer;

  private Tile[][] gameBoard;

  private enum RoundOutcome {
    PLAYER_WINS,
    DRAW,
    CONTINUE;
  }

  ConnectFour() {
    super();
  }

  private static ConstraintsFunctions.intConstraint dimensionConstraintFunc = (input) -> input >= minDimensions
      && input <= maxDimensions;

  private ConstraintsFunctions.intConstraint insertedRowConstraint = (input) -> {
    return input >= 1 && input <= columns && gameBoard[input - 1][rows - 1] == Tile.EMPTY;
  };

  private void initializeEmptyBoard() throws Exception {

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
    // lets assume that we are counting rows bottom to top (in ascending index
    // order), and columns left to right (in ascending index order).

    // print some padding
    for (int i = 0; i < boardPadding; i++) {
      Util.println("");
    }

    for (int i = 0; i < rows; i++) {
      Util.print(String.format("%s|  ", " ".repeat(boardPadding)));
      for (int j = 0; j < columns; j++) {
        Util.print(gameBoard[j][rows - i - 1].toString());
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
    for (int k = 1; k <= columns; k++) {
      Util.print(String.valueOf(k) + (k >= 10 ? " " : "  ")); // two digit numbers need less padding
    }
    Util.println("");

  }

  public void startGameSetup() {

    // prompt player names
    playerA = new Player(Util.readLine("Please enter the name of the 1st player"));
    playerB = new Player(
        Util.readLine("Please enter the name of the 2nd player", "Player names must be different, enter another name",
            (input) -> !playerA.name.toLowerCase().equals(input.toLowerCase())));

    // prompt chips
    playerA.setPlayerChip(Util.readChip(String.format("%s, please select your chip ('x' or 'o')", playerA.name),
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

    try {
      initializeEmptyBoard();
      printBoard();

    } catch (Exception e) {
      System.err.println(e.getMessage());
      return;
    }

  }

  public void startGame() {

    // lets assume this check is sufficient for now, as these values are private and
    // are set in the startGameSetup method only
    if (rows == 0 || columns == 0) {
      System.err.println("Run game setup first.");
      return;
    }

    currentPlayer = playerA; // playerA starts
    while (true) {
      int insertedCol = promptColumnForInsertion(currentPlayer); // prompt and verify input for col number
      int insertedRow = insertChip(currentPlayer.chip, insertedCol); // row (counting from 0) for code cleanliness

      printBoard();

      RoundOutcome outcome = calculateRoundOutcome(insertedCol, insertedRow);

      if (outcome == RoundOutcome.PLAYER_WINS) {
        Util.println(String.format("GAME OVER. THE WINNER IS %s!",
            currentPlayer.name));
        return;
      }

      if (outcome == RoundOutcome.DRAW) {
        Util.println("GAME OVER. WE HAVE A DRAW.");
        return;
      }

      // swap players before next cycle
      currentPlayer = playerA.chip == currentPlayer.chip ? playerB : playerA;
    }
  }

  // insertedCol and insertedRow are counted from 0 (first col is 0 not 1)
  private RoundOutcome calculateRoundOutcome(int insertedCol, int insertedRow) {

    boolean someoneHasWon = scanHorizontalWin(insertedCol, insertedRow) || scanVerticalWin(insertedCol, insertedRow)
        || scanDiagonalWin(insertedCol, insertedRow, 1) || scanDiagonalWin(insertedCol, insertedRow, -1);
    ;

    if (someoneHasWon) {
      return RoundOutcome.PLAYER_WINS;
    }

    if (boardFull()) {
      return RoundOutcome.DRAW;
    }

    return RoundOutcome.CONTINUE;
  }

  private int promptColumnForInsertion(Player player) {
    return Util.readInt(String.format("%s, your turn. Select column", player.name), "Invalid input, enter again",
        insertedRowConstraint) - 1;
  }

  private int insertChip(Chip chip, int column) {
    for (int i = 0; i < rows; i++) {
      if (gameBoard[column][i] == Tile.EMPTY) {
        gameBoard[column][i] = chip.toTile();
        return i;
      }
    }
    return -1; // this case should not hit, promptColumnForInsertion should take care of this
  }

  private boolean scanVerticalWin(int insertedCol, int insertedRow) {
    int offset = -(lineForWin - 1); // let's offset bottom to top
    int consecutiveChips = 0;

    while (offset <= 0) {

      // need to make sure this is within bounds
      if (!rowIndexOffsetWithinBounds(insertedRow, offset)) {
        return false; // can early exit, as if the chain is broken once here it cannot be long enough
      }

      // within bounds, check tile
      if (gameBoard[insertedCol][insertedRow + offset] == currentPlayer.chip.toTile()) {
        consecutiveChips++;
      } else {
        return false; // can early exit here too.
      }

      // check before next loop
      if (consecutiveChips >= lineForWin) {
        return true;
      }
      offset++;
    }

    return false;
  }

  private boolean scanHorizontalWin(int insertedCol, int insertedRow) {
    int offset = -(lineForWin - 1); // let's offset left to right
    int consecutiveChips = 0;

    while (offset < lineForWin) {

      // need to make sure this is within bounds
      if (!columnIndexOffsetWithinBounds(insertedCol, offset)) {
        offset++;
        continue;
      }

      // within bounds, check tile
      if (gameBoard[insertedCol + offset][insertedRow] == currentPlayer.chip.toTile()) {
        consecutiveChips++;
      } else {
        consecutiveChips = 0; // reset
        if (offset > 0) { // early exit if needed, this chip no longer determines a win in this case
          break;
        }
      }

      // check before next loop
      if (consecutiveChips >= lineForWin) {
        return true;
      }
      offset++;
    }

    return false;
  }

  private boolean scanDiagonalWin(int insertedCol, int insertedRow, int slope) {
    if (slope != -1 && slope != 1) {
      System.err.println("only values of -1 and 1 are acceptable as slope param");
      return false;
    }

    int offset = -(lineForWin - 1); // let's offset left to right
    int consecutiveChips = 0;

    while (offset < lineForWin) {

      // need to make sure this is within bounds
      if (!(rowIndexOffsetWithinBounds(insertedRow, offset * slope)
          && columnIndexOffsetWithinBounds(insertedCol, offset))) {
        offset++;
        continue;
      }

      // within bounds, check tile
      if (gameBoard[insertedCol + offset][insertedRow + offset * slope] == currentPlayer.chip.toTile()) {
        consecutiveChips++;
      } else {
        consecutiveChips = 0; // reset
        if (offset > 0) { // early exit if needed, this chip no longer determines a win in this case
          break;
        }
      }

      // check before next loop
      if (consecutiveChips >= lineForWin) {
        return true;
      }
      offset++;
    }

    return false;

  }

  private boolean rowIndexOffsetWithinBounds(int rowIndex, int indexOffset) {
    return rowIndex + indexOffset >= 0 && rowIndex + indexOffset < rows;
  }

  private boolean columnIndexOffsetWithinBounds(int columnIndex, int indexOffset) {
    return columnIndex + indexOffset >= 0 && columnIndex + indexOffset < columns;
  }

  // returns true if board is full (can't add more chips)
  private boolean boardFull() {
    for (int i = 0; i < columns; i++) {
      if (gameBoard[i][rows - 1] == Tile.EMPTY) {
        return false;
      }
    }
    return true;
  }

}