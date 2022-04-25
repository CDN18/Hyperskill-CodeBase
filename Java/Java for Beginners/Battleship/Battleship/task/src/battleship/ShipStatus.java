package battleship;

public class ShipStatus {
    public Main.Ship type;
    String[] coordinates;
    int lowerRow;
    int lowerColumn;
    boolean vertical;
    public StringBuilder alive;

    public boolean isSank() {
        return alive.toString().equals("0".repeat(type.getLength()));
    }

    public void setAlive(int index, boolean status) {
        this.alive.setCharAt(index, status ? '1' : '0');
    }

    public ShipStatus(Main.Ship type, String[] coordinates) {
        this.type = type;
        this.coordinates = coordinates;
        this.lowerRow = Math.min(coordinates[0].charAt(0) - 'A',
                coordinates[1].charAt(0) - 'A');
        this.lowerColumn = Math.min(Integer.parseInt(coordinates[0].substring(1)),
                Integer.parseInt(coordinates[1].substring(1)));
        this.vertical = Integer.parseInt(coordinates[0].substring(1)) ==
                Integer.parseInt(coordinates[1].substring(1));
        this.alive = new StringBuilder("1".repeat(type.getLength()));
    }

    public boolean matchCoordinate(String target) {
        int targetRow = target.charAt(0) - 'A';
        int targetColumn = Integer.parseInt(target.substring(1));
        if (!vertical) {
            return targetRow == lowerRow && targetColumn >= lowerColumn &&
                    targetColumn < lowerColumn + type.getLength();
        } else {
            return targetColumn == lowerColumn && targetRow >= lowerRow &&
                    targetRow < lowerRow + type.getLength();
        }
    }

    public int getLowerRow() {
        return lowerRow;
    }

    public int getLowerColumn() {
        return lowerColumn;
    }

    public boolean isVertical() {
        return vertical;
    }
}
