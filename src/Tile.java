package src;

public enum Tile {
  X {
    @Override
    public String toString() {
      return "x";
    }
  },
  O {
    @Override
    public String toString() {
      return "o";
    }
  },
  EMPTY {
    @Override
    public String toString() {
      return "-";
    }

  };

  public boolean isEqual(Tile tile){
    return this == tile;
  }

  public boolean isEqual(Chip chip){
    return this == chip.toTile();
  }
}
