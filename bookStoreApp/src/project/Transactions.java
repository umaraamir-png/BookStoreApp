package project;

import java.util.ArrayList;

public class Transactions {

    // Instance Variables (IV)
    private int pointsUsed; 
    private Customer customer;
    private ArrayList<Book> bookCart; // books in cart
    private int pointsEarned;
    private static int transactionID;
    private boolean redeemState;
    private double totalPrice = 0;

    // Constructor
    public Transactions(Customer customer, ArrayList<Book> bookCart, int transactionID, boolean redeemState){
        this.customer = customer; 
        this.bookCart = bookCart; 
        this.transactionID = transactionID; 
        this.redeemState = redeemState;
    }
    
    public void buyBooks() {
        // Reset total price before recalculating
        totalPrice = calculatePrice();  

        // Handle Points Redemption 
        if (redeemState) {
            int points = customer.getPoints();
            int costInPoints = (int) (totalPrice * 100); // Convert dollars to points

            if (points >= costInPoints) {
                // Customer has enough points to cover the full purchase
                customer.setPoints(points - costInPoints);
                totalPrice = 0; 
                pointsEarned = 0;  // No points earned when paying with points
            } else { 
                //Partial payment using points, remaining balance is paid with cash
                int pointsUsed = points; //use up all points    
                totalPrice -= pointsUsed /100;  //deduct points from total cost
                customer.setPoints(0);  // points become zero        
                pointsEarned = (int) (totalPrice * 10); // Earn points only on remaining balance
                customer.setPoints(pointsEarned);
            }
        } else {
            // If not redeeming points, earn points normally
            pointsEarned = (int) (totalPrice * 10);
            customer.setPoints(customer.getPoints() + pointsEarned);
        }

        updateStatus();
    }

    // Ensures `totalPrice` is correctly calculated
    public double calculatePrice() {
        double price = 0;
        for (Book book : bookCart) {
            price += book.getPrice();
        }
        return price;
    }

    private int calculatePoints() {
        double total = calculatePrice();
        return (int) (total * 100);
    }

    public double getFinalCost(){ 
        return totalPrice; 
    }
    
    // undating the customer status based on new points total 
    
    public void updateStatus(){ 
        double currentPoints = customer.getPoints(); 
        
        currentPoints = customer.getPoints();
        
    
        if (currentPoints >= 1000){
            customer.setStatus("Gold"); 
        }else{
            customer.setStatus("Silver");
    }
    }
    
     public String getTransactionSummary(){
         return "Total cost: $" + String.format("%.2f", getFinalCost()) + "\n" + "Points: "+ customer.getPoints() + "\n" + "Status: " + customer.getStatus(); 
     }
        
    }
