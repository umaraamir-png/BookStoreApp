package project;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Book {
    // Instance Variables
    private String title;
    private double price;
    private BooleanProperty selected = new SimpleBooleanProperty(false); // added for checkbox

    // Constructor
    public Book(String title, double price) {
        this.title = title;
        this.price = price;
    }

    // Method to show details of book
    public String showDetail() {
        return "Book title: " + this.title + " | Price: " + this.price;
    }

    // Getters
    public String getTitle() {
        return this.title;
    }

    public double getPrice() {
        return this.price;
    }

    // Getter for checkbox selection (used by JavaFX TableView)
    public Boolean getSelected() {
        return selected.get();
    }

    public void setSelected(Boolean selected) {
        this.selected.set(selected);
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }
}