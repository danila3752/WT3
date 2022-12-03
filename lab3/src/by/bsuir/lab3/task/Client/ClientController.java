package by.bsuir.lab3.task.Client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientController {
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private BufferedReader inputUser;
    private String IP;
    private int port;

    public ClientController(String IP, int port) {
        this.IP = IP;
        this.port = port;
        try {
            this.socket = new Socket(IP, port);
        } catch (IOException e) {
            System.err.println("Socket failed");
        }
        try {
            inputUser = new BufferedReader(new InputStreamReader(System.in));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            new ClientController.ReadMsg().start();
            this.menu();
        } catch (IOException e) {
            this.downService();
        } catch (NullPointerException e){
            System.out.println("Start server please");
        }
    }

    private void menu() {
        Scanner scan = new Scanner(System.in);
        while(true) {
            System.out.println("""
                    Personal files of BSUIR students
                     Menu:
                    1) View personal file
                    2) Сreate a new personal file
                    3) Edit personal file
                    4) Exit""");
            System.out.print("Input number of the command: ");
            String command = scan.nextLine();
            switch (command) {
                case ("1") -> {
                    System.out.println("Input student's name: ");
                    String name1 = scan.nextLine();
                    System.out.println("Input student's lastname: ");
                    String lastname1 = scan.nextLine();
                    if ((name1.equals("") && lastname1.equals("")))
                        send("viewAll&");
                     else
                        send("view&" + name1 + "&" + lastname1);
                    return;
                }
                case ("2") -> {
                    System.out.println("Input student's name: ");
                    String name2 = scan.nextLine();
                    System.out.println("Input student's lastname: ");
                    String lastname2 = scan.nextLine();
                    System.out.println("Input student's patronymic: ");
                    String patronymic2 = scan.nextLine();
                    System.out.println("Input student's age: ");
                    String age2 = scan.nextLine();
                    System.out.println("Input student's group: ");
                    String group2 = scan.nextLine();
                    send("create&" + name2 + "&" + lastname2 + "&" + patronymic2 + "&" + age2 + "&" + group2);
                    return;
                }
                case ("3") -> {
                    System.out.println("Input student's name: ");
                    String name3 = scan.nextLine();
                    System.out.println("Input student's lastname: ");
                    String lastname3 = scan.nextLine();
                    System.out.println("Input student's patronymic: ");
                    String patronymic3 = scan.nextLine();
                    System.out.print("Input new student's name : ");
                    String newName = scan.nextLine();
                    System.out.print("Input new student's lastname : ");
                    String newLastname = scan.nextLine();
                    System.out.print("Input new student's patronymic : ");
                    String newPatronymic = scan.nextLine();
                    System.out.print("Input new student's age : ");
                    String newAge = scan.nextLine();
                    System.out.print("Input new number group : ");
                    String newGroup = scan.nextLine();
                    send("edit&" + name3 + "&" + lastname3 + "&" + patronymic3 + "&" + newName + "&" + newLastname + "&" + newPatronymic + "&" + newAge + "&" + newGroup);
                    return;
                }
                case ("4") -> {
                    send("disconnect");
                    ClientController.this.downService();
                    return;
                }
                default -> System.out.println("invalid command");
            }
        }
    }

    private void downService() {
        try {
            if (!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
            }
        } catch (IOException ignored) {}
    }

    private void send(String msg) {
        try {
            out.write(msg + "\n");
            out.flush();
        } catch (IOException ignored) {}
    }

    private class ReadMsg extends Thread {
        @Override
        public void run() {

            String word;
            try {
                while (true) {
                    word = in.readLine();
                    switch (word) {
                        case ("stop") : {
                            ClientController.this.downService();
                            break;
                        }
                        case ("view: error") : {
                            System.out.println(word);

                            break;
                        }
                        case ("view: ok") : {
                            System.out.println(word);
                            ClientController.this.menu();
                            break;
                        }
                        default : {
                            for (String str : word.split(";")) {
                                System.out.println(str);
                            }
                            ClientController.this.menu();
                        }
                    }
                }
            } catch (IOException e) {
                ClientController.this.downService();
            }
        }
    }

}
