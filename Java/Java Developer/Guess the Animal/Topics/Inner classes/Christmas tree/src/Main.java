class ChristmasTree {

    private String color;

    public ChristmasTree(String color) {
        this.color = color;
    }

    // create method putTreeTopper()

    class TreeTopper {

        private String color;        

        public TreeTopper(String color) {
            this.color = color;
        }

        // create method sparkle()
    }
}

// this code should work
class CreateHoliday {

    public static void main(String[] args) {

        ChristmasTree christmasTree = new ChristmasTree("green");
        christmasTree.putTreeTopper("silver");
    }
}