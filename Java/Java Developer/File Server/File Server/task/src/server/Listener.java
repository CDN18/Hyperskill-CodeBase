package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

public class Listener implements Runnable {
    ServerSocket server;
    ExecutorService sessions;

    public Listener(ServerSocket server, ExecutorService sessions) {
        this.server = server;
        this.sessions = sessions;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket socket = server.accept();
                Session session = new Session(socket);
                sessions.submit(session);
            }
            catch (IOException ioException) {
                // System.err.println("[Server ERR] I/O error on main.running(): " + ioException);
                break;
            }
        }
    }
}
