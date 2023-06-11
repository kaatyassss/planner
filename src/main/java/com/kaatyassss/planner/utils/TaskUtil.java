package com.kaatyassss.planner.utils;

import com.kaatyassss.planner.PlannerMain;
import com.kaatyassss.planner.controllers.PlannerController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class TaskUtil {

    private static PlannerController controller;

    public static void closeScene(Button button) {
        Stage stage = (Stage) button.getScene().getWindow();
        stage.hide();
    }

    public static void openScene(String window, String title) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(PlannerMain.class.getResource(window)));
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle(title);
        stage.getIcons().add(new Image(Objects.requireNonNull(PlannerMain.class.getResourceAsStream("images/checkbox.png"))));
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    public static void saveTaskAlert(String context, String header) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Внимание!");
        alert.setHeaderText(header);
        alert.setContentText(context);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Objects.requireNonNull(PlannerMain.class.getResourceAsStream("images/warning.png"))));
        alert.showAndWait();
    }

    public static PlannerController getController() {
        return controller;
    }

    public static void setController(PlannerController controller) {
        TaskUtil.controller = controller;
    }
}
