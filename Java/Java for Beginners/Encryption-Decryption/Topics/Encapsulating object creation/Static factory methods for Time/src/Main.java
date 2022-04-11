import java.util.Scanner;

class Time {

    int hour;
    int minute;
    int second;

    public static Time noon() {
        // write your code here
        Time returnTime = new Time();
        returnTime.hour = 12;
        returnTime.minute = 0;
        returnTime.second = 0;
        return returnTime;
    }

    public static Time midnight() {
        // write your code here
        Time returnTime = new Time();
        returnTime.hour = 0;
        returnTime.minute = 0;
        returnTime.second = 0;
        return returnTime;
    }

    public static Time ofSeconds(long seconds) {
        // write your code here
        final int secondsADay = 60 * 60 * 24;
        final int secondsAnHour = 60 * 60;
        final int secondsAMinute = 60;
        Time returnTime = new Time();
        returnTime.second = (int) (seconds % secondsADay % secondsAnHour % secondsAMinute);
        returnTime.minute = (int) ((seconds % secondsADay % secondsAnHour) / 60);
        returnTime.hour = (int) (seconds % secondsADay / 60 / 60);
        return returnTime;
    }

    public static Time of(int hour, int minute, int second) {
        // write your code here
        Time returnTime = new Time();
        if (hour < 0 || hour > 23 ||
            minute < 0 || minute > 59 ||
            second < 0 || second > 59) {
            return null;
        }
        returnTime.hour = hour;
        returnTime.minute = minute;
        returnTime.second = second;
        return returnTime;
    }
}

/* Do not change code below */
public class Main {

    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);

        final String type = scanner.next();
        Time time = null;

        switch (type) {
            case "noon":
                time = Time.noon();
                break;
            case "midnight":
                time = Time.midnight();
                break;
            case "hms":
                int h = scanner.nextInt();
                int m = scanner.nextInt();
                int s = scanner.nextInt();
                time = Time.of(h, m, s);
                break;
            case "seconds":
                time = Time.ofSeconds(scanner.nextInt());
                break;
            default:
                time = null;
                break;
        }

        if (time == null) {
            System.out.println(time);
        } else {
            System.out.printf("%s %s %s", time.hour, time.minute, time.second);
        }
    }
}