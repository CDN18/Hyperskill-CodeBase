class Person {
    String name;
    int age;
}

class MakingChanges {
    public static void changeIdentities(Person p1, Person p2) {
        // write your code here
        String tmpName = p1.name.toString();
        int tmgAge = p1.age;
        p1.name = p2.name.toString();
        p1.age = p2.age;
        p2.name = tmpName.toString();
        p2.age = tmgAge;
    }
}