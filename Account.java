package banking;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class Account {
    final LuhnAlgorithmChecker algo = new LuhnAlgorithmChecker();
    private String customerID;
    private String bankIdNumber;
    private String accountNumber;
    private String cardNumber;
    private String pin;
    private float balance;

    Account(Bank bank , Database db) {
        //randomly generated 4 digit pin from 1000-9999
        this.pin = String.valueOf(ThreadLocalRandom.current().nextInt(1000, 9999));
        //randomly generated account number
        this.accountNumber = generateAccountNumber();
        //set card number
        this.cardNumber = generateCardNumber(bank.getBankIdNumber(), accountNumber);
        //set bank id account belongs to
        this.bankIdNumber = bank.getBankIdNumber();
        //set sequential customer id number
        this.customerID = String.valueOf(bank.getBankAccounts().size() + 1);
        db.insertNewAccount(customerID, cardNumber, pin);
        System.out.println("\nYour card has been created\nYour card number:\n" + this.cardNumber + "\n" + "Your card PIN:\n" + this.pin + "\n");
    }
    //randomly generated account number
    private String generateAccountNumber() {
        //todo loop through existing bank accounts and
        // check that the generated account number
        // doesnt match any previously existing ones
        // beforing issueing the number to the client.
        // use a while loop to keep it generating
        // new numbers until it finds one that isnt being used.
        return String.valueOf(ThreadLocalRandom.current().nextInt(100000000, 999999999));
    }
    private String generateCardNumber(String bankIdNumber, String accountNumber) {
        String cardSeed = bankIdNumber + accountNumber;
        return bankIdNumber + accountNumber + algo.returnCheckDigit(cardSeed);
    }
    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
    public float getBalance() {
        return balance;
    }
    public void setBalance(float balance) {
        this.balance = balance;
    }
    public void depositFunds(float deposit) {
        this.balance+=deposit;
    }
    public void withdrawFunds(float withdrawal) {
        this.balance-=withdrawal;
    }
    public String getBankIdNumber() {
        return bankIdNumber;
    }

    public String getCustomerID() {
        return customerID;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
}
