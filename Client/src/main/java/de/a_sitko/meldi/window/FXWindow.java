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
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * The main window of the app.
 * "Apparently meant to be the main program by JavaFX too, but not used as such."
 * Mostly used to init most things
 * Basically the View of MVC
 */
public class FXWindow extends Application {
    /**
     * The main program controlling everything (while the program is running)
     * Used for updates and stuff. (Controller MVC)
     */
    Main main;
    /**
     * The scene of JavaFX
     */
    Scene scene;

    /**
     * Inits stuff, like the main program
     * (Why not in the Constructor? JavaFX)
     */
    @Override
    public void init(){
        main = new Main();
    }

    /**
     * Starts everything and inits the window? Gives us the stage to append everything to
     * @param stage The Stage to append to
     */
    @Override
    public void start(Stage stage) {
        // Transparent text overlay
        main.setStage(stage); // Make the stage accessible everywhere from Main and for Main
        Window w = new Window(main); // Create the Main window box with everything in it
        // Set the pos of the outside window to the upper right corner, with space to close windows
        int[] pos = Config.getInstance().screen_position;
        if(pos[0] < 0 ) pos[0] = (int)Screen.getPrimary().getBounds().getWidth() + pos[0] - main.WINDOW_WIDTH;
        if(pos[1] < 0 ) pos[1] = (int)Screen.getPrimary().getBounds().getWidth() + pos[1] - main.WINDOW_HEIGHT;
        stage.setX(pos[0]);
        stage.setY(pos[1]);
        System.out.println("Pos: " + pos[0]  + "  " + pos[1]);
        // Create the scene on which everything appends to.
        scene = new Scene(w, main.WINDOW_WIDTH, main.WINDOW_HEIGHT, Color.WHEAT);
        stage.initStyle(StageStyle.TRANSPARENT); // Make the scene invisible.
        stage.setScene(scene); // Append the scene
        stage.setTitle(Texts.TITLE); // Set the window title (mainly for the sys tray)
        stage.getIcons().add(new Image(FXWindow.class.getResourceAsStream("ico.png"))); // Sys Tray Icon
        stage.show(); // Show the window
        main.start(); // Start the main thread for updates every few seconds
    }

    /**
     * The main method for starting JavaFX.
     * @param args
     */
    public static void main(String[] args){
        Runtime.getRuntime().addShutdownHook(new Main.OnShut());
        launch(args);
    }
}
