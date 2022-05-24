package common;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class SerializeTest {
    public static void serialize(Object obj) {
        try {
            FileOutputStream fos = new FileOutputStream("SerializeTest_" + obj.getClass());
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(obj);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
