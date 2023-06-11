package com.kaatyassss.planner.controllers;

import com.kaatyassss.planner.TasksModel;
import com.kaatyassss.planner.utils.InfoUtil;
import com.kaatyassss.planner.utils.TaskUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TaskItemController implements Initializable {

    @FXML
    private BorderPane BPaneMain;

    @FXML
    private Button btnDeleteTask;

    @FXML
    private Button btnEditTask;

    @FXML
    private CheckBox checkBoxTask;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblStatus;

    @FXML
    private Label lblTaskName;

    private int id;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnEditTask.setOnAction(event -> {
            InfoUtil.setCurrentTaskId(id);
            try {
                TaskUtil.openScene("fxml/edit-window.fxml", "Изменить задачу");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        btnDeleteTask.setOnAction(event -> {
            InfoUtil.setCurrentTaskId(id);
            try {
                TaskUtil.openScene("fxml/delete-window.fxml", "Удалить задачу");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        checkBoxTask.setOnAction(event -> {
            try {
                InfoUtil.checkedTask(id);
                TaskUtil.getController().refreshTasks();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            colorCheckedTask();
        });
    }

    public void setTask(TasksModel tasksModel) {
        this.id = tasksModel.getTaskId();
        this.lblTaskName.setText(tasksModel.getTaskName());
        this.lblStatus.setText(tasksModel.getStatus());
        this.lblDate.setText(tasksModel.getDate());
        this.checkBoxTask.setSelected(tasksModel.isChecked());
        colorCheckedTask();
    }

    public void colorCheckedTask() {
        if (this.checkBoxTask.isSelected()) {
            this.BPaneMain.setStyle("-fx-background-color: #a2f5bd");
        } else {
            this.BPaneMain.setStyle("-fx-background-color: #ffffff");
        }
    }
}