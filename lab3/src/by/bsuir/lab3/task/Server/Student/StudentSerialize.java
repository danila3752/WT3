package by.bsuir.lab3.task.Server.Student;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StudentSerialize {

    private static final String PATH = "src\\by\\bsuir\\lab3\\task\\Server\\Archive\\students.xml";
    private static Document doc;

    private List<Student> getStudents() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        List<Student> studentsList = new ArrayList<>();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(PATH);
            NodeList elementList = doc.getElementsByTagName("student");
            for (int i = 0; i < elementList.getLength(); i++) {
                Node p = elementList.item(i);
                if (p.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) p;
                    NodeList nameList = element.getChildNodes();
                    String firstname = "";
                    String lastname = "";
                    String patronymic = "";
                    int age = 0;
                    int group = 0;
                    for (int j = 0; j < nameList.getLength(); j++) {
                        Node n = nameList.item(j);
                        if (n.getNodeType() == Node.ELEMENT_NODE) {
                            Element name = (Element) n;
                            try {
                                switch (name.getTagName()) {
                                    case ("name") -> firstname = name.getTextContent();
                                    case ("lastname") -> lastname = name.getTextContent();
                                    case ("patronymic") -> patronymic = name.getTextContent();
                                    case ("age") -> age = Integer.parseInt(name.getTextContent());
                                    case ("group") -> group = Integer.parseInt(name.getTextContent());
                                }
                            } catch (Exception ignore){}
                        }
                    }
                    Student student = new Student(firstname, lastname, patronymic, age, group);
                    studentsList.add(student);
                }
            }
            return studentsList;
        } catch (SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }
        return studentsList;
    }

    public String readStudentCase(String firstname, String lastname){
        String result = "";
        List<Student> students = getStudents();
        for (Student student : students){
            if (student.getFirstname().equals(firstname) || student.getLastname().equals(lastname)){
                result += student.toString();
            }
        }
        return result;
    }

    public String readAllStudentCases(){
        String result = "";
        List<Student> students = getStudents();
        for (Student student : students){
            result += student.toString() + ";";
        }
        return result;
    }

    public void addNewStudent(Student student) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(PATH);
        Element root = document.getDocumentElement();

        Element newStudent = document.createElement("student");

        Element firstname = document.createElement("name");
        firstname.appendChild(document.createTextNode(student.getFirstname()));
        newStudent.appendChild(firstname);

        Element lastname = document.createElement("lastname");
        lastname.appendChild(document.createTextNode(student.getLastname()));
        newStudent.appendChild(lastname);

        Element patronymic = document.createElement("patronymic");
        patronymic.appendChild(document.createTextNode(student.getPatronymic()));
        newStudent.appendChild(patronymic);

        Element age = document.createElement("age");
        age.appendChild(document.createTextNode(String.valueOf(student.getAge())));
        newStudent.appendChild(age);

        Element group = document.createElement("group");
        group.appendChild(document.createTextNode(String.valueOf(student.getGroup())));
        newStudent.appendChild(group);

        root.appendChild(newStudent);

        DOMSource source = new DOMSource(document);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        StreamResult result = new StreamResult(PATH);
        transformer.transform(source, result);
    }

    public boolean editStudent(Student oldStudent, Student newStudent) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        boolean isStudentFind = false;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        doc = builder.parse(PATH);
        NodeList elementList = doc.getElementsByTagName("student");
        for (int i = 0; i < elementList.getLength(); i++) {
            Node p = elementList.item(i);
            if (p.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) p;
                NodeList nameList = element.getChildNodes();
                String firstname = "";
                String lastname = "";
                String patronymic = "";
                for (int j = 0; j < nameList.getLength(); j++) {
                    Node n = nameList.item(j);
                    if (n.getNodeType() == Node.ELEMENT_NODE) {
                        Element name = (Element) n;
                        if (name.getTagName().equals("name")){
                            firstname = name.getTextContent();
                        }
                        if (name.getTagName().equals("lastname")){
                            lastname = name.getTextContent();
                        }
                        if (name.getTagName().equals("patronymic")){
                            patronymic = name.getTextContent();
                        }
                    }
                }
                if (oldStudent.getFirstname().equals(firstname) && oldStudent.getLastname().equals(lastname) && oldStudent.getPatronymic().equals(patronymic)){
                    isStudentFind = true;
                    for (int j = 0; j < nameList.getLength(); j++) {
                        Node n = nameList.item(j);
                        if (n.getNodeType() == Node.ELEMENT_NODE) {
                            Element name = (Element) n;
                            switch (name.getTagName()) {
                                case ("firstname"):
                                    name.setTextContent(newStudent.getFirstname());
                                    break;
                                case ("lastname"):
                                    name.setTextContent(newStudent.getLastname());
                                    break;
                                case ("patronymic"):
                                    name.setTextContent(newStudent.getPatronymic());
                                    break;
                                case ("age"):
                                    name.setTextContent(String.valueOf(newStudent.getAge()));
                                    break;
                                case ("group"):
                                    name.setTextContent(String.valueOf(newStudent.getGroup()));
                                    break;
                            }
                        }
                    }
                    DOMSource source = new DOMSource(doc);

                    TransformerFactory transformerFactory = TransformerFactory.newInstance();
                    Transformer transformer = transformerFactory.newTransformer();
                    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
                    StreamResult result = new StreamResult(PATH);
                    transformer.transform(source, result);
                }
            }
        }
        return isStudentFind;
    }
}
