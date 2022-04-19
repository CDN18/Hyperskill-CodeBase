package readability;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(new File(args[0]))) {
            int sentences = 0;
            int words = 0;
            int characters = 0;
            int syllables = 0;
            int polysyllables = 0;
            while (scanner.hasNextLine()) {
                String content = scanner.nextLine();
                String[] paragraph = content.split("[.!?;]");
                sentences += paragraph.length;
                for (String sentence : paragraph) {
                    String[] wordList = sentence.trim().split("\\s");
                    words += wordList.length;
                    for (String wd : wordList) {
                        characters += wd.length();
                        int syllableCounter = 0;
                        wd = wd.toLowerCase();
                        String[] vowels = wd.split("[^aeiouy]");
                        for (String vowel : vowels) {
                            if (!"".equals(vowel)) {
                                syllableCounter++;
                            }
                        }
                        if (syllableCounter > 0 &&
                                "e".equals(vowels[vowels.length - 1]) &&
                                wd.charAt(wd.length() - 1) == 'e') {
                            syllableCounter--;
                        }
                        if (syllableCounter == 0) {
                            syllableCounter = 1;
                        }
                        syllables += syllableCounter;
                        if (syllableCounter > 2) {
                            polysyllables++;
                        }
                    }
                    characters++;
                }
                if (!content.substring(content.length() - 1).
                        matches("[.!?;]")) {
                    characters--;
                }
            }
            System.out.println("Words: " + words);
            System.out.println("Sentences: " + sentences);
            System.out.println("Characters: " + characters);
            System.out.println("Syllables: " + syllables);
            System.out.println("Polysyllables: " + polysyllables);
            System.out.print("Enter the score you want to calculate " +
                    "(ARI, FK, SMOG, CL, all): ");
            Scanner inputScanner = new Scanner(System.in);
            double ari = GenerateARI(characters, words, sentences);
            double fk = GenerateFK(words, sentences, syllables);
            double smog = GenerateSMOG(polysyllables, sentences);
            double cl = GenerateCL(words, characters, sentences);
            int ariAge = ComputeAge(ari);
            int fkAge = ComputeAge(fk);
            int smogAge = ComputeAge(smog);
            int clAge = ComputeAge(cl);
            double avrAge = (double) (ariAge + fkAge + smogAge + clAge) / 4;
            switch (inputScanner.nextLine()) {
                case "ARI":
                    System.out.println("");
                    System.out.printf("Automated Readability Index: %.2f" +
                            " (about %d-year-olds).\n", ari, ariAge);
                    break;
                case "FK":
                    System.out.println("");
                    System.out.printf("Flesch–Kincaid readability tests: %.2f" +
                            " (about %d-year-olds).\n", fk, fkAge);
                    break;
                case "SMOG":
                    System.out.println("");
                    System.out.printf("Simple Measure of Gobbledygook: %.2f" +
                            " (about %d-year-olds).\n", smog, smogAge);
                    break;
                case "CL":
                    System.out.println("");
                    System.out.printf("Coleman–Liau index: %.2f" +
                            " (about %d-year-olds).\n", cl, clAge);
                    break;
                case "all":
                    System.out.println("");
                    System.out.printf("Automated Readability Index: %.2f" +
                            " (about %d-year-olds).\n", ari, ariAge);
                    System.out.printf("Flesch–Kincaid readability tests: %.2f" +
                            " (about %d-year-olds).\n", fk, fkAge);
                    System.out.printf("Simple Measure of Gobbledygook: %.2f" +
                            " (about %d-year-olds).\n", smog, smogAge);
                    System.out.printf("Coleman–Liau index: %.2f" +
                            " (about %d-year-olds).\n", cl, clAge);
                    System.out.printf("\nThis text should be understood in average " +
                            "by %.2f-year-olds.", avrAge);
                    break;
                default:
            }
        } catch (FileNotFoundException fnf) {
            throw new RuntimeException(fnf);
        }
    }

    private static int ComputeAge(double score) {
        return (int) Math.ceil(score) + 5;
    }

    private static double GenerateCL(int words, int characters, int sentences) {
        double L = (double) (characters * 100) / words;
        double S = (double) (sentences * 100) / words;
        return 0.0588 * L - 0.296 * S - 15.8;
    }

    private static double GenerateSMOG(int polysyllables, int sentences) {
        return (1.043 * (Math.sqrt((double) (polysyllables * 30) / sentences))) + 3.129_1;
    }

    private static double GenerateFK(int words, int sentences, int syllables) {
        return 0.39 * words / sentences + 11.8 * syllables / words - 15.59;
    }

    private static double GenerateARI(int characters, int words, int sentences) {
        return 4.71 * characters / words + 0.5 * words / sentences - 21.43;
    }
}
