module com.mordvinovdsw.library {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;

    opens com.mordvinovdsw.library.itemControllers to javafx.fxml;
    opens com.mordvinovdsw.library to javafx.fxml;
    exports com.mordvinovdsw.library;
    exports com.mordvinovdsw.library.controllers;
    opens com.mordvinovdsw.library.controllers to javafx.fxml;
    exports com.mordvinovdsw.library.Database;
    opens com.mordvinovdsw.library.Database to javafx.fxml;
    exports com.mordvinovdsw.library.supportControllers;
    opens com.mordvinovdsw.library.supportControllers to javafx.fxml;
}