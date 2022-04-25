package machine;

import java.util.Scanner;

public class CoffeeMachine {
    int water;
    int milk;
    int beans;
    int cups;
    int money;
    MachineState state;

    public CoffeeMachine(int water, int milk, int beans, int cups, int money) {
        this.water = water;
        this.milk = milk;
        this.beans = beans;
        this.cups = cups;
        this.money = money;
        state = MachineState.CHOOSE_ACTION;
    }

    public void PrintState() {
        System.out.println("The coffee machine has:");
        System.out.println(water + " ml of water");
        System.out.println(milk + " ml of milk");
        System.out.println(beans + " g of coffee beans");
        System.out.println(cups + " disposable cups");
        System.out.println("$" + money + " of money");
        System.out.println("");
    }

    public void BuyCoffee() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu:");
        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                if (water >= 250 && beans >= 16) {
                    water -= 250;
                    beans -= 16;
                    cups--;
                    money += 4;
                    System.out.println("I have enough resources, making you a coffee!");
                } else if (water < 250) {
                    System.out.println("Sorry, not enough water!");
                } else {
                    System.out.println("Sorry, not enough beans!");
                }
                break;
            case "2":
                if (water >= 350 && milk >= 75 && beans >= 20) {
                    water -= 350;
                    milk -= 75;
                    beans -= 20;
                    cups--;
                    money += 7;
                    System.out.println("I have enough resources, making you a coffee!");
                } else if (water < 350) {
                    System.out.println("Sorry, not enough water!");
                } else if (milk < 75) {
                    System.out.println("Sorry, not enough milk!");
                } else {
                    System.out.println("Sorry, not enough beans!");
                }
                break;
            case "3":
                if (water >= 200 && milk >= 100 && beans >= 12) {
                    water -= 200;
                    milk -= 100;
                    beans -= 12;
                    cups--;
                    money += 6;
                    System.out.println("I have enough resources, making you a coffee!");
                } else if (water < 200) {
                    System.out.println("Sorry, not enough water!");
                } else if (milk < 100) {
                    System.out.println("Sorry, not enough milk!");
                } else {
                    System.out.println("Sorry, not enough beans!");
                }
                break;
            case "back":
                break;
            default:
                System.out.println("Invalid choice!");
                break;
        }
    }

    public void Fill() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Write how many ml of water do you want to add:");
        water += scanner.nextInt();
        System.out.println("Write how many ml of milk do you want to add:");
        milk += scanner.nextInt();
        System.out.println("Write how many grams of coffee beans do you want to add:");
        beans += scanner.nextInt();
        System.out.println("Write how many disposable cups of coffee do you want to add:");
        cups += scanner.nextInt();
    }

    public void Take() {
        System.out.println("I gave you $" + money);
        money = 0;
    }

    public void Run() {
        switch (state) {
            case CHOOSE_ACTION:
                Scanner scanner = new Scanner(System.in);
                System.out.println("Write action (buy, fill, take, remaining, exit):");
                String choice = scanner.nextLine();
                switch (choice) {
                    case "buy":
                        state = MachineState.CHOOSE_COFFEE;
                        break;
                    case "fill":
                        state = MachineState.FILL_RESOURCES;
                        break;
                    case "take":
                        state = MachineState.TAKE_MONEY;
                        break;
                    case "remaining":
                        state = MachineState.REMAINING;
                        break;
                    case "exit":
                        state = MachineState.EXIT;
                        break;
                    default:
                        System.out.println("Invalid choice!");
                }
                break;
            case CHOOSE_COFFEE:
                BuyCoffee();
                state = MachineState.CHOOSE_ACTION;
                break;
            case FILL_RESOURCES:
                Fill();
                state = MachineState.CHOOSE_ACTION;
                break;
            case TAKE_MONEY:
                Take();
                state = MachineState.CHOOSE_ACTION;
                break;
            case REMAINING:
                PrintState();
                state = MachineState.CHOOSE_ACTION;
                break;
            case EXIT:
                break;
            default:
        }
    }

    public static void main(String[] args) {
        CoffeeMachine coffeeMachine = new CoffeeMachine(400, 540, 120, 9, 550);
        while (coffeeMachine.state != MachineState.EXIT) {
            coffeeMachine.Run();
        }
    }
}

enum MachineState {
    CHOOSE_ACTION(1),
    CHOOSE_COFFEE(2),
    FILL_RESOURCES(3),
    TAKE_MONEY(4),
    REMAINING(5),
    EXIT(-1);

    final int value;

    MachineState(int value) {
        this.value = value;
    }
}
