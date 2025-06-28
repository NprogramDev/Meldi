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

public class Main extends Thread {
    public final int WINDOW_WIDTH = 250;
    public final int WINDOW_HEIGHT = 200;
    public final long SEC = 1000;
    public final long DELAY = 5 * SEC;

    private API api;
    private Stage stage;
    private Stats stats;
    private Button raiseButton;
    private String param_name;
    private String param_device = UUID.randomUUID().toString();
    private String param_uuid;
    private boolean isRunning = true;
    private boolean isHandRaised = false;
    private int currentPosition = -1;
    private Label connectionLabel;
    private static Main instance;
    /**
     * It's the main constructor
     */
    public Main() {
        instance = this;
        api = new API();
        try {
            param_device = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
        }
        param_name = System.getProperty("user.name");
        if (param_name == null) param_name = UUID.randomUUID().toString();
        param_uuid = Changeables.USER_UUID;
        api.setUserID(param_device, param_name, param_uuid);
    }


    /**
     * The main Loop handling the status updates
     */
    @Override
    public void run() {
        while (isRunning) {
            this.setConnectionInfo(api.testServerConnection());
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

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stats getStats() {
        return stats;
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }

    public void setRaiseButton(Button raiseButton) {
        this.raiseButton = raiseButton;
    }

    public void setConnectionLabel(Label connectionLabel) {
        this.connectionLabel = connectionLabel;
    }

    public String getParam_name() {
        return param_name;
    }

    public String getParam_device() {
        return param_device;
    }

    public String getParam_uuid() {
        return param_uuid;
    }

    /**
     * Needed for packaging the java app into a jar file!
    */
    public static void main(String[] args){
        FXWindow.main(args);
    }
    public static class OnShut extends Thread {
        @Override
        public void run(){
            Main.instance.destroy();
        }
    }
}
