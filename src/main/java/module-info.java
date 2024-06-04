module org.example.bs {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.mail;
    //requires java.mail;


    opens org.example.bs to javafx.fxml;
    exports org.example.bs;
}