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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * The JavaFX Container for displaying the stats
 * This contains the id, user, connection
 */
public class Stats extends VBox implements ParentBox {
    private Label title, user, connection;

    /**
     * Creates the Stats box with sub-boxes
     * @param main The main program, controlling everything
     */
    public Stats(Main main) {
        // Set style
        this.setAlignment(Pos.TOP_LEFT);
        // create and format title label
        title = new Label(Texts.USER + main.getParam_name() + Texts.DEVICE + main.getParam_device());
        title.setPadding(new Insets(10, 0, 0, 0));
        append(title);
        // create and format user id label
        user = new Label(Texts.USER_ID + main.getParam_uuid().substring(0, 14));
        append(user);
        //create and format the connection label
        connection = new Label(Texts.NO_CONNECTION);
        main.setConnectionLabel(connection);
        append(connection);
    }
    @Override
    public void append(Node child){
        this.getChildren().add(child);
    }
}
