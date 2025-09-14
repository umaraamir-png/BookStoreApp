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


//Context Class Bookstore
public class bookStore extends Application {
    
   Button loginButton;
   
    
    
    //Start of the Application (login screen)
    //It should have 3 gui items, username field, password field, and login button
    @Override
    public void start(Stage primaryStage) {
        //Welcome to the Book Store 
        Label welcome = new Label("Welcome to the Book Store!");
        //Username and Password text
        Label username = new Label("Username:");
        Label password = new Label("Password:");
        
        //Username Field 
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username"); //placeholder text
        usernameField.setMaxWidth(200);
        usernameField.setMaxHeight(30);
        
        //Password Field
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password"); //placeholder text
        passwordField.setMaxWidth(200);
        passwordField.setMaxHeight(30);
        
        
        //Log in button
        loginButton = new Button();
        loginButton.setText("Login");
        //action handler when login button is clicked
        //when clicked, goes to the handleLogin method to check for the user input
        loginButton.setOnAction(event -> handleLogin(usernameField.getText(),passwordField.getText(),primaryStage));
        
        //Hbox to align username text and username field
        HBox usernameBox = new HBox(10);
        usernameBox.getChildren().addAll(username,usernameField);
        usernameBox.setAlignment(Pos.CENTER);
        
        //Hbox to align password text and password field
        HBox passwordBox = new HBox(10);
        passwordBox.getChildren().addAll(password,passwordField);
        passwordBox.setAlignment(Pos.CENTER);
        
        
        //Create the layout
        VBox loginLayout = new VBox(10); // spacing between the elements
        //add all the gui elements
        loginLayout.getChildren().addAll(welcome,usernameBox,passwordBox,loginButton);
        //set the elements to be in the center
        loginLayout.setAlignment(Pos.CENTER);
        

        //Starting the login Stage
        //Scene 1 is the login Screen (first part of the application)
        Scene loginScene = new Scene(loginLayout, 600, 400);
        primaryStage.setTitle("Book Store");
        primaryStage.setScene(loginScene);
        primaryStage.show(); 
    }
    
    
    //Handling the login based on user input
    private void handleLogin(String username, String password, Stage primaryStage){
        //If the input is admin,admin go to Owner Screen
        if(username.equals("admin") && password.equals("admin")){
            System.out.println("Login Successful");
            Owner owner = new Owner(); //Create owner instance to call the method below
            owner.ownerStartScreen(primaryStage); //Transition to ownerStartScreen
        }
    }
    
    
    
    
   

    

    public static void main(String[] args) {
        launch(args);
    }
}