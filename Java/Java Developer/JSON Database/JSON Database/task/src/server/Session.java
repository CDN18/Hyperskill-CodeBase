package server;

import common.DefaultResponse;
import common.Request;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import static common.MessageUtil.*;
import static server.Main.gson;

public class Session implements Runnable{
    private final Socket socket;

    public Session(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try(
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output  = new DataOutputStream(socket.getOutputStream())
        ) {
            Request request = gson.fromJson(ReceiveMsg(input), Request.class);
            switch (request.type) {
                case "get":
                    Main.get(request.key, output);
                    break;
                case "set":
                    Main.set(request.key, request.value, output);
                    break;
                case "delete":
                    Main.delete(request.key, output);
                    break;
                case "exit":
                    SendDefault(output, DefaultResponse.OK);
                    Main.shutdown();
                    break;
                default:
                    SendMsg(output, "ERROR");
                    break;
            }
        } catch (Exception e) {
            System.err.println("[Session ERR] " + e.getMessage() + " ");
            e.printStackTrace();
        }
    }
}
