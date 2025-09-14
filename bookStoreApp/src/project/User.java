
package project;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;


//abstract class User
public abstract class User {
private String username;
private String password;
private boolean loginStatus;


//Methods to be implemented by customer and owner

public boolean login(){
    return loginStatus;
}

public void logout(Stage primaryStage){
    
}




}





