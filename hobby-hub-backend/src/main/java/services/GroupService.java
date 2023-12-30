/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import database.DatabaseManager;
import java.util.List;
import jwtManager.JWTManager;
import models.Group;
import models.Post;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import serviceResult.ServiceResult;

/**
 *
 * @author Zuucker
 */
@Service
public class GroupService {

    public ServiceResult addGroup(String name, String description, String jwtToken) {

        ServiceResult result = new ServiceResult();
        DatabaseManager manager = DatabaseManager.getInstance();
        JWTManager jwtManager = new JWTManager();

        int userId = Integer.parseInt(jwtManager.getSubject(jwtToken));

        if (description == null) {
            description = "";
        }

        List<String> groups = manager.getGroupNames();

        if (!groups.contains(name)) {
            result.status = manager.addGroup(name, description, userId);
        }

        result.value = result.status ? "ok" : "nieok";

        return result;
    }

    public ServiceResult subcribeToGroup(String jwtToken, int groupId) {

        ServiceResult result = new ServiceResult();
        DatabaseManager manager = DatabaseManager.getInstance();

        JWTManager jwtManager = new JWTManager();

        int userId = Integer.parseInt(jwtManager.getSubject(jwtToken));

        boolean isSubscribed = manager.checkIfSubscribed(userId, groupId);

        if (!isSubscribed) {
            result.status = manager.subscribeToGroup(userId, groupId);
        }

        result.value = result.status ? "ok" : "nieok";

        return result;
    }

    public ServiceResult isNameFree(String name) {

        ServiceResult result = new ServiceResult();
        DatabaseManager manager = DatabaseManager.getInstance();

        List<String> groupNames = manager.getGroupNames();

        result.status = true;

        for (int i = 0; i < groupNames.size(); i++) {
            if (groupNames.get(i).equals(name)) {
                result.status = false;
                break;
            }
        }

        result.value = result.status ? "ok" : "nieok";

        return result;
    }

    public ServiceResult getGroupData(String name) {

        ServiceResult result = new ServiceResult();
        DatabaseManager manager = DatabaseManager.getInstance();

        Group group = manager.getGroupData(name);

        result.json = group.toJson();

        result.json.put("owner", manager.getUsername(group.getOwnerId()));

        result.status = true;
        result.value = result.status ? "ok" : "nieok";

        return result;
    }

    public ServiceResult editGroup(int id, String name, String description, String jwtToken) {

        ServiceResult result = new ServiceResult();
        DatabaseManager manager = DatabaseManager.getInstance();
        JWTManager jwtManager = new JWTManager();

        int userId = Integer.parseInt(jwtManager.getSubject(jwtToken));

        if (description == null) {
            description = "";
        }

        Group group = manager.getGroupData(id);

        if (userId == group.getOwnerId()) {
            result.status = manager.editGroup(id, name, description);
        }

        result.value = result.status ? "ok" : "nieok";

        return result;
    }

    public ServiceResult leaveGroup(int id, String jwtToken) {

        ServiceResult result = new ServiceResult();
        DatabaseManager manager = DatabaseManager.getInstance();
        JWTManager jwtManager = new JWTManager();

        int userId = Integer.parseInt(jwtManager.getSubject(jwtToken));

        Group group = manager.getGroupData(id);

        if (userId == group.getOwnerId()) {
            result.status = manager.removeGroup(id);
        } else {
            result.status = manager.removeGroupSubscription(id, userId);
        }

        result.value = result.status ? "ok" : "nieok";

        return result;
    }

    public ServiceResult getGroupPosts(int groupId) {

        ServiceResult result = new ServiceResult();
        DatabaseManager manager = DatabaseManager.getInstance();

        List<Post> posts = manager.getGroupPosts(groupId);

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

    public ServiceResult getTopGroups() {

        ServiceResult result = new ServiceResult();
        DatabaseManager manager = DatabaseManager.getInstance();

        List<Group> groups = manager.getTopGroups();

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
}
