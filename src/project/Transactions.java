

package project;
import java.util.ArrayList;
public class Transactions {
    
    
    //IV
    private Customer customer;
    private ArrayList<Book> bookCart; //books in cart
    private int pointsEarned;
    private int transactionID;
    
    //METHODS
    public double calculatePrice(){ //calculates price of books in the cart
        int totalPrice=0;
        for (Book book : bookCart){
           totalPrice += book.getPrice();
           
        }
        
      return totalPrice;
    }
    
    
    
}
