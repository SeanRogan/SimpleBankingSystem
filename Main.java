package banking;


import javax.xml.crypto.Data;
import java.sql.*;
import java.util.InputMismatchException;

import java.util.Scanner;

public class Main {
    //initialize bank class with id number.
    final public static Bank bank = new Bank("400000");
    final public static Scanner scan = new Scanner(System.in);

    public static boolean isLoggedIn() {
        return loggedIn;
    }

    public static void setLoggedIn(boolean loggedIn) {
        Main.loggedIn = loggedIn;
    }

    private static boolean loggedIn = false;
    public static void main(String[] args) throws SQLException {

        Database db = new Database(args);
        db.createCardTable();
        boolean running = true;

        //todo this while loop is breaking everything. when we log into an account,
        // it should remain logged in and in the logged in menu, until you select exit,
        // or log out which brings you bakc to the main menu. We need to break the account access method
        // into a login method and account menu method
        // .
        // maybe the method should return ints or strings to guide what to do next
        // .
        // .
        // case 2:
        // while(loggedIn()) {
        //                      accountMenu();
        // }
        // loggedIn(){
        //      if(!isLoggedIn()){
        //                          logIn();
        //                       } else return true;
        // }
        // logIn()
        // "enter card
        // card = scan.nextlin
        // "enter pin
        // pin = scan.nextline
        // if(checkCreds(card, pin)){
        //                          setLoggedIn(true);
        //                          } else setLoggedIn(false);
        // }



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

    private static boolean accessAccount(Bank bank, Database db) throws SQLException {
        //returns a boolean, true if the program is to exit, false otherwise.
        //String accountNumber; not needed
        System.out.println("Enter your card number:");
        try {
            String inputCardNumber = scan.next().trim();
            if(inputCardNumber.length() == 16) {
               //accountNumber = inputCardNumber.substring(6, 15); not needed
            } else return false;
            System.out.println("Enter your PIN:");
            String inputPinNumber = scan.next();

            //if wrong credentials, exit to menu
            if(!checkLoginCredentials(bank, db, inputCardNumber, inputPinNumber)) {
                return false;
            }
            setLoggedIn(true);
            System.out.println("You have successfully logged in!\n");

            while (isLoggedIn()) {
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
                        System.out.println();
                        return false;
                    }
                    case 2: {
                        //DEPOSIT
                        System.out.println("Enter income:");
                        float deposit = scan.nextInt();
                        float balance = db.queryBalance(inputCardNumber);
                        float newBalance = balance + deposit;
                        db.updateBalance(Float.toString(newBalance) , inputCardNumber);
                        System.out.println("Income was added!");
                        return false;
                    }
                    case 3: {
                        //TRANSFER
                        boolean transferComplete = false;
                        while(!transferComplete) {
                            System.out.println("Transfer");
                            System.out.println("Enter card number:");
                            String cardNumber = scan.next();

                            if (!new LuhnAlgorithmChecker().verifyCardNumber(cardNumber)) {
                                System.out.println("Probably you made a mistake in the card number. Please try again!");
                                return false;
                            }
                            if (!db.findAccount(cardNumber)) {
                                System.out.println("Such a card does not exist.");
                                return false;
                            }
                            System.out.println("Enter how much money you want to transfer:");
                            String transfer = scan.nextLine();
                            if (Float.parseFloat(transfer) > db.queryBalance(inputCardNumber)) {
                                System.out.println("Not enough money!");
                                return false;
                            }
                            if (cardNumber.equals(inputCardNumber)) {
                                System.out.println("You can't transfer money to the same account!");
                                return false;
                            }
                            //if it gets past all the exceptions, transfer the money
                            float currentBalance = db.queryBalance(cardNumber);
                            String newBalance = Float.toString(currentBalance + Float.parseFloat(transfer));
                            db.updateBalance(newBalance, cardNumber);
                            System.out.println("Success!");
                            transferComplete = true;
                        }
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
        } catch (InputMismatchException e){
            System.out.println("Sorry! That was not a valid input.");
            return false;
        }
        return false;
    }
    private static boolean checkLoginCredentials(Bank bank, Database db, String inputCardNumber, String inputPinNumber) throws SQLException {
        Connection connection = null;
        try {
            connection = db.connect();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM card WHERE number = " + inputCardNumber
                    + " AND pin = " + inputPinNumber);
            return ps.executeQuery().next();
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if(connection != null) {
                connection.close();
            }
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