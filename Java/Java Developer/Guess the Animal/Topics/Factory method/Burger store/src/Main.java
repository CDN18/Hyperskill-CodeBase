class TestDrive {
    public static void main(String[] args) throws InterruptedException {
        /* write your code here */
        BurgerStore burgerStore = new BurgerStore();
        burgerStore.orderBurger("Chinese");
        burgerStore.orderBurger("American");
        burgerStore.orderBurger("Russian");
    }
}

abstract class BurgerFactory {

    abstract Burger createBurger(String type);

    Burger orderBurger(String type) throws InterruptedException {
        Burger burger = createBurger(type);
        if (burger == null) {
            System.out.println("Sorry, we are not able to create this kind of burger\n");
            return null;
        }
        System.out.println("Making a " + burger.getName());
        burger.putBun();/* write your code here */
        burger.putCutlet();/* write your code here */
        burger.putSauce();/* write your code here */
        Thread.sleep(1500L);
        System.out.println("Done a " + burger.getName() + "\n");
        return burger;
    }
}

class BurgerStore extends BurgerFactory {
    @Override
    Burger createBurger(String type) {
        switch (type) {
            case "Chinese":
                /* write your code here */
                return new ChineseBurger();
            case "American":
                /* write your code here */
                return new AmericanBurger();
            case "Russian":
                /* write your code here */
                return new RussianBurger();
            default:
                return null;
        }
    }
}

abstract class Burger {
    private String name;

    Burger(String name) {
        this.name = name;
    }

    String getName() {
        return name;
    }

    void putBun() {
        System.out.println("Putting bun");
    }

    void putCutlet() {
        System.out.println("Putting cutlet");
    }

    void putSauce() {
        System.out.println("Putting sauce");
    }

}

class ChineseBurger extends Burger {
    /* write your code here */
    ChineseBurger() {
        super("Chinese Burger");
    }
}

class AmericanBurger extends Burger {
    /* write your code here */
    AmericanBurger() {
        super("American Burger");
    }
}

class RussianBurger extends Burger {
    /* write your code here */
    RussianBurger() {
        super("Russian Burger");
    }
}