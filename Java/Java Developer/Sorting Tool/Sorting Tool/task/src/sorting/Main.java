package sorting;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(final String[] args) {
        SortType sortType = SortType.NATURAL; // Default value
        DataType dataType = DataType.WORD;
        File inputFile = null;
        File outputFile = null;
        ArrayList<String> argList = new ArrayList<>(Arrays.asList(args));
        if (argList.contains("-inputFile")) {
            int index = argList.indexOf("-inputFile");
            try {
                inputFile = new File(argList.get(index + 1));
                argList.remove(index);
                argList.remove(index);
            } catch (IndexOutOfBoundsException e) {
                System.out.println("No input file provided");
                inputFile = null;
            }
        }
        if (argList.contains("-outputFile")) {
            int index = argList.indexOf("-outputFile");
            try {
                outputFile = new File(argList.get(index + 1));
                argList.remove(index);
                argList.remove(index);
            } catch (IndexOutOfBoundsException e) {
                System.out.println("No output file provided");
                outputFile = null;
            }
        }
        if (argList.contains("-sortingType")) {
            try {
                String value = argList.get(argList.indexOf("-sortingType") + 1);
                if (value.contains("-")) {
                    System.out.println("No sorting type defined!");
                }
                switch (value) {
                    case "natural":
                        sortType = SortType.NATURAL;
                        break;
                    case "byCount":
                        sortType = SortType.BY_COUNT;
                        break;
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("No sorting type defined!");
            }
            argList.remove("-sortingType");
        }
        if (argList.contains("-dataType")) {
            try {
                String value = argList.get(argList.indexOf("-dataType") + 1);
                if (value.contains("-")) {
                    System.out.println("No data type defined!");
                }
                switch (value) {
                    case "word":
                        dataType = DataType.WORD;
                        break;
                    case "long":
                        dataType = DataType.LONG;
                        break;
                    case "line":
                        dataType = DataType.LINE;
                        break;
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("No data type defined!");
            }
            argList.remove("-dataType");
        }
        for (String arg : argList) {
            if (arg.contains("-")) {
                System.out.println("\"" + arg + "\" is not a valid parameter. It will be skipped.");
            }
        }
        parseData(sortType, dataType, inputFile, outputFile);
    }

    private static void parseData(SortType sortType,
                                  DataType dataType, File inputFile, File outputFile) {
        Scanner scanner = new Scanner(System.in);
        PrintStream outputStream = System.out;
        PrintStream errorStream = System.err;
        PrintStream stdout = System.out;
        PrintStream stderr = System.err;
        if (inputFile != null) {
            try {
                scanner = new Scanner(inputFile);
            } catch (FileNotFoundException e) {
                System.out.println("Input file doesn't exist");
            }
        }
        if (outputFile != null) {
            if (!outputFile.exists()) {
                try {
                    outputFile.createNewFile();
                } catch (IOException e) {
                    System.out.println("Output file can't be created");
                }
            }
            try {
                outputStream = new PrintStream(new BufferedOutputStream(new FileOutputStream(outputFile)), true);
                errorStream = stdout;
                System.setOut(outputStream);
                System.setErr(errorStream);
            } catch (FileNotFoundException e) {
                System.out.println("Output file doesn't exist"); // This should never happen
                System.setOut(stdout);
                System.setErr(stderr);
            }
        }
        switch (dataType) {
            case WORD:
                parseWord(scanner, sortType);
                break;
            case LINE:
                parseLine(scanner, sortType);
                break;
            case LONG:
                parseLong(scanner, sortType);
                break;
        }
        scanner.close();
    }

    private static void parseLong(Scanner scanner, SortType sortType) {
        ArrayList<Long> list = new ArrayList<>();
        while (scanner.hasNext()) {
            String value = scanner.next();
            try {
                list.add(Long.parseLong(value));
            } catch (NumberFormatException e) {
                System.err.println("\"" + value + "\" is not a long. It will be skipped.");
            }
        }
        System.out.printf("Total numbers: %d.\n", list.size());
        switch (sortType) {
            case NATURAL:
                Collections.sort(list);
                System.out.println("Sorted data: " + list.toString()
                        .substring(1, list.toString().length() - 1)
                        .replaceAll(", ", " "));
                break;
            case BY_COUNT:
                Map<Long, Integer> map = new LinkedHashMap<>();
                for (Long l : list) {
                    if (map.containsKey(l)) {
                        map.put(l, map.get(l) + 1);
                    } else {
                        map.put(l, 1);
                    }
                }
                map.entrySet().stream().sorted(new LongCountComparator())
                        .forEach(e -> System.out.printf("%d: %d time(s), %d%%\n",
                                e.getKey(), e.getValue(), e.getValue() * 100 / list.size()));
                break;
        }
    }

    private static void parseWord(Scanner scanner, SortType sortType) {
        ArrayList<String> list = new ArrayList<>();
        while (scanner.hasNext()) {
            list.add(scanner.next());
        }
        System.out.println("Total words: " + list.size());
        switch (sortType) {
            case NATURAL:
                System.out.println("Sorted data: " + list.stream().sorted(Comparator.comparing(String::length))
                        .collect(Collectors.toList()).toString().substring(1, list.toString().length() - 1)
                        .replaceAll(", ", " "));
                break;
            case BY_COUNT:
                Map<String, Integer> map = new LinkedHashMap<>();
                for (String s : list) {
                    if (map.containsKey(s)) {
                        map.put(s, map.get(s) + 1);
                    } else {
                        map.put(s, 1);
                    }
                }
                map.entrySet().stream().sorted(new StringCountComparator())
                        .forEach(e -> System.out.printf("%s: %d time(s), %d%%\n",
                                e.getKey(), e.getValue(), e.getValue() * 100 / list.size()));
                break;
        }
    }

    private static void parseLine(Scanner scanner, SortType sortType) {
        ArrayList<String> list = new ArrayList<>();
        while (scanner.hasNextLine()) {
            list.add(scanner.nextLine());
        }
        System.out.println("Total lines: " + list.size());
        switch (sortType) {
            case NATURAL:
                System.out.println("Sorted data:");
                list.stream().sorted(Comparator.comparing(String::length)).forEach(System.out::println);
                break;
            case BY_COUNT:
                Map<String, Integer> map = new LinkedHashMap<>();
                for (String s : list) {
                    if (map.containsKey(s)) {
                        map.put(s, map.get(s) + 1);
                    } else {
                        map.put(s, 1);
                    }
                }
                map.entrySet().stream().sorted(new StringCountComparator())
                        .forEach(e -> System.out.printf("%s: %d time(s), %d%%\n",
                                e.getKey(), e.getValue(), e.getValue() * 100 / list.size()));
                break;
        }
    }
}

class LongCountComparator implements Comparator<Map.Entry<Long, Integer>> {

    @Override
    public int compare(Map.Entry<Long, Integer> e1, Map.Entry<Long, Integer> e2) {
        if (Objects.equals(e1.getValue(), e2.getValue())) {
            return e1.getKey().compareTo(e2.getKey());
        } else {
            return e1.getValue().compareTo(e2.getValue());
        }
    }
}

class StringCountComparator implements Comparator<Map.Entry<String, Integer>> {

    @Override
    public int compare(Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) {
        if (Objects.equals(e1.getValue(), e2.getValue())) {
            return e1.getKey().compareTo(e2.getKey());
        } else {
            return e1.getValue().compareTo(e2.getValue());
        }
    }
}

enum SortType {
    NATURAL, BY_COUNT
}

enum DataType {
    LONG, WORD, LINE
}
