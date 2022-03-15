package banking;


import javax.xml.crypto.Data;
import java.sql.Connection;
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
        boolean validInput = true;
        System.out.println("Enter your card number:");
        try {
            String inputCardNumber = scan.next();
            String accountNumber = inputCardNumber.substring(6, 15);

            System.out.println("Enter your PIN:");
            String inputPinNumber = scan.next();
            //if wrong credentials, exit to menu
            if(!checkLoginCredentials(bank, inputCardNumber, inputPinNumber)) {
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
                        //formats balance as float to two decimals
                        System.out.printf("%.2f", bank.getBankAccounts().get(accountNumber).getBalance());
                        System.out.println();
                        return false;
                    }
                    case 2: {
                        System.out.println("Enter income:");
                        float balance = 0;
                        float deposit = scan.nextFloat();
                        bank.getBankAccounts().get(accountNumber).depositFunds(deposit);
                        //todo make query to get current balance and then update ++deposit to database
                        db.update();
                        System.out.println("Income was added!");
                        return false;
                    }
                    case 3: {

                        //todo this probably should be a transfer method
                        System.out.println("Transfer");
                        System.out.println("Enter card number:");
                        String cardNumber = scan.nextLine();

                        System.out.println("Enter how much money you want to transfer:");
                        String transfer = scan.nextLine();
                        //query if theres enough in the account, if not
                        System.out.println("Not enough money!");
                        System.out.println("You can't transfer money to the same account!");
                        System.out.println("Probably you made a mistake in the card number. Please try again!");
                        System.out.println("Such a card does not exist.");
                        return false;
                    }
                    case 4: {
                        //todo delete account
                        System.out.println("The account has been closed!");
                        return false;
                    }

                    case 5: {
                        System.out.println("You have successfully logged out!");
                        loggedIn = false;
                    }
                    break;
                    default:
                        System.out.println("Please choose either 0, 1, or 2!");
                        return false;
                }
            }
        } catch (Exception e){
            System.out.println("Sorry! That was not a valid input.");
            return false;
        }
        return false;
    }


    private static boolean checkLoginCredentials(Bank bank, String inputCardNumber, String inputPinNumber) {
        try {
            if (bank.getBankAccounts().get(inputCardNumber.substring(6, 15)).getPin().equals(inputPinNumber) &&
                    (bank.getBankAccounts().get(inputCardNumber.substring(6, 15)).getCardNumber().equals(inputCardNumber))) {
                return true;
            }
        } catch (NullPointerException e) {
            System.out.println("Wrong card number or PIN!");
        }

        return false;
    }

    private static void createAccount(Bank bank, Database db) {
        bank.createAccount(bank , db);
    }

    public static void printMenu() {
        System.out.println("1. Create an account\n2. Log into account\n0. Exit");
    }


}