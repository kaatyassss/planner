package com.kaatyassss.planner;

public class TasksModel {

    private int taskId;
    private String taskName;
    private String status;
    private String date;
    private boolean checked;

    public TasksModel() {
    }

    public TasksModel(int taskId, String taskName, String status, String date, boolean checked) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.status = status;
        this.date = date;
        this.checked = checked;
    }

    public int getTaskId() {
        return taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getStatus() {
        return status;
    }

    public String getDate() {
        return date;
    }

    public boolean isChecked() {
        return checked;
    }

    @Override
    public String toString() {
        return String.format("{ id: %d, taskName: %s, taskDate: %s, status: %s, checked: %b }", taskId, taskName, date, status, checked);
    }
}
