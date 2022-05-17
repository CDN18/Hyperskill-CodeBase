class Pizza {

    //Fix this method
    public static void cookVeganPizza() throws InterruptedException {
        Base base = new Base();
        Tomatoes tomatoes = new Tomatoes();
        Tofu tofu = new Tofu();
        Bake bake = new Bake();
        java.util.List<Thread> stepOfCook = new java.util.ArrayList<>();
        stepOfCook.add(base);
        stepOfCook.add(tomatoes);
        stepOfCook.add(tofu);
        stepOfCook.add(bake);
        for (Thread step : stepOfCook) {
            step.start();
            step.join();
        }
    }

    //Don't change the code below
    static class Base extends Thread {
        @Override
        public void run() {
            System.out.println("cook base");
        }
    }

    static class Tomatoes extends Thread {
        @Override
        public void run() {
            for (int i = 3; i >= 1; i--) {
                System.out.println("slice tomatoes " + i);
            }
        }
    }

    static class Tofu extends Thread {
        @Override
        public void run() {
            System.out.println("fry tofu");
        }
    }

    static class Bake extends Thread {
        @Override
        public void run() {
            for (int i = 4; i >= 0; i--) {
                if (i == 0) {
                    System.out.println("Your pizza is ready!");
                    break;
                }
                System.out.println("to bake..." + i + " min");

            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        cookVeganPizza();
    }
}