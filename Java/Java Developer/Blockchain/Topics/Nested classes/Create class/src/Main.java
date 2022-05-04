class StringOperations {

    // create static nested class EngString
    static class EngString {
        boolean english;
        String string;

        // constructor
        public EngString(boolean english, String string) {
            this.english = english;
            this.string = string;
        }

        // getters
        public boolean isEnglish() {
            return english;
        }

        public String getString() {
            return string;
        }
    }
}