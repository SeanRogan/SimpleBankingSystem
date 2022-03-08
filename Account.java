package banking;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class Account {

    Account() {
        //randomly generated 4 digit pin from 1000-9999
        this.pin = String.valueOf(ThreadLocalRandom.current().nextInt(1000, 9999));
        this.accountNumber = generateAccountNumber();
        this.cardNumber = generateCardNumber(bankIdNumber, accountNumber);
    }
    //randomly generated account number
    private String generateAccountNumber() {
        return String.valueOf(ThreadLocalRandom.current().nextInt(100000000, 999999999));
    }
    private String generateCardNumber(String bankIdNumber, String accountNumber) {
        return bankIdNumber + accountNumber + findCheckDigit(bankIdNumber,accountNumber);

    }
    //luhn algorithm applied to cardNumberSeed
    // to find the check digit on the end of the card number
    private String findCheckDigit(String bankIdNumber, String accountNumber) {
        String cardNumberSeed = bankIdNumber + accountNumber;

        return String.valueOf(10 - (Arrays.stream(cardNumberSeed.split("")).
                map(Integer::parseInt).
                map(i -> i % 2 == 0 ? i * 2 : i).
                map(j -> j > 9 ? j - 9 : j).
                mapToInt(Integer::intValue).
                sum()));
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
    public void setBankIdNumber(String bankIdNumber) {
        this.bankIdNumber = bankIdNumber;
    }
    public String getCustomerID() {
        return customerID;
    }
    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    private String customerID;
    private String bankIdNumber;
    private String accountNumber;
    private String cardNumber;
    private String pin;
    private float balance;




}
