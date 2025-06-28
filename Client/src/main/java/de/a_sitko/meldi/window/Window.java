package de.a_sitko.meldi.window;

import de.a_sitko.meldi.Main;
import de.a_sitko.meldi.Texts;
import javafx.geometry.Pos;
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
        editLine.setMinSize(main.WINDOW_WIDTH,main.WINDOW_HEIGHT * 0.1);
        stats = new Stats(main);
        stats.setMinSize(main.WINDOW_WIDTH,main.WINDOW_HEIGHT * 0.4);
        main.setStats(stats);
        raise = new Button(Texts.RAISE);
        raise.setMinSize(main.WINDOW_WIDTH,main.WINDOW_HEIGHT * 0.5);
        main.setRaiseButton(raise);
        raise.setOnAction(e -> main.updateHand());
        Font f = raise.getFont();
        f = new Font(f.getFamily(),15);
        raise.setFont(f);
        append(editLine);
        append(stats);
        append(raise);
    }
    private void append(Node a){
        this.getChildren().add(a);
    }
}
