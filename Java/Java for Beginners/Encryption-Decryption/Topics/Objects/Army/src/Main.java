import java.util.ArrayList;

class Army {

    public static void createArmy() {
        // Create all objects here
        int capacityUnit = 5;
        int capacityKnight = 3;
        int capacityGeneral = 1;
        int capacityDoctor = 1;
        ArrayList<Unit> units = new ArrayList<>();
        ArrayList<Knight> knights = new ArrayList<>();
        ArrayList<General> generals = new ArrayList<>();
        ArrayList<Doctor> doctors = new ArrayList<>();
        for (int i = 0; i < capacityUnit; i++) {
            units.add(new Unit("Unit " + i));
        }
        for (int i = 0; i < capacityKnight; i++) {
            knights.add(new Knight("Knight " + i));
        }
        for (int i = 0; i < capacityGeneral; i++) {
            generals.add(new General("General "+ i));
        }
        for (int i = 0; i < capacityDoctor; i++) {
            doctors.add(new Doctor("Doctor " + i));
        }
    }


    // Don't change the code below
    static class Unit {
        static String nameUnit;
        static int countUnit;

        public Unit(String name) {
            countUnit++;
            nameUnit = name;

        }
    }

    static class Knight {
        static String nameKnight;
        static int countKnight;

        public Knight(String name) {
            countKnight++;
            nameKnight = name;

        }
    }

    static class General {
        static String nameGeneral;
        static int countGeneral;

        public General(String name) {
            countGeneral++;
            nameGeneral = name;

        }
    }

    static class Doctor {
        static String nameDoctor;
        static int countDoctor;

        public Doctor(String name) {
            countDoctor++;
            nameDoctor = name;

        }
    }

    public static void main(String[] args) {
        createArmy();
        System.out.println(Unit.countUnit);
        System.out.println(Knight.countKnight);
        System.out.println(General.countGeneral);
        System.out.println(Doctor.countDoctor);
    }

}