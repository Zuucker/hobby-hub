/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import database.DatabaseManager;
import java.util.ArrayList;
import java.util.List;
import models.SearchResult;
import org.json.JSONObject;
import serviceResult.ServiceResult;

/**
 *
 * @author Zuucker
 */
public class SearchService {

    public ServiceResult getSerachResults(String titleQuery, String groupsQuery, String usersQuery, String sortBy) {

        ServiceResult result = new ServiceResult();
        DatabaseManager manager = DatabaseManager.getInstance();

        List<SearchResult> searchResults = new ArrayList();

        if (titleQuery != null) {
            searchResults.addAll(manager.searchPostsResults(titleQuery, sortBy));
        }

        if (groupsQuery != null) {
            searchResults.addAll(manager.searchGroupsResults(groupsQuery, sortBy));
        }

        if (usersQuery != null) {
            searchResults.addAll(manager.searchUsersResults(usersQuery, sortBy));
        }
        JSONObject newJson = new JSONObject();

        for (int i = 0;
                i < searchResults.size();
                i++) {
            newJson.append("searchResults", searchResults.get(i).toJson());
        }

        result.json = newJson;
        result.status = true;

        return result;
    }
}
