/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package subscriptionmanager;

import java.io.BufferedReader;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;

/**
 *
 * @author YOUR NAME
 */
public class Main {

    private static final DecimalFormat df = new DecimalFormat("0.0");

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int selected = menu();
        switch (selected) {
            case 0:
                System.out.println("selected is 0 ");
                System.exit(0);
                break;
            case 1:
                System.out.println("selected is 1 ");
                newSubscription();
                break;
            case 2:
                System.out.println("selected is 2 ");
                displaySummary();
                break;
            case 3:
                System.out.println("selected is 3 ");
                break;
            case 4:
                System.out.println("selected is 4 ");
                break;
                    
                

        }
        System.out.println("Thanks for using my System, hava a good day and see you soon :)");
    }

    public static int menu() {
        int choice;
        String options = "\n1- Enter new Subscription "
                + "\n2- Display summary of subscriptsions"
                + " \n3- Display summary of subscription for selected month "
                + "\n4- Find and display subscription "
                + "\n0- Exit";
        choice = askUser("Choose one of the follwoing ", options, 4, true);
        return choice;
    }

    // start of new subscription functionlity 
    public static void newSubscription() {
        System.out.println("New sub");
        String currentDate = DateHelper.getDate();

        String name = getName();
        int packageWatned = getPackage();
        int duration = getDoration();
        String discountCode = getDiscountCode();
        int payment = getPayment();
        System.out.println("=".repeat(20));
        double[][] table = { // nice and decent way to repesent the table
            {1, 3, 6, 12},
            {6, 5, 4, 3},
            {8, 7, 6, 5},
            {9.99, 8.99, 7.99, 6.99}
        };
        double cost = table[0][duration - 1] * table[packageWatned][duration - 1];

        if (!discountCode.equals("-")) {
            double discountValue = Integer.parseInt(discountCode.substring(discountCode.length() - 1, discountCode.length())) / 100.0; // get the discount from the code
            cost -= cost * discountValue; // if a discount code found will be applied
        }
        if (payment == 1) {
            cost -= cost * 0.05;  // if a one-off payment a 5% discount applied 
        }
        String costInPences = Double.toString(cost * 100).substring(0, Double.toString(cost * 100).lastIndexOf(".")); // convert the cost to pences by multibly 100 and get rid of the . and the 0 after it; ready to store in the file

        try {
            File myObj = new File(System.getProperty("user.dir") + "/" + "subscription.txt"); // just to make sure it's created in the current directory
            System.out.println(myObj.exists());
            if (!myObj.exists()) { // check if the file exists, if not will be created
                myObj.createNewFile();
            }
        } catch (IOException e) {
            System.out.println("An error occurred. ");
        }
        // gathering the line to be appended and printed
        String line = currentDate + "\t" + (packageWatned == 1 ? "B" : packageWatned == 2 ? "S" : "G")
                + "\t" + (int) table[0][duration - 1] + "\t" + discountCode + "\t" + (payment == 1 ? "O" : "M") + "\t"
                + costInPences + "\t" + name + "\n";
        try {

            try ( FileWriter myWriter = new FileWriter("subscription.txt", true)) {
                myWriter.append(line);
                printSummary(line);// if no errors and it's appended print it;
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
        }
    }

    public static String getName() {
        boolean flag;
        String[] answerArray = {};
        do {
            flag = false;
            String name = askUser("Full costomer's name: ");
            answerArray = name.split(" ");
            for (String letter : name.split("")) {
                if (isNumeric(letter)) {
                    System.out.println("Number " + letter + " found, You can't have numbers in your name!");
                    System.out.println("Please rewrite your name correctly");
                    flag = true;
                    break;
                }
            }
            if (answerArray.length < 2) {
                System.out.println("Please enter both first and last names");
                flag = true;
            }
            if (name.length() > 24) {
                flag = true;
                System.out.println("Name can't be over 25 letter");
            }
            //            check for the name here!
        } while (flag);
        String firstName = answerArray[0].substring(0, 1).toUpperCase();
        char firstLetterInLastName = answerArray[answerArray.length - 1].charAt(0); // Get the first letter from the last name 
        char capitalLetter = Character.toUpperCase(firstLetterInLastName); // Capitalize the letter
        String lastName = answerArray[answerArray.length - 1].replace(answerArray[answerArray.length - 1].charAt(0), capitalLetter); // Replace the first letter with capitalized created one 
        String name = firstName + " " + lastName;
        return name;
    }

    public static int getPackage() {
        return askUser("Choose a package ", "1- Bronze"
                + "\n2- Silver \n3- Gold ", 3, false);
    }

    public static int getDoration() {
        String options = "1- one month \n2- Three months \n3- six months \n4- one year( twelve months )";
        return askUser("Choose a duration", options, 4, false);
    }

    public static String getDiscountCode() {
        String code = "-";
        int choice = askUser("Do you have a discount code?", "1- Yes \n2- No", 2, false); // ask if he has a code
        if (choice == 2) {
            return code;
        }
        String currentDate = DateHelper.getDate();
        String currentYear = currentDate.substring(currentDate.length() - 2, currentDate.length()); // getting the current year which is the last two digits

        while (true) {
            code = askUser("your discount code");
            if (code.equalsIgnoreCase("x")) {
                code = "-";
                break;
            }

            if (code.trim().length() == 6) { // check for the length should be 6

                if (code.substring(2, 4).equalsIgnoreCase(currentYear)) { // check for the 2nd, 3rd numbers sould match the current year.
                    String[] codeArray = code.split("");
                    if (!isNumeric(codeArray[0]) && !isNumeric(codeArray[1]) && !isNumeric(codeArray[4])) { // check that 1st, 2nd, and 5th are letters not numbers

                        if (codeArray[4].equalsIgnoreCase("L") || codeArray[4].equalsIgnoreCase("E")) { //check for the 5th letter is E or L 

                            if (isNumeric(codeArray[5]) && !codeArray[5].equals("0")) { // check for the last letter which should be number && not 0 ;
                                System.out.println("*".repeat(20) + " Code Accepted " + "*".repeat(20));
                                return code;
                            }
                        }
                    }
                }
            }
            System.out.println("Code Rejected, check your code and try again");
            System.out.println("if you wish to cancel entering the code, Enter: x");
        }
        return code;
    }

    public static int getPayment() {
        return askUser("Choose a payment methods", "1- One-off payment \n2- Monthly payment", 2, false);
    }

    public static void printSummary(String line) {
        String[] orig = line.split("\n");
        String[] array = orig[0].split("\t");
        String name = " Customer: " + array[6];
        String date = "   Date: " + array[0] + "     " + "Discount Code: " + array[3];
        String packageString = array[1].equalsIgnoreCase("s") ? "Selver" : array[1].equalsIgnoreCase("B") ? "Bronze" : "Gold";
        String durationString = array[4].equalsIgnoreCase("1") ? "One" : array[4].equalsIgnoreCase("3") ? "Three" : array[4].equalsIgnoreCase("6") ? "Six" : "Twelve";
        String packDur = "  Pacage: " + packageString + " ".repeat(14) + "Duration: " + durationString;
        String term = "    Terms: " + (array[4].equalsIgnoreCase("o") ? "One-off" : "Monthly");
        String price = array[5];
        String last2Dig = price.substring(price.length() - 2);
        String firstDigits = price.substring(0, price.length() - 2);
        price = firstDigits + "." + last2Dig;

        String subsc = (array[4].equalsIgnoreCase("o") ? "One-off" : "Monthly") + "  Subscription: " + "£" + price;

        // chars used 
        int middelSpace = 47;
        int num = middelSpace - (((middelSpace - subsc.length()) / 2) + subsc.length());
        String upAndDown = "+" + "=".repeat(middelSpace) + "+";
        String leftAndRight = "|";
        String emptySpace = " ";
        System.out.println(upAndDown);
        System.out.println(leftAndRight + emptySpace.repeat(middelSpace) + leftAndRight); // 2nd empty line 
        System.out.println(leftAndRight + name + emptySpace.repeat(middelSpace - name.length()) + leftAndRight); // 3rd line for name 
        System.out.println(leftAndRight + emptySpace.repeat(middelSpace) + leftAndRight); // 4th empty line 
        System.out.println(leftAndRight + date + emptySpace.repeat(middelSpace - date.length()) + leftAndRight); // 5th ling for date and code
        System.out.println(leftAndRight + packDur + emptySpace.repeat(middelSpace - packDur.length()) + leftAndRight); // 6th line for package and duration
        System.out.println(leftAndRight + term + emptySpace.repeat(middelSpace - term.length()) + leftAndRight); // 7th line terms
        System.out.println(leftAndRight + emptySpace.repeat(middelSpace) + leftAndRight); // empty line
        System.out.println(leftAndRight + emptySpace.repeat((middelSpace - subsc.length()) / 2) + subsc + " ".repeat(num) + leftAndRight); // logic here for one letter space if the number of the spaces is odd 
        System.out.println(leftAndRight + emptySpace.repeat(middelSpace) + leftAndRight); //empty line
        System.out.println(upAndDown);
    }

    // method to ask for String
    public static String askUser(String askFor) {
        String answer = "";
        System.out.println("Enter " + askFor);
        do {
            try {
                Scanner keyboard = new Scanner(System.in);
                answer = keyboard.nextLine();

            } catch (InputMismatchException e) {
                System.out.println("Invail input "); // it will accept numbers but just in case errors happend
            }
        } while (answer.length() < 1); // it run while the string length less than 1

        return answer;
    }

    // overloading method to reutn int choice 
    public static int askUser(String askFor, String options, int numberOfChoices, boolean isZeroUsed) {
        int choice;
        do {
            try {
                System.out.println(askFor);
                System.out.println(options);
                Scanner keyboard = new Scanner(System.in);
                choice = keyboard.nextInt();
                if (choice > numberOfChoices || (choice < (isZeroUsed ? 0 : 1))) { // if the input is number but it's not a valid option
                    System.out.println("\nChoice should be between " + (isZeroUsed ? "0" : "1") + " and " + numberOfChoices);
                }
            } catch (InputMismatchException e) { // if the input is not a number will be handeled here and notify the user
                System.out.println("\nInvalid input, Please write a number ");
                choice = 999;
            }
        } while (choice > numberOfChoices || choice < (isZeroUsed ? 0 : 1)); // it rerun till the user enter number between 0 and the number of choices
        return choice;
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    } // checks for numbers
//start of new subscription

//    Start of the Display summary functionality 
    public static void displaySummary() {
        System.out.println("display summary");

        int chosenFile = askUser("Which file would you see summary for?", "1- Current \n2- Sample", 2, false);
        String fileToLookIn = chosenFile == 1 ? "current.txt" : "sample.txt";

        int totalSubInFile = 0;
        boolean isEmpty = true;
        int goldAppear = 0;
        int selvierAppear = 0;
        int bronzeAppear = 0;

        HashMap<String, Integer> subPerMnonth = new HashMap<>();

        try ( BufferedReader reader = new BufferedReader(new FileReader(fileToLookIn))) {
            String line = reader.readLine();

            while (line != null) {
                isEmpty = false;
                String[] array = line.split("\t");
                totalSubInFile++;
                switch (array[1]) {
                    case "B":
                        bronzeAppear++;
                        break;
                    case "G":
                        goldAppear++;
                        break;
                    case "S":
                        selvierAppear++;
                        break;
                }
                int appear = 1;
                String month = array[0].split("-")[1]; // Get the month
                if (!subPerMnonth.containsKey(month)) {
                    subPerMnonth.put(month, appear);
                } else {
                    int currentValue = subPerMnonth.get(month);
                    currentValue++;
                    subPerMnonth.put(month, currentValue);
                }
                line = reader.readLine();
            }
            reader.close();
            if (isEmpty) {
                System.out.println("The chosen file is empty");
            }
        } catch (IOException ex) {
            System.out.println("Unable to open file for writing...");
        }

        printInfoSummary(totalSubInFile, goldAppear, bronzeAppear, selvierAppear, subPerMnonth);

    }

    public static void printInfoSummary(int totalSubInFile, int goldAppear, int bronzeAppear, int selvierAppear, HashMap subPerMnonth) {
        String[] arrayOfMonths = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        double totalAppearnce = bronzeAppear + goldAppear + selvierAppear;
        String edges = "|";

        String space = " ";

        System.out.println("-".repeat(71));
        String toBePrinted = edges + " Total number of subscrtiptoins: " + totalSubInFile;
        System.out.println(toBePrinted + space.repeat(70 - toBePrinted.length()) + edges);
        toBePrinted = edges + " Average monthly subscription: " + Math.round(totalSubInFile / (double) 12 * 100.0) / 100.0;
        System.out.println(toBePrinted + space.repeat(70 - toBePrinted.length()) + edges);
        System.out.println(edges + space.repeat(70 - 1) + edges);
        toBePrinted = edges + " Precentage of subscriptions:";
        System.out.println(toBePrinted + space.repeat(70 - toBePrinted.length()) + edges);
        toBePrinted = edges + " Bronze: " + (bronzeAppear == 0 ? 0 : df.format(((bronzeAppear * 100.0) / totalAppearnce))) + "%";
        System.out.println(toBePrinted + space.repeat(70 - toBePrinted.length()) + edges);
        toBePrinted = edges + " Silver: " + (selvierAppear == 0 ? 0 : df.format(((selvierAppear * 100.0) / totalAppearnce))) + "%";
        System.out.println(toBePrinted + space.repeat(70 - toBePrinted.length()) + edges);
        toBePrinted = edges + " Gold: " + (goldAppear == 0 ? 0 : df.format(((goldAppear * 100.0) / totalAppearnce))) + "%";
        System.out.println(toBePrinted + space.repeat(70 - toBePrinted.length()) + edges);
        System.out.println(edges + space.repeat(70 - 1) + edges);
        System.out.print(edges + space);
        toBePrinted = "";
        for (String i : arrayOfMonths) {
            toBePrinted += i + "  "; // loop throught the months array and print every month in it and 2 spaces after it
        }
        System.out.println(toBePrinted + space.repeat(70 - toBePrinted.length() - 2) + edges);
        toBePrinted = " ";
        for (String i : arrayOfMonths) {
            // Enter every month as a key for the hash map so I get the value for each one same order
            toBePrinted += subPerMnonth.get(i) != null ? subPerMnonth.get(i) : "0"; // if the file has no month return 0 istaed of null
            toBePrinted += subPerMnonth.get(i) != null ? " ".repeat(5 - subPerMnonth.get(i).toString().length()) : space.repeat(4); //  to adjust the spaces after the number so if 999 or 1 will give the same spaces after it
        }
        System.out.println(edges + toBePrinted + space.repeat(70 - toBePrinted.length() - 1) + edges);
        System.out.println("-".repeat(71)); 


    }
}
