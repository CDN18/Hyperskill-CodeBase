package common;

import java.io.*;

public class Utils {
    public static void sendReq(OutputStream output, Request req) throws IOException {
        // BufferedOutputStream bos = new BufferedOutputStream(output);
        ObjectOutputStream oos = new ObjectOutputStream(output);
        oos.writeObject(req);
        oos.flush();
        System.out.println("The request was sent.");
    }

    public static Request receiveReq(InputStream input) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(input);
        Request req = (Request) ois.readObject();
        return req;
    }

    public static void sendResp(OutputStream output, Response resp) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(output);
        oos.writeObject(resp);
        oos.flush();
    }

    public static Response receiveResp(InputStream input) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(input);
        Response resp = (Response) ois.readObject();
        return resp;
    }
}
