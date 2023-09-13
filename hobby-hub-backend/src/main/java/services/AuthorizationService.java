/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import database.DatabaseManager;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author Zuucker
 */
@Service
public class AuthorizationService {
    
    public boolean isUsernameFree(String username){
        
        DatabaseManager manager = DatabaseManager.getInstance();
        
        List<String> usernames = manager.getUsernames();
        
        boolean isFree = true;
                
        for(int i = 0; i < usernames.size(); i++)
        {
            if(usernames.get(i).equals(username))
            {
                isFree = false;
                break;
            }
        }
                
        return isFree;
    }
    
    public boolean registerUser(String username, String email, String password, String passwordConfirmation){
        
        DatabaseManager manager = DatabaseManager.getInstance();
        
        //other checks should be added here like is correct username/email/password etc
        if(password.equals(passwordConfirmation) && isUsernameFree(username))
            return manager.addUser(username, email, password);
        
        return false;
    }
    
}
