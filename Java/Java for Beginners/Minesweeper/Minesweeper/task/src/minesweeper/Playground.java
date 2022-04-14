package minesweeper;

import java.util.*;

class Playground {
    private final int mines;
    ArrayList<Boolean>[] mineField;
    ArrayList<Integer>[] mineAround;
    ArrayList<Boolean>[] marked;
    ArrayList<Boolean>[] visible;
    int visibleCounter;
    ArrayList<Integer> mineIndex;
    ArrayList<Boolean> markedIndex;
    StringBuilder[] current;
    char mineSymbol = 'X';
    private boolean completed = false;
    private boolean failed = false;
    private boolean mineSet = false;
    Deque<Integer> exploreStack;

    public Playground(int size, int mines) {
        this.mineField = new ArrayList[size];
        this.mineAround = new ArrayList[size];
        this.marked = new ArrayList[size];
        this.visible = new ArrayList[size];
        this.exploreStack =  new ArrayDeque<>(size * size);
        this.visibleCounter = 0;
        current = new StringBuilder[size];
        for(int i = 0; i < mineField.length; i++) {
            mineField[i] = new ArrayList<>(Collections.nCopies(size, false));
            mineAround[i] = new ArrayList<>(Collections.nCopies(size, 0));
            marked[i] = new ArrayList<>(Collections.nCopies(size, false));
            visible[i] = new ArrayList<>(Collections.nCopies(size, false));
            current[i] = new StringBuilder("-".repeat(size));
        }
        this.mines = mines;
        this.mineIndex = new ArrayList<>(mines);
        this.markedIndex = new ArrayList<>(Collections.nCopies(mines, false));
    }

    public void setMine(int x, int y) {
        Random random = new Random();
        int counter = 0;
        while (counter < this.mines) {
            int row = random.nextInt(mineField.length);
            ArrayList<Integer> availableIndex = new ArrayList<>();
            for (int i = 0; i < mineField.length; i++) {
                if (!mineField[row].get(i)) {
                    availableIndex.add(i);
                }
            }
            if (availableIndex.isEmpty()) {
                continue;
            }
            int colum = availableIndex.get(random.nextInt(availableIndex.size()));
            if (row == x - 1 && colum == y - 1) {
                continue;
            }
            mineField[row].set(colum, true);
            mineAround[row].set(colum, -1);
            mineIndex.add(row * mineField.length + colum);
            counter++;
        }
        generateInfo();
    }

    /**
     * Initialize mineAround
     */
    public void generateInfo() {
        for(int i = 0; i < mineAround.length; i++) {
            mineAround[i] = new ArrayList<>(Collections.nCopies(mineAround.length, 0));
        }
        for (int index : mineIndex) {
            int row = index / mineField.length;
            int colum = index % mineField.length;
            // Row above the mine
            if (row - 1 >= 0) {
                if (colum - 1 >= 0) {
                    mineAround[row - 1].set(colum - 1,
                            mineAround[row - 1].get(colum - 1) + 1);
                }
                mineAround[row - 1].set(colum,
                        mineAround[row - 1].get(colum) + 1);
                if (colum + 1 < mineField.length) {
                    mineAround[row - 1].set(colum + 1,
                            mineAround[row - 1].get(colum + 1) + 1);
                }
            }
            // Row of the mine
            if (colum - 1 >= 0) {
                mineAround[row].set(colum - 1,
                        mineAround[row].get(colum - 1) + 1);
            }
            if (colum + 1 < mineField.length) {
                mineAround[row].set(colum + 1,
                        mineAround[row].get(colum + 1) + 1);
            }
            // Row below the mine
            if (row + 1 < mineField.length) {
                if (colum - 1 >= 0) {
                    mineAround[row + 1].set(colum - 1,
                            mineAround[row + 1].get(colum - 1) + 1);
                }
                mineAround[row + 1].set(colum,
                        mineAround[row + 1].get(colum) + 1);
                if (colum + 1 < mineField.length) {
                    mineAround[row + 1].set(colum + 1,
                            mineAround[row + 1].get(colum + 1) + 1);
                }
            }
        }
        // Update fields with a mine in mineAround
        for (int index : mineIndex) {
            int row = index / mineField.length;
            int colum = index % mineField.length;
            mineAround[row].set(colum, -1);
        }
    }

