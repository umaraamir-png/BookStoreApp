package project;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;


//Owner state class
public class Owner extends User {
    
    //tableview for books
    private TableView<Book> table;
    private ObservableList<Book> bookList;
    //tableview for customers
    private TableView<Customer> customerTable;
    private ObservableList<Customer> customerList;
    private String adminName, adminPass;
    
    public Owner(String username, String password){
        adminName = username;
        adminPass = password;
    }
    
    
    //Transition to ownerStartScreen
    public void ownerStartScreen(Stage primaryStage){
        
    //Welcome Owner Text
    Label welcomeOwner = new Label("Welcome Owner!");
    //3 gui elements, books,customers,logout
    
    Button books = new Button(); //books button 
    books.setText("Books");
    books.setMaxWidth(150);
    //When book is clicked, go to the Ownerbookscreen 
    books.setOnAction(event -> ownerBookScreen(primaryStage));
    
    
    //customers button
    Button customers = new Button(); 
    customers.setText("Customers");
    customers.setMaxWidth(150);
    customers.setOnAction(event -> ownerCustomerScreen(primaryStage));
    
    //logout button
    Button logout = new Button(); 
    logout.setText("Logout");
    logout.setMaxWidth(150);
    //When logout is clicked, go back to login-screen
    logout.setOnAction(event -> {
        logout(primaryStage);
    });
    
    
    //Vbox layout for the 3 gui elements
    VBox ownerStartScreenLayout = new VBox(10);
    //add the 3 gui elements in the layout
    ownerStartScreenLayout.getChildren().addAll(books,customers,logout);
    ownerStartScreenLayout.setAlignment(Pos.CENTER);
    
    //Create the new Stage (window)
    Scene ownerStartScreen = new Scene(ownerStartScreenLayout, 600, 400);
    //update current stage with the new scene 
    primaryStage.setTitle("Owner's Start Screen");
    primaryStage.setScene(ownerStartScreen);
           
    }

    
    
//Owner-books-screen (when button books is clicked)
public void ownerBookScreen(Stage primaryStage){
    
    //Create a list of books
    bookList = FXCollections.observableArrayList();
    //Load existing books from books.txt by calling the loadBooksFromFile method created
    loadBooksfromFile();

    //it has 3 parts, top part,middle,bottom
    //top part contains table w/ 2 columns w heading book name and and book price
    //owner only has 1 copy of each book
    
    
    //********** TOP PART *********
    //Creating a table
    table = new TableView<>();
    table.setPrefHeight(500); //Consume 1/3rd of the screen
    table.setItems(bookList); //add the books to the table
    
    
    //Table column : For name and price
    TableColumn<Book, String> nameColumn = new TableColumn<>("Book Title");
    nameColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
    nameColumn.setMinWidth(300);
    
    TableColumn<Book,Double> priceColumn = new TableColumn<>("Book Price");
    priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    priceColumn.setMinWidth(200);
    
    table.getColumns().addAll(nameColumn,priceColumn);
    
    //**END OF TOP PART**
    
    //***MIDDLE PART ***
    //Contains 3 gui items: Textfields Name and Price ||  Button: Add
    
    //Create button and name it Add
    Button addButton = new Button();
    addButton.setText("Add");
    
    //Create a placeholder for both textfield
    TextField bookName = new TextField();
    bookName.setPromptText("Title"); //placeholder text
    bookName.setMaxWidth(200);
    bookName.setMaxHeight(30);
    
    TextField bookPrice = new TextField();
    bookPrice.setPromptText("Price");
    bookPrice.setMaxWidth(200);
    bookPrice.setMaxHeight(30);
    
    //Hbox to align name and price of book and add button
    HBox bookDetail = new HBox(10);
    bookDetail.getChildren().addAll(bookName,bookPrice, addButton);
    bookDetail.setAlignment(Pos.CENTER);
    
    //Create the layout
    VBox middlePart = new VBox(10); 
    middlePart.getChildren().addAll(bookDetail);
    middlePart.setAlignment(Pos.CENTER);
    //Make it tiny so that top part has a bigger space
    middlePart.setPrefHeight(150);
    
    
    //Function for handling add when clicked
    addButton.setOnAction(event -> {
        
        String titleText = bookName.getText().trim(); //Takes the input for title and price
        String priceText = bookPrice.getText().trim();    
        double priceValue;
        //If one of the input is empty, show alert
        if(titleText.isEmpty() || priceText.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setHeaderText(null);
            alert.setContentText("Please enter both title and price!");
            alert.showAndWait();
            return;
        }
        
        try{
            priceValue = Double.parseDouble(priceText); //converts the string of price to a double
        }catch(NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a valid numeric price!");
            alert.showAndWait();
            return;           
        }
            
        // If price is a negative value or zero
        if(priceValue <= 0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a valid price!");
            alert.showAndWait();
            return;   
        }
        
        //Check for duplicates
        for (Book book : bookList){
            if(book.getTitle().equalsIgnoreCase(titleText)){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Input");
                alert.setHeaderText(null);
                alert.setContentText("Book already exists!");
                alert.showAndWait();
            return;   
                
            }
            
        }
        
        //IF all conditions pass, then create the new book and add it to the list
        Book book = new Book(titleText,priceValue);
        bookList.add(book);
        //Clear input fields after adding
        bookName.clear();
        bookPrice.clear();
        saveBooksToFile(); //save the new list of books
  
    });
    
    
    //**END OF MIDDLE PART** 
    
    //***** BOTTOM PART***
    // Delete button to delete selected row in top part
    // Back button to go back to OwnerStartScreen
    
    Button deleteButton = new Button();
    deleteButton.setText("Delete");
    
    
    Button backButton = new Button();
    backButton.setText("Back");
    //Handle backButton  when clicked
    backButton.setOnAction(event -> ownerStartScreen(primaryStage));
    
    
    
    //Handle deleteButton when clicked
    deleteButton.setOnAction(event -> {
        
        Book selectedBook = table.getSelectionModel().getSelectedItem(); //Highlihts the book that is currently selected
        if (selectedBook!= null){
            bookList.remove(selectedBook); // remove the book from the bookList
            saveBooksToFile(); //save the new list of books
        }
        else{
          //show error if no book is selected
          Alert alert = new Alert(Alert.AlertType.ERROR);
          alert.setTitle("No books selected");
          alert.setHeaderText(null);
          alert.setContentText("Please select a book to be deleted!");
          alert.showAndWait(); 
        }
        
        
    });
    

    
    //Align them horizontally
    HBox bottomPart = new HBox(10);
    bottomPart.getChildren().addAll(deleteButton, backButton);
    bottomPart.setAlignment(Pos.CENTER);
    bottomPart.setPrefHeight(150);
    
   
    
    

    //*** end of bottom part ***
    
    
    //Borderpane to organize the 3 different parts
    BorderPane root = new BorderPane();
    root.setPadding(new Insets(10));
    root.setTop(table);
    root.setCenter(middlePart);
    root.setBottom(bottomPart);
    
    //Create the scene 
    Scene ownerBookScreen = new Scene(root, 600, 800);
    primaryStage.setTitle("Owner's Books Screen");
    primaryStage.setScene(ownerBookScreen);
    primaryStage.show();
    return;
}    
    
//Method for reading data from books.txt and loading its values into the bookList 
public void loadBooksfromFile(){
    
    File file = new File("books.txt");
    
    
    try{
        //If file doesn't exist
        if (file.exists() == false){
            file.createNewFile(); //Create new File named books.txt    
        }
        
        
        BufferedReader reader = new BufferedReader(new FileReader("books.txt"));
        String line;
        while ((line = reader.readLine()) != null){ //Read each line of txt until there is no more text
            String [] parts = line.split(",");
            String title = parts[0];
            double price = Double.parseDouble(parts[1]);
            bookList.add(new Book(title,price));
        }
        reader.close();
        
        
    } catch (IOException e){
      e.printStackTrace();
    }
    
    
}

//Method to add books in the books.txt file 
public void saveBooksToFile(){
    
    File file = new File("books.txt");
    
    
    try{ //If the file books.txt doesn't exist
        if(file.exists() == false){
           file.createNewFile(); //Create new File named books.txt
        }
        
        BufferedWriter writer = new BufferedWriter(new FileWriter("books.txt"));
        for (Book book : bookList){
            writer.write(book.getTitle() + "," + book.getPrice());
            writer.newLine();
        }
        writer.close();
    }catch (IOException e){
        e.printStackTrace();
    }
}

public void ownerCustomerScreen(Stage primaryStage){
    customerList = FXCollections.observableArrayList();
    loadCustomers();

    // Create TableView
    customerTable = new TableView<>();
    customerTable.setPrefHeight(500); 
    customerTable.setItems(customerList); 
    
    // Table columns
    TableColumn<Customer, String> nameColumn = new TableColumn<>("Username");
    nameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
    nameColumn.setMinWidth(200);
    
    TableColumn<Customer, String> passwordColumn = new TableColumn<>("Password");
    passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
    passwordColumn.setMinWidth(200);
    
    TableColumn<Customer, Integer> pointsColumn = new TableColumn<>("Points");
    pointsColumn.setCellValueFactory(new PropertyValueFactory<>("points"));
    pointsColumn.setMinWidth(200);
    
    
    customerTable.getColumns().addAll(nameColumn, passwordColumn, pointsColumn);
    
    Button addButton = new Button();
    addButton.setText("Add Customer");
    
    //Create a placeholder for both textfield
    TextField newUsername = new TextField();
    newUsername.setPromptText("Username"); //placeholder text
    newUsername.setMaxWidth(200);
    newUsername.setMaxHeight(30);
    
    TextField userPass = new TextField();
    userPass.setPromptText("Password");
    userPass.setMaxWidth(200);
    userPass.setMaxHeight(30);
    
    //Hbox to align name and price of book and add button
    HBox customerDetail = new HBox(10);
    customerDetail.getChildren().addAll(newUsername,userPass, addButton);
    customerDetail.setAlignment(Pos.CENTER);
    
    //Create the layout
    VBox middlePart = new VBox(10); 
    middlePart.getChildren().addAll(customerDetail);
    middlePart.setAlignment(Pos.CENTER);
    //Make it tiny so that top part has a bigger space
    middlePart.setPrefHeight(150);
    
    
    //Function for handling add when clicked
    addButton.setOnAction(event -> {
        
        String userText = newUsername.getText().trim(); 
        String passText = userPass.getText().trim();         

        //Try and catch to check for invalid inputs
     
        //If one of the input is empty, show alert
        if(userText.isEmpty() || passText.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setHeaderText(null);
            alert.setContentText("Please enter both username and password!");
            alert.showAndWait();
            return;
        }
        
        
        //Check for duplicates
        for (Customer customer : customerList){
            if(customer.getUsername().equalsIgnoreCase(userText)){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Input");
                alert.setHeaderText(null);
                alert.setContentText("Username already exists! Please enter a different username.");
                alert.showAndWait();
            return;   
            
          
                
            }
            //if they type admin for username
            if(userText.equalsIgnoreCase("admin")){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Input");
                alert.setHeaderText(null);
                alert.setContentText("Username already exists! Please enter a different username.");
                alert.showAndWait();
                return;   
                
            }
            
        }
        
        Customer customer = new Customer(userText, passText, 0);
        customerList.add(customer);
        //Clear input fields after adding
        newUsername.clear();
        userPass.clear();
        saveCustomers();   
    });
    
    Button deleteButton = new Button();
    deleteButton.setText("Delete");
    
    
    Button backButton = new Button();
    backButton.setText("Back");
    //Handle backButton  when clicked
    backButton.setOnAction(event -> ownerStartScreen(primaryStage));
    
    
    
    //Handle deleteButton when clicked
    deleteButton.setOnAction(event -> {
        
        Customer selectedUser = customerTable.getSelectionModel().getSelectedItem(); //Highlihts the book that is currently selected
        if (selectedUser!= null){
            customerList.remove(selectedUser); 
            saveCustomers(); 
        }
        else{
          //show error if no book is selected
          Alert alert = new Alert(Alert.AlertType.ERROR);
          alert.setTitle("Customer Not Selected");
          alert.setHeaderText(null);
          alert.setContentText("Please select a customer to be deleted!");
          alert.showAndWait(); 
        }
        
        
    });

    //Align them horizontally
    HBox bottomPart = new HBox(10);
    bottomPart.getChildren().addAll(deleteButton, backButton);
    bottomPart.setAlignment(Pos.CENTER);
    bottomPart.setPrefHeight(150);

    //*** end of bottom part ***
        
    //Borderpane to organize the 3 different parts
    BorderPane root = new BorderPane();
    root.setPadding(new Insets(10));
    root.setTop(customerTable);
    root.setCenter(middlePart);
    root.setBottom(bottomPart);
    
    //Create the scene 
    Scene ownerCustomerScreen = new Scene(root, 600, 800);
    primaryStage.setTitle("Owner's Customers List");
    primaryStage.setScene(ownerCustomerScreen);
    primaryStage.show();
    return;
}    
      

public void loadCustomers(){
    File file = new File("customers.txt");
    
    try{
        //If file doesn't exist
        if (file.exists() == false){
            file.createNewFile(); //Create new File named books.txt    
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
}

public void saveCustomers(){
    try (BufferedWriter writer = new BufferedWriter(new FileWriter("customers.txt"))) {
        for (Customer customer : customerList) {
            writer.write(customer.getUsername() + "," + customer.getPassword() + "," + customer.getPoints());
            writer.newLine();
        }
    } catch (IOException e) {
        System.out.println("Error saving customers.");
    }
}
    @Override
    public boolean login(){
        return adminName.equals("admin") && adminPass.equals("admin");
    }
    
    @Override
    public void logout(Stage primaryStage){
        try{
            //Create new instance of main application class
            new bookStore().start(primaryStage);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    

}