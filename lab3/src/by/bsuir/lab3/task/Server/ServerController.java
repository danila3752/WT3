package by.bsuir.lab3.task.Server;

import by.bsuir.lab3.task.Server.Student.Student;
import by.bsuir.lab3.task.Server.Student.StudentSerialize;

import java.io.*;
import java.net.Socket;

public class ServerController extends Thread{

    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private static StudentSerialize studentSerialize = new StudentSerialize();

    public ServerController(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        start();
    }

    @Override
    public void run() {
        String inputData;
        try {
            while (true) {
                inputData = in.readLine();
                if(inputData.equals("disconnect")) {
                    this.downService();
                    break;
                }
                String name;
                String lastname;
                String patronymic;
                int age = 0;
                int group = 0;
                switch (inputData.split("&")[0]) {
                    case "view":
                        name = inputData.split("&")[1];
                        lastname = inputData.split("&")[2];
                        String message = studentSerialize.readStudentCase(name, lastname);
                        if (message.length() == 0){
                            send("student not found");
                        } else{
                            send(message);
                        }
                        break;

                    case "viewAll":
                        send(studentSerialize.readAllStudentCases());
                        break;

                    case "edit":
                        String oldName = inputData.split("&")[1];
                        String oldLastname = inputData.split("&")[2];
                        String oldPatronymic = inputData.split("&")[3];
                        Student oldStudent = new Student(oldName, oldLastname, oldPatronymic, 0,0);
                        name = inputData.split("&")[4];
                        lastname = inputData.split("&")[5];
                        patronymic = inputData.split("&")[6];
                        try {
                            age = Integer.parseInt(inputData.split("&")[7]);
                        } catch (Exception ignore) {
                        }
                        try {
                            group = Integer.parseInt(inputData.split("&")[8]);
                        } catch (Exception ignore) {
                        }
                        Student newStudent = new Student(name, lastname, patronymic, age, group);
                        try {
                            boolean isStudentFind = studentSerialize.editStudent(oldStudent, newStudent);
                            if (isStudentFind) {
                                send("student edited");
                            } else {
                                send("student not found");
                            }
                        } catch (Exception e) {
                            send("Edit error");
                        }
                        break;
                    case "create":
                        name = inputData.split("&")[1];
                        lastname = inputData.split("&")[2];
                        patronymic = inputData.split("&")[3];
                        try {
                            age = Integer.parseInt(inputData.split("&")[4]);
                        } catch (Exception ignore) {
                        }
                        try {
                            group = Integer.parseInt(inputData.split("&")[5]);
                        } catch (Exception ignore) {
                        }
                        Student student = new Student(name, lastname, patronymic, age, group);
                        try {
                            studentSerialize.addNewStudent(student);
                            send("student added");
                        } catch (Exception e) {
                            send("Add error");
                        }
                        break;
                    default:
                        continue;
                }
            }

        } catch (IOException ignored) {
        }

    }

    private void send(String msg) {
        try {
            out.write(msg + "\n");
            out.flush();
        } catch (IOException ignored) {}

    }

    private void downService() {
        try {
            if(!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();

            }
        } catch (IOException ignored) {}
    }
}
