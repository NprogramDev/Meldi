package de.a_sitko.meldi.window;

import de.a_sitko.meldi.Main;
import de.a_sitko.meldi.Student;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class StudentList extends VBox {
    private Main main;
    public StudentList(Main m){
        this.main = m;
    }
    public void display(List<Student> studentList){
        this.getChildren().clear();
        for (Student student : studentList) {
            HBox v = new HBox();
            Label l = new Label(student.getName() + " " + student.getDevice());
            Button removeButton = new Button("✔");
            removeButton.setOnAction((e)->main.removeStudent(student));
            v.getChildren().add(l);
            v.getChildren().add(removeButton);
            this.getChildren().add(v);
        }
    }
}
