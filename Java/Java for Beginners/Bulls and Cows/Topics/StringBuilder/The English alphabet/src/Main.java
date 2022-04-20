class EnglishAlphabet {

    public static StringBuilder createEnglishAlphabet() {
        // write your code here
        StringBuilder stringBuilder = new StringBuilder();
        for (char i = 'A'; i <= 'Z'; i++) {
            stringBuilder.append(i).append(" ");
        }
        return stringBuilder.deleteCharAt(stringBuilder.length() - 1);
    }
}