    public boolean isCompleted() {
        return this.completed;
    }

    public void setCompleted(boolean value) {
        this.completed = value;
    }

    public boolean isFailed() {
        return this.failed;
    }

    public void setFailed(boolean value) {
        this.failed = value;
    }

    public boolean isMineSet() {
        return this.mineSet;
    }

    public void setMineSet(boolean value) {
        this.mineSet = value;
    }

    private void updateMarkedIndex(int x, int y) {
        int index = (x - 1) * mineField.length + y - 1;
        if (mineIndex.contains(index)) {
            markedIndex.set(mineIndex.indexOf(index), marked[x - 1].get(y - 1));
        }
    }
    // mark == true : Successfully marked
    // mark == false : Mark operation failed

    /**
     * Marks filed at row x, colum y, updates visible status
     * and updates markedIndex
     * @param x the row of the filed to be marked
     * @param y the colum of the field to be marked
     * @return whether the marking operation is successful
     */
    public boolean mark(int x, int y) {
        if (mineAround[x - 1].get(y - 1) > 0 && visible[x - 1].get(y - 1)) {
            System.out.println("There is a number here!");
            return false;
        } else {
            marked[x - 1].set(y - 1, !marked[x - 1].get(y - 1));
            updateMarkedIndex(x, y);
            return true;
        }
    }

    public void explore (int x, int y, boolean loop) {
        if (!visible[x - 1].get(y - 1)) {
            visible[x - 1].set(y - 1, true);
            visibleCounter++;
            switch (mineAround[x - 1].get(y - 1)) {
                case -1:
                    if (!loop) {
                        setFailed(true);
                    }
                    break;
                case 0:
                    // Add adjacent fields to stack
                    // Row above the field
                    if (x - 1 >= 1) {
                        if (y - 1 >= 1) {
                            exploreStack.push((x - 2) * mineField.length + y - 2);
                        }
                        exploreStack.push((x - 2) * mineField.length + y - 1);
                        if (y + 1 <= mineField.length) {
                            exploreStack.push((x - 2) * mineField.length + y);
                        }
                    }
                    // Row of the mine
                    if (y - 1 >= 1) {
                        exploreStack.push((x - 1) * mineField.length + y - 2);
                    }
                    if (y + 1 <= mineField.length) {
                        exploreStack.push((x - 1) * mineField.length + y);
                    }
                    // x below the mine
                    if (x + 1 <= mineField.length) {
                        if (y - 1 >= 1) {
                            exploreStack.push(x * mineField.length + y - 2);
                        }
                        exploreStack.push(x * mineField.length + y - 1);
                        if (y + 1 <= mineField.length) {
                            exploreStack.push(x * mineField.length + y);
                        }
                    }
                    while (!exploreStack.isEmpty()) {
                        int index = exploreStack.pop();
                        int newX = index / mineField.length + 1;
                        int newY = index % mineField.length + 1;
                        explore(newX, newY, true);
                    }
                    break;
                default:
            }
        }
    }

    /**
     * check if the player gains victory
     */
    public void check() {
        for (boolean value : markedIndex) {
            if (!value) {
                this.completed = false;
                break;
            }
            this.setCompleted(true);
        }
        if (visibleCounter == mineField.length * mineField.length - mines) {
            this.setCompleted(true);
        }
    }

    /**
     * parse input and return whether parsing is successful
     * @param input coordinate and command that the player inputs each time
     * @return whether parsing is successful
     */
    public boolean parseInput(String[] input) {
        // y - column, x - row
        int y = Integer.parseInt(input[0]);
        int x = Integer.parseInt(input[1]);
        if("mine".equals(input[2])) {
            return this.mark(x, y);
        }
        if("free".equals(input[2])) {
            if (!this.isMineSet()) {
                setMine(x, y);
                this.setMineSet(true);
            }
            this.explore(x, y, false);
        }
        return true;
    }

