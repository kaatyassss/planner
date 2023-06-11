package com.kaatyassss.planner.controllers;

import com.kaatyassss.planner.utils.InfoUtil;
import com.kaatyassss.planner.utils.TaskUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DeleteWindowController implements Initializable {

    @FXML
    private Button btnDeleteTask;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnDeleteTask.setOnAction(event -> {
            deleteTask();
            TaskUtil.getController().refreshTasks();
            TaskUtil.closeScene(btnDeleteTask);
        });
    }

    private void deleteTask() {
        try {
            InfoUtil.deleteTask();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
