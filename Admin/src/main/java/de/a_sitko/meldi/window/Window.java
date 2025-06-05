package de.a_sitko.meldi.window;

import de.a_sitko.meldi.Main;
import de.a_sitko.meldi.Texts;
import javafx.beans.property.ListProperty;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;

public class Window extends VBox {
    private EditLine editLine;
    private Stats stats;
    private StudentList stList;

    public Window(Main main){
        super();
        this.setAlignment(Pos.TOP_CENTER);
        editLine = new EditLine(main);
        //editLine.setMinSize(main.WINDOW_WIDTH,main.WINDOW_HEIGHT * 0.1);
        stats = new Stats(main);
      //  stats.setMinSize(main.WINDOW_WIDTH,main.WINDOW_HEIGHT * 0.4);
        stList = new StudentList(main);
        main.setStudentList(stList);
        main.setStats(stats);
        append(editLine);
        append(stats);
        append(stList);
    }
    private void append(Node a){
        this.getChildren().add(a);
    }
    private void close(){
        System.exit(0);
    }
}