    public String showCurrentInfo() {
        if (failed) {
            for (int index : mineIndex) {
                int xIndex = index / mineField.length;
                int yIndex = index % mineField.length;
                visible[xIndex].set(yIndex, true);
                visibleCounter++;
            }
        }
        StringBuilder fieldString = new StringBuilder();
        // Append First Line
        fieldString.append(" |");
        for (int i = 0; i < mineField.length; i++) {
            fieldString.append(i + 1);
        }
        fieldString.append("|");
        // Append Second Line
        fieldString.append("-|");
        fieldString.append("-".repeat(mineField.length));
        fieldString.append("|");
        // Append main playground fields and extra margins
        for (int i = 0; i < mineField.length; i++) {
            fieldString.append(i + 1).append("|");
            for(int j = 0; j < mineField[i].size(); j++) {
                if (visible[i].get(j)) {
                    if (mineAround[i].get(j) == -1) {
                        fieldString.append(mineSymbol);
                    } else if (mineAround[i].get(j) == 0) {
                        fieldString.append("/");
                    } else {
                        fieldString.append(mineAround[i].get(j));
                    }
                } else if (marked[i].get(j)) {
                    fieldString.append("*");
                } else {
                    fieldString.append(".");
                }
            }
            fieldString.append("|");
        }
        // Append bottom margin
        fieldString.append("-|");
        fieldString.append("-".repeat(mineField.length));
        fieldString.append("|");

        // Generate output
        String output = String.join("\n",
                Utils.getListStr(fieldString.toString(),
                        this.mineField.length + 3));
        output = output.substring(0, output.length() - 1);
        return output;
    }

    @Deprecated
    // Deprecated by Stage 5
    // Show info without mine symbol after adding marks assigned by player
    public String showDefaultInfo() {
        StringBuilder fieldString = new StringBuilder();
        // Append First Line
        fieldString.append(" |");
        for (int i = 0; i < mineField.length; i++) {
            fieldString.append(i + 1);
        }
        fieldString.append("|");
        // Append Second Line
        fieldString.append("-|");
        fieldString.append("-".repeat(mineField.length));
        fieldString.append("|");
        // Append mineField and leftMargin
        for (int i = 0; i < mineField.length; i++) {
            fieldString.append(i + 1).append("|");
            for(int j = 0; j < mineField[i].size(); j++) {
                if (mineField[i].get(j)) {
                    if (marked[i].get(j)) {
                        fieldString.append("*");
                    } else {
                        fieldString.append(".");
                    }
                } else {
                    if (mineAround[i].get(j) > 0) {
                        fieldString.append(mineAround[i].get(j));
                    } else {
                        if (marked[i].get(j)) {
                            fieldString.append("*");
                        } else {
                            fieldString.append(".");
                        }
                    }
                }
            }
            fieldString.append("|");
        }
        // Append bottom margin
        fieldString.append("-|");
        fieldString.append("-".repeat(mineField.length));
        fieldString.append("|");
        return String.join("\n", Utils.getListStr(fieldString.toString(), this.mineField.length + 3));
    }

    // Show all original info (marks is not displayed)
    // The style of the info isn't updated to the requirements of stage 4
    @Deprecated
    public String showAllInfo() {
        StringBuilder fieldString = new StringBuilder();
        for (int i = 0; i < mineField.length; i++) {
            for(int j = 0; j < mineField[i].size(); j++) {
                if (mineField[i].get(j)) {
                    fieldString.append(mineSymbol);
                } else {
                    if (mineAround[i].get(j) > 0) {
                        fieldString.append(mineAround[i].get(j));
                    } else {
                        fieldString.append(".");
                    }
                }
            }
        }
        return String.join("\n", Utils.getListStr(fieldString.toString(), this.mineField.length));
    }

    @Deprecated
    public void setMineSymbol(char mineSymbol) {
        this.mineSymbol = mineSymbol;
    }

    @Override
    public String toString() {
        StringBuilder fieldString = new StringBuilder();
        for (ArrayList<Boolean> row : this.mineField) {
            for(boolean field : row) {
                if (field) {
                    fieldString.append(mineSymbol);
                } else {
                    fieldString.append(".");
                }
            }
        }
        return String.join("\n", Utils.getListStr(fieldString.toString(), this.mineField.length));
    }

}