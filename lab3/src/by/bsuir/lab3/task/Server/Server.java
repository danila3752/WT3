package by.bsuir.lab3.task.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class Server {

    public static final int PORT = 4044;
    private static List<ServerController> serverThreads = new LinkedList<>();


    public static void main(String[] args) throws IOException {
        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("Server Started");
            while (true) {
                Socket socket = server.accept();
                System.out.println("Client connected");
                try {
                    serverThreads.add(new ServerController(socket));
                } catch (IOException e) {
                    socket.close();
                }
            }
        } finally {
            System.out.println("Server Closed");
        }
    }
}
