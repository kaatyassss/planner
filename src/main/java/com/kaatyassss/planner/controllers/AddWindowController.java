package com.kaatyassss.planner.controllers;

import com.kaatyassss.planner.utils.InfoUtil;
import com.kaatyassss.planner.utils.TaskUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static com.kaatyassss.planner.utils.InfoUtil.FORMATTER;

public class AddWindowController implements Initializable {

    @FXML
    private DatePicker tfDate;

    @FXML
    private TextField tfNameTask;

    @FXML
    private Button btnAddTaskAdd;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnAddTaskAdd.setOnAction(event -> {
            saveTask();
            TaskUtil.getController().refreshTasks();
        });
    }

    private void saveTask() {
        String text = tfNameTask.getText();
        LocalDate dateTask = tfDate.getValue();

        if (!checkFieldsFilling(text, dateTask) || !checkDatesOrder(dateTask)) {
            return;
        }

        try {
            InfoUtil.createTask(text, tfDate.getValue().format(FORMATTER), "В работе", false);
            TaskUtil.closeScene(btnAddTaskAdd);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean checkFieldsFilling(String text, LocalDate date) {
        if (text.equals("")) {
            TaskUtil.saveTaskAlert("Пустая задача", "Введите название задачи");
            return false;
        }
        if (date == null) {
            TaskUtil.saveTaskAlert("Пустая дата", "Введите дату задачи");
            return false;
        }
        return true;
    }

    private boolean checkDatesOrder(LocalDate dateTask) {
        LocalDate today = LocalDate.now();
        if (dateTask.isBefore(today)) {
            TaskUtil.saveTaskAlert("Некорректная дата", "Введите дату не ранее сегодняшнего дня");
            return false;
        }
        return true;
    }
}
