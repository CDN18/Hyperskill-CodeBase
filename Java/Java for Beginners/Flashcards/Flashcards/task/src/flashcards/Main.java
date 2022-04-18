package flashcards;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static flashcards.Utils.getKeyByValue;


public class Main {
    public static LogUtil logger = new LogUtil();
    static Map<String, String> cards = new LinkedHashMap<>();
    static Map<String, Integer> mistakeCounter = new HashMap<>();
    
    public static void main(String[] args) throws IOException {
        boolean exit = false;
        ArrayList<String> argList = Arrays.stream(args).collect(Collectors.toCollection(ArrayList::new));
        String fromFile = argList.contains("-import") ?
                argList.get(argList.indexOf("-import") + 1) : "";
        String toFile = argList.contains("-export") ?
                argList.get(argList.indexOf("-export") + 1) : "";
        while (!exit) {
            if (!"".equals(fromFile)) {
                ImportCards(fromFile);
            }
            DisplayMenu();
            String input = logger.ScanNextLine();
            switch (input) {
                case "add":
                    AddCard();
                    break;
                case "remove":
                    RemoveCard();
                    break;
                case "import":
                    ImportCards();
                    break;
                case "export":
                    ExportCards();
                    break;
                case "ask":
                    Ask();
                    break;
                case "log":
                    SaveLog();
                    break;
                case "hardest card":
                    DisplayHardest();
                    break;
                case "reset stats":
                    ResetStats();
                    break;
                case "exit":
                    if (!"".equals(toFile)) {
                        ExportCards(toFile);
                    }
                    exit = true;
                    break;
                default:
            }
        }
        logger.Output("Bye bye!");
    }

    private static void ResetStats() {
        for (var entry : mistakeCounter.entrySet()) {
            entry.setValue(0);
        }
        logger.Output("Card statistics have been reset.");
    }

