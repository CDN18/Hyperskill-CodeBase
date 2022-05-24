package server;

import common.*;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Objects;

import static server.Config.*;

public class Session implements Runnable {
    Socket socket;

    public Session(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (InputStream input = socket.getInputStream();
             OutputStream output  = socket.getOutputStream()) {
            Request request = Utils.receiveReq(input);
            Response response = new Response();
            switch (request.operation) {
                case PUT:
                    response.code = createFile(request, response) ? 200 : 403;
                    break;
                case GET:
                    response.code = getFile(request, response) ? 200 : 404;
                    break;
                case DEL:
                    response.code = deleteFile(request, response) ? 200 : 404;
                    break;
                case EXIT:
                    Main.shutdown();
                    return;
                default:
            }
            Utils.sendResp(output, response);
        } catch (IOException ioException) {
            System.err.println("[Server ERR] I/O error on Session.run(): " + ioException);
        } catch (ClassNotFoundException classNotFoundException) {
            System.err.println("[Server ERR] ClassNotFoundException on Session.run(): " + classNotFoundException);
        }
    }

    private static boolean createFile(Request request, Response response) {
        response.fileID = Main.generateFileID();
        String fileName = Objects.equals(request.fileName, "") ? String.valueOf(response.fileID) : request.fileName;
        File file = new File(dataPath + fileName);
        if (file.exists()) {
            return false;
        }
        try {
            Main.addFile(response.fileID, fileName);
            Files.write(file.toPath(), request.fileContent);
        } catch (IOException ioException) {
            System.err.println("[Server ERR] I/O error on createFile(): " + ioException);
            return false;
        }
        Main.unlockFile(response.fileID);
        return true;
    }

    private static boolean getFile(Request request, Response response) {
        String fileName = request.by == ParamType.BY_NAME ? request.fileName : Main.idMap.get(request.fileID);
        if (fileName == null) {
            System.err.println("[Server ERR] Failed to get file by ID: " + request.fileID);
            System.err.println("[Server] IDMap Info: Keys: " + Arrays.toString(Main.idMap.keySet().toArray()) + " Values: " + Arrays.toString(Main.idMap.values().toArray()));
        }
        // System.out.println("[DEBUG] Data path: " + dataPath);
        File file = new File(dataPath + fileName);
        if (!file.exists()) {
            return false;
        }
        try {
            int fileID = request.by == ParamType.BY_NAME ? Main.getID(fileName) : request.fileID;
            while (Main.isLocked(fileID)) {
                Thread.sleep(100);
            }
            Main.lockFile(fileID, 0);
            response.rawData = Files.readAllBytes(file.toPath());
            Main.unlockFile(fileID);
        } catch (IOException ioException) {
            System.err.println("[Server ERR] I/O error on getFile(): " + ioException);
            return false;
        } catch (InterruptedException e) {
            System.err.println("[Server ERR] InterruptedException on getFile(): " + e);
        }
        return true;
    }

    private static boolean deleteFile(Request request, Response response) {
        String fileName = request.by == ParamType.BY_NAME ? request.fileName : Main.idMap.get(request.fileID);
        switch (request.by) {
            case BY_NAME:
                response.fileName = fileName;
                break;
            case BY_ID:
                response.fileID = request.fileID;
                break;
        }
        File file = new File(dataPath + fileName);
        int fileID = request.by == ParamType.BY_NAME ? Main.getID(fileName) : request.fileID;
        try {
            while (Main.isLocked(fileID)) {
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            System.err.println("[Server ERR] InterruptedException on deleteFile(): " + e);
        }
        Main.lockFile(fileID, 1);
        if (!file.exists()) {
            return false;
        }
        boolean result = file.delete();
        Main.unlockFile(fileID);
        return result;
    }
}