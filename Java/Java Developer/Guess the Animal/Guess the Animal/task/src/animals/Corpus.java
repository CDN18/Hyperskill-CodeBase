package animals;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class Corpus {
    // Greetings and goodbyes

    static ResourceBundle msgI18n = ResourceBundle.getBundle("messages", Locale.getDefault());
    static ResourceBundle ptnI18n = ResourceBundle.getBundle("patterns", Locale.getDefault());

    static List<String> greetings = Arrays.asList(msgI18n.getString("greeting").split("\\f"));
    static List<String> morningGreetings = List.of(msgI18n.getString("greeting.morning"));
    static List<String> afternoonGreetings = List.of(msgI18n.getString("greeting.afternoon"));
    static List<String> eveningGreetings = List.of(msgI18n.getString("greeting.evening"));
    static List<String> earlyMorningGreetings = List.of(msgI18n.getString("greeting.early"));
    static List<String> lateNightGreetings = List.of(msgI18n.getString("greeting.night"));
    static List<String> goodbye = Arrays.asList(msgI18n.getString("farewell").split("\\f"));

    // Questions and answers

    static String positiveResponse = ptnI18n.getString("positiveAnswer.isCorrect");
    static String negativeResponse = ptnI18n.getString("negativeAnswer.isCorrect");
    static List<String> askToClarify = Arrays.asList(msgI18n.getString("ask.again").split("\\f"));
    static List<String> playAgain = Arrays.asList(msgI18n.getString("game.again").split("\\f"));

    // Reply

    static List<String> positiveReply = Arrays.asList(msgI18n.getString("animal.nice").split("\\f"));
    static String negativeReply = msgI18n.getString("game.giveUp");

    //Methods

    public static String getCorpus(List<String> list) {
        return list.get((int) (Math.random() * list.size()));
    }
}

/*
public class Corpus {

    // Greetings and goodbyes

    static List<String> morningGreetings = List.of(
            "Good morning!", "Top of the morning to you!",
            "Morning!"
    );

    static List<String> afternoonGreetings = List.of(
            "Good afternoon!", "Afternoon!"
    );

    static List<String> eveningGreetings = List.of(
            "Good evening!", "Evening!"
    );

    static List<String> earlyMorningGreetings = List.of(
            "Good early morning!", "Early morning!",
            "Morning, early bird!"
    );

    static List<String> lateNightGreetings = List.of(
            "Hi, Night Owl!"
    );

    static List<String> goodbye = List.of(
            "Goodbye!", "See you later!", "Bye!",
            "Have a good day!", "See you soon!"
    );


    // Questions and answers

    static List<String> positiveResponse = List.of(
            "y", "yes", "yeah", "yep", "sure", "right",
            "affirmative", "correct", "indeed", "you bet", "exactly",
            "you said it", "you're right"
    );

    static List<String> negativeResponse = List.of(
            "n", "no", "no way", "nah", "nope",
            "negative", "I don't think so", "yeah no", "you're wrong", "you said it wrong"
    );

    static List<String> askToClarify = List.of(
            "I'm not sure I caught you: was it yes or no?",
            "Funny, I still don't understand, is it yes or no?",
            "Oh, it's too complicated for me: just tell me yes or no.",
            "Could you please simply say yes or no?",
            "Oh, no, don't try to confuse me: say yes or no."
    );

    // Reply
    static List<String> positiveReply = List.of(
            "Wonderful!", "Nice!", "Great!", "Good!");

    static List<String> negativeReply = List.of("I give up.");

    //Methods

    public static String getCorpus(List<String> list) {
        return list.get((int) (Math.random() * list.size()));
    }

}
 */
