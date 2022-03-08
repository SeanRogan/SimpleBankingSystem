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

    public String getBankIdNumber() {
        return bankIdNumber;
    }
    public void createAccount() {
        //todo this has to create the account number,
        String customerID = String.valueOf(bankAccounts.size() + 1);
        bankAccounts.put(customerID,new Account());
    }

}
