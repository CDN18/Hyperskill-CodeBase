package minesweeper;


import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // write your code here
        final int defaultSize = 9;
        Scanner scanner = new Scanner(System.in);
        System.out.print("How many mines do you want on the field? > ");
        int mines = scanner.nextInt();
        if (mines > defaultSize * defaultSize) {
            mines = defaultSize * defaultSize - 1;
        }
        Playground playground =  new Playground(defaultSize, mines);
        System.out.println(playground.showCurrentInfo());
        while (!playground.isCompleted() && !playground.isFailed()) {
            System.out.print("Set/unset mine marks or claim a cell as free: > ");
            String[] input = new String[3];
            Arrays.setAll(input, i -> scanner.next());
            if (!playground.parseInput(input)) {
                continue;
            }
            System.out.println("");
            System.out.println(playground.showCurrentInfo());
            playground.check();
        }
        if (playground.isCompleted() && !playground.isFailed()) {
            System.out.println("Congratulations! You found all mines!");
        } else if (playground.isFailed()) {
            System.out.println("You stepped on a mine and failed!");
        }
    }
}
