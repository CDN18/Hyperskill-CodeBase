class Converter {

    /**
     * It returns a double value or 0 if an exception occurred
     */
    public static double convertStringToDouble(String input) {
        if (input == null || !input.matches("\\d+\\.?\\d*")) {
            input = "0.0";
        }
        return Double.parseDouble(input);
    }
}