package de.a_sitko.meldi.window;

import de.a_sitko.meldi.Main;
import de.a_sitko.meldi.Texts;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class EditLine extends HBox {
    private static final String CLOSE_BUTTON_TEXT = "✖";
    private static final String SET_HIDEABLE_TEXT = "\uD83D\uDC41";
    private static final String SET_INFRONT_TEXT = "\uD83D\uDCCC";
    private static final String CLOSE_STATS_TEXT = "▼";
    private static final String OPEN_STATS_TEXT = "▲";
    private Button exit;
    private Button notOnTop;
    private Button closeStates;
    private boolean notOnTopState = false;
    private boolean statesShown = true;
    private Main main;
    public EditLine(Main main){
        this.main = main;
        setAlignment(Pos.TOP_RIGHT);
        exit = new Button(CLOSE_BUTTON_TEXT);
        exit.setBorder(null);
        exit.setOnAction((e)->main.exit());

        notOnTop = new Button(SET_HIDEABLE_TEXT);
        notOnTop.setOnAction((e)->this.toggleForceOnTop());
        closeStates = new Button(CLOSE_STATS_TEXT);
        closeStates.setOnAction((e)-> toggleStatsVisibility());

        Label title = new Label(Texts.TITLE);

        closeStates.setMaxHeight(main.WINDOW_HEIGHT * 0.1);
        notOnTop.setMaxHeight(main.WINDOW_HEIGHT * 0.1);
        exit.setMaxHeight(main.WINDOW_HEIGHT * 0.1);
        closeStates.setMinWidth(main.WINDOW_WIDTH * 0.1);
        notOnTop.setMinWidth(main.WINDOW_WIDTH * 0.1);
        exit.setMinWidth(main.WINDOW_WIDTH * 0.1);
        title.setMinWidth(main.WINDOW_WIDTH * 0.5);

        closeStates.setBorder(null);
        notOnTop.setBorder(null);
        exit.setBorder(null);

        Platform.runLater(()->{
            toggleStatsVisibility();
        });
        append(title);
        append(closeStates);
        append(notOnTop);
        append(exit);
    }
    private void toggleStatsVisibility(){
        statesShown = !statesShown;
        closeStates.setText(statesShown ? CLOSE_STATS_TEXT  : OPEN_STATS_TEXT );
        main.getStats().setMinSize(main.WINDOW_WIDTH,statesShown? main.WINDOW_HEIGHT * 0.4 : 0);
        main.getStage().setHeight(main.WINDOW_HEIGHT * (statesShown ? 1 : 0.6));
    }
    private void toggleForceOnTop(){
        notOnTopState = !notOnTopState;
        notOnTop.setText(notOnTopState ? SET_INFRONT_TEXT : SET_HIDEABLE_TEXT);
        main.getStage().setAlwaysOnTop(!notOnTopState);
    }
    private void append(Node a){
        this.getChildren().add(a);
    }
}
