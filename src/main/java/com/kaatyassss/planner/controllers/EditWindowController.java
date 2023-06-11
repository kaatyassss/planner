package com.kaatyassss.planner.controllers;

import com.kaatyassss.planner.TasksModel;
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
import static java.time.format.DateTimeFormatter.ofPattern;

public class EditWindowController implements Initializable {

    @FXML
    private Button btnEditTask;

    @FXML
    private DatePicker tfDateEdit;

    @FXML
    private TextField tfNameTaskEdit;

    TasksModel task;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            task = InfoUtil.getTask(InfoUtil.getCurrentTaskId());
            tfNameTaskEdit.setText(task.getTaskName());
            tfDateEdit.setValue(parseDate(task.getDate()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        btnEditTask.setOnAction(event -> {
            updateTask();
            TaskUtil.getController().refreshTasks();
        });
    }

    private void updateTask() {
        String text = tfNameTaskEdit.getText();
        LocalDate dateTask = tfDateEdit.getValue();

        if (!checkFieldsFilling(text, dateTask) || !checkDatesOrder(dateTask)) {
            return;
        }

        try {
            InfoUtil.updateTask(task.getTaskId(), text, task.getStatus(), dateTask.format(FORMATTER), task.isChecked());
            TaskUtil.closeScene(btnEditTask);
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

    private LocalDate parseDate(String date) {
        return LocalDate.parse(date, ofPattern("dd.MM.yyyy"));
    }
}
