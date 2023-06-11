package com.kaatyassss.planner.controllers;

import com.kaatyassss.planner.PlannerMain;
import com.kaatyassss.planner.TasksModel;
import com.kaatyassss.planner.utils.InfoUtil;
import com.kaatyassss.planner.utils.TaskUtil;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import static com.kaatyassss.planner.utils.InfoUtil.FORMATTER;

public class PlannerController implements Initializable {

    @FXML
    private Button btnAddTask;

    @FXML
    private FlowPane flowPane;

    @FXML
    private Text monthText;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox vTaskItems;

    @FXML
    private Text yearText;

    @FXML
    private Text textNull;

    private String pickedDate;

    LocalDate dateFocus;
    LocalDate today;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        TaskUtil.setController(this);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        btnAddTask.setOnAction(event -> {
            try {
                TaskUtil.openScene("fxml/add-window.fxml", "Добавить задачу");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        dateFocus = LocalDate.now();
        today = LocalDate.now();
        refreshTasks();
    }

    public void scroll(double v) {
        scrollPane.applyCss();
        scrollPane.layout();
        scrollPane.setVvalue(v);
    }

    public void refreshTasks() {
        double v = scrollPane.getVvalue();

        List<TasksModel> listOfTasks;
        try {
            listOfTasks = InfoUtil.getListByDate(pickedDate);
            drawCalendar();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        vTaskItems.getChildren().clear();

        Node[] nodes = new Node[listOfTasks.size()];
        if (nodes.length > 0) {
            for (int i = 0; i < nodes.length; i++) {
                FXMLLoader loader = new FXMLLoader(PlannerMain.class.getResource("fxml/task-item.fxml"));
                TaskItemController controller = new TaskItemController();
                loader.setController(controller);
                try {
                    nodes[i] = loader.load();
                    vTaskItems.getChildren().add(nodes[i]);
                    controller.setTask(listOfTasks.get(i));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            vTaskItems.getChildren().add(textNull);
        }
        Platform.runLater(() -> scroll(v));
    }

    @FXML
    void backOneMonth() throws IOException {
        dateFocus = dateFocus.minusMonths(1);
        drawCalendar();
    }

    @FXML
    void forwardOneMonth() throws IOException {
        dateFocus = dateFocus.plusMonths(1);
        drawCalendar();
    }

    private void drawCalendar() throws IOException {
        flowPane.getChildren().clear();
        yearText.setText(String.valueOf(dateFocus.getYear())); // год текст
        monthText.setText(getMonthOnRussian(String.valueOf(dateFocus.getMonth()))); // название месяца текст

        double calendarWidth = flowPane.getPrefWidth();
        double calendarHeight = flowPane.getPrefHeight();
        double strokeWidth = 1;
        double spacingH = 0.6;
        double spacingV = flowPane.getVgap();

        int monthMaxDate = !isLeap(dateFocus.getYear()) && dateFocus.getMonthValue() == 2 ? 28 : dateFocus.getMonth().maxLength();

        int dateOffset = LocalDate.of(dateFocus.getYear(), dateFocus.getMonthValue(), 1).getDayOfWeek().getValue();

        for (int i = 0; i < 6; i++) {
            for (int j = 1; j <= 7; j++) {
                StackPane stackPane = new StackPane();

                Rectangle rectangle = new Rectangle();
                rectangle.setFill(Color.WHITE);
                rectangle.setStroke(Color.rgb(222, 222, 222));
                rectangle.setStrokeWidth(strokeWidth);
                double rectangleWidth = (calendarWidth / 7) - strokeWidth - spacingH;
                rectangle.setWidth(rectangleWidth);
                double rectangleHeight = (calendarHeight / 7) - strokeWidth - spacingV;
                rectangle.setHeight(rectangleHeight);
                stackPane.getChildren().add(rectangle);

                int calculatedDay = j + (7 * i) + 1;
                if (calculatedDay > dateOffset) {
                    int currentDay = calculatedDay - dateOffset;
                    if (currentDay <= monthMaxDate) {
                        Text date = new Text(String.valueOf(currentDay));
                        double textTranslationY = -(rectangleHeight / 2) * 0.75;
                        date.setTranslateY(textTranslationY);
                        stackPane.getChildren().add(date);
                        stackPane.setOnMouseClicked(event -> {
                                    String newDate = String.format("%02d.%02d.%04d", currentDay, dateFocus.getMonth().getValue(), dateFocus.getYear());
                                    pickedDate = newDate.equals(pickedDate) ? null : newDate;

                                    refreshTasks();
                                }
                        );

                        LocalDate currentDate = LocalDate.of(dateFocus.getYear(), dateFocus.getMonthValue(), currentDay);

                        if (getCalendarModelMonth().contains(currentDate)) {
                            createCalendarModel(currentDate, rectangleHeight, rectangleWidth, stackPane);
                        }
                    }
                    if (today.getYear() == dateFocus.getYear() && today.getMonth() == dateFocus.getMonth() && today.getDayOfMonth() == currentDay) {
                        rectangle.setStroke(Color.RED);
                    }
                }
                flowPane.getChildren().add(stackPane);
            }
        }
    }

    private void createCalendarModel(LocalDate date, double rectangleHeight, double rectangleWidth, StackPane stackPane) throws IOException {
        VBox calendarActivityBox = new VBox();
        calendarActivityBox.setTranslateY((rectangleHeight / 2) * 0.20);
        calendarActivityBox.setMaxWidth(rectangleWidth * 0.9);
        calendarActivityBox.setMaxHeight(rectangleHeight * 0.6);

        List<TasksModel> noCheckedTasks = InfoUtil.getListByDate(date.format(FORMATTER)).stream()
                .filter(task -> !task.isChecked())
                .toList();

        if (noCheckedTasks.isEmpty()) {
            calendarActivityBox.setStyle("-fx-background-color: #a2f5bd");
        } else {
            calendarActivityBox.setStyle("-fx-background-color: #fcb7af");
        }
        stackPane.getChildren().add(calendarActivityBox);
    }


    private List<LocalDate> getCalendarModelMonth() throws IOException {
        return InfoUtil.getList().stream()
                .map(TasksModel::getDate)
                .map(dateStr -> LocalDate.parse(dateStr, FORMATTER))
                .toList();
    }

    private String getMonthOnRussian(String month) {
        return switch (month.toUpperCase()) {
            case "JANUARY" -> "Январь";
            case "FEBRUARY" -> "Февраль";
            case "MARCH" -> "Март";
            case "APRIL" -> "Апрель";
            case "MAY" -> "Май";
            case "JUNE" -> "Июнь";
            case "JULY" -> "Июль";
            case "AUGUST" -> "Август";
            case "SEPTEMBER" -> "Сентябрь";
            case "OCTOBER" -> "Октябрь";
            case "NOVEMBER" -> "Ноябрь";
            case "DECEMBER" -> "Декабрь";
            default -> "";
        };
    }

    private boolean isLeap(int year) {
        return (year % 400 == 0) || (year % 4 == 0 && year % 100 != 0);
    }
}