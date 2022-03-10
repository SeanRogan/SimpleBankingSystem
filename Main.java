package banking;


import java.util.Arrays;
import java.util.HashMap;
import java.util.InputMismatchException;

import java.util.Scanner;

public class Main {
    //initialize bank class with id number.
    final public static Bank bank = new Bank("400000");
    final public static Scanner scan = new Scanner(System.in);
    public static void main(String[] args) {

        Connect.connect(args);

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
                    case 1: {
                        createAccount(bank);
                    }
                    break;
                    case 2: {
                        accessAccount(bank);
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

    private static void accessAccount(Bank bank) {
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
                return;
            }
            loggedIn = true;
            System.out.println("You have successfully logged in!\n");

            while (loggedIn) {
                System.out.println("1 Balance\n2. Log out\n0. Exit\n");
                int input = scan.nextInt();
                switch (input) {
                    case 0: {
                        return;
                    }
                    case 1: {
                        //formats balance as float to two decimals
                        System.out.printf("%.2f", bank.getBankAccounts().get(accountNumber).getBalance());
                        System.out.println();

                    }
                    break;
                    case 2: {
                        System.out.println("You have successfully logged out!");
                        loggedIn = false;
                    }
                    break;
                    default:
                        System.out.println("Please choose either 0, 1, or 2!");
                }
            }
        } catch (Exception e){
            System.out.println("Sorry! That was not a valid input.");
        }
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

    private static void createAccount(Bank bank) {
        bank.createAccount(bank);
    }

    public static void printMenu() {
        System.out.println("1. Create an account\n2. Log into account\n0. Exit");
    }


}