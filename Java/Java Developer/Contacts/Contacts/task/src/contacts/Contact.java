package contacts;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

public class Contact {
    public String number;
    LocalDateTime timeCreated;
    LocalDateTime timeLastEdit;


    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public boolean isPerson() {
        return this.getClass().equals(Person.class);
    }

    public boolean isOrganization() {
        return this.getClass().equals(Organization.class);
    }

    public String getBaseInfo() {
        // Base Method, override in Person and Organization
        return "";
    }

    public boolean matches(String query) {
        Field[] fields = this.getClass().getFields();
        try {
            for (Field field : fields) {
                // System.out.println("[DEBUG] " + field.getName() + ": " + field.get(this));
                if (field.get(this) == null) {
                    continue;
                }
                if (field.get(this).toString().toLowerCase().contains(query.toLowerCase()) || field.getName().matches(query)) {
                    return true;
                }
            }
        } catch (IllegalAccessException e) {
            System.err.println("Error in searching fields: " + e.getMessage());
            return false;
        }
        return false;
    }
}
