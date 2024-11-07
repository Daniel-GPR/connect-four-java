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
  }
}
