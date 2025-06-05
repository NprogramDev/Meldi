package de.a_sitko.meldi.window;

import de.a_sitko.meldi.ExpireTime;
import de.a_sitko.meldi.Main;
import de.a_sitko.meldi.Texts;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class EditLine extends HBox {
    private static final String CLOSE_BUTTON_TEXT = "✖";
    private static final String SET_HIDEABLE_TEXT = "□";
    private static final String SET_INFRONT_TEXT = "●";
    private static final String CLOSE_STATS_TEXT = "▼";
    private static final String OPEN_STATS_TEXT = "▲";
    private Button exit;
    private Button notOnTop;
    private Button closeStates;
    private Label expire_label;
    private ComboBox<ExpireTime> expire_time;
    private boolean notOnTopState = false;
    private boolean statesShown = true;
    private Main main;
    public EditLine(Main main){
        this.main = main;
        setAlignment(Pos.TOP_RIGHT);
        exit = new Button(CLOSE_BUTTON_TEXT);
        exit.setBorder(null);
        exit.setOnAction((e)->main.exitButton());

        notOnTop = new Button(SET_HIDEABLE_TEXT);
        notOnTop.setOnAction((e)->this.notOnTopAction());
        closeStates = new Button(CLOSE_STATS_TEXT);
        closeStates.setOnAction((e)->closeStatesAction());

        closeStates.setMaxHeight(main.WINDOW_HEIGHT * 0.1);
        notOnTop.setMaxHeight(main.WINDOW_HEIGHT * 0.1);
        exit.setMaxHeight(main.WINDOW_HEIGHT * 0.1);
        closeStates.setMinWidth(main.WINDOW_WIDTH * 0.1);
        notOnTop.setMinWidth(main.WINDOW_WIDTH * 0.1);
        exit.setMinWidth(main.WINDOW_WIDTH * 0.1);

        expire_label = new Label(Texts.EXSPIRE_DATE);
        expire_time = new ComboBox<ExpireTime>(FXCollections.observableArrayList(List.of(ExpireTime.values())));
        append(expire_label);
        append(expire_time);
        append(closeStates);
        append(notOnTop);
        append(exit);
        main.setEditLine(this);
    }
    private void closeStatesAction(){
        statesShown = !statesShown;
        closeStates.setText(statesShown ? CLOSE_STATS_TEXT  : OPEN_STATS_TEXT );
        main.getStats().setMinSize(main.WINDOW_WIDTH,statesShown? main.WINDOW_HEIGHT * 0.4 : 0);
        main.getStage().setHeight(main.WINDOW_HEIGHT * (statesShown ? 1 : 0.6));
    }
    private void notOnTopAction(){
        notOnTopState = !notOnTopState;
        notOnTop.setText(notOnTopState ? SET_INFRONT_TEXT : SET_HIDEABLE_TEXT);
        main.getStage().setAlwaysOnTop(!notOnTopState);
    }
    public void setExpireTime(int minutes){
        ExpireTime time = ExpireTime.ofTime(minutes);
        expire_time.getSelectionModel().select(time);
    }
    private void append(Node a){
        this.getChildren().add(a);
    }
}
