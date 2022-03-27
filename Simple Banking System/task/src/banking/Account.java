package banking;

import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class Account {

    private static String accountNumber;
    private static String accountPin;
    private final Map<String, String> mapsOfAccounts;
    private SQLiteDataSource database;
    private boolean dbCreated;

    static int id = 101;

    public Account(String pathToDatabase) {
        this.mapsOfAccounts = new HashMap<>();
        this.database = new SQLiteDataSource();
        this.database.setUrl("jdbc:sqlite:" + pathToDatabase);

        this.dbCreated = false;
    }

    public void menu() {
        bankLogin();

    }

    public void bankLogin() {

        if (!dbCreated) {
            createDatabase();
            dbCreated = true;
        }

        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("1. Create an account");
            System.out.println("2. Log into account");
            System.out.println("0. Exit");

            int action = scanner.nextInt();

            switch (action) {
                case 1:
                    createAccount();
                    break;

                case 2:
                    logIntoAccount(accountNumber, accountPin);
                    break;
                case 0:
                    exitAccount();
                    break;
            }

        }
    }

    public void createAccount() {

        Random random = new Random();

        String bin = "400000";
        long number = random.nextInt(999_999_999 - 100_000_000 + 1) + 100_000_000;
        accountPin = String.valueOf(random.nextInt(9999 - 1000 + 1) + 1000);
        String cardNumbers = bin + number;

        int sum = 0;
        boolean second = false;

        for (int i = cardNumbers.length() - 1; i >= 0; --i) {
            int digit = Character.getNumericValue(cardNumbers.charAt(i));
            digit = (second = !second) ? (digit * 2) : digit;
            digit = (digit > 9) ? (digit - 9) : digit;
            sum += digit;
        }
        int lastDigit = (sum * 9) % 10;
        accountNumber = cardNumbers + lastDigit;


        System.out.println("Your card has been created");
        System.out.println("Your card number:");
        System.out.println(accountNumber);
        System.out.println("Your card PIN:");
        System.out.println(accountPin);
        mapsOfAccounts.put(accountNumber, accountPin);
        saveCardToDatabase(accountNumber, accountPin);
        bankLogin();

    }


    public void logIntoAccount(String cardNumber, String pin) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input card number:");
        String inputCardNumber = scanner.next();
        System.out.println("Input PIN:");
        String inputCardPin = scanner.next();

        if (!mapsOfAccounts.containsKey(inputCardNumber) || !mapsOfAccounts.get(inputCardNumber).equals(inputCardPin)) {
            System.out.println("Wrong card number or PIN!");
            bankLogin();
        } else {
            System.out.println("You have successfully logged in!");
            logInSuccess();
        }
    }

    public void logInSuccess() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("1. Balance");
        System.out.println("2. Log out");
        System.out.println("0. Exit");
        int action = scanner.nextInt();

        switch(action) {
            case 1:
                System.out.println("Balance: 0");
                logInSuccess();
                break;
            case 2:
                System.out.println("You have successfully logged out!");
                bankLogin();
                break;
            case 0:
                exitAccount();
                break;

        }
    }
    public void exitAccount() {
        System.out.println("Bye!");
        System.exit(0);
    }

    private void createDatabase() {

        String createTable = "CREATE TABLE IF NOT EXISTS card ("
                + "      id INTEGER PRIMARY KEY,"
                + "      NUMBER text,"
                + "      pin TEXT,"
                + "      balance INTEGER DEFAULT 0"
                + ");";

        try (Connection connection = this.database.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(createTable);
            }
        } catch (SQLException e){
            System.out.println("Exception");
        }
    }

    private void saveCardToDatabase(String accountNumber, String accountPin) {
        String command = "INSERT INTO card(id, number, pin, balance) VALUES (?,?,?,?);";

        try (Connection connection = database.getConnection() ;
             PreparedStatement statement = connection.prepareStatement(command)) {

            statement.setInt(1, id);
            statement.setString(2, accountNumber);
            statement.setString(3, accountPin);
            statement.executeUpdate();
            id++;
        } catch (SQLException e) {
            System.out.println("Exception");
        }
    }
}