    private static void DisplayHardest() {
        int max = 1;
        ArrayList<String> terms = new ArrayList<>();
        for (var entry : mistakeCounter.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
                terms.clear();
                terms.add(entry.getKey());
            } else if (entry.getValue() == max) {
                terms.add(entry.getKey());
            }
        }
        switch (terms.size()) {
            case 0:
                logger.Output("There are no cards with errors.");
                break;
            case 1:
                logger.Output("The hardest card is \"" + terms.get(0) +
                        "\". You have " + mistakeCounter.get(terms.get(0)) +
                        " errors answering it");
                break;
            default:
                StringBuilder cardList = new StringBuilder();
                cardList.append("\"").append(terms.get(0)).append("\"");
                for (int i = 1; i < terms.size(); i++) {
                    cardList.append(", \"").append(terms.get(i)).append("\"");
                }
                logger.Output("The hardest cards are " + cardList +
                        ". You have " + max +" errors answering them.");
        }
    }

    private static void SaveLog() {
        logger.Output("File name:");
        String fileName = logger.ScanNextLine();
        try (FileWriter log = new FileWriter(fileName)) {
            log.write(logger.log.toString());
            log.write("The log has been saved.\n");
            log.write("Input the action (add, remove, import, export, " +
                    "ask, exit, log, hardest card, reset stats):\n");
            // System.out.print("Saved Log:" + logger.log.toString());
            logger.Output("The log has been saved.");
        } catch (IOException exception) {
            logger.Output("ERR: IOException");
        }
    }

    private static void Ask() {
        logger.Output("How many times to ask?");
        int times = Integer.parseInt(logger.ScanNextLine());
        // Random generator = new Random();
        String[] termArray = cards.keySet().toArray(new String[0]);
        for (int i = 0; i < times; i++) {
            // String term = termArray[generator.nextInt(termArray.length)];
            String term = termArray[i];
            logger.Output("Print the definition of \"" + term + "\":");
            String answer = logger.ScanNextLine();
            if (cards.get(term).equals(answer)) {
                logger.Output("Correct!");
            } else if (cards.containsValue(answer)) {
                logger.Output("Wrong. The right answer is \"" +
                        cards.get(term) +
                        "\", but your definition is correct for \"" +
                        getKeyByValue(cards, answer) + "\".");
                mistakeCounter.put(term, mistakeCounter.get(term) + 1);
            } else {
                logger.Output("Wrong. The right answer is \"" + cards.get(term) + "\".");
                mistakeCounter.put(term, mistakeCounter.get(term) + 1);
            }
        }
    }

    private static void ExportCards (String toFile) {
        try (FileWriter file = new FileWriter(toFile)) {
            for (var card : cards.entrySet()) {
                file.write(card.getKey() + "\n");
                file.write(card.getValue() + "\n");
                file.write(mistakeCounter.get(card.getKey()) + "\n");
            }
            logger.Output(cards.size() + " cards have been saved.");
        } catch (IOException e) {
            logger.Output("ERR: IOException");
            throw new RuntimeException(e);
        }
    }

    private static void ExportCards() {
        logger.Output("File name:");
        String fileName = logger.ScanNextLine();
        try (FileWriter file = new FileWriter(fileName)) {
            for (var card : cards.entrySet()) {
                file.write(card.getKey() + "\n");
                file.write(card.getValue() + "\n");
                file.write(mistakeCounter.get(card.getKey()) + "\n");
            }
            logger.Output(cards.size() + " cards have been saved.");
        } catch (IOException e) {
            logger.Output("ERR: IOException");
            throw new RuntimeException(e);
        }
    }

    private static void ImportCards(String fromFile) {
        Path file = Paths.get(fromFile);
        long pairNumber;
        try (Stream<String> lines = Files.lines(file)) {
            pairNumber = lines.count() / 3;
        } catch (IOException exception) {
            pairNumber = 0;
        }
        if (file.toFile().exists()) {
            try (Scanner fileScanner = new Scanner(file)) {
                int n = 0;
                for (int i = 0; i < pairNumber; i++) {
                    String term = fileScanner.nextLine();
                    String definition = fileScanner.nextLine();
                    int mistakes = Integer.parseInt(fileScanner.nextLine());
                    if (cards.containsKey(term) || !cards.containsValue(definition)) {
                        cards.put(term, definition);
                        mistakeCounter.put(term, mistakes);
                        n++;
                    }
                }
                logger.Output(n + " cards have been loaded.");
            } catch (IOException exception) {
                logger.Output("ERR: IOException");
            }
        } else {
            logger.Output("File not found.");
        }
    }

    private static void ImportCards() throws IOException {
        logger.Output("File name:");
        String fileName = logger.ScanNextLine();
        Path file = Paths.get(fileName);
        long pairNumber;
        try (Stream<String> lines = Files.lines(file)) {
            pairNumber = lines.count() / 3;
        } catch (IOException exception) {
            pairNumber = 0;
        }
        if (file.toFile().exists()) {
            Scanner fileScanner = new Scanner(file);
            // int previous = cards.size();
            int n = 0;
            for (int i = 0; i < pairNumber; i++) {
                String term = fileScanner.nextLine();
                String definition = fileScanner.nextLine();
                int mistakes = Integer.parseInt(fileScanner.nextLine());
                if (cards.containsKey(term) || !cards.containsValue(definition)) {
                    cards.put(term, definition);
                    mistakeCounter.put(term, mistakes);
                    n++;
                }
                // System.out.println(term + " : " + definition + " : " + mistakes);
            }
            logger.Output(n + " cards have been loaded.");
        } else {
            logger.Output("File not found.");
        }
    }

    private static void RemoveCard() {
        logger.Output("Which card?");
        String term = logger.ScanNextLine();
        if (cards.containsKey(term)) {
            cards.remove(term);
            mistakeCounter.remove(term);
            logger.Output("The card has been removed.");
        } else {
            String info = "Can't remove \"" + term +
                    "\": there is no such card.";
            logger.Output(info);
        }
    }

    private static void AddCard() {
        logger.Output("The card:");
        String term = logger.ScanNextLine();
        if (cards.containsKey(term)) {
            logger.Output("The card \"" + term +
                    "\" already exists.");
            return;
        }
        logger.Output("The definition of the card:");
        String definition = logger.ScanNextLine();
        if (cards.containsValue(definition)) {
            logger.Output("The definition \"" + definition +
                    "\" already exists.");
            return;
        }
        cards.put(term, definition);
        mistakeCounter.put(term, 0);
        logger.Output("The pair (\"" + term + "\":\"" +
                definition + "\") has been added.");
    }

    private static void DisplayMenu() {
        logger.Output("Input the action (add, remove, import, export," +
                " ask, exit, log, hardest card, reset stats):");
    }

}
