package org.example.bs;

import javafx.scene.control.Alert;

public class AlterBox {
    private Alert alert;
    public void error(String mg){
        alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mg);
        alert.showAndWait();
    }
    public void  update(String mg){
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Massage");
        alert.setHeaderText(null);
        alert.setContentText(mg);
        alert.showAndWait();
    }
}
