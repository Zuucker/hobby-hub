/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import database.DatabaseManager;
import imageManager.ImageManager;
import java.util.List;
import jwtManager.JWTManager;
import models.Group;
import models.User;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import serviceResult.ServiceResult;

/**
 *
 * @author Zuucker
 */
@Service
public class UserService {

    public ServiceResult getUserData(String username) {

        ServiceResult result = new ServiceResult();
        DatabaseManager manager = DatabaseManager.getInstance();
        ImageManager imageManager = new ImageManager();

        User user = manager.getUser(username);

        result.json = user.toJson();
        result.status = user.getUsername() != null;

        String avatarImg = imageManager.readImagetoBase64(user.getId());
        if (avatarImg != "-1") {
            result.json.append("profilePic", avatarImg);
        } else {
            result.status = false;
        }

        return result;
    }

    public ServiceResult editUserData(User userData, String jwtToken, String newImg) {

        ServiceResult result = new ServiceResult();
        DatabaseManager manager = DatabaseManager.getInstance();
        ImageManager imageManager = new ImageManager();

        JWTManager jwtManager = new JWTManager();
        boolean validated = jwtManager.validatetoken(jwtToken, userData.getId());

        if (validated) {
            result.status = manager.editUser(userData.getId(), userData.getUsername(), userData.getBio());
        }

        if (result.status) {
            imageManager.saveBase64toDisk(newImg, userData.getId());
        }

        result.value = result.status ? "ok" : "nieok";

        return result;
    }

    public ServiceResult getUsernameFromJwt(String token) {

        ServiceResult result = new ServiceResult();
        JWTManager jwtManager = new JWTManager();

        int userId = Integer.parseInt(jwtManager.getSubject(token));

        DatabaseManager manager = DatabaseManager.getInstance();

        result.value = manager.getUsername(userId);

        result.status = true;

        return result;
    }

    public ServiceResult addGroup(String name, String description, String token) {

        ServiceResult result = new ServiceResult();
        JWTManager jwtManager = new JWTManager();

        int userId = Integer.parseInt(jwtManager.getSubject(token));

        DatabaseManager manager = DatabaseManager.getInstance();

        result.status = manager.addGroup(name, description, userId);

        result.value = result.status ? "ok" : "nieok";

        return result;
    }

    public ServiceResult getUserGroups(String token) {

        ServiceResult result = new ServiceResult();
        JWTManager jwtManager = new JWTManager();

        int userId = Integer.parseInt(jwtManager.getSubject(token));

        DatabaseManager manager = DatabaseManager.getInstance();

        List<Group> groups = manager.getUserGroups(userId);

        JSONObject newJson = new JSONObject();
        for (int i = 0;
                i < groups.size();
                i++) {
            newJson.put(String.valueOf(i), groups.get(i).toJson());
        }

        result.json = newJson;
        result.status = true;

        return result;
    }
}
