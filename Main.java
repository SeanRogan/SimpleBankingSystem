package banking;


import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;

import java.util.Scanner;

public class Main {
    //initialize bank class with id number.
    final public static Bank bank = new Bank("400000");
    final public static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) throws SQLException {
        Database db = new Database(args);
        db.createCardTable();

        boolean running = true;
        while(running) {
            printMenu();
            //take number as input
            try {
                int input = scan.nextInt();

                switch (input) {
                    case 0: {
                        running = false;
                    }
                    break;
                    case 1: {
                        createAccount(bank, db);
                    }
                    break;
                    case 2: {
                        if(accessAccount(bank, db)) {
                            running = false;
                        }
                    }
                    break;

                    default:
                        System.out.println("Please choose either 0, 1, or 2!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Please choose either 0, 1, or 2!\n");
            }
            //clear scanner for next input
            scan.nextLine();
        }
        System.out.println("Bye!");
        scan.close();

    }

    private static boolean accessAccount(Bank bank, Database db) {
        //returns a boolean, true if the program is to exit, false otherwise
        boolean loggedIn;
        String accountNumber;
        System.out.println("Enter your card number:");
        try {
            String inputCardNumber = scan.next();
            if(inputCardNumber.length() == 16) {
                accountNumber = inputCardNumber.substring(6, 15);
            } else return false;
            System.out.println("Enter your PIN:");
            String inputPinNumber = scan.next();

            //if wrong credentials, exit to menu
            if(!checkLoginCredentials(bank, db, inputCardNumber, inputPinNumber)) {
                return false;
            }
            loggedIn = true;
            System.out.println("You have successfully logged in!\n");

            while (loggedIn) {
                System.out.println("1 Balance\n2. Add income\n3. Do transfer\n4. Close account\n5. Log out\n0. Exit\n");
                int input = scan.nextInt();
                switch (input) {
                    case 0: {
                        return true;
                    }
                    case 1: {
                        //GET BALANCE
                        //formats balance as float to two decimals
                        //System.out.printf("%.2f", bank.getBankAccounts().get(accountNumber).getBalance());
                        System.out.printf("%.2f", db.queryBalance(inputCardNumber));
                        return false;
                    }
                    case 2: {
                        //DEPOSIT
                        System.out.println("Enter income:");
                        float deposit = scan.nextInt();
                        float balance = db.queryBalance(inputCardNumber);
                        float newBalance = balance + deposit;
                        bank.getBankAccounts().get(accountNumber).depositFunds(deposit);
                        db.updateBalance(Float.toString(newBalance) , inputCardNumber);
                        System.out.println("Income was added!");
                        return false;
                    }
                    case 3: {
                        //TRANSFER
                        System.out.println("Transfer");
                        System.out.println("Enter card number:");
                        String cardNumber = scan.nextLine();
                        System.out.println("Enter how much money you want to transfer:");
                        String transfer = scan.nextLine();
                        if(Float.parseFloat(transfer) > db.queryBalance(inputCardNumber)) {
                            System.out.println("Not enough money!");
                            return false;
                        }
                        if(cardNumber.equals(inputCardNumber)) {
                            System.out.println("You can't transfer money to the same account!");
                            return false;
                        }
                        if(!new LuhnAlgorithmChecker().verifyCardNumber(cardNumber)) {
                            System.out.println("Probably you made a mistake in the card number. Please try again!");
                            return false;
                        }
                        if(!findAccount(db , cardNumber)) {
                            System.out.println("Such a card does not exist.");
                            return false;
                        }
                        //if it gets past all the exceptions, transfer the money
                        float currentBalance = db.queryBalance(cardNumber);
                        String newBalance = Float.toString(currentBalance + Float.parseFloat(transfer));
                        db.updateBalance(newBalance, cardNumber);
                        System.out.println("Success!");
                    }
                    break;
                    case 4: {
                        //CLOSE ACCOUNT

                        bank.getBankAccounts().remove(inputCardNumber);
                        db.deleteAccount(inputCardNumber);
                        System.out.println("The account has been closed!");
                        return false;
                    }

                    case 5: {
                        //LOG OUT
                        System.out.println("You have successfully logged out!");
                        loggedIn = false;
                    }
                    break;
                    default:
                        System.out.println("Please choose a number, from 0 to 5!");
                        return false;
                }
            }
        } catch (Exception e){
            System.out.println("Sorry! That was not a valid input.");
            return false;
        }
        return false;
    }

    private static boolean findAccount(Database db, String cardNumber) {
        String sql = "SELECT * FROM card";
        ResultSet queryResult = db.select(sql);
        try{
            while(queryResult.next()) {
                if (queryResult.getString("number").equals(cardNumber)) {
                    return true;
                }
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }


    private static boolean checkLoginCredentials(Bank bank, Database db, String inputCardNumber, String inputPinNumber) {


        //todo rewrite this to query the data base for matches PRIORITY 1

        return false;
    }

    private static void createAccount(Bank bank, Database db) {
        bank.createAccount(bank , db);
    }

    public static void printMenu() {
        System.out.println("1. Create an account\n2. Log into account\n0. Exit");
    }


}