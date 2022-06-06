package contacts;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Person extends Contact{
    public String name;
    public String surname;
    public LocalDate birthday;
    public Gender gender;

    public Person(String name, String surname, String number, LocalDate birthday, Gender gender) {
        this.name = name;
        this.surname = surname;
        super.number = number;
        this.birthday = birthday;
        this.gender = gender;
        super.timeCreated = LocalDateTime.now();
        super.timeLastEdit = timeCreated;
    }

    public String getSurname() {
        return surname;
    }

    public Gender getGender() {
        return gender;
    }

    public void setName(String name) {
        this.name = name;
        super.timeLastEdit = LocalDateTime.now();
    }

    public void setSurname(String surname) {
        this.surname = surname;
        super.timeLastEdit = LocalDateTime.now();
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
        super.timeLastEdit = LocalDateTime.now();
    }

    public void setGender(Gender gender) {
        this.gender = gender;
        super.timeLastEdit = LocalDateTime.now();
    }

    public void setGender(String gender) {
        switch (gender) {
            case "M":
                setGender(Gender.Male);
                break;
            case "F":
                setGender(Gender.Female);
                break;
            default:
                System.out.println("Bad Gender!");
                break;
        }
    }

    @Override
    public String toString() {
        return "Name: " + name + "\n"
                + "Surname: " + surname + "\n"
                + "Birth date: " + (birthday == null ? "[no data]" : birthday) + "\n"
                + "Gender: " + (gender.equals(Gender.Unknown) ? "[no data]" : gender) + "\n"
                + "Number: " + ("".equals(number) ? "[no number]" : number) + "\n"
                + "Time created: " + timeCreated + "\n"
                + "Time last edit: " + timeLastEdit;
    }

    @Override
    public String getBaseInfo() {
        return name + " " + surname;
    }
}

enum Gender{
    Male, Female, Unknown
}
