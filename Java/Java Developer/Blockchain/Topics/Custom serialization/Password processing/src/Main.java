import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

class UserProfile implements Serializable {
    private static final long serialVersionUID = 26292552485L;

    private String login;
    private String email;
    private transient String password;

    public UserProfile(String login, String email, String password) {
        this.login = login;
        this.password = password;
        this.email = email;
    }

    // implement readObject and writeObject properly
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        String encryptedPassword = encrypt(password);
        out.writeObject(encryptedPassword);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        String encryptedPassword = (String) in.readObject();
        password = decrypt(encryptedPassword);
    }

    private static String encrypt(String string) {
        StringBuilder encrypted = new StringBuilder();
        for (char c : string.toCharArray()) {
            encrypted.append((char) (c + 1));
        }
        return encrypted.toString();
    }

    private static String decrypt(String string) {
        StringBuilder decrypted = new StringBuilder();
        for (char c : string.toCharArray()) {
            decrypted.append((char) (c - 1));
        }
        return decrypted.toString();
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
}