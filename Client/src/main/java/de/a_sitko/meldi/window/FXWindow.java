package de.a_sitko.meldi.window;

import de.a_sitko.meldi.Main;
import de.a_sitko.meldi.Texts;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class FXWindow extends Application {
    private static final String STYLE_CSS_FX = "-fx-background-color: transparent;";

    Main main;
    Scene scene;
    @Override
    public void init(){
        main = new Main();
    }
    @Override
    public void start(Stage stage) {
        // Transparent text overlay
        main.setStage(stage);
        Window w = new Window(main);
        stage.setX(Screen.getPrimary().getBounds().getWidth()-main.WINDOW_WIDTH);
        stage.setY(100);
        scene = new Scene(w, main.WINDOW_WIDTH, main.WINDOW_HEIGHT, Color.WHEAT);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setAlwaysOnTop(true);
        stage.setScene(scene);
        stage.setTitle(Texts.TITLE);
        stage.getIcons().add(new Image(FXWindow.class.getResourceAsStream("ico.png")));
        stage.show();
        main.start();
    }
    public static void main(String[] args){
        launch(args);
    }
    @Override
    public void stop(){
        main.destroy();
    }
}
