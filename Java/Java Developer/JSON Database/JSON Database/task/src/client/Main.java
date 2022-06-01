package client;

import com.beust.jcommander.JCommander;
import com.google.gson.*;
import common.Request;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;

import static client.Config.*;
import static common.MessageUtil.ReceiveMsg;
import static common.MessageUtil.SendMsg;

public class Main {
    static Gson gson = new GsonBuilder().registerTypeAdapter(Request.class, new RequestParser()).create();

    public static void main(String[] args) throws IOException {
        System.out.println("[Client] Current directory: " + System.getProperty("user.dir"));
        Args cmd = new Args();
        JCommander jc = new JCommander(cmd);
        jc.parse(args);
        Request request;
        if (cmd.input != null) {
            if (!cmd.input.contains("/")) {
                cmd.input = DATA_DIR + cmd.input;
            }
            System.out.println("Content :" + Files.readString(new File(cmd.input).toPath()));
            request = gson.fromJson(Files.readString(new File(cmd.input).toPath()), Request.class);
        } else {
            request = new Request(cmd.type, gson.toJsonTree(cmd.key), gson.toJsonTree(cmd.value));
        }
        try (
                Socket socket = new Socket(InetAddress.getByName(ADDRESS), PORT);
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())
            ) {
            System.out.println("Client started!");
            SendMsg(output, gson.toJson(request));
            ReceiveMsg(input);
        } catch (UnknownHostException e) {
            System.err.println("[Client ERR] Unknown host: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("[Client ERR] IOException: " + e.getMessage());
        }
    }
}

class RequestParser implements JsonDeserializer<Request> {

    @Override
    public Request deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Request request = new Request();
        JsonObject jsonObject = json.getAsJsonObject();
        request.type = jsonObject.get("type").getAsString();
        request.key = jsonObject.get("key");
        request.value = jsonObject.get("value");
        return request;
    }
}