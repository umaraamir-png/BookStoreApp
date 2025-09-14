
package project;


public class Book {
    //IV
    private String title;
    private double price;
    
    //Constructor
    public Book(String title, double price){
        this.title=title;
        this.price=price;
    }
    
    //method to show details of book
    public String showDetail(){
        
        return ("Book title: " +this.title +"Price: " + this.price);
    }
    
    
    //getter for the price and title
    public String getTitle(){
        return this.title;
    }
    
    public double getPrice(){
        return this.price;
    }
    
    
}
