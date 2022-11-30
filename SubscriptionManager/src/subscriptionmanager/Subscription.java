/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package subscriptionmanager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author hariri
 */
public class Subscription {
        private final String name;
    private final String packageWatned;
    private final int duration;
    private final String discountCode;
    private final String payment;
    private final String costInPences ;
    private final String currentDate;
    

    public Subscription(String name, int packageWatned, int duration, String discountCode, int payment) {
        double[][] table = { // nice and decent way to repesent the table
            {1, 3, 6, 12},
            {6, 5, 4, 3},
            {8, 7, 6, 5},
            {9.99, 8.99, 7.99, 6.99}
        };
        this.name = name;
        this.currentDate   = DateHelper.getDate(); 
        this.packageWatned = packageWatned == 1 ? "B" : packageWatned == 2 ? "S" : "G";
        this.duration = (int) table[0][duration - 1] ;
        this.discountCode = discountCode;
        this.payment = (payment == 1 ? "O" : "M");
            
        double cost = table[0][duration - 1] * table[packageWatned][duration - 1];

        if (!discountCode.equals("-")) {
            double discountValue = Integer.parseInt(discountCode.substring(discountCode.length() - 1, discountCode.length())) / 100.0; // get the discount from the code
            cost -= cost * discountValue; // if a discount code found will be applied
        }
        if (payment == 1) {
            cost -= cost * 0.05;  // if a one-off payment a 5% discount applied 
        }
        this.costInPences = Double.toString(cost * 100).substring(0, Double.toString(cost * 100).lastIndexOf(".")); // convert the cost to pences by multibly 100 and get rid of the . and the 0 after it; ready to store in the file

                
    }
  
    public String getLine(){
    String line = this.currentDate + "\t" + this.packageWatned
                + "\t" + this.duration + "\t" + discountCode + "\t" + this.payment
               +"\t"  + this.costInPences + "\t" + this.name + "\n";
    return line;
    }

    public void saveToFile(){

    try {
            File myObj = new File(System.getProperty("user.dir") + "/" + "subscription.txt"); // just to make sure it's created in the current directory
            if (!myObj.exists()) { // check if the file exists, if not will be created
                myObj.createNewFile();
            }
        } catch (IOException e) {
            System.out.println("An error occurred. ");
        }
    
    String line = currentDate + "\t" + this.packageWatned
                + "\t" + this.duration + "\t" + discountCode + "\t" + this.payment + "\t"
                + this.costInPences + "\t" + this.name + "\n";
            try {

            try ( FileWriter myWriter = new FileWriter("subscription.txt", true)) {
                myWriter.append(line);
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
        }
    
    }

}
