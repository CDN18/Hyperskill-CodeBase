package server;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static server.Config.*;

public class Main {
    // private static final long serialVersionUID = 1L;
    volatile static int fileIDCounter = 10;
    static ExecutorService sessions = Executors.newCachedThreadPool();
    static ExecutorService listener = Executors.newSingleThreadExecutor();
    static Map<Integer, String> idMap;
    static Map<Integer, Integer> lockStatus = new java.util.concurrent.ConcurrentHashMap<>(); // 0 - get lock, 1 - put/del lock
    public static boolean isRunning = true;

    public static void main(String[] args) {
        if (Files.exists(new File(metaData).toPath())) {
            try (FileInputStream fis = new FileInputStream(metaData);
                 BufferedInputStream bis = new BufferedInputStream(fis);
                 ObjectInputStream ois = new ObjectInputStream(bis)) {
                idMap = (Map<Integer, String>) ois.readObject();
                System.out.println("[Server DEBUG] idMap loaded, SIZE: " + idMap.size());
                System.out.println("[Server DEBUG] idMap: " + idMap);
                // Files.deleteIfExists(new File(metaData).toPath());
            } catch (Exception e) {
                idMap = new java.util.concurrent.ConcurrentHashMap<>();
            }
        }
        try (ServerSocket server = new ServerSocket(port, 100, InetAddress.getByName(address))) {
            System.out.println("DEBUG: Working directory: " + System.getProperty("user.dir"));
            System.out.println("Server started!");
            File dir = new File(dataPath);
            if (!dir.exists()) {
                dir.mkdir();
            }
            listener.submit(new Listener(server, sessions));
            while (isRunning) {
                Thread.sleep(500);
            }
            listener.shutdownNow();
            sessions.shutdownNow();
        } catch (UnknownHostException unknownHostException) {
            System.err.println("[Server ERR] Unknown host: " + address);
        } catch (IOException ioException) {
            System.err.println("[Server ERR] I/O error on main.main(): " + ioException);
        } catch (InterruptedException e) {
            System.err.println("[Server ERR] InterruptedException on main.main(): " + e);
        }
    }

    synchronized public static int generateFileID() {
        return ++fileIDCounter;
    }

    // File is locked automatically after adding it to the list
    synchronized public static void addFile(int id, String fileName) {
        idMap.put(id, fileName);
        lockFile(id, 1);
    }

    public static void shutdown() {
        System.out.println("[Server] Exiting...");
        // Save idMap
        try (FileOutputStream fos = new FileOutputStream(metaData);
             BufferedOutputStream bos = new BufferedOutputStream(fos);
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(idMap);
        } catch (FileNotFoundException e) {
            System.err.println("[Server ERR] FileNotFoundException on shutdown.saveData(): " + e);
        } catch (IOException e) {
            System.err.println("[Server ERR] IOException on shutdown.saveData(): " + e);
        }
        // Shutdown
        sessions.shutdown();
        /* Disabled because the test only gives 0.5s to shut down
        try {
            if (!sessions.awaitTermination(1, TimeUnit.SECONDS)) {
                System.err.println("[Server ERR] Shutdown timed out, exiting uncleanly...");
            }
        } catch (InterruptedException interruptedException) {
            System.err.println("[Server ERR] Shutdown interrupted: " + interruptedException);

        }
         */
        isRunning = false;
        // System.exit(0); // The test does not support System.exit()
    }

    synchronized public static void lockFile(Integer fileID, Integer lockType) {
        // 0 - get lock, 1 - put/del lock
        lockStatus.put(fileID, lockType);
    }

    synchronized public static void unlockFile(Integer fileID) {
        lockStatus.remove(fileID);
    }

    public static boolean isLocked(Integer fileID) {
        return lockStatus.containsKey(fileID);
    }

    public static int getID(String fileName) {
        for (Map.Entry<Integer, String> entry : idMap.entrySet()) {
            if (entry.getValue().equals(fileName)) {
                return entry.getKey();
            }
        }
        return -1;
    }
}