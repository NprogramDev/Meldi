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
    private static final String CLOSE_BUTTON_TEXT = "✕";;
    private static final String SET_HIDEABLE_TEXT = "\uD83D\uDC41";
    private static final String SET_INFRONT_TEXT = "\uD83D\uDCCC";
    private static final String CLOSE_STATS_TEXT = "▼";
    private static final String OPEN_STATS_TEXT = "▲";
    //
    private Button exit;
    private Button pinOnWindow;
    private Button closeStates;
    private boolean notOnTopState = false;
    private boolean statesShown = true;
    private Main main;

    /**
     * The constructor who creates all the inner content
     * @param main The main app
     */
    public EditLine(Main main){
        this.main = main;
        setAlignment(Pos.TOP_RIGHT);
        exit = new Button(CLOSE_BUTTON_TEXT);
        exit.setBorder(null);
        exit.setOnAction((e)->main.exit());

        pinOnWindow = new Button(SET_HIDEABLE_TEXT);
        pinOnWindow.setOnAction((e)->this.toggleForceOnTop());
        closeStates = new Button(CLOSE_STATS_TEXT);
        closeStates.setOnAction((e)-> toggleStatsVisibility());

        Label title = new Label(Texts.TITLE);
        setTitleStyle(title);
        setButtonStyle(closeStates,"#aaaaaa");
        setButtonStyle(pinOnWindow,"#aaaaff");
        setButtonStyle(exit,"#ff0000");

        closeStates.setMaxHeight(main.WINDOW_HEIGHT * 0.1);
        pinOnWindow.setMaxHeight(main.WINDOW_HEIGHT * 0.1);
        exit.setMaxHeight(main.WINDOW_HEIGHT * 0.1);
        closeStates.setMinWidth(main.WINDOW_WIDTH * 0.1);
        pinOnWindow.setMinWidth(main.WINDOW_WIDTH * 0.1);
        exit.setMinWidth(main.WINDOW_WIDTH * 0.1);
        title.setMinWidth(main.WINDOW_WIDTH * 0.5);

        closeStates.setBorder(null);
        pinOnWindow.setBorder(null);
        exit.setBorder(null);

        Platform.runLater(()->{
            toggleStatsVisibility();
        });
        append(title);
        append(closeStates);
        append(pinOnWindow);
        append(exit);
    }
    private void setTitleStyle(Label label){
        label.setStyle("""
                -fx-text-fill: black;
                -fx-padding: 4 8 4 8;
    -fx-font-size: 12px;
    -fx-font-weight: bold;""");
    }
     /**
      * Sets the style of a button in the edit line to a windows like close button
      * @param button The button to change the style
      * @param color The color on hover
      */
    private void setButtonStyle(Button button,String color){
        button.setStyle("""
    -fx-background-color: transparent;
    -fx-text-fill: black;
    -fx-font-size: 12px;
    -fx-font-weight: bold;
    -fx-padding: 4 8 4 8;
""");

        button.setOnMouseEntered(e -> button.setStyle("""
    -fx-background-color: """+ color + """
    ; /* Windows red hover */
    -fx-text-fill: black;
    -fx-font-size: 12px;
    -fx-font-weight: bold;
    -fx-padding: 4 8 4 8;
"""));

        button.setOnMouseExited(e -> button.setStyle("""
    -fx-background-color: transparent;
    -fx-text-fill: black;
    -fx-font-size: 12px;
    -fx-font-weight: bold;
    -fx-padding: 4 8 4 8;
"""));

    }
    private void toggleStatsVisibility(){
        statesShown = !statesShown;
        closeStates.setText(statesShown ? CLOSE_STATS_TEXT  : OPEN_STATS_TEXT );
        main.getStats().setMinSize(main.WINDOW_WIDTH,statesShown? main.WINDOW_HEIGHT * 0.4 : 5);
        main.getStage().setHeight(main.WINDOW_HEIGHT * (statesShown ? 1 : 0.6));
    }
    private void toggleForceOnTop(){
        notOnTopState = !notOnTopState;
        pinOnWindow.setText(notOnTopState ? SET_INFRONT_TEXT : SET_HIDEABLE_TEXT);
        main.getStage().setAlwaysOnTop(!notOnTopState);
    }
    private void append(Node a){
        this.getChildren().add(a);
    }
}
