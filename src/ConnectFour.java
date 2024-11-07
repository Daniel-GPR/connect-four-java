package src;

import src.Util.ConstraintsFunctions;

public class ConnectFour {

  private static final int minDimensions = 4;
  private static final int maxDimensions = 16;

  private int rows;
  private int columns;
  private Player playerA;
  private Player playerB;

  private Tile[][] gameBoard;


  ConnectFour(){
    super();
  }

  private static ConstraintsFunctions.intConstraint dimensionConstraintFunc =  (input) -> input >= minDimensions && input <= maxDimensions;

  public void startGameSetup() {
    playerA = new Player(Util.readLine("Please enter the name of the 1st player"));
    playerB = new Player(Util.readLine("Please enter the name of the 2nd player"));

    playerA.setPlayerChip(Util.readChip(String.format("%s, please select your chip", playerA.name), "Invalid input, please enter 'x' or 'o'"));
    playerB.setPlayerChip(playerA.chip == Chip.O ? Chip.X : Chip.O);
    Util.println(String.format("%s selected chip: %s", playerA.name, playerA.chip));
    Util.println(String.format("%s, your chip is: %s", playerB.name, playerB.chip));

    rows = Util.readInt("Please enter the number of rows", String.format("Incorrect input. Please enter the number of rows [%s, %s]", minDimensions, maxDimensions), dimensionConstraintFunc);
    columns = Util.readInt("Please enter the number of columns", String.format("Incorrect input. Please enter the number of columns [%s, %s]", minDimensions, maxDimensions), dimensionConstraintFunc);

  }

}