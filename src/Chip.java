package src;

public enum Chip {
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
  };
  public Tile toTile() {
    return Tile.valueOf(this.toString().toUpperCase());
  }
  
}
