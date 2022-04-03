package banking;

import java.util.Scanner;

public class Main {

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        Account account = new Account(args[1]);
        CardDatabaseSqlite dao = new CardDatabaseSqlite(args[1]);

        String command = " ";
        String accountNumberCheck;
        String pinNumberCheck;
        int amountToTransfer;

        while (!command.equals("0")) {

            startMenu();
            command = scanner.next();

            switch (command) {
                case "1":
                    System.out.println("Your card has been created");
                    account.createNewCardNumber();

                    System.out.println("Your card number:");
                    System.out.println(account.getNewCardNumber());

                    account.createNewPin();
                    System.out.println("Your card PIN:");
                    System.out.println(account.getNewPin());

                    dao.insertAccount(account);
                    dao.listAll().put(account.getId(), account);
                    break;

                case "2":
                    System.out.println("Enter your card number:");
                    accountNumberCheck = scanner.next();
                    System.out.println("Enter your PIN:");
                    pinNumberCheck = scanner.next();

                    if (!dao.checkLogIn(accountNumberCheck, pinNumberCheck)) {
                        System.out.println("Wrong card number or PIN number!");
                    } else if (dao.checkLogIn(accountNumberCheck, pinNumberCheck)) {
                        System.out.println("You have successfully logged in!");

                        String action = "";

                        while (!action.equals("0")) {

                            System.out.println("1. Balance \n" +
                                    "2. Add income \n" +
                                    "3. Do transfer \n" +
                                    "4. Close account \n" +
                                    "5. Log out \n" +
                                    "0. Exit");

                            action = scanner.next();

                            switch (action) {
                                case "1":
                                    System.out.println(dao.viewBalance(accountNumberCheck));
                                    break;
                                case "2":
                                    System.out.println("Enter income: ");
                                    amountToTransfer = scanner.nextInt();
                                    dao.updateData(amountToTransfer, accountNumberCheck);
                                    System.out.println("Income was added!");
                                    break;
                                case "3":
                                    System.out.println("Enter card number: ");
                                    String cardNumberToTransfer = scanner.next();

                                    if (!account.checkLuhnAlgorithm(cardNumberToTransfer)) {
                                        System.out.println("Probably you made a mistake in the card number. Please try again!");
                                    } else if (!dao.checkCard(cardNumberToTransfer)) {
                                        System.out.println("Such card does not exist.");
                                    } else if (accountNumberCheck.equals(cardNumberToTransfer)) {
                                        System.out.println("You can't transfer amountToTransfer to the same account!");
                                    } else if (dao.checkCard(cardNumberToTransfer)) {

                                        System.out.println("Enter how much amountToTransfer you want to transfer: ");
                                        amountToTransfer = scanner.nextInt();
                                        if (dao.viewBalance(accountNumberCheck) >= amountToTransfer) {
                                            dao.updateData(-amountToTransfer, accountNumberCheck);
                                            dao.updateData(amountToTransfer, cardNumberToTransfer);
                                            System.out.println("Success!");
                                        } else {
                                            System.out.println("Not enough amountToTransfer!");
                                        }

                                    } else {
                                        System.out.println("Wrong input");
                                    }
                                    break;
                                case "4":
                                    dao.closeAccount(accountNumberCheck);
                                    System.out.println("The account has been closed!");
                                    action = "0";
                                    break;
                                case "5":
                                    System.out.println("You have successfully logged out!");
                                    action = "0";
                                    break;
                                case "0":
                                    System.out.println("Bye!");
                                    command = "0";
                                    break;
                                default:
                                    System.out.println("Wrong input!");
                                    break;
                            }
                        }
                    } else {
                        System.out.println("Wrong input!");
                    }

                    break;
                case "0":
                    System.out.println("Bye!");
                    break;
                default:
                    System.out.println("Wrong input!");
                    break;
            }
        }
    }

    public static void startMenu() {
        System.out.println("1. Create an account \n" +
                "2. Log into account \n" +
                "0. Exit");
    }
}