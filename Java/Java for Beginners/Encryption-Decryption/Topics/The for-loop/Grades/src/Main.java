import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // start coding here
        int n = scanner.nextInt();
        int gradeD = 0;
        int gradeC = 0;
        int gradeB = 0;
        int gradeA = 0;
        for (int i = 0; i < n; i++) {
            char grade = scanner.next().charAt(0);
            if (grade == 'A') {
                gradeA++;
            } else if (grade == 'B') {
                gradeB++;
            } else if (grade == 'C') {
                gradeC++;
            } else if (grade == 'D') {
                gradeD++;
            }
        }
        System.out.println(gradeD + " " + gradeC + " " +
                gradeB + " " + gradeA);
    }
}