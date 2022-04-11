
class Person {
    String name;
    char sex; // 0 - Female, 1 - Male, X - Unknown
    int age;


    public Person(String name, char sex, int age) {
        this.name = name;
        this.sex = sex;
        if (sex != '0' && sex != '1') {
            this.sex = 'X';
        }
        this.age = age;
    }

    public String getName() {
        return this.name;
    }

    public char getSex() {
        return this.sex;
    }

    public int getAge() {
        return this.age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSex(char sex) {
        this.sex = sex;
    }

    public void setSex(String readableSex) {
        if ("Male".equals(readableSex) || "male".equals(readableSex)) {
            this.sex = '1';
        } else if ("Female".equals(readableSex) || "female".equals(readableSex)) {
            this.sex = '0';
        } else {
            this.sex = 'X';
        }
    }

    public void setAge(int age) {
        this.age = age;
    }

}

class Employee extends Person {
    String id;
    String department;
    double salary;

    public Employee(String name, char sex,
                    int age, String id, String department, double salary) {
        super(name, sex, age);
        this.id = id;
        this.department = department;
        this.salary = salary;
    }

    public String getId() {
        return this.id;
    }

    public String getDepartment() {
        return this.department;
    }

    public double getSalary() {
        return this.salary;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
}

class Doctor extends Employee {
    int workingYrs;
    String specialty;

    public Doctor(String name, char sex, int age,
                  String id, String department, double salary,
                  int workingYrs, String specialty) {
        super(name, sex, age, id, department, salary);
        this.workingYrs = workingYrs;
        this.specialty = specialty;
    }

    public int getWorkingYrs() {
        return workingYrs;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setWorkingYrs(int workingYrs) {
        this.workingYrs = workingYrs;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }
}

class Patient extends Person {
    String id;
    boolean hospitalized;
    String description;

    public Patient(String name, char sex, int age,
                   String id, boolean hospitalized, String description) {
        super(name, sex, age);
        this.id = id;
        this.hospitalized = hospitalized;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public boolean isHospitalized() {
        return hospitalized;
    }

    public String getDescription() {
        return description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setHospitalized(boolean hospitalized) {
        this.hospitalized = hospitalized;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}