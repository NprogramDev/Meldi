/*
 * This class is part of the Meldi Software
 * See /development.md for more information or https://github.com/NprogramDev/Meldi
 * Copyright (C) 2025, Alexander Sitko, Noah Pani
 * Only use this under the licence given
 * @author NprogramDev (nprogramdev@gmail.com)
 */
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

/**
 * The main box of the Window
 */
public class Window extends VBox implements ParentBox{
    /**
     * The custom top header bar of the App, with X, Pin and title
     */
    private EditLine editLine;
    /**
     * The container for the stats including title, user, connection
     */
    private Stats stats;
    /**
     * The button to raise your hand
     */
    private Button raise;

    /**
     * Creates the main box
     * @param main The main program, controlling everything
     */
    public Window(Main main){
        super();
        // Style the box
        this.setAlignment(Pos.TOP_CENTER);
        //Create the sub-boxes
        editLine = new EditLine(main);
        stats = new Stats(main);
        raise = new Button(Texts.RAISE);
        // Set the min size of the sub-boxes to fit the window
        editLine.setMinSize(main.WINDOW_WIDTH,main.WINDOW_HEIGHT * 0.1);
        stats.setMinSize(main.WINDOW_WIDTH,main.WINDOW_HEIGHT * 0.4);
        raise.setMinSize(main.WINDOW_WIDTH,main.WINDOW_HEIGHT * 0.5);
        // On-Click action of the button
        raise.setOnAction(e -> main.updateHand());
        //Style the button
        setRaiseButtonStyle(raise);
        raise.setCursor(Cursor.HAND);
        // "register" the sub-boxes for the main program
        main.setStats(stats);
        main.setRaiseButton(raise);
        // Add all sub-boxes to the plain
        append(editLine);
        append(stats);
        append(raise);
    }

    /**
     * Styles the button
     * Sets only font color and size
     * @param button The button to style
     */
    private void setRaiseButtonStyle(Button button){
        button.setStyle("""
    -fx-text-fill: black;
    -fx-font-size: 16px;""");
    }
    @Override
    public void append(Node a){
        this.getChildren().add(a);
    }
}
