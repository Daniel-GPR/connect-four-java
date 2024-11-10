package src;

import java.util.UUID;

public class Player {

  public UUID id;
  public String name;
  public Chip chip;

  public Player(String name, Chip chip){
    this(name);
    this.chip = chip;
  }

  public Player(String name){
    this.id = UUID.randomUUID();
    this.name = name;
  }

  public void setPlayerChip(Chip chip) {
    this.chip = chip;
  }


}
