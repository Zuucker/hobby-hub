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
    
    private DatabaseManager(){};
    
    public static DatabaseManager getInstance() {
        if(databaseManagerInstance == null) {
            try {
                Class.forName("org.sqlite.JDBC");
            } catch (ClassNotFoundException e) {
                System.err.println("SQLite JDBC driver not found");
                e.printStackTrace();
            }
            
            try {
                connection = DriverManager.getConnection(databaseUrl);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            
            
            //check if schema exists
            boolean schemaExists = false;
            try{
                DatabaseMetaData databaseMetaData = connection.getMetaData();
                ResultSet resultSet = databaseMetaData.getTables(null, null, null, new String[] {"TABLE"});
                schemaExists = !resultSet.next();
            }catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            
            String createSql = "";
            
            //create schema
            if(schemaExists){
                String filePath = "../DB/databaseSchema.sql";
                
                try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        createSql+=line;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                
                try{
                    Statement statement = connection.createStatement();
                    statement.execute(createSql);
                }catch (SQLException e) {
                    System.out.println(e.getMessage());
                }


                //insert initial data
                try{
                    String sql = "INSERT INTO users(username, email, password) VALUES(?,?,?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, "user");
                    preparedStatement.setString(2, "email");
                    preparedStatement.setString(3, "password");
                    preparedStatement.executeUpdate();
                }catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
            
            databaseManagerInstance = new DatabaseManager();
        }
        
        return databaseManagerInstance;
    }
    
    public List<String> getUsers(){
        String sql = "SELECT * FROM users";
        try{
            Statement statement  = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            List<String> results = new ArrayList();
            
            while (resultSet.next()) {
                results.add(resultSet.getString("username"));
            }
            return results;
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    public void close(){
        try{
            connection.close();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
