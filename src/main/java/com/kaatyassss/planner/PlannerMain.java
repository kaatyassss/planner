package com.kaatyassss.planner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class PlannerMain extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        System.setProperty("prism.lcdtext", "false");
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("fxml/main-window.fxml")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Планировщик дел");
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/checkbox.png"))));
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
