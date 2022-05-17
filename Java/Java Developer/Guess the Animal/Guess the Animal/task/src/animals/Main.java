package animals;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static animals.Corpus.*;

public class Main {
    static AnimalTree animalTree = new AnimalTree();
    static Scanner scanner = new Scanner(System.in);
    static String fileName = "animals";
    static ObjectMapper treeDataMapper;
    static boolean isEsperanto = false;
    public static void main(String[] args) {
        if (Arrays.stream(args).anyMatch(arg -> arg.matches("-Duser.language=.*"))) {
            String language = Arrays.stream(args).filter(arg -> arg.matches("-Duser.language=.*"))
                    .findFirst().get().split("=")[1].replaceFirst("[-_].*", "").toLowerCase();
            fileName = fileName + "_" + language;
            isEsperanto = language.equals("eo");
        }
        if (!Locale.getDefault().getLanguage().equals("en")) {
            fileName = fileName + "_" + Locale.getDefault().getLanguage();
            isEsperanto = Locale.getDefault().getLanguage().equals("eo");
        }
        if (isEsperanto) {
            Locale.setDefault(new Locale("eo"));
        }
        switch (Arrays.stream(args).filter(s -> s.matches("(json|xml|yaml)")).findFirst().orElse("json")) {
            case "json":
                fileName = fileName + ".json";
                treeDataMapper = new JsonMapper();
                break;
            case "xml":
                fileName = fileName + ".xml";
                treeDataMapper = new XmlMapper();
                break;
            case "yaml":
                fileName = fileName + ".yaml";
                treeDataMapper = new YAMLMapper();
                break;
            default:
                System.err.println("Invalid format");
                treeDataMapper = new JsonMapper();
        }
        greet();
        loadData();
        showMenu(true);
    }

    private static void greet() {
        LocalTime now = LocalTime.now();
        if (now.isAfter(LocalTime.of(5, 0)) && now.isBefore(LocalTime.of(6, 30))) {
            System.out.println(getCorpus(earlyMorningGreetings));
        } else if (now.isBefore(LocalTime.of(12, 0))) {
            System.out.println(getCorpus(morningGreetings));
        } else if (now.isBefore(LocalTime.of(18, 0))) {
            System.out.println(getCorpus(afternoonGreetings));
        } else if (now.isBefore(LocalTime.of(22, 0))) {
            System.out.println(getCorpus(eveningGreetings));
        } else {
            System.out.println(getCorpus(lateNightGreetings));
        }
        System.out.println();
    }

    static String generateInterrogator(String predicate) {
        switch (predicate) {
            case "is":
            case "can":
                return predicate.replace(predicate.charAt(0), (char) (predicate.charAt(0) - 32)) + " it";
            case "has":
                return "Does it have";
            case "ĝi":
                return "Ĉu ĝi";
            default:
                return "";
        }
    }

    private static String reply(boolean isPositive) {
        if (isPositive) {
            return getCorpus(positiveReply) + " " + msgI18n.getString("animal.learnedMuch");
        }
        return Corpus.negativeReply;
    }

    private static boolean isPositive(String input) {
        /*
            String answerString = Arrays.stream(scanner.nextLine().strip().split("\\s+")).
                    map(str -> str.replaceAll("\\W", "")).collect(Collectors.joining(" "));
        */
        input = input.strip();
        boolean positive = input.matches(positiveResponse);
        boolean negative = input.matches(negativeResponse);
        if (positive) {
            return true;
        } else if (negative) {
            return false;
        }
        return false;
    }

    private static boolean isUnclear(String input) {
        input = input.strip();
        boolean positive = input.matches(positiveResponse);
        boolean negative = input.matches(negativeResponse);
        return !positive && !negative;
    }

    private static Animal goThroughTree(AnimalTree animalTree) {
        AnimalTreeNode currentNode = animalTree.root;
        while (currentNode.getClass() == Fact.class) {
            System.out.printf("%s %s?\n", generateInterrogator(((Fact) currentNode).predicate), ((Fact) currentNode).getFactPhrase());
            String answer = scanner.nextLine().strip();
            while (isUnclear(answer)) {
                System.out.println(getCorpus(askToClarify));
                answer = scanner.nextLine().strip();
            }
            if (isPositive(answer)) {
                currentNode = currentNode.getRight();
            } else {
                currentNode = currentNode.getLeft();
            }
        }
        return (Animal) currentNode;
    }

