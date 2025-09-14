package project;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;


//Context Class Bookstore
public class bookStore extends Application {
    private boolean loginStatus;
    private ArrayList<Customer> customerList = new ArrayList<>();
    
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
 
        Owner owner = new Owner(username, password); //Create owner instance to call the method below
        loginStatus = owner.login();
        if (loginStatus){
            owner.ownerStartScreen(primaryStage); //Transition to ownerStartScreen
            return;
        }
        else{
            customerList = getCustomers();
            boolean userExists = false;
            
            for (Customer customer: customerList){
                String checkUser = customer.getUsername();
                if (checkUser.equals(username)){
                    userExists = true;
                    String checkPass = customer.getPassword();
                    if (checkPass.equals(password)){
                        customer.customerStartScreen(primaryStage);
                        return;
                    }
                    else{
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Failed to Log In");
                        alert.setHeaderText(null);
                        alert.setContentText("Please Enter the Correct Username and Password");
                        alert.showAndWait(); 
                    }
                }
            }
            if (!userExists){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Failed to Log In");
                alert.setHeaderText(null);
                alert.setContentText("This Username Does Not Exist");
                alert.showAndWait();
            }
        }
        
        
    }
    
    public ArrayList<Customer> getCustomers(){
        File file = new File("customers.txt");
    
        try{
            //If file doesn't exist
            if (file.exists() == false){
                file.createNewFile();  
            }

            BufferedReader reader = new BufferedReader(new FileReader("customers.txt"));
            String line;
            while ((line = reader.readLine()) != null){ //Read each line of txt until there is no more text
                String [] parts = line.split(",");
                String username = parts[0];
                String password = parts[1];
                int points =  Integer.parseInt(parts[2]);
                customerList.add(new Customer(username, password, points));
            }
            reader.close();
        } catch (IOException e){
          e.printStackTrace();
        }
        
        return customerList;
    }
    
    public void rewritePoints(int points, String username){
        customerList = getCustomers();
        for(Customer customer : customerList){
            if (customer.getUsername().equals(username)){
                customer.setPoints(points);
            }
        }    
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("customers.txt"))) {
            for (Customer customer : customerList) {
                writer.write(customer.getUsername() + "," + customer.getPassword() + "," + customer.getPoints());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error updating points");
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}