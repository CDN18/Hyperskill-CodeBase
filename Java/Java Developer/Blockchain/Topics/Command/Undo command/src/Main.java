interface Movable {
    int getX();
    int getY();
    void setX(int newX);
    void setY(int newY);
}

interface Storable {
    int getInventoryLength();
    String getInventoryItem(int index);
    void setInventoryItem(int index, String item);
}

interface Command {
    void execute();
    void undo();
}

class CommandMove implements Command {
    Movable entity;
    int xMovement;
    int yMovement;
    int x;
    int y;

    public CommandMove() {
    }

    @Override
    public void execute() {
        x = entity.getX();
        y = entity.getY();
        entity.setX(entity.getX() + xMovement);
        entity.setY(entity.getY() + yMovement);
    }

    @Override
    public void undo() {
        if (x != entity.getX() || y != entity.getY()) {
            entity.setX(x);
            entity.setY(y);
        }
    }
}

class CommandPutItem implements Command {
    Storable entity;
    String item;
    String previousItem;
    int index = -1;

    public CommandPutItem() {
    }

    @Override
    public void execute() {
        for (int i = 0; i < entity.getInventoryLength(); i++) {
            if (entity.getInventoryItem(i) == null) {
                index = i;
                previousItem = entity.getInventoryItem(i);
                entity.setInventoryItem(i, item);
                break;
            }
        }
    }

    @Override
    public void undo() {
        if (index != -1) {
            entity.setInventoryItem(index, previousItem);
            previousItem = null;
        }
    }
}