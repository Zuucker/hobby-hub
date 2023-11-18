/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import database.DatabaseManager;
import java.util.List;
import jwtManager.JWTManager;
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

    public ServiceResult subcribeToGroup(int userId, int groupId) {

        ServiceResult result = new ServiceResult();
        DatabaseManager manager = DatabaseManager.getInstance();

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
}
