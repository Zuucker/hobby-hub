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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import models.Comment;
import models.Group;
import models.Notification;
import models.Post;
import models.SearchResult;
import models.User;
import org.javatuples.Pair;
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

                filePath = "../DB/exampleData.txt";

                sqlFileParser = new SqlFileParser(filePath);
                List<String> insertStatements = sqlFileParser.getStatements();

                try {
                    for (String sqlStatement : insertStatements) {
                        Statement statement = connection.createStatement();
                        statement.execute(sqlStatement);
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
            printError(e);
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
            printError(e);
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
            printError(e);
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
            printError(e);
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
            printError(e);
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
            printError(e);
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
            printError(e);
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
            printError(e);
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
            printError(e);
        }

        sql = "DELETE FROM verifications WHERE code=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, code);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            status = false;
            printError(e);
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
            printError(e);
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
            printError(e);
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
            printError(e);
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
            printError(e);
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
            printError(e);
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
            printError(e);
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
            printError(e);
        }

        return false;
    }

    public List<Group> getUserGroups(int id) {
        String sql = "SELECT g.*, u.username FROM groups g join users u on u.id = g.owner_id WHERE g.id in (SELECT group_id FROM"
                + " group_subscriptions WHERE user_id = ?) OR owner_id = ? ORDER BY name";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.setInt(2, id);
            ResultSet rs = preparedStatement.executeQuery();
            List<Group> results = new ArrayList();

            while (rs.next()) {
                results.add(new Group(rs.getInt("id"), rs.getInt("owner_id"), rs.getString("name"), rs.getString("description"), rs.getString("username")));
            }

            return results;
        } catch (SQLException e) {
            printError(e);
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
            printError(e);
        }
        return null;
    }

    public List<Group> getTopGroups() {
        String sql = "SELECT g.*, u.username FROM groups g join users u on u.id = g.owner_id ORDER BY name ASC";
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            List<Group> results = new ArrayList();

            while (rs.next()) {
                results.add(new Group(rs.getInt("id"), rs.getInt("owner_id"), rs.getString("name"), rs.getString("description"), rs.getString("username")));
            }
            return results;
        } catch (SQLException e) {
            printError(e);
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
            printError(e);
        }
        return false;
    }

    public Group getGroupData(String groupName) {

        List<String> groups = this.getGroupNames();

        if (!groups.contains(groupName)) {
            return new Group("no such group");
        }

        String sql = "SELECT * FROM groups WHERE name = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, groupName);
            ResultSet resultSet = preparedStatement.executeQuery();

            Group group = new Group(
                    resultSet.getInt("id"),
                    resultSet.getInt("owner_id"),
                    resultSet.getString("name"),
                    resultSet.getString("description"));

            resultSet.close();

            return group;
        } catch (SQLException e) {
            printError(e);
        }
        return new Group();
    }

    public Group getGroupData(int id) {

        String sql = "SELECT * FROM groups WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            Group group = new Group(
                    resultSet.getInt("id"),
                    resultSet.getInt("owner_id"),
                    resultSet.getString("name"),
                    resultSet.getString("description"));

            resultSet.close();

            return group;
        } catch (SQLException e) {
            printError(e);
        }
        return new Group();
    }

    public boolean editGroup(int id, String name, String description) {

        boolean status = false;

        String sql = "Update groups set name = ?, description = ? WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, description);
            preparedStatement.setInt(3, id);
            preparedStatement.executeUpdate();

            status = true;
        } catch (SQLException e) {
            printError(e);
        }

        return status;
    }

    public boolean removeGroup(int id) {

        boolean status = false;

        String sql = "DELETE FROM groups WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

            status = true;
        } catch (SQLException e) {
            printError(e);
        }

        return status;
    }

    public boolean removeGroupSubscription(int id, int userId) {

        boolean status = false;

        String sql = "DELETE FROM group_subscriptions WHERE user_id = ? and group_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();

            status = true;
        } catch (SQLException e) {
            printError(e);
        }

        return status;
    }

    public int addPost(Post post) {
        String sql = "INSERT INTO posts(author_id, group_id, title, type, link, up_votes, down_votes, date) values (?, ?, ?, ?, ?, ?, ?, ?)";
        LocalDateTime currentDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        String formattedTime = currentDate.format(formatter);

        boolean status = false;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, post.getAuthorId());
            preparedStatement.setInt(2, post.getGroupId());
            preparedStatement.setString(3, post.getTitle());
            preparedStatement.setString(4, post.getType());
            preparedStatement.setString(5, post.getLink());
            preparedStatement.setInt(6, post.getUpVotes());
            preparedStatement.setInt(7, post.getDownVotes());
            preparedStatement.setString(8, formattedTime);
            preparedStatement.executeUpdate();

            status = true;
        } catch (SQLException e) {
            printError(e);
        }

        if (status) {
            sql = "SELECT id FROM posts WHERE author_id = ? AND group_id = ? AND date = ?";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, post.getAuthorId());
                preparedStatement.setInt(2, post.getGroupId());
                preparedStatement.setString(3, formattedTime);
                ResultSet rs = preparedStatement.executeQuery();
                return rs.getInt("id");
            } catch (SQLException e) {
                printError(e);
            }
        }
        return -1;
    }

    public boolean isLikedByUser(int postId, int userId) {
        String sql = "SELECT COUNT(*) > 0 AS 'result' FROM liked_posts WHERE user_id = ? AND post_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, postId);
            ResultSet resultSet = preparedStatement.executeQuery();
            boolean result = resultSet.getBoolean("result");
            resultSet.close();

            return result;
        } catch (SQLException e) {
            printError(e);
        }

        return false;
    }

    public boolean isDislikedByUser(int postId, int userId) {
        String sql = "SELECT COUNT(*) > 0 AS 'result' FROM disliked_posts WHERE user_id = ? AND post_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, postId);
            ResultSet resultSet = preparedStatement.executeQuery();
            boolean result = resultSet.getBoolean("result");
            resultSet.close();

            return result;
        } catch (SQLException e) {
            printError(e);
        }

        return false;
    }

    public boolean likePost(int userId, int postId) {
        String sql = "INSERT INTO liked_posts(user_id, post_id) VALUES(?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, postId);
            preparedStatement.executeUpdate();

            sql = "Update posts set up_votes = up_votes + 1 WHERE id = ?";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, postId);
            preparedStatement.executeUpdate();

            return true;
        } catch (SQLException e) {
            printError(e);
        }

        return false;
    }

    public boolean dislikePost(int userId, int postId) {
        String sql = "INSERT INTO disliked_posts(user_id, post_id) VALUES(?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, postId);
            preparedStatement.executeUpdate();

            sql = "Update posts set down_votes = down_votes + 1 WHERE id = ?";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, postId);
            preparedStatement.executeUpdate();

            return true;
        } catch (SQLException e) {
            printError(e);
        }

        return false;
    }

    public boolean unLikePost(int userId, int postId) {
        String sql = "DELETE FROM liked_posts WHERE user_id = ? AND post_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, postId);
            preparedStatement.executeUpdate();

            sql = "Update posts set up_votes = up_votes - 1 WHERE id = ?";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, postId);
            preparedStatement.executeUpdate();

            return true;
        } catch (SQLException e) {
            printError(e);
        }

        return false;
    }

    public boolean unDislikePost(int userId, int postId) {
        String sql = "DELETE FROM disliked_posts WHERE user_id = ? AND post_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, postId);
            preparedStatement.executeUpdate();

            sql = "Update posts set down_votes = up_votes - 1 WHERE id = ?";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, postId);
            preparedStatement.executeUpdate();

            return true;
        } catch (SQLException e) {
            printError(e);
        }

        return false;
    }

    public List<Post> getGroupPosts(int id, int userId) {
        String sql = "SELECT p.*, u.username AS 'author', g.name AS 'group',"
                + " CASE WHEN l.user_id IS NOT NULL THEN TRUE ELSE FALSE END as up_voted,"
                + " CASE WHEN d.user_id IS NOT NULL THEN TRUE ELSE FALSE END as down_voted FROM 'posts' p JOIN 'users' u"
                + " ON p.author_id = u.id JOIN 'groups' g ON g.id = p.group_id left join liked_posts as l on l.post_id = p.id"
                + " left join disliked_posts d on d.post_id = p.id WHERE group_id = ? AND p.author_id"
                + " NOT IN(SELECT blocked_id FROM blocked_users WHERE blocker_id = ?) ORDER BY date";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.setInt(2, userId);
            ResultSet rs = preparedStatement.executeQuery();
            List<Post> results = new ArrayList();

            while (rs.next()) {
                results.add(new Post(
                        rs.getInt("id"),
                        rs.getInt("author_id"),
                        rs.getString("author"),
                        rs.getInt("group_id"),
                        rs.getString("group"),
                        rs.getString("title"),
                        rs.getString("type"),
                        rs.getString("link"),
                        rs.getInt("up_votes"),
                        rs.getInt("down_votes"),
                        rs.getBoolean("up_voted"),
                        rs.getBoolean("down_voted")
                ));
            }

            return results;
        } catch (SQLException e) {
            printError(e);
        }
        return null;
    }

    public List<Post> getUserPosts(int id) {
        String sql = "SELECT p.*, u.username AS 'author', g.name AS 'group',"
                + " CASE WHEN l.user_id IS NOT NULL THEN TRUE ELSE FALSE END as up_voted,"
                + " CASE WHEN d.user_id IS NOT NULL THEN TRUE ELSE FALSE END as down_voted FROM 'posts' p JOIN 'users' u"
                + " ON p.author_id = u.id JOIN 'groups' g ON g.id = p.group_id left join liked_posts as l on l.post_id = p.id"
                + " left join disliked_posts d on d.post_id = p.id WHERE author_id = ? ORDER BY date";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            List<Post> results = new ArrayList();

            while (rs.next()) {
                results.add(new Post(
                        rs.getInt("id"),
                        rs.getInt("author_id"),
                        rs.getString("author"),
                        rs.getInt("group_id"),
                        rs.getString("group"),
                        rs.getString("title"),
                        rs.getString("type"),
                        rs.getString("link"),
                        rs.getInt("up_votes"),
                        rs.getInt("down_votes"),
                        rs.getBoolean("up_voted"),
                        rs.getBoolean("down_voted")
                ));
            }

            return results;
        } catch (SQLException e) {
            printError(e);
        }
        return null;
    }

    public List<Post> getAllUserPosts(int id) {
        String sql = "SELECT p.*, u.username AS 'author', g.name AS 'group',"
                + " CASE WHEN l.user_id IS NOT NULL THEN TRUE ELSE FALSE END as up_voted,"
                + " CASE WHEN d.user_id IS NOT NULL THEN TRUE ELSE FALSE END as down_voted"
                + " FROM 'posts' p JOIN 'users' u ON p.author_id = u.id JOIN 'groups' g ON g.id = p.group_id "
                + " LEFT JOIN liked_posts AS l ON l.post_id = p.id LEFT JOIN disliked_posts d ON d.post_id = p.id WHERE group_id "
                + " IN (SELECT g.id FROM groups g WHERE g.id "
                + " IN (SELECT group_id FROM group_subscriptions WHERE user_id = ?)"
                + " AND p.author_id NOT IN(SELECT blocked_id FROM blocked_users WHERE blocker_id = ?)"
                + " OR owner_id = ? ORDER BY name)"
                + " ORDER BY date";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.setInt(2, id);
            preparedStatement.setInt(3, id);
            ResultSet rs = preparedStatement.executeQuery();
            List<Post> results = new ArrayList();

            while (rs.next()) {
                results.add(new Post(
                        rs.getInt("id"),
                        rs.getInt("author_id"),
                        rs.getString("author"),
                        rs.getInt("group_id"),
                        rs.getString("group"),
                        rs.getString("title"),
                        rs.getString("type"),
                        rs.getString("link"),
                        rs.getInt("up_votes"),
                        rs.getInt("down_votes"),
                        rs.getBoolean("up_voted"),
                        rs.getBoolean("down_voted")
                ));
            }

            return results;
        } catch (SQLException e) {
            printError(e);
        }
        return null;
    }

    public Post getPost(int id) {
        String sql = "SELECT p.*, u.username AS 'author', g.name AS 'group',"
                + " CASE WHEN l.user_id IS NOT NULL THEN TRUE ELSE FALSE END as up_voted,"
                + " CASE WHEN d.user_id IS NOT NULL THEN TRUE ELSE FALSE END as down_voted FROM 'posts' p JOIN 'users' u"
                + " ON p.author_id = u.id JOIN 'groups' g ON g.id = p.group_id left join liked_posts as l on l.post_id = p.id"
                + " left join disliked_posts d on d.post_id = p.id WHERE p.id = ? ORDER BY date";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            return new Post(
                    rs.getInt("id"),
                    rs.getInt("author_id"),
                    rs.getString("author"),
                    rs.getInt("group_id"),
                    rs.getString("group"),
                    rs.getString("title"),
                    rs.getString("type"),
                    rs.getString("link"),
                    rs.getInt("up_votes"),
                    rs.getInt("down_votes"),
                    rs.getBoolean("up_voted"),
                    rs.getBoolean("down_voted")
            );

        } catch (SQLException e) {
            printError(e);
        }

        return null;
    }

    public boolean addNotifcation(int userId, String content) {
        String sql = "INSERT INTO notifications(user_id, content) VALUES (?, ?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, content);

            preparedStatement.executeUpdate();

            return true;
        } catch (SQLException e) {
            printError(e);
        }

        return false;
    }

    public boolean addCommentToPost(int authorId, int postId, String content) {
        String sql = "INSERT INTO comments(author_id, post_id, content, points) VALUES (?, ?, ?, 0)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, authorId);
            preparedStatement.setInt(2, postId);
            preparedStatement.setString(3, content);
            preparedStatement.executeUpdate();

            return true;
        } catch (SQLException e) {
            printError(e);
        }

        return false;
    }

    public boolean addsubcomment(int authorId, int commentId, String content) {
        String sql = "INSERT INTO comments(author_id, comment_id, content, points) VALUES (?, ?, ?, 0)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, authorId);
            preparedStatement.setInt(2, commentId);
            preparedStatement.setString(3, content);
            preparedStatement.executeUpdate();

            return true;
        } catch (SQLException e) {
            printError(e);
        }

        return false;
    }

    public Comment getComment(int id) {
        String sql = "SELECT c.*, u.username FROM comments c join users u on u.id = c.author_id where c.id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            return new Comment(
                    rs.getInt("id"),
                    rs.getInt("author_id"),
                    rs.getString("username"),
                    rs.getInt("post_id"),
                    rs.getInt("comment_id"),
                    rs.getString("content"),
                    rs.getInt("points")
            );

        } catch (SQLException e) {
            printError(e);
        }

        return null;
    }

    public boolean hasInteractedWithCommentPoint(int commentId, int userId) {
        String sql = "SELECT COUNT(*) > 0 AS 'result' FROM liked_comments WHERE user_id = ? AND comment_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, commentId);
            ResultSet resultSet = preparedStatement.executeQuery();
            boolean result = resultSet.getBoolean("result");
            resultSet.close();

            return result;
        } catch (SQLException e) {
            printError(e);
        }

        return false;
    }

    public boolean getInteractionType(int userId, int commnetId) {
        String sql = "SELECT upvoted FROM liked_comments where user_id = ? AND comment_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, commnetId);
            ResultSet rs = preparedStatement.executeQuery();

            return rs.getBoolean("upvoted");

        } catch (SQLException e) {
            printError(e);
        }

        return false;
    }

    public boolean interactWithCommentPoint(int userId, int commentId, boolean up) {
        String sql = "INSERT INTO liked_comments(user_id, comment_id, upvoted) VALUES(?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, commentId);
            preparedStatement.setBoolean(3, up);
            preparedStatement.executeUpdate();

            sql = "Update comments set points = points " + (up ? "+ 1 " : "- 1 ") + "WHERE id = ?";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, commentId);
            preparedStatement.executeUpdate();

            return true;
        } catch (SQLException e) {
            printError(e);
        }

        return false;
    }

    public boolean updateInteractionWithCommnet(int commentId, int userId, boolean up) {
        String sql = "Update comments set points = points " + (up ? "+ 2 " : "- 2 ") + "WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, commentId);
            preparedStatement.executeUpdate();

            sql = "Update liked_comments SET upvoted = ? WHERE user_id = ? AND comment_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setBoolean(1, up);
            preparedStatement.setInt(2, userId);
            preparedStatement.setInt(3, commentId);
            preparedStatement.executeUpdate();

            return true;
        } catch (SQLException e) {
            printError(e);
        }

        return false;
    }

    public boolean removeInteraction(int commentId, int userId, boolean up) {
        String sql = "Update comments set points = points " + (up ? "- 1 " : "+ 1 ") + "WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, commentId);
            preparedStatement.executeUpdate();

            sql = "DELETE FROM liked_comments WHERE user_id = ? AND comment_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, commentId);
            preparedStatement.executeUpdate();

            return true;
        } catch (SQLException e) {
            printError(e);
        }

        return false;
    }

    public List<Comment> getPostcomments(int postId, int userId) {
        String sql = "SELECT c.*, CASE WHEN l.user_id IS NOT NULL AND l.user_id = ? AND l.comment_id IS NOT NULL THEN TRUE ELSE FALSE END"
                + " AS interacted, l.upvoted, u.username FROM comments c LEFT JOIN liked_comments l ON c.id = l.comment_id"
                + " AND c.author_id = l.user_id JOIN users u ON u.id = c.author_id WHERE c.post_id = ?"
                + " AND c.author_id NOT IN(SELECT blocked_id FROM blocked_users WHERE blocker_id = ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, postId);
            preparedStatement.setInt(3, userId);
            ResultSet rs = preparedStatement.executeQuery();
            List<Comment> results = new ArrayList();

            while (rs.next()) {
                results.add(new Comment(
                        rs.getInt("id"),
                        rs.getInt("author_id"),
                        rs.getString("username"),
                        rs.getInt("post_id"),
                        rs.getInt("comment_id"),
                        rs.getString("content"),
                        rs.getInt("points"),
                        rs.getBoolean("interacted"),
                        rs.getBoolean("upvoted")
                ));
            }

            return results;
        } catch (SQLException e) {
            printError(e);
        }
        return null;
    }

    public List<Comment> getSubcomments(int commentId, int userId) {
        String sql = "SELECT c.*, u.username, CASE WHEN l.user_id IS NOT NULL AND l.user_id = ? AND l.comment_id IS NOT NULL "
                + "THEN TRUE ELSE FALSE END AS interacted, l.upvoted FROM comments c join users u on u.id = c.author_id "
                + "left join liked_comments l on c.id = l.comment_id WHERE c.comment_id = ?"
                + "AND c.author_id NOT IN(SELECT blocked_id FROM blocked_users WHERE blocker_id = ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, commentId);
            preparedStatement.setInt(3, userId);
            ResultSet rs = preparedStatement.executeQuery();
            List<Comment> results = new ArrayList();

            while (rs.next()) {
                results.add(new Comment(
                        rs.getInt("id"),
                        rs.getInt("author_id"),
                        rs.getString("username"),
                        rs.getInt("post_id"),
                        rs.getInt("comment_id"),
                        rs.getString("content"),
                        rs.getInt("points"),
                        rs.getBoolean("interacted"),
                        rs.getBoolean("upvoted")
                ));
            }

            return results;
        } catch (SQLException e) {
            printError(e);
        }
        return null;
    }

    public List<SearchResult> searchPostsResults(String title, String orderBy) {
        String sql = "SELECT * FROM posts JOIN groups ON posts.group_id = groups.id WHERE posts.title LIKE ? ORDER BY posts.title " + orderBy;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, ("%" + title + "%"));

            ResultSet rs = preparedStatement.executeQuery();
            List<SearchResult> results = new ArrayList();

            while (rs.next()) {
                results.add(new SearchResult().withPost(rs.getString("title"), rs.getString("name"), rs.getInt("id")));
            }

            return results;
        } catch (SQLException e) {
            printError(e);
        }
        return null;
    }

    public List<SearchResult> searchGroupsResults(String name, String orderBy) {
        String sql = "SELECT * FROM groups WHERE name LIKE ? ORDER BY groups.name " + orderBy;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, ("%" + name + "%"));

            ResultSet rs = preparedStatement.executeQuery();
            List<SearchResult> results = new ArrayList();

            while (rs.next()) {
                results.add(new SearchResult().withGroup(rs.getString("name")));
            }

            return results;
        } catch (SQLException e) {
            printError(e);
        }
        return null;
    }

    public List<SearchResult> searchUsersResults(String username, String orderBy) {
        String sql = "SELECT * FROM users WHERE username LIKE ? ORDER BY users.username " + orderBy;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, ("%" + username + "%"));

            ResultSet rs = preparedStatement.executeQuery();
            List<SearchResult> results = new ArrayList();

            while (rs.next()) {
                results.add(new SearchResult().withUser(rs.getString("username"), rs.getInt("id")));
            }

            return results;
        } catch (SQLException e) {
            printError(e);
        }
        return null;
    }

    public List<Notification> getUserNotifications(int userId) {
        String sql = "SELECT n.*, u.username, g.name, p.up_votes FROM notifications n left join users u on u.id = n.user_id"
                + " left join groups g on g.id = n.group_id left join posts p on p.id = n.post_id WHERE n.owner_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, userId);

            ResultSet rs = preparedStatement.executeQuery();
            List<Notification> results = new ArrayList();

            while (rs.next()) {
                Notification notification = new Notification(rs.getInt("id"));
                notification.setType(rs.getInt("type"));
                notification.setContent(rs.getString("content"));

                notification.setGroupName(rs.getString("name"));
                if (rs.wasNull()) {
                    notification.setGroupName(null);
                }

                notification.setUserName(rs.getString("username"));
                if (rs.wasNull()) {
                    notification.setUserName(null);
                }

                notification.setLikesAmmount(rs.getInt("up_votes"));
                if (rs.wasNull()) {
                    notification.setLikesAmmount(null);
                }

                notification.setPostId(rs.getInt("post_id"));
                if (rs.wasNull()) {
                    notification.setPostId(null);
                }

                notification.setUserId(rs.getInt("user_id"));
                if (rs.wasNull()) {
                    notification.setUserId(null);
                }

                results.add(notification);
            }

            return results;
        } catch (SQLException e) {
            printError(e);
        }
        return null;
    }

    public boolean addNotification(int type, String content, Integer groupId, String username, Integer likesAmmount, Integer postId, Integer userId, Integer ownerId) {
        String sql = "";

        if (type == 0) {//likes
            sql = "INSERT INTO notifications(owner_id, content, type, post_id)  VALUES (?, ?, ?, ?);";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, ownerId);
                preparedStatement.setString(2, content);
                preparedStatement.setInt(3, type);
                preparedStatement.setInt(4, postId);
                preparedStatement.executeUpdate();

                return true;
            } catch (SQLException e) {
                printError(e);
                return false;
            }
        } else if (type == 2) {//mention
            sql = "INSERT INTO notifications(owner_id, content, type, user_id, post_id)  VALUES (?, ?, ?, ?, ?);";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, ownerId);
                preparedStatement.setString(2, content);
                preparedStatement.setInt(3, type);
                preparedStatement.setInt(4, userId);
                preparedStatement.setInt(5, postId);
                preparedStatement.executeUpdate();

                return true;
            } catch (SQLException e) {
                printError(e);
                return false;
            }
        } else if (type == 1) {//joined group
            sql = "INSERT INTO notifications(owner_id, content, type, group_id)  VALUES (?, ?, ?, ?);";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, ownerId);
                preparedStatement.setString(2, content);
                preparedStatement.setInt(3, type);
                preparedStatement.setInt(4, groupId);
                preparedStatement.executeUpdate();

                return true;
            } catch (SQLException e) {
                printError(e);
                return false;
            }
        } else if (type == 3) {//reply
            sql = "INSERT INTO notifications(owner_id, content, type, post_id, user_id)  VALUES (?, ?, ?, ?, ?);";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, ownerId);
                preparedStatement.setString(2, content);
                preparedStatement.setInt(3, type);
                preparedStatement.setInt(4, postId);
                preparedStatement.setInt(5, userId);
                preparedStatement.executeUpdate();

                return true;
            } catch (SQLException e) {
                printError(e);
                return false;
            }
        } else if (type == 4) {//register
            sql = "INSERT INTO notifications(owner_id, content, type)  VALUES (?, ?, ?);";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, ownerId);
                preparedStatement.setString(2, content);
                preparedStatement.setInt(3, type);
                preparedStatement.executeUpdate();

                return true;
            } catch (SQLException e) {
                printError(e);
                return false;
            }
        }

        return false;
    }

    public boolean blockUser(int blockingUserId, int blockedUserId) {
        String sql = "INSERT INTO blocked_users(blocker_id, blocked_id) VALUES(?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, blockingUserId);
            preparedStatement.setInt(2, blockedUserId);
            preparedStatement.executeUpdate();

            return true;
        } catch (SQLException e) {
            printError(e);
        }

        return false;
    }

    public boolean unBlockUser(int blockingUserId, int blockedUserId) {
        String sql = "DELETE FROM blocked_users WHERE blocker_id = ? AND blocked_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, blockingUserId);
            preparedStatement.setInt(2, blockedUserId);
            preparedStatement.executeUpdate();

            return true;
        } catch (SQLException e) {
            printError(e);
        }

        return false;
    }

    public boolean hasBlocked(int blockingUserId, int blockedUserId) {
        String sql = "SELECT COUNT(*) > 0 AS 'result' FROM blocked_users WHERE blocker_id = ? AND blocked_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, blockingUserId);
            preparedStatement.setInt(2, blockedUserId);
            ResultSet resultSet = preparedStatement.executeQuery();
            boolean result = resultSet.getBoolean("result");
            resultSet.close();

            return result;
        } catch (SQLException e) {
            printError(e);
        }
        return false;
    }

    public List<Pair<String, String>> getBlockedUsers(int userId) {
        String sql = "SELECT * FROM blocked_users b JOIN users u ON b.blocked_id = u.id WHERE b.blocker_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);

            ResultSet rs = preparedStatement.executeQuery();
            List<Pair<String, String>> results = new ArrayList();

            while (rs.next()) {
                results.add(new Pair(rs.getString("username"), rs.getInt("id")));
            }

            return results;
        } catch (SQLException e) {
            printError(e);
        }
        return null;
    }

    private void printError(Exception e) {
        if (e.getMessage() != "ResultSet closed") {
            System.out.println(e.getMessage());
        }
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
