import org.hyperskill.hstest.exception.outcomes.WrongAnswer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonFinder {

    private static final Pattern pattern = Pattern.compile("\\{.+}");

    public static String findRequestJsonObject(String output) {

        Matcher matcher = pattern.matcher(output);

        while (matcher.find()) {
            String json = matcher.group();
            if (json.contains("type")) {
                return json;
            }
        }

        throw new WrongAnswer("Can't find request JSON object in the output.\n" +
            "It should contain 'type' key");
    }

    public static String findResponseJsonObject(String output) {

        Matcher matcher = pattern.matcher(output);

        while (matcher.find()) {
            String json = matcher.group();
            if (json.contains("response")) {
                return json;
            }
        }

        throw new WrongAnswer("Can't find request JSON object in the output.\n" +
            "It should contain 'response' key");
    }
}
