/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import database.DatabaseManager;
import java.util.List;
import jwtManager.JWTManager;
import keyGenerator.KeyGenerator;
import org.springframework.stereotype.Service;

/**
 *
 * @author Zuucker
 */
@Service
public class AuthorizationService {

    public boolean isUsernameFree(String username) {

        DatabaseManager manager = DatabaseManager.getInstance();

        List<String> usernames = manager.getUsernames();

        boolean isFree = true;

        for (int i = 0; i < usernames.size(); i++) {
            if (usernames.get(i).equals(username)) {
                isFree = false;
                break;
            }
        }

        return isFree;
    }

    public boolean registerUser(String username, String email, String password, String passwordConfirmation) {

        DatabaseManager manager = DatabaseManager.getInstance();

        //other checks should be added here like is correct username/email/password etc
        //and send email with validation link
        boolean status = false;

        if (password.equals(passwordConfirmation) && isUsernameFree(username)) {
            status = manager.addUser(username, email, password);
        }

        KeyGenerator keyGenerator = new KeyGenerator();
        String code = keyGenerator.generateKey(128);

        status = status && manager.addVerificationCodeForUser(username, code);

        return status;
    }

    public String loginUser(String username, String password) {

        DatabaseManager manager = DatabaseManager.getInstance();

        boolean userExists = manager.checkIfUserExists(username);
        String usersPassword = manager.getPassword(username);
        boolean isVerified = manager.getVerified(username);

        if (userExists && isVerified && usersPassword.equals(password)) {
            JWTManager jwtManager = new JWTManager();
            String token = jwtManager.generateToken(username);

            return token;
        }

        return "-1";
    }

    public boolean verifyUser(String code) {

        DatabaseManager manager = DatabaseManager.getInstance();

        return manager.verifyUser(code);
    }

    public boolean checkIfUserIfVerified(String username) {

        DatabaseManager manager = DatabaseManager.getInstance();

        return manager.getVerified(username);
    }

}
