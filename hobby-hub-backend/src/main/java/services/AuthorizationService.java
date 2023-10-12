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
import serviceResult.ServiceResult;

/**
 *
 * @author Zuucker
 */
@Service
public class AuthorizationService {

    public boolean isUsernameFree(String username) {

        ServiceResult result = new ServiceResult();
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

    public ServiceResult registerUser(String username, String email, String password, String passwordConfirmation) {

        ServiceResult result = new ServiceResult();
        DatabaseManager manager = DatabaseManager.getInstance();

        //other checks should be added here like is correct username/email/password etc
        //and send email with validation link
        if (password.equals(passwordConfirmation) && isUsernameFree(username)) {
            result.status = manager.addUser(username, email, password);
            result.value = "ok";
        }

        KeyGenerator keyGenerator = new KeyGenerator();
        String code = keyGenerator.generateKey(128);

        result.status = result.status && manager.addVerificationCodeForUser(username, code);
        if (!result.status) {
            result.value = "not ok";
        }

        return result;
    }

    public ServiceResult loginUser(String username, String password) {

        ServiceResult result = new ServiceResult();
        DatabaseManager manager = DatabaseManager.getInstance();

        boolean userExists = manager.checkIfUserExists(username);
        String usersPassword = manager.getPassword(username);
        boolean isVerified = manager.getVerified(username);

        if (userExists && isVerified && usersPassword.equals(password)) {
            JWTManager jwtManager = new JWTManager();
            String token = jwtManager.generateToken(username);

            result.value = token;
            result.status = true;
        }

        if (!userExists) {
            result.value = "No such user exists";
            result.status = false;
            return result;
        }

        if (userExists && !usersPassword.equals(password)) {
            result.value = "Incorrect password";
            result.status = false;
            return result;
        }

        if (!isVerified) {
            result.value = "User is not verified";
            result.status = false;
            return result;
        }

        return result;
    }

    public ServiceResult verifyUser(String code) {

        ServiceResult result = new ServiceResult();
        DatabaseManager manager = DatabaseManager.getInstance();

        result.status = manager
                .verifyUser(code);
        result.value = "ok";

        return result;
    }

    public ServiceResult checkIfUserIfVerified(String username) {

        ServiceResult result = new ServiceResult();
        DatabaseManager manager = DatabaseManager.getInstance();

        result.status = manager.getVerified(username);
        result.value = "ok";

        return result;
    }

}
