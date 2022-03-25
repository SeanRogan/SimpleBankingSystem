package banking;


import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;
//todo this whole thing might be cleaner if i just had the ATM program in the bank class...\\

//todo implement logging
public class Main {
    //initialize bank class with id number.
    final public static Bank bank = new Bank("400000");
    final public static Scanner scan = new Scanner(System.in);

    public static void main(String[] args){

        Database db = new Database(args);
        TellerService atm = new TellerService(bank);
        atm.runTellerService(db);
    }
}