package by.bsuir.lab3.task.Server.Student;

public class Student {

    private String firstname;
    private String lastname;
    private String patronymic;
    private int age;
    private int group;

    public Student(String firstname, String lastname, String patronymic, int age, int group) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.patronymic = patronymic;
        this.age = age;
        this.group = group;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return "Student: " +
                "name='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", age=" + age +
                ", group='" + group + '\'';
    }
}
