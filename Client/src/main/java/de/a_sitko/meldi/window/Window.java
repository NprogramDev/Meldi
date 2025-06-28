package de.a_sitko.meldi.window;

import de.a_sitko.meldi.Main;
import de.a_sitko.meldi.Texts;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Window extends VBox {
    private EditLine editLine;
    private Stats stats;
    private Button raise;

    public Window(Main main){
        super();
        this.setAlignment(Pos.TOP_CENTER);

        editLine = new EditLine(main);
        stats = new Stats(main);
        raise = new Button(Texts.RAISE);

        editLine.setMinSize(main.WINDOW_WIDTH,main.WINDOW_HEIGHT * 0.1);
        stats.setMinSize(main.WINDOW_WIDTH,main.WINDOW_HEIGHT * 0.4);
        raise.setMinSize(main.WINDOW_WIDTH,main.WINDOW_HEIGHT * 0.5);

        raise.setOnAction(e -> main.updateHand());

        setRaiseButtonStyle(raise);
        raise.setCursor(Cursor.HAND);

        main.setStats(stats);
        main.setRaiseButton(raise);

        append(editLine);
        append(stats);
        append(raise);
    }
    private void setRaiseButtonStyle(Button button){
        button.setStyle("""
    -fx-text-fill: black;
    -fx-font-size: 16px;""");
    }
    private void append(Node a){
        this.getChildren().add(a);
    }
}
