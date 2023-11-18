/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import models.Group;
import models.User;
import sqlFileParser.SqlFileParser;

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
                resultSet.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

            String createSql = "";

            //create schema
            if (schemaExists) {
                String filePath = "../DB/databaseSchema.sql";

                SqlFileParser sqlFileParser = new SqlFileParser(filePath);
                List<String> createStatements = sqlFileParser.getStatements();

                try {
                    for (String sqlStatement : createStatements) {
                        Statement statement = connection.createStatement();
                        statement.execute(sqlStatement);
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }

                //insert initial data
                try {
                    String sql = "INSERT INTO users(username, email, password, register_date) VALUES(?, ?, ?, ?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    LocalDate currentDate = LocalDate.now();

                    for (int i = 1; i < 11; i++) {
                        preparedStatement.setString(1, "user" + Integer.toString(i));
                        preparedStatement.setString(2, "email" + Integer.toString(i));
                        preparedStatement.setString(3, "password" + Integer.toString(i));
                        preparedStatement.setString(4, currentDate.toString());
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

                try {
                    String sql = "UPDATE users SET verified = TRUE WHERE username='user2'";
                    PreparedStatement pstmt = connection.prepareStatement(sql);

                    pstmt.executeUpdate();
                    System.out.println("Updated initial data");
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }

                String sql = "INSERT INTO groups(owner_id, name, description) VALUES(?, ?, ?)";
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    for (int i = 1; i < 11; i++) {
                        preparedStatement.setInt(1, 1);
                        preparedStatement.setString(2, "group_name" + Integer.toString(i));
                        preparedStatement.setString(3, "description" + Integer.toString(i));
                        preparedStatement.executeUpdate();
                    }

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
            resultSet.close();
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
        String sql = "INSERT INTO users(username, email, password, register_date, bio) VALUES(?, ?, ?, ?, ?)";
        LocalDate currentDate = LocalDate.now();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);
            preparedStatement.setString(4, currentDate.toString());
            preparedStatement.setString(5, username + "'s profile");
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

    public String getVerificationCode(String username) {

        String sql = "SELECT code FROM verifications WHERE user_id=(SELECT id FROM users WHERE username=?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            String result = resultSet.getString("code");
            resultSet.close();

            return result;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return "-1";
    }

    public boolean getVerified(String username) {
        String sql = "SELECT verified FROM users WHERE username = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            boolean result = resultSet.getBoolean("verified");
            resultSet.close();

            return result;
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
            boolean result = resultSet.getBoolean("result");
            resultSet.close();

            return result;
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
            String result = resultSet.getString("password");
            resultSet.close();

            return result;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public boolean verifyUser(String code) {

        boolean status = false;

        String sql = "UPDATE users SET verified = true WHERE id=(SELECT user_id FROM verifications WHERE code=?)";
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

    public User getUser(String username) {

        if (!this.checkIfUserExists(username)) {
            return new User().username("nie działa XD");
        }

        String sql = "SELECT * FROM users WHERE username = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            User user = new User()
                    .id(resultSet.getInt("id"))
                    .username(resultSet.getString("username"))
                    .email(resultSet.getString("email"))
                    .password(resultSet.getString("password"))
                    .isVerified(resultSet.getBoolean("verified"))
                    .registerDate(LocalDate.parse(resultSet.getString("register_date")))
                    .bio(resultSet.getString("bio"));

            resultSet.close();

            return user;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return new User();
    }

    public User getUser(int userId) {

        String sql = "SELECT * FROM users WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            User user = new User()
                    .id(resultSet.getInt("id"))
                    .username(resultSet.getString("username"))
                    .email(resultSet.getString("email"))
                    .password(resultSet.getString("password"))
                    .isVerified(resultSet.getBoolean("verified"))
                    .registerDate(LocalDate.parse(resultSet.getString("register_date")))
                    .bio(resultSet.getString("bio"));

            resultSet.close();

            return user;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return new User();
    }

    public int getUserId(String username) {

        String sql = "SELECT id FROM users WHERE username=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            int result = resultSet.getInt("id");
            resultSet.close();

            return result;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return -1;
    }

    public boolean editUser(int id, String newUsername, String newBio) {

        boolean status = false;

        String sql = "Update users set username = ?, bio = ? WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, newUsername);
            preparedStatement.setString(2, newBio);
            preparedStatement.setInt(3, id);
            preparedStatement.executeUpdate();

            status = true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return status;
    }

    public String getUsername(int id) {
        String sql = "SELECT username FROM users WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            String result = resultSet.getString("username");
            resultSet.close();

            return result;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return "-1";
    }

    public boolean addGroup(String name, String description, int ownerId) {
        String sql = "INSERT INTO groups(owner_id, name, description) VALUES(?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, ownerId);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, description);
            preparedStatement.executeUpdate();

            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    public boolean subscribeToGroup(int userId, int groupId) {
        String sql = "INSERT INTO group_subscriptions(user_id, group_id) VALUES(?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, groupId);
            preparedStatement.executeUpdate();

            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    public List<Group> getUserGroups(int id) {
        String sql = "SELECT * FROM groups WHERE id in (SELECT group_id FROM group_subscriptions WHERE user_id = ?) OR owner_id = ? ORDER BY name";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.setInt(2, id);
            ResultSet rs = preparedStatement.executeQuery();
            List<Group> results = new ArrayList();

            while (rs.next()) {
                results.add(new Group(rs.getInt("id"), rs.getInt("owner_id"), rs.getString("name"), rs.getString("description")));
            }

            return results;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<String> getGroupNames() {
        String sql = "SELECT name FROM groups";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            List<String> results = new ArrayList();

            while (resultSet.next()) {
                results.add(resultSet.getString("name"));
            }
            return results;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public boolean checkIfSubscribed(int userId, int groupId) {
        String sql = "SELECT COUNT(*) > 0 AS 'result' FROM group_subscriptions WHERE user_id = ? and group_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, groupId);
            ResultSet resultSet = preparedStatement.executeQuery();
            boolean result = resultSet.getBoolean("result");
            resultSet.close();

            return result;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
