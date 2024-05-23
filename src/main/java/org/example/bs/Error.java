package org.example.bs;

import javafx.scene.control.Alert;

public class Error {
    private Alert alert;
    public void setfield(String mg){
        alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mg);
        alert.showAndWait();
    }
//    public void  registered(){
//        alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setTitle("Information Massage");
//        alert.setHeaderText(null);
//        alert.setContentText("Successfully registered Account.");
//        alert.showAndWait();
//    }
//    public void pass(){
//        alert = new Alert(Alert.AlertType.ERROR);
//        alert.setTitle("Error");
//        alert.setHeaderText(null);
//        alert.setContentText("Your password should have at least 5 characters.");
//        alert.showAndWait();
//    }
//    public void name(){
//        alert = new Alert(Alert.AlertType.ERROR);
//        alert.setTitle("Error");
//        alert.setHeaderText(null);
//        alert.setContentText("This username has already been taken.");
//        alert.showAndWait();
//    }
//    public void login(){
//        alert = new Alert(Alert.AlertType.ERROR);
//        alert.setTitle("Error");
//        alert.setHeaderText(null);
//        alert.setContentText("Invalid information.");
//        alert.showAndWait();
//    }
//    public void equal(){
//        alert = new Alert(Alert.AlertType.ERROR);
//        alert.setTitle("Error");
//        alert.setHeaderText(null);
//        alert.setContentText("The passwords are not same");
//        alert.showAndWait();
//    }
    public void  update(String mg){
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Massage");
        alert.setHeaderText(null);
        alert.setContentText(mg);
        alert.showAndWait();
    }
//    public void exist(){
//        alert = new Alert(Alert.AlertType.ERROR);
//
//        alert.setTitle("Error");
//        alert.setHeaderText(null);
//        alert.setContentText("This book has already been taken.");
//        alert.showAndWait();
//    }
//    public void  addbook(){
//        alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setTitle("Information Massage");
//        alert.setHeaderText(null);
//        alert.setContentText("Successfully Added.");
//        alert.showAndWait();
//    }
}
