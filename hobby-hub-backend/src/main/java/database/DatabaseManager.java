/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Zuucker
 */
public class DatabaseManager {

    private static DatabaseManager databaseManagerInstance;
    private static String databaseUrl = "jdbc:sqlite:/Repos/hobby-hub/DB/database.db";
    private static Connection connection = null;

    private DatabaseManager() {
    }

    ;
    
    public static DatabaseManager getInstance() {
        if (databaseManagerInstance == null) {
            try {
                Class.forName("org.sqlite.JDBC");
            } catch (ClassNotFoundException e) {
                System.err.println("SQLite JDBC driver not found");
                System.out.println(e.getMessage());
            }

            try {
                connection = DriverManager.getConnection(databaseUrl);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

            //check if schema exists
            boolean schemaExists = false;
            try {
                DatabaseMetaData databaseMetaData = connection.getMetaData();
                ResultSet resultSet = databaseMetaData.getTables(null, null, null, new String[]{"TABLE"});
                schemaExists = !resultSet.next();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

            String createSql = "";

            //create schema
            if (schemaExists) {
                String filePath = "../DB/databaseSchema.sql";

                try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        createSql += line;
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }

                try {
                    while (createSql.contains(";")) {
                        int index = createSql.indexOf(";");
                        String sql = createSql.substring(0, index + 1);
                        createSql = createSql.replace(sql, "");
                        System.out.println(sql);
                        Statement statement = connection.createStatement();
                        statement.execute(sql);
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }

                //insert initial data
                try {
                    String sql = "INSERT INTO users(username, email, password) VALUES(?, ?, ?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);

                    for (int i = 1; i < 11; i++) {
                        preparedStatement.setString(1, "user" + Integer.toString(i));
                        preparedStatement.setString(2, "email" + Integer.toString(i));
                        preparedStatement.setString(3, "password" + Integer.toString(i));
                        preparedStatement.executeUpdate();
                    }

                    System.out.println("Inserted initial data");
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }

                try {
                    String sql = "UPDATE users SET verified = TRUE WHERE username='user1'";
                    PreparedStatement pstmt = connection.prepareStatement(sql);

                    pstmt.executeUpdate();
                    System.out.println("Updated initial data");
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }

            }

            databaseManagerInstance = new DatabaseManager();
        }

        return databaseManagerInstance;
    }

    public List<String> getUsers() {
        String sql = "SELECT * FROM users";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            List<String> results = new ArrayList();

            while (resultSet.next()) {
                results.add(resultSet.getString("username"));
            }
            return results;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<String> getUsernames() {
        String sql = "SELECT username FROM users";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            List<String> results = new ArrayList();

            while (resultSet.next()) {
                results.add(resultSet.getString("username"));
            }
            return results;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public boolean addUser(String username, String email, String password) {
        String sql = "INSERT INTO users(username, email, password) VALUES(?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);
            preparedStatement.executeUpdate();

            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    public boolean addVerificationCodeForUser(String username, String code) {

        String sql = "INSERT INTO verifications(user_id, code) VALUES((SELECT id FROM users WHERE username=?), ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, code);
            preparedStatement.executeUpdate();

            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    public boolean getVerified(String username) {
        String sql = "SELECT verified FROM users WHERE username = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.getBoolean("verified");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    public boolean checkIfUserExists(String username) {
        String sql = "SELECT COUNT(*) > 0 AS 'result' FROM users WHERE username = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.getBoolean("result");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public String getPassword(String username) {
        String sql = "SELECT password FROM users WHERE username = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.getString("password");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public boolean verifyUser(String code) {

        boolean status = false;

        String sql = "UPDATE users SET verified = false WHERE id=(SELECT user_id FROM verifications WHERE code=?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, code);
            preparedStatement.executeUpdate();

            status = true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        sql = "DELETE FROM verifications WHERE code=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, code);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            status = false;
            System.out.println(e.getMessage());
        }

        return status;
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
