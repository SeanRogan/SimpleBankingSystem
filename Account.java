package banking;

public class Account {

    Account(String cardNumber, String pin, String accountNumber) {
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.accountNumber = accountNumber;

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
    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
    private String accountNumber;
    private String cardNumber;
    private String pin;
    private float balance;
    private boolean loggedIn;



}
