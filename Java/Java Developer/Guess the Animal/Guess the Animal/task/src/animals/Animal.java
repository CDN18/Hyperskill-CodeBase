package animals;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.io.Serializable;
import java.util.Comparator;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeName("Animal")
public class Animal implements AnimalTreeNode, Serializable {
    String name;
    String undefinedArticle;
    static String definedArticle = Main.isEsperanto ? "la" : "the";
    // Fact fact;
    boolean factIsTrue;

    AnimalTreeNode leftChild;
    AnimalTreeNode rightChild;

    public Animal(String nameInput) {
        nameInput = nameInput.strip().toLowerCase();
        if (Main.isEsperanto) {
            this.name = parseAnimal(nameInput);
        } else {
            this.name = parseAnimal(nameInput).split(" ")[1];
        }
        if (Main.isEsperanto) {
            this.undefinedArticle = "";
        } else {
            this.undefinedArticle = parseAnimal(nameInput).split(" ")[0];
        }
    }

    public Animal() {
    }

    /*
    public void setFact(Fact fact) {
        this.fact = fact;
    }
     */

    static String parseAnimal(String input) {
        if (Main.isEsperanto) {
            return input.replaceFirst("[lL]a ", "");
        }
        if (input.startsWith("a ") || input.startsWith("an ")) {
            return input;
        }
        if (input.startsWith("the ")) {
            input = input.replaceFirst("the ", "");
        }
        boolean startWithVowel = String.valueOf(input.charAt(0)).matches("[aeiou]");
        String undefinedArticle = startWithVowel ? "an" : "a";
        return undefinedArticle + " " + input;
    }

    @Override
    public String toString() {
        if (Main.isEsperanto) {
            return name;
        }
        return this.undefinedArticle + " " + this.name;
    }


    @Override @JsonIgnore
    public AnimalTreeNode getLeft() {
        return leftChild;
    }

    @Override @JsonIgnore
    public AnimalTreeNode getRight() {
        return rightChild;
    }

    @Override
    public void setLeft(AnimalTreeNode left) {
        this.leftChild = left;
    }

    @Override
    public void setRight(AnimalTreeNode right) {
        this.rightChild = right;
    }
}

class AnimalComparator implements Comparator<Animal> {
    @Override
    public int compare(Animal animal1, Animal animal2) {
        return animal1.name.compareTo(animal2.name);
    }
}
