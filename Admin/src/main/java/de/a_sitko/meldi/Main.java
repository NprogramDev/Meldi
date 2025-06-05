package de.a_sitko.meldi;

import de.a_sitko.meldi.window.EditLine;
import de.a_sitko.meldi.window.Stats;
import de.a_sitko.meldi.window.StudentList;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

public class Main extends Thread {
    public final int WINDOW_WIDTH = 750;
    public final int WINDOW_HEIGHT = 400;
    public final long SEC = 1000;
    public final long DELAY = 1 * SEC;
    public final int DELAY_TICKS = 10;
    public static final int DEFAULT_EXPIRE_TIME_MIN = 5;
    private API api;
    private Stage stage;
    private Stats stats;
    private StudentList studentList;
    private EditLine editLine;
    private String param_uuid;
    private Label connectionLabel;
    private int ticksToHoldFor = 0;


    private boolean isRunning = true;

    public Main() {
        api = new API();
        param_uuid = Changeables.USER_UUID;
        api.setUserID(param_uuid);
    }

    @Override
    public void run() {
        while (isRunning) {
            this.setConnectionInfo(api.testServerConnection());
            if(studentList != null)
                Platform.runLater(()->
                this.studentList.display(api.fetchStudents()));
            try {
                sleep(DELAY);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if(this.ticksToHoldFor <= 0){
                this.ticksToHoldFor = DELAY_TICKS;
                editLine.setExpireTime(api.getExpireTime());
            }else this.ticksToHoldFor--;
        }
    }
    public void exitButton() {
        System.exit(0);
    }

    public void setConnectionInfo(API.ConnectionType connected) {
        Platform.runLater(() -> {
            switch (connected){
                case API.ConnectionType.NO: this.connectionLabel.setText(Texts.NO_CONNECTION); return;
                case API.ConnectionType.NEW_VERSION: this.connectionLabel.setText(Texts.UPDATE_AVAL); return;
                case API.ConnectionType.PERFECT: this.connectionLabel.setText(Texts.CONNECTED); return;
                case API.ConnectionType.OLD_SERVER: this.connectionLabel.setText(Texts.OLD_SERVER); return;

            }
        });
    }

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



    public void setConnectionLabel(Label connectionLabel) {
        this.connectionLabel = connectionLabel;
    }



    public String getParam_uuid() {
        return param_uuid;
    }

    public void destroy() {
        isRunning = false;
    }
    public void removeStudent(Student student){
        api.unraise(student.getOriginal());

    }

    public void setStudentList(StudentList studentList) {
        this.studentList = studentList;
    }

    public void setEditLine(EditLine editLine) {
        this.editLine = editLine;
    }
}
