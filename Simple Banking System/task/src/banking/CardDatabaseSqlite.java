package banking;

import java.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class CardDatabaseSqlite {
    private Connection conn;
    Map<Integer, Account> card = new HashMap<>();

    CardDatabaseSqlite(String fileName) {

        String createTableSql = "CREATE TABLE IF NOT EXISTS card (\n" +
                "id INTEGER PRIMARY KEY, \n" +
                "number VARCHAR NOT NULL, \n" +
                "pin VARCHAR NOT NULL, \n" +
                "balance INTEGER DEFAULT 0 \n" +
                ");";

        try {
            conn = DriverManager.getConnection("jdbc:sqlite:" + fileName);
            Statement s = conn.createStatement();
            s.execute(createTableSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void insertAccount(Account account) {
        String sql = "INSERT INTO card (number, pin, balance) VALUES (?, ?, ?)";

        try {
            PreparedStatement a = conn.prepareStatement(sql);
            a.setString(1, account.getNewCardNumber());
            a.setString(2, account.getNewPin());
            a.setInt(3, account.getBalance());

            a.execute();
            account.setId(a.getGeneratedKeys().getInt(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public Map<Integer, Account> listAll() {
        String sql = "SELECT * FROM card";

        try {
            Statement s = conn.createStatement();
            ResultSet resultSet = s.executeQuery(sql);

            while (resultSet.next()) {
                Account account = new Account();
                account.setId(resultSet.getInt("id"));
                account.setAccountNumber(resultSet.getString("number"));
                account.setAccountPin(resultSet.getString("pin"));
                account.setBalance(resultSet.getInt("balance"));

                card.put(account.getId(), account);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return card;

    }

    public void updateData(int money, String account) {
        String sql = "UPDATE card SET balance = balance + ? WHERE number = ?";
        try {
            PreparedStatement a = conn.prepareStatement(sql);
            a.setInt(1, money);
            a.setString(2, account);
            a.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public int viewBalance(String accountNumberCheck) {
        String sql = "SELECT balance FROM card WHERE number = ?";

        try {
            PreparedStatement a = conn.prepareStatement(sql);
            a.setString(1, accountNumberCheck);
            try (ResultSet resultSet = a.executeQuery()) {
                resultSet.next();
                if (!resultSet.isClosed()) {
                    int balance = resultSet.getInt("balance");
                    return balance;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
                e.printStackTrace();
        }
        return 0;
    }


    public boolean checkLogIn(String accountNumberCheck, String pinNumberCheck) {
        String sql = "SELECT number, pin FROM card WHERE number = ? AND pin = ?;";

        try {
            PreparedStatement a = conn.prepareStatement(sql);
            a.setString(1,accountNumberCheck);
            a.setString(2, pinNumberCheck);

            try (ResultSet resultSet = a.executeQuery()) {
                resultSet.next();
                if (!resultSet.isClosed()) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean checkCard(String accountNumberCheck) {
        String sql = "SELECT number, pin FROM card WHERE number = ?";

        try {
            PreparedStatement a = conn.prepareStatement(sql);
            a.setString(1, accountNumberCheck);

            try (ResultSet resultSet = a.executeQuery()) {
                resultSet.next();
                if (!resultSet.isClosed()) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void closeAccount(String accountNumberCheck) {
        String sql = "DELETE FROM card WHERE number = ?";

        try {
            PreparedStatement a = conn.prepareStatement(sql);
            a.setString(1, accountNumberCheck);

            a.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


}