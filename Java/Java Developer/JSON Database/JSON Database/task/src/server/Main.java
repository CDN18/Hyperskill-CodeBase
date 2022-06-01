package server;

import com.google.gson.*;
import common.DefaultResponse;
import common.Response;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static common.MessageUtil.SendMsg;
import static common.MessageUtil.SendDefault;
import static server.Config.*;

public class Main {
    static File dataFile = new File(DATA_PATH);
    static JsonObject storage;
    static ExecutorService listener;
    static ExecutorService sessions;
    static Gson gson = new GsonBuilder().create();
    static boolean isRunning;
    static ReadWriteLock lock = new ReentrantReadWriteLock();
    static Lock readLock = lock.readLock();
    static Lock writeLock = lock.writeLock();

    public static void main(String[] args) {
        System.out.println("[Server] Current directory: " + System.getProperty("user.dir"));
        if (dataFile.exists()) {
            try {
                readLock.lock();
                String content = Files.readString(dataFile.toPath());
                readLock.unlock();
                storage = gson.toJsonTree(content).getAsJsonObject();
            } catch (Exception e) {
                storage = new JsonObject();
            }
        } else {
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                System.err.println("[Server ERR] IOException on creating datafile: " + e.getMessage());
            }
            storage = new JsonObject();
        }

        try(ServerSocket serverSocket = new ServerSocket(PORT, 100, InetAddress.getByName(ADDRESS))) {
            sessions = Executors.newCachedThreadPool();
            listener = Executors.newSingleThreadExecutor();
            listener.submit(new Listener(serverSocket, sessions));
            System.out.println("Server started!");
            isRunning = true;
            while (isRunning) {
                Thread.sleep(50);
            }
            listener.shutdownNow();
            sessions.shutdownNow();
        } catch (UnknownHostException e) {
            System.err.println("[Server ERR] Unknown host on main: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("[Server ERR] IOException on main: " + e.getMessage());
        } catch (InterruptedException e) {
            System.err.println("[Server ERR] InterruptedException on main: " + e.getMessage());
        }
    }

    synchronized static void get(JsonElement key, DataOutputStream output) {
        Response res = new Response();
        if (key.isJsonPrimitive()) {
            if (storage.has(key.getAsString())) {
                res.response = "OK";
                res.value = storage.get(key.getAsString());
            } else {
                SendDefault(output, DefaultResponse.NO_SUCH_KEY);
                return;
            }
        } else {
            JsonArray array = key.getAsJsonArray();
            JsonObject current = storage.getAsJsonObject(array.get(0).getAsString());
            for (int i = 1; i < array.size() - 1; i++) {
                if (current.has(array.get(i).getAsString())) {
                    current = current.getAsJsonObject(array.get(i).getAsString());
                } else {
                    SendDefault(output, DefaultResponse.NO_SUCH_KEY);
                    return;
                }
            }
            JsonElement result;
            if (array.size() == 1) {
                result = current;
            } else {
                result = current.get(array.get(array.size() - 1).getAsString());
            }
            if (result == null) {
                SendDefault(output, DefaultResponse.NO_SUCH_KEY);
                return;
            } else {
                res.response = "OK";
                res.value = result;
                // System.out.println("[DEBUG] value isJsonPrimitive: " + result.isJsonPrimitive() + ", toString: " + result);
            }
        }
        if (res.response.equals("OK")) {
            updateStorage();
        }
        SendMsg(output, gson.toJson(res));
    }

    synchronized static void set(JsonElement key, JsonElement value, DataOutputStream output) {
        if (key.isJsonPrimitive()) {
            storage.add(key.getAsString(), value);
        } else {
            JsonArray array = key.getAsJsonArray();
            JsonObject current = storage;
            for (int i = 0; i < array.size() - 1; i++) {
                if (!current.has(array.get(i).getAsString())) {
                    current.add(array.get(i).getAsString(), new JsonObject());
                }
                current = current.getAsJsonObject(array.get(i).getAsString());
            }
            current.add(array.get(array.size() - 1).getAsString(), value);
        }
        updateStorage();
        SendDefault(output, DefaultResponse.OK);
    }

    synchronized static void delete(JsonElement key, DataOutputStream output) {
        if (key.isJsonPrimitive()) {
            if (storage.has(key.getAsString())) {
                storage.remove(key.getAsString());
                updateStorage();
                SendDefault(output, DefaultResponse.OK);
            } else {
                SendDefault(output, DefaultResponse.NO_SUCH_KEY);
            }
        } else {
            JsonArray array = key.getAsJsonArray();
            JsonObject current = storage;
            for (int i = 0; i < array.size() - 1; i++) {
                if (current.has(array.get(i).getAsString())) {
                    current = current.getAsJsonObject(array.get(i).getAsString());
                } else {
                    SendDefault(output, DefaultResponse.NO_SUCH_KEY);
                    return;
                }
            }
            if (current.has(array.get(array.size() - 1).getAsString())) {
                current.remove(array.get(array.size() - 1).getAsString());
                updateStorage();
                SendDefault(output, DefaultResponse.OK);
            } else {
                SendDefault(output, DefaultResponse.NO_SUCH_KEY);
            }
        }
    }

    private static void updateStorage() {
        try {
            writeLock.lock();
            Files.writeString(dataFile.toPath(), gson.toJson(storage));
            writeLock.unlock();
        } catch (IOException e) {
            System.err.println("[Server ERR] IOException on updateStorage: " + e.getMessage());
        }
    }

    static void shutdown() {
        listener.shutdown();
        sessions.shutdown();
        isRunning = false;
    }
}
