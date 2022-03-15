package banking;

import java.util.HashMap;

public class Bank {

    private String bankIdNumber;
    private HashMap<String, Account> bankAccounts;
    public Bank(String bankIdNumber) {
        this.bankIdNumber = bankIdNumber;
        this.bankAccounts = new HashMap<>();
    }

    public HashMap<String, Account> getBankAccounts() {
        return bankAccounts;
    }
    public String getBankIdNumber() {
        return bankIdNumber;
    }

    public void createAccount(Bank bank , Database db) {
        Account newAccount = new Account(bank , db);
        bank.getBankAccounts().put(newAccount.getAccountNumber(),newAccount);
    }

}
