import java.util.Scanner;


class CheckTheEssay {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String text = scanner.nextLine();

        // write your code here
        text = text.replaceAll("Franse", "France")
                .replaceAll("Eifel tower", "Eiffel Tower")
                .replaceAll("19th", "XIXth")
                .replaceAll("20th", "XXth")
                .replaceAll("21st", "XXIst");
        System.out.println(text);
    }
}