package de.a_sitko.meldi.window;

import de.a_sitko.meldi.Main;
import de.a_sitko.meldi.Texts;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Stats extends VBox {
    private Label title, user, connection;
    public Stats(Main m){
        this.setAlignment(Pos.TOP_LEFT);
        user = new Label(Texts.USER_ID + m.getParam_uuid().substring(0,14));
        append(user);
        connection = new Label(Texts.NO_CONNECTION);
        m.setConnectionLabel(connection);
        append(connection);

    }
    void append(Node a){
        this.getChildren().add(a);
    }
}
