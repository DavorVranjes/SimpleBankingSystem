package banking;

import java.util.*;

public class Account {

    private String accountNumber;
    private String accountPin;
    private int balance;
    private int id;

    public Account() {
    }

    public Account(String arg) {
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setAccountPin(String accountPin) {
        this.accountPin = accountPin;
    }


    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getBalance() {
        return balance;
    }

    public String getNewCardNumber() {
        return accountNumber;
    }

    public String getNewPin() {
        return accountPin;
    }

    public String createNewCardNumber() {

        String bin = "400000";

        Random random = new Random();
        StringBuilder customerAccountNumber = new StringBuilder(String.valueOf(random.nextInt(Integer.MAX_VALUE)));

        if (customerAccountNumber.length() > 9) {
            customerAccountNumber.deleteCharAt(random.nextInt(customerAccountNumber.length()));
        } else if (customerAccountNumber.length() < 9) {

            while (customerAccountNumber.length() < 9) {
                customerAccountNumber.insert(0, 0);
            }
        }

        String checksum = String.valueOf(generateChecksum(bin + customerAccountNumber));

        accountNumber = bin + customerAccountNumber + checksum;
        return accountNumber;
    }


    public int generateChecksum(String initialNumber) {

        int total = 0;

        for (int i = 1; i <= initialNumber.length(); i++) {

            int current = ((int) initialNumber.charAt(i - 1) - 48);

            if (i % 2 == 1) {

                current *= 2;

                if (current > 9) {
                    current -= 9;
                }

            }
            total += current;
        }

        int checkSum = 0;
        while ((total + checkSum) % 10 > 0) {
            checkSum++;
        }

        return checkSum;
    }

    public void createNewPin() {

        accountPin = String.valueOf(new Random().nextInt((9999 - 1000) + 1) + 1000);
    }

    public boolean checkLuhnAlgorithm(String accountNumberCheck) {
        int sum = 0;
        boolean alternate = false;
        for (int i = accountNumberCheck.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(accountNumberCheck.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }
}