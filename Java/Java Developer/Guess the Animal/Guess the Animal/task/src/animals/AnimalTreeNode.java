package animals;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo( use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Animal.class, name = "Animal"),
        @JsonSubTypes.Type(value = Fact.class, name = "Fact"),
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface AnimalTreeNode {
    public AnimalTreeNode getLeft();
    public AnimalTreeNode getRight();

    public void setLeft(AnimalTreeNode left);
    public void setRight(AnimalTreeNode right);
}
