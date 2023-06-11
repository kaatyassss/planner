module com.kaatyassss.planner {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.kaatyassss.planner to javafx.fxml, javafx.controls;
    exports com.kaatyassss.planner;

    opens com.kaatyassss.planner.controllers to javafx.fxml, javafx.controls;
    exports com.kaatyassss.planner.controllers;

    opens com.kaatyassss.planner.utils to javafx.fxml, javafx.controls;
    exports com.kaatyassss.planner.utils;
}