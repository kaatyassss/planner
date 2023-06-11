package com.kaatyassss.planner.utils;

import com.kaatyassss.planner.TasksModel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.OptionalInt;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.*;

public final class InfoUtil {

    private InfoUtil() {
    }

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final String REGEX = "\\{ id: (?<id>[0-9]+), taskName: (?<taskName>.+), taskDate: (?<taskDate>[0-9,\\s]{2}\\.[0-9]{2}\\.[0-9]{4}), status: (?<status>.+), checked: (?<checked>(true|false)) }";
    private static final Pattern PATTERN = Pattern.compile(REGEX);
    private static final Path PATH = Path.of("infoPlanner", "info.txt");
    private static int currentTaskId;

    public static List<TasksModel> getList() throws IOException {
        Files.createDirectories(PATH.getParent());
        if (!Files.exists(PATH)) {
            Files.createFile(PATH);
        }

        List<TasksModel> listOfTasks;
        try (Stream<String> lines = Files.lines(PATH, UTF_8)) {
            listOfTasks = lines.map(PATTERN::matcher)
                    .filter(Matcher::find)
                    .map(matcher -> new TasksModel(Integer.parseInt(matcher.group("id")), matcher.group("taskName"), matcher.group("status"), matcher.group("taskDate"), Boolean.parseBoolean(matcher.group("checked"))))
                    .sorted(Comparator.comparingLong(task -> LocalDate.parse(task.getDate(), FORMATTER).atStartOfDay(ZoneId.systemDefault()).toInstant().getEpochSecond()))
                    .toList();
        }
        return listOfTasks;
    }

    public static List<TasksModel> getListByDate(String date) throws IOException {
        if (date != null) {
            return getList().stream()
                    .filter(task -> task.getDate().equals(date))
                    .toList();

        }
        return getList();
    }

    public static TasksModel getTask(int id) throws IOException {
        return getList().stream()
                .filter(tasksModel -> tasksModel.getTaskId() == id)
                .findFirst()
                .orElseGet(TasksModel::new);
    }

    public static void createTask(String name, String date, String status, boolean checked) throws IOException {
        Files.createDirectories(PATH.getParent());

        int id = getLastId() + 1;
        String taskLine = String.format("{ id: %d, taskName: %s, taskDate: %s, status: %s, checked: %b }\n", id, name, date, status, checked);
        Files.writeString(PATH, taskLine, UTF_8, CREATE, APPEND);
    }

    public static void updateTask(int id, String name, String status, String date, boolean checked) throws IOException {
        List<TasksModel> lines = getList().stream()
                .map(taskModel -> {
                    if (taskModel.getTaskId() == id) {
                        taskModel = new TasksModel(id, name, status, date, checked);
                    }
                    return taskModel;
                }).toList();


        refillFile(lines);
    }

    public static void deleteTask() throws IOException {
        List<TasksModel> lines = getList().stream()
                .filter(taskModel -> taskModel.getTaskId() != currentTaskId)
                .toList();

        refillFile(lines);
    }

    public static void refillFile(List<TasksModel> list) throws IOException {
        List<String> lines = list.stream()
                .map(TasksModel::toString)
                .toList();

        Files.write(PATH, lines, UTF_8, CREATE, TRUNCATE_EXISTING);
    }

    private static int getLastId() throws IOException {
        if (Files.exists(PATH)) {
            try (Stream<String> lines = Files.lines(PATH, UTF_8)) {
                OptionalInt optionalInt = lines.map(PATTERN::matcher)
                        .filter(Matcher::find)
                        .mapToInt(matcher -> Integer.parseInt(matcher.group("id")))
                        .max();
                if (optionalInt.isPresent()) {
                    return optionalInt.getAsInt();
                }
            }
        }
        return 0;
    }

    public static void checkedTask(int id) throws IOException {
        TasksModel taskModel = getTask(id);
        if (!taskModel.isChecked()) {
            updateTask(taskModel.getTaskId(), taskModel.getTaskName(), "Выполнено", taskModel.getDate(), true);
        } else {
            updateTask(taskModel.getTaskId(), taskModel.getTaskName(), "В работе", taskModel.getDate(), false);
        }
    }

    public static int getCurrentTaskId() {
        return currentTaskId;
    }

    public static void setCurrentTaskId(int currentTaskId) {
        InfoUtil.currentTaskId = currentTaskId;
    }
}