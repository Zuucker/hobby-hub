/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import database.DatabaseManager;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Zuucker
 */

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class testController {
    
    @GetMapping("/test")
    public String test(){
            
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", "Hello, world!");
        
        DatabaseManager manager = DatabaseManager.getInstance();
        
        System.out.println(manager.getUsers().get(0));

        return jsonObject.toString();
    }
}
