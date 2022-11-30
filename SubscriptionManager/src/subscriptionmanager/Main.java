package subscriptionmanager;

import java.io.BufferedReader;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author Abdulrahman Al Hariri / c2644873
 */
public class Main {

    private static final DecimalFormat df = new DecimalFormat("0.0");

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        while (true) {

            int selected = menu();
            if (selected == 0) {
                break;
            }
            switch (selected) {
                case 1:
                    newSubscription();
                    break;
                case 2:
                    displaySummary();
                    break;
                case 3:
                    displayMonthSummary();
                    break;
                case 4:
                    searchFor();
                    break;

            }
            
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

        String name = getName();
        int packageWatned = getPackage();
        int duration = getDoration();
        String discountCode = getDiscountCode();
        int payment = getPayment();
        System.out.println();

        Subscription person = new Subscription(name, packageWatned, duration, discountCode, payment);
        person.saveToFile();
        printSummary(person.getLine());

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
        while (true) {
            code = askUser("your discount code");
            if (code.equalsIgnoreCase("x")) {
                code = "-";
                break;
            }

            if (validateCode(code)) {
                break;
            } else {
                System.out.println("");
                System.out.println("if you wish to cancel entering the code, Enter: x");
            }
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
        String date = "     Date: " + array[0] + "     " + "Discount Code: " + array[3];
        String packageString = array[1].equalsIgnoreCase("s") ? "Silver" : array[1].equalsIgnoreCase("B") ? "Bronze" : "Gold";
        String durationString = array[2].equalsIgnoreCase("1") ? "One" : array[2].equalsIgnoreCase("3") ? "Three" : array[2].equalsIgnoreCase("6") ? "Six" : "Twelve";
        String packDur = "  Package: " + packageString + " ".repeat(14) + "Duration: " + durationString;
        String term = "    Terms: " + (array[4].equalsIgnoreCase("o") ? "One-off" : "Monthly");
        String price = array[5];
        String last2Dig = price.substring(price.length() - 2);
        String firstDigits = price.substring(0, price.length() - 2);
        price = firstDigits + "." + last2Dig;

        String subsc = (array[4].equalsIgnoreCase("o") ? "One-off" : "Monthly") + "  Subscription: " + "£" + price;

        // chars used 
        int middelSpace = 49;
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
    // End of new subscription functionlity 

    // Start of  Display month summary  
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
    // End of new subscription functionlity 

    //    Start of  Display month summary  
    public static void displayMonthSummary() {
        System.out.println(" display month summary");

        int chosenFile = askUser("Which file would you see summary for?", "1- Current \n2- Sample", 2, false);
        String fileToLookIn = chosenFile == 1 ? "current.txt" : "sample.txt"; // get the file the user would like to see a summary for
        String chosenMonth = getMonth();  // get the month the user would like to see a summary for 

        int totalSubInMonth = 0;
        boolean isEmpty = true;
        int goldAppear = 0;
        int selvierAppear = 0;
        int bronzeAppear = 0;
        double fee = 0;

        try ( BufferedReader reader = new BufferedReader(new FileReader(fileToLookIn))) {
            String line = reader.readLine();

            while (line != null) {
                isEmpty = false;
                String[] array = line.split("\t");
                String month = array[0].split("-")[1]; // Get the month

                if (month.equalsIgnoreCase(chosenMonth)) {
                    totalSubInMonth++;
                    String fee1 = array[5].substring(0, (array[5].length() - 2));
                    String fee2 = "." + array[5].substring((array[5].length() - 2));
                    String feeString = fee1 + fee2;
                    fee += Float.parseFloat(feeString);
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

        printMonthSummary(bronzeAppear, selvierAppear, goldAppear, totalSubInMonth, fee, chosenMonth);
    }

    public static String getMonth() {

        String[] arrayOfMonths = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        HashMap<Integer, String> hashOfMonths = new HashMap<>(); // Initialising the hashmap 
        for (int i = 0; i < arrayOfMonths.length; i++) { //filling the hash map according with the array of months and add 1 to each so jan will be the value of the key 1 and so on
            hashOfMonths.put(i + 1, arrayOfMonths[i]);
        }

        System.out.println("You can enter a month nubmber like 1, 2, 3 or the first three letters of the month like Jan, Feb, dec... ignored cases");
        boolean isMonthFound = false;
        String month = "";
        while (!isMonthFound) {
            String chosenMonth = askUser("the month you would like to see a summary for:");

            if (isNumeric(chosenMonth)) { // check if it's a number 
                if (Float.parseFloat(chosenMonth) > 0 && Float.parseFloat(chosenMonth) < 13) { // check if it's a correct month number
//                    monthsNumber = (int) Float.parseFloat(chosenMonth); // so even if the user enter 2.3 will be taken as 2
                    for (int i : hashOfMonths.keySet()) {
                        if (i == (int) Float.parseFloat(chosenMonth)) {
                            month = hashOfMonths.get((i));
                            isMonthFound = true;
                            break;
                        }
                    }
                    break;
                } else {
                    System.out.println("Well, it's a number but it's an invalid month's number");
                }

            } else {
                for (int i : hashOfMonths.keySet()) {
                    if (hashOfMonths.get(i).equalsIgnoreCase(chosenMonth)) { // compare if the string is a valid month 
                        month = hashOfMonths.get((i));
                        isMonthFound = true;
                        break;
                    }
                }

            }
            System.out.println("Ooops, invalid month :(");

        }
        return month;

    }

    public static void printMonthSummary(int bronze, int silver, int gold, int total, double fee, String month) {
        System.out.println("Average sus fee : " + df.format(fee / total));
        System.out.println("bronze : " + df.format(bronze / (float) total * 100.0));
        System.out.println("Silver : " + df.format(silver / (float) total * 100.0));
        System.out.println("Gold: " + df.format(gold / (float) total * 100.0));
        System.out.println(total);
        String line1 = " Total number of subscriptions for " + month + ": " + total;
        String line2 = " Average subscription fee: £" + df.format(fee / total);
        String line3 = " Percentage of subscriptions:";
        String edge = "|";
        String space = " ";
        System.out.println("-".repeat(72));
        System.out.println(edge + line1 + space.repeat(70 - line1.length()) + edge);
        System.out.println(edge + line2 + space.repeat(70 - line2.length()) + edge);
        System.out.println(edge + space.repeat(70) + edge);
        System.out.println(edge + line3 + space.repeat(70 - line3.length()) + edge);
        if (bronze > 1) {
            String line4 = " bronze : " + df.format(bronze / (float) total * 100.0);
            System.out.println(edge + line4 + space.repeat(70 - line4.length()) + edge);
        }
        if (silver > 1) {
            String line5 = " bronze : " + df.format(silver / (float) total * 100.0);
            System.out.println(edge + line5 + space.repeat(70 - line5.length()) + edge);
        }
        if (gold > 1) {
            String line6 = " bronze : " + df.format(gold / (float) total * 100.0);
            System.out.println(edge + line6 + space.repeat(70 - line6.length()) + edge);
        }
        System.out.println("-".repeat(72));
    }
    //    End of  Display month summary  

    //    Start of search for a name 
    public static void searchFor() {
        System.out.println("Search for a user");
        int chosenFile = askUser("Which file would you see summary for?", "1- Current \n2- Sample", 2, false);
        String fileToLookIn = chosenFile == 1 ? "current.txt" : "sample.txt"; // get the file the user would like to see a summary for

        String name = askUser("name you want to search for");

        boolean isEmpty = true;

        try ( BufferedReader reader = new BufferedReader(new FileReader(fileToLookIn))) {
            String line = reader.readLine();

            while (line != null) {
                isEmpty = false;
                String[] array = line.split("\t");
                if (array[6].contains(name)) {
                    printSummary(line);
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

    }
    //    End of search for a name 

    public static boolean validateCode(String code) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy"); // a format to get the month as a number so I can comapre the month easily
        Date date = new Date();

        String[] arrayDate = formatter.format(date).split("/");

        Integer currentMonth = Integer.valueOf(arrayDate[1]); // getting the current month in an integer format so I can use it to validate the Discound code
        String currentYear = arrayDate[2].substring(arrayDate[2].length() - 2, arrayDate[2].length()); // getting the current year which is the last two digits

        if (code.trim().length() == 6) { // check for the length should be 6
            if (code.substring(2, 4).equalsIgnoreCase(currentYear)) { // check for the 2nd, 3rd numbers sould match the current year.
                String[] codeArray = code.split("");

                if (!isNumeric(codeArray[0]) && !isNumeric(codeArray[1]) && !isNumeric(codeArray[4])) { // check that 1st, 2nd, and 5th are letters not numbers
                
                    if ((codeArray[4].equalsIgnoreCase("L") && currentMonth > 6) || (codeArray[4].equalsIgnoreCase("E") && currentMonth <= 6)) { //check for the 5th letter is E or L 

                        if (isNumeric(codeArray[5]) && !codeArray[5].equals("0")) { // check for the last letter which should be number && not 0 ;
                            System.out.println("*".repeat(15) + "    (" + code + " Code Accepted)    " + "*".repeat(15));
                            return true;
                        } else {
                            System.out.println("The last char should be number!");
                        }
                    } else {
                        System.out.println("The fifth letter should be 'E' or 'L' And compatabel with the current month!");
                    }
                } else {
                    System.out.println("Check the first, second or fifth, should be letters!");
                }
            } else {
                System.out.println("The year dose not match");
            }
        } else {
            System.out.println("Check your Code length"); // it's not valid length
        }
        return false;
    }

}
