package animals;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import javax.xml.namespace.QName;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeName("Fact")
public class Fact implements AnimalTreeNode, Serializable {
    String fact;
    String predicate;

    AnimalTreeNode leftChild; //The fact is false
    AnimalTreeNode rightChild; // The fact is true

    public Fact(String factString) {
        this.fact = turnFactToTrue(factString);
        if (Main.isEsperanto) {
            this.predicate = "ĝi";
        } else {
            Pattern predicatePattern = Pattern.compile("(can|has|is)");
            Matcher predicateMatcher = predicatePattern.matcher(fact);
            this.predicate = predicateMatcher.find() ? predicateMatcher.group() : "";
        }
        // this.factIsTrue = factString.contains(this.predicate);
    }

    public Fact() {
    }

    @JsonIgnore
    public String getPredicateNegation() {
        switch (predicate) {
            case "can":
                return  "can't";
            case "has":
                return "doesn't have";
            case "is":
                return "isn't";
            case "ĝi":
                return "ĝi ne";
            default:
                return "";
        }
    }

    @JsonIgnore
    public String getFactNegation() {
        return this.fact.replaceFirst(this.predicate, getPredicateNegation());
    }

    static String turnFactToTrue(String fact) {
        String trueFact = fact.strip();
        if (Main.isEsperanto && trueFact.contains("ĝi ne")) {
            trueFact = trueFact.replaceFirst("ĝi ne", "ĝi");
        }
        if (fact.contains("isn't") || fact.contains("is not")) {
            trueFact = fact.replaceFirst("isn't", "is").replaceFirst("is not", "is");
        }
        if (fact.contains("can't") || fact.contains("can not")) {
            trueFact = fact.replaceFirst("can't", "can").replaceFirst("can not", "can");
        }
        if (fact.contains("don't have") || fact.contains("doesn't have") ||
                fact.contains("have not") || fact.contains("has not")) {
            trueFact = fact.replaceFirst("(don't|doesn't|do not|does not) have", "have")
                    .replaceFirst("have not", "have").replaceFirst("has not", "has");
        }
        return trueFact;
    }

    @JsonIgnore
    public String getFactPhrase() {
        if (Main.isEsperanto) {
            return fact.replaceFirst("[Ĝĝ]i", "").strip();
        } else {
            return fact.replaceFirst("It (can|has|is)", "").strip();
        }
    }

    @Override @JsonIgnore
    public AnimalTreeNode getLeft() {
        return this.leftChild;
    }

    @Override @JsonIgnore
    public AnimalTreeNode getRight() {
        return this.rightChild;
    }

    @Override
    public void setLeft(AnimalTreeNode left) {
        this.leftChild = left;
    }

    @Override
    public void setRight(AnimalTreeNode right) {
        this.rightChild = right;
    }

    @Override
    public String toString() {
        return this.fact;
    }
}
