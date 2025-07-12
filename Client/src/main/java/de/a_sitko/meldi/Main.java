/*
 * This class is part of the Meldi Software
 * See /development.md for more information or https://github.com/NprogramDev/Meldi
 * Copyright (C) 2025, Alexander Sitko, Noah Pani
 * Only use this under the licence given
 * @author NprogramDev (nprogramdev@gmail.com)
 */
package de.a_sitko.meldi;

import de.a_sitko.meldi.window.FXWindow;
import de.a_sitko.meldi.window.Stats;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

/**
 * The Main Control Logic of this software
 * Basically the controller in MVC System
 */
public class Main extends Thread {
    /**
     * The window width
     */
    public final int WINDOW_WIDTH = 250;
    /**
     * The window height
     */
    public final int WINDOW_HEIGHT = 200;
    /**
     * The second in java time
     */
    public final long SEC = 1000;
    /**
     * The delay to refresh from the server
     */
    public final long DELAY = 5 * SEC;

    /**
     * The Web Connection is made here.
     */
    private API api;
    /**
     * The JavaFX window stage
     */
    private Stage stage;
    /**
     * The JavaFX Container for displaying the stats
     */
    private Stats stats;
    /**
     * The button to click on to raise your hand
     */
    private Button raiseButton;
    /**
     * The param, where the name of the user is stored
     */
    private String param_name;
    /**
     * The param, where the device of the user is stored
     */
    private String param_device = UUID.randomUUID().toString();
    /**
     * The param of the admin uuid (Copied from Changeables)
     */
    private String param_uuid;
    /**
     * true, if the program is not stopping
     */
    private boolean isRunning = true;
    /**
     * Is the hand currently raised
     */
    private boolean isHandRaised = false;
    /**
     * The current position of the user on the waiting list
     * Update defined by DELAY
     * -1 = Not on the list
     *
     */
    private int currentPosition = -1;
    /**
     * The connection label from javafx to be updated
     */
    private Label connectionLabel;
    /**
     * The only main instance
     */
    private static Main instance;
    /**
     * It's the main constructor
     */
    public Main() {
        instance = this;
        api = new API();
        // Try to get the device name, if not possible use the random uuid
        try {
            param_device = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
        }
        // Get the user.name from the system, else use a random UUID
        param_name = System.getProperty("user.name");
        if (param_name == null) param_name = UUID.randomUUID().toString();
        // The UUID from Changeables
        param_uuid = Changeables.USER_UUID;
        // Set the three core params for the network api to register
        api.setUserID(param_device, param_name, param_uuid);
    }


    /**
     * The main Loop handling the status updates every DELAY
     */
    @Override
    public void run() {
        while (isRunning) {
            // Update the connection label info
            this.setConnectionInfo(api.testServerConnection());
            // Update the button text
            this.updateButton();
            try {
                sleep(DELAY);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


    /**
     * Sets the status label of a users hand based on a position
     * @param position The position is the servers list
     *                 -1 = Texts.RAISE The text with the prompt to raise the hand is shown
     *                 0 = Texts.READY  THe text
     */
    public void setStatus(int position) {
        if (this.raiseButton == null) return;
        this.currentPosition = position;
        raiseButton.setText(String.format(Texts.WAIT, position));
        if (position < 0) {
            raiseButton.setText(Texts.RAISE);
            isHandRaised = false;
        }
        if (position == 0) raiseButton.setText(Texts.READY);
    }

    /**
     * Sets a connection label of the App
     * @param connected The connection status of the App
     */
    public void setConnectionInfo(API.ConnectionType connected) {
        Platform.runLater(() -> {
            switch (connected){
                case NO: this.connectionLabel.setText(Texts.NO_CONNECTION); return;
                case NEW_VERSION: this.connectionLabel.setText(Texts.UPDATE_AVAL); return;
                case PERFECT: this.connectionLabel.setText(Texts.CONNECTED); return;
                case OLD_SERVER: this.connectionLabel.setText(Texts.OLD_SERVER); return;

            }
        });
    }

    /**
     * Toggles whether the hand is raised and unraised
     * (On-Click action of the button)
     */
    public void updateHand() {
        isHandRaised = !isHandRaised;
        updateButton();
        this.api.raise(isHandRaised);
    }

    /**
     * Updates the Button after a change or in a tick
     */
    public void updateButton() {
        Platform.runLater(() -> {
            if (this.isHandRaised) {
                this.setStatus(this.api.fetchPosition());
            } else if (this.currentPosition != -1) {
                this.setStatus(-1);
            }
        });
    }

    /**
     * The deconstructor of Main App parts
     * Stops the loops and removes the raise users if raised
     */
    public void destroy() {
        isRunning = false;
        System.out.println("Unraise on exit");
        this.api.raise(false);
    }

    /**
     * Exits the entire Program w/ ExitCode 0
     */
    public void exit() {
        System.exit(0);
    }

    /*
    * Getters & Setter
    * */

    /**
     * Getter for the JavaFX main window stage
     * @return the Main Window Stage
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Setter for the JavaFX main window stage
     * @param stage Only ever set with JavaFX Main Window
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Getter for the stats box of the window
     * @return the Stats box of JavaFX
     */
    public Stats getStats() {
        return stats;
    }

    /**
     * Setter for the stats box of the window
     * @param stats Only ever set the Stats box of the main window
     */
    public void setStats(Stats stats) {
        this.stats = stats;
    }

    /**
     * Setter for the button with the raise text and action on it
     * @param raiseButton The Button
     */
    public void setRaiseButton(Button raiseButton) {
        this.raiseButton = raiseButton;
    }

    /**
     * Setter for the connection label in the Stats box
     * @param connectionLabel  Only ever set the connection label of the stats box
     */
    public void setConnectionLabel(Label connectionLabel) {
        this.connectionLabel = connectionLabel;
    }

    /**
     * Getter for the name parameter of the user
     * @return the unchanged name parameter
     */
    public String getParam_name() {
        return param_name;
    }

    /**
     * Getter for the device parameter of the user
     * @return the unchanged device parameter
     */
    public String getParam_device() {
        return param_device;
    }

    /**
     * Getter for the UUID of the admin user
     * @return The unchanged Admin User UUID
     */
    public String getParam_uuid() {
        return param_uuid;
    }

    /**
     * The actual entry point of the program. JavaFX needs a direct call for some reason
     * Needed for packaging the java app into a jar file!
    */
    public static void main(String[] args){
        FXWindow.main(args);
    }

    /**
     * The shutdown job because JavaFX doesn't want to.
     */
    public static class OnShut extends Thread {
        @Override
        public void run(){
            // Just destroy the main instance
            Main.instance.destroy();
        }
    }
}
