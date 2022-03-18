package banking;


import java.sql.*;
import java.util.InputMismatchException;

import java.util.Scanner;

public class Main {
    //initialize bank class with id number.
    final public static Bank bank = new Bank("400000");
    final public static Scanner scan = new Scanner(System.in);

    private static boolean loggedIn = false;
    public static boolean isLoggedIn() {
        return loggedIn;
    }

    public static void setLoggedIn(boolean loggedIn) {
        Main.loggedIn = loggedIn;
    }
    private static String userCardNumber;
    public static String getUserCardNumber() {
        return userCardNumber;
    }

    public static void setUserCardNumber(String userCardNumber) {
        Main.userCardNumber = userCardNumber;
    }
    public static void main(String[] args) throws SQLException {

        Database db = new Database(args);
        db.createCardTable();
        boolean running = true;

        program: while(running) {
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
                        logInLoop: while(loggedIn(db,bank)) {
                            switch(accountMenu(db)) {
                                case 0: {
                                    break program;
                                }
                                case 1: {
                                    break logInLoop;
                                }
                                default: {}
                                break;
                            }
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
        }

        System.out.println("Bye!");
        scan.close();

    }

    private static boolean loggedIn(Database db, Bank bank) throws SQLException {

        if(!isLoggedIn()){

            if(!logIn(db, bank)) return false;
        } else return true;
        return true;
    }

    private static boolean logIn(Database db, Bank bank) throws SQLException {
        System.out.println("Enter your card number:");
        String inputCardNumber = scan.next().trim();

        System.out.println("Enter your PIN:");
        String inputPinNumber = scan.next().trim();

        //if wrong credentials, exit to menu
        if (!checkLoginCredentials(bank, db, inputCardNumber, inputPinNumber)) {
            System.out.println("Wrong card number or PIN!\n");
            return false;
        }
        setUserCardNumber(inputCardNumber);
        setLoggedIn(true);
        System.out.println("You have successfully logged in!\n");
        return true;
    }

    private static int accountMenu(Database db){
        //returns a boolean, true if the program is to exit, false otherwise.
        //String accountNumber; not needed
        String inputCardNumber = getUserCardNumber();
        loggedInMenu: if(isLoggedIn()) {
            System.out.println("1 Balance\n2. Add income\n3. Do transfer\n4. Close account\n5. Log out\n0. Exit\n");
            int input = scan.nextInt();
            scan.nextLine();
            switch (input) {
                case 0: {
                    setLoggedIn(false);
                    return 0;
                }
                case 1: {
                    //GET BALANCE
                    //formats balance as float to two decimals
                    //System.out.printf("%.2f", bank.getBankAccounts().get(accountNumber).getBalance());
                    System.out.printf("%.2f", db.queryBalance(inputCardNumber));
                    System.out.println();
                    return 2;
                }

                case 2: {
                    //DEPOSIT
                    System.out.println("Enter income:");
                    float deposit = scan.nextInt();
                    float balance = db.queryBalance(inputCardNumber);
                    float newBalance = balance + deposit;
                    db.updateBalance(Float.toString(newBalance) , inputCardNumber);
                    System.out.println("Income was added!");
                    return 2;
                }

                case 3: {
                    //TRANSFER
                    boolean transferComplete = false;
                    while(!transferComplete) {
                        System.out.println("Transfer");
                        System.out.println("Enter card number:");
                        String transfer = "";
                        String cardNumber = scan.nextLine().trim();

                        if (!new LuhnAlgorithmChecker().verifyCardNumber(cardNumber)) {
                            System.out.println("Probably you made a mistake in the card number. Please try again!");
                        }
                        if (!db.findAccount(cardNumber)) {
                            System.out.println("Such a card does not exist.");
                            return 2;
                        }else {

                            System.out.println("Enter how much money you want to transfer:");

                            transfer = scan.nextLine().trim();
                            if (Float.parseFloat(transfer) > db.queryBalance(inputCardNumber)) {
                                System.out.println("Not enough money!");
                                return 2;
                            }
                            if (cardNumber.equals(inputCardNumber)) {
                                System.out.println("You can't transfer money to the same account!");
                                return 2;
                            }
                            //if it gets past all the exceptions, transfer the money
                            //get receiving account balance
                            float receivingAccountStartingBalance = db.queryBalance(cardNumber);
                            float depositingAccountStartingBalance = db.queryBalance(getUserCardNumber());
                            db.updateBalance(Float.toString(depositingAccountStartingBalance - Float.parseFloat(transfer)), getUserCardNumber());
                            db.updateBalance(Float.toString(receivingAccountStartingBalance + Float.parseFloat(transfer)), cardNumber);
                            System.out.println("Success!");
                            transferComplete = true;
                        }
                    }
                    return 2;
                }

                case 4: {
                    //CLOSE ACCOUNT

                    bank.getBankAccounts().remove(getUserCardNumber());
                    db.deleteAccount(getUserCardNumber());
                    setLoggedIn(false);
                    System.out.println("The account has been closed!\n");
                    return 1;

                }

                case 5: {
                    //LOG OUT
                    System.out.println("You have successfully logged out!");
                    setLoggedIn(false);
                    break loggedInMenu;
                }

                default:
                    System.out.println("Please choose a number, from 0 to 5!");
                    return 2;
            }
        }

        return 2;
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