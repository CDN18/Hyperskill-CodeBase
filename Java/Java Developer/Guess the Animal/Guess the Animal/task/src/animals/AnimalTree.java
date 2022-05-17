package animals;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AnimalTree implements Serializable {
    AnimalTreeNode root;

    public AnimalTree() {
        root = null;
    }

    public AnimalTree (AnimalTreeNode root) {
        this.root = root;
    }

    public int getNumberOfNodes() {
        return getNumberOfNodes(root);
    }
    private int getNumberOfNodes(AnimalTreeNode node) {
        if (node == null) {
            return 0;
        }
        return 1 + getNumberOfNodes(node.getLeft()) + getNumberOfNodes(node.getRight());
    }

    public int getNumberOfAnimals() {
        return getNumberOfAnimals(root);
    }
    private int getNumberOfAnimals(AnimalTreeNode node) {
        if (node == null) {
            return 0;
        }
        if (node.getLeft() == null && node.getRight() == null) {
            return 1;
        }
        return getNumberOfAnimals(node.getLeft()) + getNumberOfAnimals(node.getRight());
    }

    public int getHeight() {
        return Math.max(getHeight(root) - 1, 0);
    }
    private int getHeight(AnimalTreeNode node) {
        if (node == null) {
            return 0;
        }
        return 1 + Math.max(getHeight(node.getLeft()), getHeight(node.getRight()));
    }

    public int getMinDepth() {
        return Math.max(getMinDepth(root) - 1, 0);
    }
    private int getMinDepth(AnimalTreeNode node) {
        if (node == null) {
            return 0;
        }
        if (node.getLeft() == null && node.getRight() == null) {
            return 1;
        }
        return 1 + Math.min(getMinDepth(node.getLeft()), getMinDepth(node.getRight()));
    }

    public double getAvgLeafDepth() {
        if (root == null) {
            return 0;
        }
        List<Animal> leaves = new ArrayList<>();
        AnimalTreeNode currentNode = root;
        Deque<AnimalTreeNode> stack = new ArrayDeque<>();
        do {
            if (currentNode.getClass() == Animal.class) {
                leaves.add((Animal) currentNode);
            }
            if (currentNode.getRight() != null) {
                stack.push(currentNode.getRight());
            }
            if (currentNode.getLeft() != null) {
                stack.push(currentNode.getLeft());
            }
            if (!stack.isEmpty()) {
                currentNode = stack.pop();
            } else {
                break;
            }
        } while (true);
        return leaves.stream().mapToDouble(this::getDepth).average().orElse(0.0);
    }
    private void getLeaves(AnimalTreeNode currentNode, List<Animal> leaves) {
        if (currentNode == null) {
            return;
        }
        if (currentNode.getLeft() == null && currentNode.getRight() == null && currentNode.getClass() == Animal.class) {
            leaves.add((Animal) currentNode);
        }
        getLeaves(currentNode.getLeft(), leaves);
        getLeaves(currentNode.getRight(), leaves);
    }

    private int getDepth(AnimalTreeNode node) {
        if (node == null && root == null) {
            return 0;
        }
        AnimalTreeNode currentNode = node;
        int depth = 1;
        while (currentNode != root) {
            currentNode = getParent(currentNode);
            depth++;
        }
        return depth;
    }

    Animal findAnimal(String animalName) {
        if (root == null) {
            return null;
        }
        Deque<AnimalTreeNode> stack = new ArrayDeque<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            AnimalTreeNode currentNode = stack.pop();
            if (currentNode.getClass() == Animal.class) {
                if(((Animal) currentNode).name.equals(animalName)) {
                    return (Animal) currentNode;
                }
            } else {
                if (currentNode.getLeft() != null) {
                    stack.push(currentNode.getLeft());
                }
                if (currentNode.getRight() != null) {
                    stack.push(currentNode.getRight());
                }
            }
        }
        return null;
    }

    public void printTree() {
        System.out.println();
        final String rootSymbol = "└";
        final String leftSymbol = "├";
        final String rightSymbol = "└";
        final String depthSymbol = "│";
        Deque<AnimalTreeNode> treeNodeQueue = new ArrayDeque<>();
        if (root == null) {
            return;
        }
        treeNodeQueue.push(root);
        StringBuilder normalNodePrefix = new StringBuilder();
        while (!treeNodeQueue.isEmpty()) {
            AnimalTreeNode currentNode = treeNodeQueue.pop();
            System.out.print(" ");
            String data = currentNode.getClass() == Animal.class ?
                    ((Animal) currentNode).name : Main.generateInterrogator(((Fact) currentNode).predicate) + " " + ((Fact) currentNode).getFactPhrase() + "?";
            if (currentNode == root) {
                System.out.println(rootSymbol + " " + data);
            } else {
                System.out.print(" " + normalNodePrefix);
                if (isLeft(currentNode)) {
                    System.out.println(leftSymbol + " " + data);
                    if (currentNode.getLeft() != null || currentNode.getRight() != null) {
                        normalNodePrefix.append(depthSymbol);
                    }
                }
                if (isRight(currentNode)) {
                    System.out.println(rightSymbol + " " + data);
                    if (currentNode.getLeft() != null || currentNode.getRight() != null) {
                        normalNodePrefix.append(" ");
                    } else {
                        if (normalNodePrefix.length() > 0) {
                            normalNodePrefix.deleteCharAt(normalNodePrefix.length() - 1);
                        }
                    }
                }
            }
            if (currentNode.getRight() != null) {
                treeNodeQueue.push(currentNode.getRight());
            }
            if (currentNode.getLeft() != null) {
                treeNodeQueue.push(currentNode.getLeft());
            }
        }
    }
    private int getDepthFromRoot(AnimalTreeNode node) {
        if (node == null) {
            return 0;
        }
        if (node == root) {
            return 1;
        }
        return 1 + getDepthFromRoot(getParent(node));
    }

    public AnimalTreeNode getParent(AnimalTreeNode node) {
        if (root == null) {
            return null;
        }
        Deque<AnimalTreeNode> treeStack = new ArrayDeque<>();
        treeStack.push(root);
        while (!treeStack.isEmpty()) {
            AnimalTreeNode currentNode = treeStack.pop();
            if (currentNode.getLeft() == node || currentNode.getRight() == node) {
                return currentNode;
            }
            if (currentNode.getLeft() != null) {
                treeStack.push(currentNode.getLeft());
            }
            if (currentNode.getRight() != null) {
                treeStack.push(currentNode.getRight());
            }
        }
        return null;
    }

    public boolean isLeft(AnimalTreeNode node) {
        AnimalTreeNode parent = getParent(node);
        if (parent == null) {
            return false;
        }
        return parent.getLeft() == node;
    }

    public boolean isRight(AnimalTreeNode node) {
        AnimalTreeNode parent = getParent(node);
        if (parent == null) {
            return false;
        }
        return parent.getRight() == node;
    }
}
