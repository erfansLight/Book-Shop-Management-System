module org.example.bs {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jakarta.mail;
    requires javafx.base;
    //requires java.mail;


    opens org.example.bs to javafx.fxml;
    exports org.example.bs;
    exports org.example.bs.Model;
    opens org.example.bs.Model to javafx.fxml;
    exports org.example.bs.Service;
    opens org.example.bs.Service to javafx.fxml;
    exports org.example.bs.Controller;
    opens org.example.bs.Controller to javafx.fxml;
    exports org.example.bs.Repository;
    opens org.example.bs.Repository to javafx.fxml;
    exports org.example.bs.Session;
    opens org.example.bs.Session to javafx.fxml;
}