    private static void saveAnimalTree(AnimalTree animalTree, ObjectMapper mapper) {
        try {
            Fact root = (Fact) animalTree.root;
            mapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValue(new File(fileName), root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void showMenu(boolean isFirstTime) {
        if (isFirstTime) {
            System.out.println();
            System.out.println(msgI18n.getString("welcome"));
        }
        System.out.println("\n" + msgI18n.getString("menu.property.title") +"\n" +
                "\n" +
                "1. " + msgI18n.getString("menu.entry.play") + "\n" +
                "2. " + msgI18n.getString("menu.entry.list") + "\n" +
                "3. " + msgI18n.getString("menu.entry.search") + "\n" +
                "4. " + msgI18n.getString("menu.entry.statistics") + "\n" +
                "5. " + msgI18n.getString("menu.entry.print") + "\n" +
                "0. " + msgI18n.getString("menu.property.exit"));
        int choice = Integer.parseInt(scanner.nextLine().strip());
        switch (choice) {
            case 1:
                playGame();
                break;
            case 2:
                listAnimals();
                break;
            case 3:
                searchAnimal();
                break;
            case 4:
                calculateStatistics();
                break;
            case 5:
                animalTree.printTree();
                showMenu();
                break;
            case 0:
                saveAnimalTree(animalTree, treeDataMapper);
                System.out.println();
                System.out.println(getCorpus(goodbye));
                break;
            default:
                System.out.println(msgI18n.getString("menu.property.error"));
                showMenu();
        }
    }
    private static void showMenu() {
        showMenu(false);
    }

    private static void loadData() {
        treeDataMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        try {
            animalTree.root = treeDataMapper.readValue(new File(fileName), Fact.class);
        } catch (IOException e) {
            System.out.println(msgI18n.getString("animal.wantLearn"));
            System.out.println(msgI18n.getString("animal.askFavorite"));
            animalTree.root = new Animal(scanner.nextLine());
        }
    }

    private static void playGame() {
        boolean finished = false;
        while (!finished) {
            System.out.println(msgI18n.getString("game.think"));
            System.out.println(msgI18n.getString("game.enter"));
            scanner.nextLine();
            Animal currentAnimal = goThroughTree(animalTree);
            if (isEsperanto) {
                System.out.printf("Ĉu ĝi estas %s?\n", currentAnimal.name);
            } else {
                System.out.printf("Is it %s %s?\n", currentAnimal.undefinedArticle, currentAnimal.name);
            }
            String answer = scanner.nextLine().strip().toLowerCase();
            while (isUnclear(answer)) {
                System.out.println(getCorpus(askToClarify));
                answer = scanner.nextLine().strip();
            }
            if (isPositive(answer)) {
                System.out.println(reply(true));
            } else {
                System.out.println(reply(false));
                Animal newAnimal = new Animal(scanner.nextLine());
                System.out.println(MessageFormat.format(msgI18n.getString("statement.prompt"), currentAnimal.toString(), newAnimal.toString()));
                String factString = scanner.nextLine().strip().replaceAll("[~!@#$%^&*()_+=|{}':;<>?./]", "");
                if (factString.charAt(0) >= 'a' && factString.charAt(0) <= 'z') {
                    factString = factString.replaceFirst(String.valueOf(factString.charAt(0)), String.valueOf((char) (factString.charAt(0) - 32)));
                }
                if (factString.charAt(0) == 'ĝ') {
                    factString = factString.replaceFirst("ĝ", "Ĝ");
                }
                while (!factString.matches("It (can|has|is) .+") && !factString.matches("Ĝi .+")) {
                    System.out.println(msgI18n.getString("statement.error"));
                    System.out.println(MessageFormat.format(msgI18n.getString("statement.prompt"), currentAnimal.toString(), newAnimal.toString()));
                    factString = scanner.nextLine().strip();
                }
                Fact fact = new Fact(factString);
                if (animalTree.root == currentAnimal) {
                    animalTree.root = fact;
                } else {
                    AnimalTreeNode originalParent = animalTree.getParent(currentAnimal);
                    if (originalParent.getLeft() == currentAnimal) {
                        originalParent.setLeft(fact);
                    } else {
                        originalParent.setRight(fact);
                    }
                }
                System.out.println(MessageFormat.format(msgI18n.getString("game.isCorrect"), newAnimal.toString()));
                answer = scanner.nextLine().strip();
                if (isPositive(answer)) {
                    newAnimal.factIsTrue = true;
                    currentAnimal.factIsTrue = false;
                    fact.setLeft(currentAnimal);
                    fact.setRight(newAnimal);
                } else {
                    currentAnimal.factIsTrue = true;
                    newAnimal.factIsTrue = false;
                    fact.setLeft(newAnimal);
                    fact.setRight(currentAnimal);
                }
                String definedArticle = isEsperanto ? "La" : "The";
                if (isEsperanto) {
                    System.out.printf(msgI18n.getString("game.learned") + "\n" +
                                    " - %s %s %s.\n" +
                                    " - %s %s %s.\n" +
                                    msgI18n.getString("game.distinguish") + "\n" +
                                    " - %s %s?\n",
                            definedArticle, currentAnimal.name, currentAnimal.factIsTrue ? fact.getFactPhrase() : "ne " + fact.getFactPhrase(),
                            definedArticle, newAnimal.name, newAnimal.factIsTrue ? fact.getFactPhrase() : "ne " + fact.getFactPhrase(),
                            generateInterrogator(fact.predicate), fact.getFactPhrase());
                } else {
                    System.out.printf(msgI18n.getString("game.learned") + "\n" +
                                    " - %s %s %s %s.\n" +
                                    " - %s %s %s %s.\n" +
                                    msgI18n.getString("game.distinguish") + "\n" +
                                    " - %s %s?\n",
                            definedArticle, currentAnimal.name, currentAnimal.factIsTrue ? fact.predicate : fact.getPredicateNegation() , fact.getFactPhrase(),
                            definedArticle, newAnimal.name, newAnimal.factIsTrue ? fact.predicate : fact.getPredicateNegation() , fact.getFactPhrase(),
                            generateInterrogator(fact.predicate), fact.getFactPhrase());
                }
                System.out.println(reply(true));
            }
            System.out.println(getCorpus(playAgain));
            finished = !isPositive(scanner.nextLine().strip());
        }
        showMenu();
    }

    private static void listAnimals() {
        System.out.println(msgI18n.getString("tree.list.animals"));
        List<Animal> animals = new ArrayList<>();
        getAnimals(animalTree.root, animals);
        List<Animal> animalsSorted = animals.stream().sorted(new AnimalComparator()).collect(Collectors.toList());
        for (Animal animal : animalsSorted) {
            System.out.println(" - " + animal.name);
        }
        showMenu();
    }
    static void getAnimals(AnimalTreeNode currentNode, List<Animal> animals) {
        if (currentNode != null) {
            if (currentNode.getLeft() != null) {
                getAnimals(currentNode.getLeft(), animals);
            }
            if (currentNode.getClass() == Animal.class) {
                animals.add((Animal) currentNode);
            }
            if (currentNode.getRight() != null) {
                getAnimals(currentNode.getRight(), animals);
            }
        }
    }

    private static void searchAnimal() {
        System.out.println(msgI18n.getString("animal.prompt"));
        String animalName = scanner.nextLine().strip().toLowerCase();
        Animal result = animalTree.findAnimal(animalName);
        if (result != null) {
            System.out.println(MessageFormat.format(msgI18n.getString("tree.search.facts"), result.name));
            printFacts(result);
        } else {
            System.out.println(MessageFormat.format(msgI18n.getString("tree.search.noFacts"), animalName));
        }
        showMenu();
    }
    private static void printFacts(Animal animal) {
        Deque<AnimalTreeNode> treeStack = new ArrayDeque<>();
        AnimalTreeNode currentNode = animalTree.getParent(animal);
        while (currentNode != null) {
            treeStack.push(currentNode);
            currentNode = animalTree.getParent(currentNode);
        }
        String factString;
        while (!treeStack.isEmpty()) {
            currentNode = treeStack.pop();
            if (treeStack.isEmpty() && currentNode.getLeft() == animal) {
                factString = ((Fact) currentNode).getFactNegation();
            } else {
                AnimalTreeNode temp = treeStack.peek();
                if (currentNode.getLeft() == temp || currentNode.getLeft() == animal) {
                    factString = ((Fact) currentNode).getFactNegation();
                } else {
                    factString = ((Fact) currentNode).fact;
                }
            }
            System.out.println(" - " + factString + ".");
        }
    }

    private static void calculateStatistics() {
        System.out.println(msgI18n.getString("tree.stats.title"));
        System.out.println();
        System.out.println(MessageFormat.format(msgI18n.getString("tree.stats.root"), animalTree.root));
        System.out.println(MessageFormat.format(msgI18n.getString("tree.stats.nodes"), animalTree.getNumberOfNodes()));
        System.out.println(MessageFormat.format(msgI18n.getString("tree.stats.animals"), animalTree.getNumberOfAnimals()));
        System.out.println(MessageFormat.format(msgI18n.getString("tree.stats.statements"), (animalTree.getNumberOfNodes() - animalTree.getNumberOfAnimals())));
        System.out.println(MessageFormat.format(msgI18n.getString("tree.stats.height"), animalTree.getHeight()));
        System.out.println(MessageFormat.format(msgI18n.getString("tree.stats.minimum"), animalTree.getMinDepth()));
        System.out.println(MessageFormat.format(msgI18n.getString("tree.stats.average"), (animalTree.getAvgLeafDepth() - 1)));
        showMenu();
    }
}
