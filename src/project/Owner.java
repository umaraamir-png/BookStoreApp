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
    
    //logout button
    Button logout = new Button(); 
    logout.setText("Logout");
    logout.setMaxWidth(150);
    //When logout is clicked, go back to login-screen
    logout.setOnAction(event -> {
        try{
            //Create new instance of main application class
            new bookStore().start(primaryStage);
        }catch(Exception e) {
            e.printStackTrace();
        }
        
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

    
    
//Owner-books-screen (when button hooks is clicked)
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
        double priceValue = Double.parseDouble(priceText); //converts the string of price to a double
        

        //Try and catch to check for invalid inputs
    try {  
        //If one of the input is empty, show alert
        if(titleText.isEmpty() || priceText.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setHeaderText(null);
            alert.setContentText("Please enter both title and price!");
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
        
        
        
        } catch (NumberFormatException e){ //Catch when the input is not a valid input (mixed characters or not a number)
          Alert alert = new Alert(Alert.AlertType.ERROR);
          alert.setTitle("Invalid Input");
          alert.setHeaderText(null);
          alert.setContentText("Price must be a valid number!");
          alert.showAndWait(); 
        }
        
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


}