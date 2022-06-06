package contacts;

import java.time.LocalDateTime;

public class Organization extends Contact{
    public String name;
    public String address;


    public Organization(String name, String address, String number) {
        this.name = name;
        this.address = address;
        super.number = number;
        super.timeCreated = LocalDateTime.now();
        super.timeLastEdit = timeCreated;
    }

    public String getAddress() {
        return address;
    }

    public void setName(String name) {
        this.name = name;
        super.timeLastEdit = LocalDateTime.now();
    }

    public void setAddress(String address) {
        this.address = address;
        super.timeLastEdit = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Organization name: " + name + "\n"
                + "Address: " + address + "\n"
                + "Number: " + ("".equals(number) ? "[no number]" : number)  + "\n"
                + "Time created: " + timeCreated + "\n"
                + "Time last edit: " + timeLastEdit;
    }

    @Override
    public String getBaseInfo() {
        return name;
    }

}
