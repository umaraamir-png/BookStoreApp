package project;

import javafx.scene.control.PasswordField;
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
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class Customer extends User {

    private String username, password, status;
    private int points = 0;
    private boolean loginStatus;

    private TableView<Book> table;
    private ObservableList<Book> bookList;
    private static int transactionID = 0;

    public Customer(String username, String password, int points) {
        this.username = username;
        this.password = password;
        this.points = points;
    }

    public void customerStartScreen(Stage primaryStage) {
        Label welcomeLabel = new Label("Welcome " + username + "! You have " + points + " points.");

        // Define customer status (Gold/Silver) based on points
        if (points >= 1000) {
            status = "G";
        } else {
            status = "S";
        }

        Label statusLabel = new Label("Your status is: " + status);

        // Create TableView and Columns
        table = new TableView<>();

        TableColumn<Book, String> titleColumn = new TableColumn<>("Book title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Book, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Book, Boolean> selectColumn = new TableColumn<>("Select");
        selectColumn.setCellValueFactory(new PropertyValueFactory<>("selected"));
        selectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selectColumn));
        table.setEditable(true);

        table.getColumns().addAll(titleColumn, priceColumn, selectColumn);

        // Load books from books.txt
        bookList = FXCollections.observableArrayList();
        try (BufferedReader reader = new BufferedReader(new FileReader("books.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String title = parts[0].trim();
                    double price = Double.parseDouble(parts[1].trim());
                    bookList.add(new Book(title, price));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        table.setItems(bookList);

        // Buttons
        Button buyButton = new Button("Buy");
        Button redeemButton = new Button("Redeem Points and Buy");
        Button logoutButton = new Button("Logout");

        buyButton.setOnAction(e -> handlePurchase(primaryStage, false));
        redeemButton.setOnAction(e -> handlePurchase(primaryStage, true));
        logoutButton.setOnAction(e -> logout(primaryStage));

        VBox layout = new VBox(10, welcomeLabel, statusLabel, table, buyButton, redeemButton, logoutButton);
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void handlePurchase(Stage stage, boolean redeemPoints) {
        transactionID += 1;
        if (redeemPoints && !"G".equals(status)) {
            showAlert("Unable to redeem points! Please reach Gold status first."); 
            return; 
        }
        
        ArrayList<Book> selectedBooks = new ArrayList<>();
        for (Book book : bookList) {
            if (book.getSelected() && !selectedBooks.contains(book)) {
                selectedBooks.add(book);
            }
            
        }

        if (selectedBooks.isEmpty()) {
            showAlert("Please select at least one book.");
            return;
        }

        

        // Create a transaction
        Transactions transaction = new Transactions(this, selectedBooks, transactionID, redeemPoints);
        transaction.buyBooks();
        
        //Points and status updated in before showing transaction summary
        this.points = getPoints();
        this.status = getStatus();
        
        showTransactionSummary(stage, transaction.getFinalCost());

        updatePoints(); 
        updateBooks(selectedBooks);
    }   
    
    private void showTransactionSummary(Stage stage, double finalCost) {
        
        Label costLabel = new Label("Total Cost: $" + String.format("%.2f", finalCost));
        costLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label infoLabel = new Label("Points: " + getPoints() + ", Status: " + getStatus());
        infoLabel.setStyle("-fx-font-size: 14px;");

        Button logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-font-size: 13px; -fx-padding: 8px 16px;");
        logoutButton.setOnAction(e -> logout(stage));

        VBox layout = new VBox(20, costLabel, infoLabel, logoutButton);
        layout.setPadding(new Insets(30));
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 400, 250);
        stage.setScene(scene);
        stage.show();
    }   
   
    
    public void updatePoints(){
        bookStore store = new bookStore();
        store.rewritePoints(points, username);   
    }
    
    public void updateBooks(ArrayList <Book> selectedBooks){
        Iterator<Book> iterator = bookList.iterator();
        while (iterator.hasNext()) {
            if (selectedBooks.contains(iterator.next())) {
                iterator.remove();
            }
        }
        try(BufferedWriter writer = new BufferedWriter(new FileWriter("books.txt"))){
           for (Book book : bookList){
                writer.write(book.getTitle() + "," + book.getPrice());
                writer.newLine();
            }
            writer.close(); 
        }catch(IOException e){
            e.printStackTrace();
        }    
    }

    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public int getPoints() {
        return this.points;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public void logout(Stage primaryStage) {
        try {
            new bookStore().start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
