/*
 * This class is part of the Meldi Software
 * See /development.md for more information or https://github.com/NprogramDev/Meldi
 * Copyright (C) 2025, Alexander Sitko, Noah Pani
 * Only use this under the licence given
 * @author NprogramDev (nprogramdev@gmail.com)
 */
package de.a_sitko.meldi.window;

import de.a_sitko.meldi.Config;
import de.a_sitko.meldi.Main;
import de.a_sitko.meldi.Texts;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * The custom top header bar of the App, with X, Pin and title
 */
public class EditLine extends HBox implements ParentBox {
    private static final String CLOSE_BUTTON_TEXT = "✕";;
    private static final String SET_HIDEABLE_TEXT = "\uD83D\uDC41";
    private static final String SET_INFRONT_TEXT = "\uD83D\uDCCC";
    private static final String CLOSE_STATS_TEXT = "▼";
    private static final String OPEN_STATS_TEXT = "▲";
    private static final String MOVE_SYMBOL = "⤧";
    //
    private Button exit;
    private Button pinOnWindow;
    private Button closeStates;
    private Button moveButton;
    private boolean notOnTopState = false;
    private boolean statesShown = true;
    private Main main;

    /**
     * The constructor who creates all the inner content
     * @param main The main app, controlling it all
     */
    public EditLine(Main main){
        this.main = main;
        // Set style
        setAlignment(Pos.TOP_RIGHT);
        // Create the window theme buttons, like close...
        exit = new Button(CLOSE_BUTTON_TEXT);
        exit.setBorder(null);
        exit.setOnAction((e)->main.exit());
        setWindowsButtonStyle(exit,"#ff0000");
        // ...,pin on,...
        pinOnWindow = new Button(SET_HIDEABLE_TEXT);
        pinOnWindow.setOnAction((e)->this.toggleForceOnTop());
        main.getStage().setAlwaysOnTop(!notOnTopState); // Use the default state for OnTop
        setWindowsButtonStyle(pinOnWindow,"#aaaaff");
        // ... and close stats
        closeStates = new Button(CLOSE_STATS_TEXT);
        closeStates.setOnAction((e)-> toggleStatsVisibility());
        setWindowsButtonStyle(closeStates,"#aaaaaa");
        // Setup the title for the window with it's style
        Label title = new Label(Texts.TITLE);
        setTitleStyle(title);
        //
        moveButton = new Button(MOVE_SYMBOL);
        moveButton.setOnMousePressed((e)->{
            this.main.startMove();
        });
        setWindowsButtonStyle(moveButton,"#00aaaa");
        moveButton.setCursor(Cursor.MOVE);
        // Arange everything with the window size in mind
        closeStates.setMaxHeight(main.WINDOW_HEIGHT * 0.1);
        pinOnWindow.setMaxHeight(main.WINDOW_HEIGHT * 0.1);
        exit.setMaxHeight(main.WINDOW_HEIGHT * 0.1);
        closeStates.setMinWidth(main.WINDOW_WIDTH * 0.1);
        pinOnWindow.setMinWidth(main.WINDOW_WIDTH * 0.1);
        exit.setMinWidth(main.WINDOW_WIDTH * 0.1);
        title.setMinWidth(main.WINDOW_WIDTH * 0.5);
        // Update the visibility of the stats sync to the window
        Platform.runLater(()->{
            if(main.getStats() != null)
                toggleStatsVisibility();
        });
        // Append everything
        append(title);
        if(Config.getInstance().allow_window_move)append(moveButton);
        append(closeStates);
        append(pinOnWindow);
        append(exit);
    }

    /**
     * Set the unique style of the title
     * @param label The title Label
     */
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
    private void setWindowsButtonStyle(Button button, String color){
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
    button.setBorder(null);
    }

    /**
     * On-Click Callback for the stats visibility button. Toggles the visibility and corrects for the size changes
     */
    private void toggleStatsVisibility(){
        statesShown = !statesShown;
        closeStates.setText(statesShown ? CLOSE_STATS_TEXT  : OPEN_STATS_TEXT );
        main.getStats().setMinSize(main.WINDOW_WIDTH,statesShown? main.WINDOW_HEIGHT * 0.4 : 5);
        main.getStage().setHeight(main.WINDOW_HEIGHT * (statesShown ? 1 : 0.6));
    }

    /**
     * On-Click Callback for the pin. Toggles always on top.
     */
    private void toggleForceOnTop(){
        notOnTopState = !notOnTopState;
        pinOnWindow.setText(notOnTopState ? SET_INFRONT_TEXT : SET_HIDEABLE_TEXT);
        main.getStage().setAlwaysOnTop(!notOnTopState);
    }
    @Override
    public void append(Node a){
        this.getChildren().add(a);
    }
}
