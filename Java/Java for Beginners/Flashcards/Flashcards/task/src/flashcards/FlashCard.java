package flashcards;

/**
 * Deprecated by stage 4.
 */
@Deprecated
public class FlashCard {
    int id;
    String term;
    String definition;

    public FlashCard(int id, String term, String definition) {
        this.id = id;
        this.term = term;
        this.definition = definition;
    }

    public int getId() {
        return id;
    }

    public String getDefinition() {
        return definition;
    }

    public String getTerm() {
        return term;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public void setTerm(String term) {
        this.term = term;
    }
}
