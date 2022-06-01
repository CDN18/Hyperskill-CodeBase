package common;

import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class MessageUtil {
    static Gson gson = new Gson();
    public static String ReceiveMsg(DataInputStream input) {
        String received = "";
        try {
            received = input.readUTF();
            System.out.println("Received: " + received);
        } catch (IOException e) {
            System.err.println("[Server ERR] IOException: " + e.getMessage());
        }
        return received;
    }

    public static void SendMsg(DataOutputStream output, String msg) {
        try {
            output.writeUTF(msg);
            System.out.println("Sent: " + msg);
        } catch (IOException e) {
            System.err.println("[Server ERR] IOException: " + e.getMessage());
        }
    }

    public static void SendDefault(DataOutputStream output, DefaultResponse response) {
        SendMsg(output, gson.toJson(response.response));
    }
}

