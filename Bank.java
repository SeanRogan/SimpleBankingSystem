package banking;

import java.util.HashMap;

public class Bank {

    private String bankIdNumber;
    private HashMap<String, Account> bankAccounts;
    public Bank(String bankIdNumber) {
        this.bankIdNumber = bankIdNumber;
    }

    public HashMap<String, Account> getBankAccounts() {
        return bankAccounts;
    }

    public void setBankAccounts(HashMap<String, Account> bankAccounts) {
        this.bankAccounts = bankAccounts;
    }

    public void createAccount(String accountNumber , Account bankAccount) {
        bankAccounts.put(accountNumber, bankAccount);
    }
    public String getBankIdNumber() {
        return bankIdNumber;
    }

    public void setBankIdNumber(String bankIdNumber) {
        this.bankIdNumber = bankIdNumber;
    }

}
