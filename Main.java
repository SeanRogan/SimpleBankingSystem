package banking;

import java.util.Scanner;

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