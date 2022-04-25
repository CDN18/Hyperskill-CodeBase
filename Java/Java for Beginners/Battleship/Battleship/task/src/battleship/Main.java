package battleship;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    static StringBuilder playground1;
    static StringBuilder fogPlayground1;
    static StringBuilder playground2;
    static StringBuilder fogPlayground2;
    static Scanner scanner = new Scanner(System.in);
    final static String fog = "~";
    final static String ship = "O";
    final static String brokenShip = "X";
    final static String miss = "M";
    static ArrayList<ShipStatus> shipArray1 = new ArrayList<>();
    static ArrayList<ShipStatus> shipArray2 = new ArrayList<>();
    static boolean win = false;

    public static void main(String[] args) {
        // Write your code here
        InitializePlaygrounds();
        System.out.println("Player 1, place your ships on the game field");
        ArrangeShip(playground1, shipArray1);
        System.out.println("Player 2, place your ships on the game field");
        ArrangeShip(playground2, shipArray2);
        while (!win) {
            DisplayPlayground(fogPlayground2);
            System.out.println("---------------------");
            DisplayPlayground(playground1);
            System.out.println("Player 1, it's your turn:");
            Attack(playground2);
            if(win) break;
            System.out.println("Press Enter and pass the move to another player");
            scanner.nextLine();
            System.out.println("...");
            DisplayPlayground(fogPlayground1);
            System.out.println("---------------------");
            DisplayPlayground(playground2);
            System.out.println("Player 2, it's your turn:");
            Attack(playground1);
            if (win) break;
            System.out.println("Press Enter and pass the move to another player");
            scanner.nextLine();
            System.out.println("...");
        }
    }

    static void ArrangeShip(StringBuilder playground, ArrayList<ShipStatus> shipArray) {
        DisplayPlayground(playground);
        shipArray.add(PlaceShip(Ship.AIR_CARRIER, playground));
        shipArray.add(PlaceShip(Ship.BATTLESHIP, playground));
        shipArray.add(PlaceShip(Ship.SUBMARINE, playground));
        shipArray.add(PlaceShip(Ship.CRUISER, playground));
        shipArray.add(PlaceShip(Ship.DESTROYER, playground));
        System.out.print("Press Enter and pass the move to another player");
        scanner.nextLine();
        System.out.println("...");
    }

    private static void Attack(StringBuilder playground) {
        StringBuilder fogPlayground;
        ArrayList<ShipStatus> shipArray;
        StringBuilder myPlayground;
        ArrayList<ShipStatus> myShipArray;
        if (playground == playground1) {
            fogPlayground = fogPlayground1;
            shipArray = shipArray1;
            myPlayground = playground2;
            myShipArray = shipArray2;
        } else {
            fogPlayground = fogPlayground2;
            shipArray = shipArray2;
            myPlayground = playground1;
            myShipArray = shipArray1;
        }
        String coordinate = scanner.nextLine();
        while (!ValidateCoordinate(coordinate)) {
            System.out.println("Invalid coordinate, try again");
            coordinate = scanner.nextLine();
        }
        int row = coordinate.charAt(0) - 'A';
        int column = Integer.parseInt(coordinate.substring(1)) - 1;
        int index = row * 10 + column;
        switch (String.valueOf(playground.charAt(index))) {
            case fog:
                playground.replace(index, index + 1, miss);
                // myPlayground.replace(index, index + 1, miss);
                fogPlayground.replace(index, index + 1, String.valueOf(playground.charAt(index)));
                // DisplayPlayground(fogPlayground);
                System.out.println("You missed!");
                break;
            case ship:
                int sankCounter = 0;
                for (ShipStatus ship : shipArray) {
                    boolean shipSank = ship.isSank();
                    if (shipSank) {
                        continue;
                    }
                    if (ship.matchCoordinate(coordinate)) {
                        int internalIndex = index - ship.getLowerRow() * 10 -
                                ship.getLowerColumn() + 1;
                        if (ship.isVertical()) {
                            internalIndex /= 10;
                        }
                        ship.setAlive(internalIndex, false);
                    }
                    if (ship.isSank()) {
                        sankCounter++;
                    }
                }
                playground.replace(index, index + 1, brokenShip);
                // myPlayground.replace(index, index + 1, brokenShip);
                fogPlayground.replace(index, index + 1, String.valueOf(playground.charAt(index)));
                // DisplayPlayground(fogPlayground);
                boolean allSank = true;
                for (ShipStatus ship : shipArray) {
                    allSank = allSank & ship.isSank();
                }
                if (allSank) {
                    win = true;
                    System.out.println("You sank the last ship. You won. Congratulations!");
                } else if (sankCounter > 0) {
                    System.out.println("You sank a ship! Specify a new target:");
                } else {
                    System.out.println("You hit a ship!");
                }
                break;
            default:
                System.out.println("Error: Can't re-attack coordinates that have been attacked.");
                // DisplayPlayground(fogPlayground);
        }
    }

    private static ShipStatus PlaceShip(Ship type, StringBuilder playground) {
        String[] coordinates;
        System.out.printf("Enter the coordinates of the %s (%d cells):\n", type.getName(), type.getLength());
        coordinates = scanner.nextLine().split(" ");
        while (!ValidatePlacement(coordinates, type, playground)) {
            coordinates = scanner.nextLine().split(" ");
        }
        int row1 = coordinates[0].charAt(0) - 'A';
        int row2 = coordinates[1].charAt(0) - 'A';
        int colum1 = Integer.parseInt(coordinates[0].substring(1)) - 1;
        int colum2 = Integer.parseInt(coordinates[1].substring(1)) - 1;
        ShipStatus newShip = new ShipStatus(type, coordinates);
        if (row1 == row2) {
            int startIndex = Math.min(colum1, colum2) + row1 * 10;
            playground.replace(startIndex,
                    startIndex + type.getLength(), ship.repeat(type.getLength()));
        } else {
            for (int i = Math.min(row1, row2); i <= Math.max(row1, row2); i++) {
                playground.setCharAt(i * 10 + colum1, ship.charAt(0));
            }
        }
        DisplayPlayground(playground);
        return newShip;
    }

    private static boolean ValidateCoordinate(String coordinate) {
        if (coordinate.length() != 2 && coordinate.length() != 3) {
            return false;
        }
        int row = coordinate.charAt(0) - 'A';
        int column = Integer.parseInt(coordinate.substring(1)) - 1;
        if (row < 0 || row >= 10 || column < 0 || column >= 10) {
            System.out.println("Error! You entered the wrong coordinates! Try again:");
            return false;
        }
        return true;
    }
    private static boolean ValidatePlacement(String[] coordinates, Ship type, StringBuilder playground) {
        // Parse Coordinates
        // Row & Column here both refers to index (Starts from 0)
        if (coordinates.length != 2 || !ValidateCoordinate(coordinates[0]) || !ValidateCoordinate(coordinates[1])) {
            System.out.println("Error! You entered the wrong coordinates! Try again:");
            return false;
        }
        int row1 = coordinates[0].charAt(0) - 'A';
        int row2 = coordinates[1].charAt(0) - 'A';
        int colum1 = Integer.parseInt(coordinates[0].substring(1));
        int colum2 = Integer.parseInt(coordinates[1].substring(1));
        // Validate length
        int l = Math.max(Math.abs(row1 - row2), Math.abs(colum1 - colum2)) + 1;
        if (l != type.getLength()) {
            System.out.printf("Error! Wrong length of the %s! Try again:\n", type.getName());
            return false;
        }
        // Validate location
        if (row1 != row2 && colum1 != colum2) {
            System.out.println("Error! Wrong ship location! Try again:");
            return false;
        }
        // Validate Nearby
        if (row1 == row2) {
            if (row1 - 1 >= 0) {
                for (int i = Math.min(colum1, colum2); i < type.getLength(); i++) {
                    if (String.valueOf(playground.charAt((row1 - 1) * 10 + i)).equals(ship)) {
                        System.out.println("Error! You placed it too close to another one. Try again:");
                        return false;
                    }
                }
            }
            if (row1 + 1 < 10) {
                for (int i = Math.min(colum1, colum2); i < type.getLength(); i++) {
                    if (String.valueOf(playground.charAt((row1 + 1) * 10 + i)).equals(ship)) {
                        System.out.println("Error! You placed it too close to another one. Try again:");
                        return false;
                    }
                }
            }
            if (Math.min(colum1, colum2) - 1 >= 0) {
                if (String.valueOf(playground.charAt((row1) * 10 + Math.min(colum1, colum2) - 1)).equals(ship)) {
                    System.out.println("Error! You placed it too close to another one. Try again:");
                    return false;
                }
            }
            if (Math.max(colum1, colum2) + 1 < 10) {
                if (String.valueOf(playground.charAt((row1) * 10 + Math.max(colum1, colum2) + 1)).equals(ship)) {
                    System.out.println("Error! You placed it too close to another one. Try again:");
                    return false;
                }
            }
        } else {
            if (colum1 - 1 >= 0) {
                for (int i = Math.min(row1, row2); i < Math.min(row1, row2) + type.getLength(); i++) {
                    if (String.valueOf(playground.charAt(i * 10 + colum1 - 1)).equals(ship)) {
                        System.out.println("Error! You placed it too close to another one. Try again:");
                        return false;
                    }
                }
            }
            if (colum1 + 1 < 10) {
                for (int i = Math.min(row1, row2); i < Math.min(row1, row2) + type.getLength(); i++) {
                    if (String.valueOf(playground.charAt(i * 10 + colum1 + 1)).equals(ship)) {
                        System.out.println("Error! You placed it too close to another one. Try again:");
                        return false;
                    }
                }
            }
            if (Math.min(row1, row2) - 1 >= 0) {
                if (String.valueOf(playground.charAt((Math.min(row1, row2) - 1) * 10 + colum1)).equals(ship)) {
                    System.out.println("Error! You placed it too close to another one. Try again:");
                    return false;
                }
            }
            if (Math.max(row1, row2) + 1 < 10) {
                if (String.valueOf(playground.charAt((Math.max(row1, row2) + 1) * 10 + colum1)).equals(ship)) {
                    System.out.println("Error! You placed it too close to another one. Try again:");
                    return false;
                }
            }
        }
        // Validate completed
        return true;
    }

    private static void DisplayPlayground(StringBuilder playground) {
        System.out.println("  1 2 3 4 5 6 7 8 9 10");
        for (int i = 0; i < 10; i++) {
            System.out.print((char) ('A' + i));
            for (int j = 0; j < 10; j++) {
                System.out.print(" " + playground.toString().charAt(i * 10 + j));
            }
            System.out.print("\n");
        }
    }

    private static void InitializePlaygrounds() {
        playground1 = new StringBuilder();
        fogPlayground1 = new StringBuilder();
        playground1.append("~".repeat(10).repeat(10));
        fogPlayground1.append(playground1.toString());
        playground2 = new StringBuilder();
        fogPlayground2 = new StringBuilder();
        playground2.append("~".repeat(10).repeat(10));
        fogPlayground2.append(playground2.toString());
    }

    enum Ship {
        AIR_CARRIER(5, "Aircraft Carrier"),
        BATTLESHIP(4, "Battleship"),
        SUBMARINE(3, "Submarine"),
        CRUISER(3, "Cruiser"),
        DESTROYER(2, "Destroyer");

        final int length;
        final String name;

        Ship(int length, String name) {
            this.length = length;
            this.name = name;
        }

        public int getLength() {
            return length;
        }

        public String getName() {
            return name;
        }
    }

}
