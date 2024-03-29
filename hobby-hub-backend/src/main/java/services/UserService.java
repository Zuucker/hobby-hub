/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import database.DatabaseManager;
import imageManager.ImageManager;
import java.util.ArrayList;
import java.util.List;
import jwtManager.JWTManager;
import models.Group;
import models.Notification;
import models.Post;
import models.User;
import org.javatuples.Pair;
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

        String avatarImg = imageManager.readPath(user.getId());

        if (avatarImg != "-1") {
            result.json.append("profilePic", avatarImg);
        } else {
            result.status = false;
        }

        return result;
    }

    public ServiceResult getUserData2(String jwToken) {

        ServiceResult result = new ServiceResult();
        DatabaseManager manager = DatabaseManager.getInstance();
        ImageManager imageManager = new ImageManager();

        JWTManager jwtManager = new JWTManager();
        int userId = Integer.parseInt(jwtManager.getSubject(jwToken));

        boolean validated = jwtManager.validatetoken(jwToken, userId);

        if (validated) {
            User user = manager.getUser(userId);

            result.json = user.toJson();
            result.status = user.getUsername() != null;

            String avatarImg = imageManager.readPath(user.getId());
            if (avatarImg != "-1") {
                result.json.append("profilePic", avatarImg);
            } else {
                result.status = false;
            }
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

    public ServiceResult getUserGroups(int userId) {

        ServiceResult result = new ServiceResult();

        DatabaseManager manager = DatabaseManager.getInstance();

        List<Group> groups = manager.getUserGroups(userId);

        JSONObject newJson = new JSONObject();
        for (int i = 0;
                i < groups.size();
                i++) {
            newJson.append("groups", groups.get(i).toJson());
        }

        result.json = newJson;
        result.status = true;

        return result;
    }

    public ServiceResult getUserPosts(int userId) {

        ServiceResult result = new ServiceResult();
        DatabaseManager manager = DatabaseManager.getInstance();
        JWTManager jwtManager = new JWTManager();

        List<Post> posts = manager.getUserPosts(userId);

        JSONObject newJson = new JSONObject();
        for (int i = 0;
                i < posts.size();
                i++) {
            newJson.append("posts", posts.get(i).toJson());
        }

        result.json = newJson;
        result.status = true;

        return result;
    }

    public ServiceResult getAllUserPosts(String jwtToken) {

        ServiceResult result = new ServiceResult();
        DatabaseManager manager = DatabaseManager.getInstance();
        JWTManager jwtManager = new JWTManager();
        int userId = Integer.parseInt(jwtManager.getSubject(jwtToken));

        List<Post> posts = manager.getAllUserPosts(userId);

        JSONObject newJson = new JSONObject();
        for (int i = 0;
                i < posts.size();
                i++) {
            newJson.append("posts", posts.get(i).toJson());
        }

        result.json = newJson;
        result.status = true;

        return result;
    }

    public ServiceResult hasJoinedGroup(String token, int groupId) {

        ServiceResult result = new ServiceResult();
        JWTManager jwtManager = new JWTManager();

        int userId = Integer.parseInt(jwtManager.getSubject(token));

        DatabaseManager manager = DatabaseManager.getInstance();

        result.status = manager.checkIfSubscribed(userId, groupId);

        result.value = "ok";

        return result;
    }

    public ServiceResult getUserNotifications(String jwtToken) {

        ServiceResult result = new ServiceResult();
        DatabaseManager manager = DatabaseManager.getInstance();
        JWTManager jwtManager = new JWTManager();

        int userId = Integer.parseInt(jwtManager.getSubject(jwtToken));

        List<Notification> notifications = manager.getUserNotifications(userId);

        JSONObject newJson = new JSONObject();
        for (int i = 0;
                i < notifications.size();
                i++) {
            newJson.append("notifications", notifications.get(i).toJson());
        }

        result.json = newJson;
        result.status = true;

        return result;
    }

    public ServiceResult blockUser(String token, int blockedUserId) {

        ServiceResult result = new ServiceResult();
        JWTManager jwtManager = new JWTManager();
        DatabaseManager manager = DatabaseManager.getInstance();

        int userId = Integer.parseInt(jwtManager.getSubject(token));

        if (userId != blockedUserId && !manager.hasBlocked(userId, blockedUserId)) {

            result.status = manager.blockUser(userId, blockedUserId);
            result.value = "ok";
        } else {
            result.status = true;
            result.value = "nie ok";
        }

        return result;
    }

    public ServiceResult unBlockUser(String token, int blockedUserId) {

        ServiceResult result = new ServiceResult();
        JWTManager jwtManager = new JWTManager();
        DatabaseManager manager = DatabaseManager.getInstance();

        int userId = Integer.parseInt(jwtManager.getSubject(token));

        if (userId != blockedUserId && manager.hasBlocked(userId, blockedUserId)) {

            result.status = manager.unBlockUser(userId, blockedUserId);
            result.value = "ok";
        } else {
            result.status = true;
            result.value = "nie ok";
        }

        return result;
    }

    public ServiceResult getBlockedUsers(int userId) {

        ServiceResult result = new ServiceResult();

        DatabaseManager manager = DatabaseManager.getInstance();

        List<Pair<String, String>> users = manager.getBlockedUsers(userId);

        JSONObject newJson = new JSONObject();
        for (int i = 0;
                i < users.size();
                i++) {
            JSONObject json = new JSONObject();
            json.put("username", users.get(i).getValue0());
            json.put("userId", users.get(i).getValue1());

            newJson.append("blockedUsers", json);
        }

        result.json = newJson;
        result.status = true;

        return result;
    }
}
