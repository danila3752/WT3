package by.bsuir.lab3.task.Client;

public class Client {
    public static String ipAddr = "localhost";
    public static int port = 4044;

    public static void main(String[] args) {
        new ClientController(ipAddr, port);
    }
}
