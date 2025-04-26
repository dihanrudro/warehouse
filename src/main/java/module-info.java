module com.stmarys.warehoused {
    requires java.sql;
    requires javafx.controls;
    requires javafx.fxml;

    opens com.stmarys.warehoused to javafx.fxml;
    opens com.stmarys.warehoused.model to javafx.base;
    opens com.stmarys.warehoused.dao;
}
