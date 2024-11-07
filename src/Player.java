package src;

public class Player {

  public String name;
  public Chip chip;

  public Player(String name, Chip chip){
    this(name);
    this.chip = chip;
  }

  public Player(String name){
    this.name = name;
  }

  public void setPlayerChip(Chip chip) {
    this.chip = chip;
  }


}
