package contacts;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class PhoneBook implements Serializable {
    private static final long serialVersionUID = 1L;
    static Scanner scanner = new Scanner(System.in);
    ArrayList<Contact> contacts;

    public PhoneBook() {
        contacts = new ArrayList<>();
    }

    public void add(Contact contact) {
        contacts.add(contact);
    }

    public void addPerson() {
        System.out.print("Enter the name: ");
        String name = scanner.nextLine();
        System.out.print("Enter the surname: ");
        String surname = scanner.nextLine();
        System.out.print("Enter the birth date: ");
        LocalDate birthday;
        try {
            birthday = LocalDate.parse(scanner.nextLine());
        } catch (DateTimeParseException e) {
            System.out.println("Bad birth date!");
            birthday = null;
        }
        System.out.print("Enter the gender (M, F): ");
        Gender gender;
        switch (scanner.nextLine()) {
            case "M":
                gender = Gender.Male;
                break;
            case "F":
                gender = Gender.Female;
                break;
            default:
                gender = Gender.Unknown;
                System.out.println("Bad gender!");
        }
        System.out.print("Enter the number: ");
        String number = scanner.nextLine();
        if (!numberIsValid(number)) {
            System.out.println("Wrong number format!");
            number = "";
        }
        add(new Person(name, surname, number, birthday, gender));
    }

    public void addOrganization() {
        System.out.print("Enter the organization name: ");
        String name = scanner.nextLine();
        System.out.print("Enter the address: ");
        String address = scanner.nextLine();
        System.out.print("Enter the number: ");
        String number = scanner.nextLine();
        if (!numberIsValid(number)) {
            System.out.println("Wrong number format!");
            number = "";
        }
        add(new Organization(name, address, number));
    }

    public Contact get(int index) {
        return contacts.get(index);
    }

    public Contact get(String name) {
        for (Contact contact : contacts) {
            if (contact.getBaseInfo().equals(name)) {
                return contact;
            }
        }
        return null;
    }

    public void remove(int index) {
        contacts.remove(index);
    }

    public void remove(Contact contact) {
        contacts.remove(contact);
    }

    public int size() {
        return contacts.size();
    }

    public void info() {
        for (int i = 0; i < contacts.size(); i++) {
            System.out.println(i + 1 + ". " + contacts.get(i).getBaseInfo());
        }
        System.out.print("Enter index to show info: ");
        int index = Integer.parseInt(scanner.nextLine()) - 1;
        if (index < 0 || index >= contacts.size()) {
            System.out.println("Wrong index!");
            return;
        }
        System.out.println(contacts.get(index));
    }

    public void list() {
        for (int i = 0; i < contacts.size(); i++) {
            System.out.println(i + 1 + ". " + contacts.get(i).getBaseInfo());
        }
    }

    public void edit(int index) {
        Contact contact = contacts.get(index);
        edit(contact);
    }

    public void edit(Contact contact) {
        if (contact.getClass() == Person.class) {
            editPerson((Person) contact);
        } else {
            editOrganization((Organization) contact);
        }
    }

    private void editPerson(Person person) {
        System.out.print("Select a field (name, surname, birth, gender, number): ");
        switch (scanner.nextLine()) {
            case "name":
                System.out.print("Enter name: ");
                person.setName(scanner.nextLine());
                break;
            case "surname":
                System.out.print("Enter surname: ");
                person.setSurname(scanner.nextLine());
                break;
            case "birth":
                System.out.print("Enter birth date: ");
                LocalDate birthday;
                try {
                    birthday = LocalDate.parse(scanner.nextLine());
                } catch (DateTimeParseException e) {
                    System.out.println("Bad birth date!");
                    birthday = null;
                }
                person.setBirthday(birthday);
                break;
            case "gender":
                System.out.print("Enter gender: ");
                Gender gender;
                switch (scanner.nextLine()) {
                    case "M":
                        gender = Gender.Male;
                        break;
                    case "F":
                        gender = Gender.Female;
                        break;
                    default:
                        gender = Gender.Unknown;
                        System.out.println("Bad gender!");
                }
                person.setGender(gender);
                break;
            case "number":
                System.out.print("Enter number: ");
                String newNumber = scanner.nextLine();
                if (!numberIsValid(newNumber)) {
                    System.out.println("Wrong number format!");
                    newNumber = "";
                    person.setNumber(newNumber);
                    break;
                }
                person.setNumber(newNumber);
                break;
            default:
                System.out.println("Invalid field!");
                break;
        }
    }

    public void editOrganization(Organization organization) {
        System.out.print("Select a field (name, address, number): ");
        switch (scanner.nextLine()) {
            case "name":
                System.out.print("Enter name: ");
                organization.setName(scanner.nextLine());
                break;
            case "address":
                System.out.print("Enter address: ");
                organization.setAddress(scanner.nextLine());
                break;
            case "number":
                System.out.print("Enter number: ");
                String newNumber = scanner.nextLine();
                if (!numberIsValid(newNumber)) {
                    System.out.println("Wrong number format!");
                    newNumber = "";
                    organization.setNumber(newNumber);
                    break;
                }
                organization.setNumber(newNumber);
                break;
            default:
                System.out.println("Invalid field!");
                break;
        }
    }

    private static boolean numberIsValid(String number) {
        boolean noMoreThanOneParentheses = number.indexOf('(') == number.lastIndexOf('(')
                && number.indexOf(')') == number.lastIndexOf(')');
        boolean parenthesesValidation = !number.contains("(")
                || (number.indexOf('(') < number.indexOf(')') && number.matches(".*\\([\\dA-Za-z]+\\).*"));
        return noMoreThanOneParentheses && parenthesesValidation
                && number.matches("\\+?\\(?[\\dA-Za-z]+\\)?([\\s-]\\(?[\\dA-Za-z]{2,}\\)?)*");
    }

}
