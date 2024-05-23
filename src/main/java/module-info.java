module org.example.bs {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.example.bs to javafx.fxml;
    exports org.example.bs;
}