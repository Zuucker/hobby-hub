/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import org.json.JSONObject;

/**
 *
 * @author Zuucker
 */
public class SearchResult {

    private String username;
    private String groupName;
    private String profilePicture;
    private String postTitle;
    private String type;
    private int postId;

    public SearchResult() {
    }

    public SearchResult withUser(String username, int userId) {
        this.type = "user";
        this.username = username;
        this.profilePicture = "../media/avatars/" + userId + ".jpg";
        return this;
    }

    public SearchResult withPost(String postTitle, String groupName, int postId) {
        this.type = "post";
        this.postTitle = postTitle;
        this.groupName = groupName;
        this.postId = postId;
        return this;
    }

    public SearchResult withGroup(String groupName) {
        this.type = "group";
        this.groupName = groupName;
        return this;
    }

    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", this.username);
        jsonObject.put("groupName", this.groupName);
        jsonObject.put("postTitle", this.postTitle);
        jsonObject.put("postId", this.postId);
        jsonObject.put("profilePicture", this.profilePicture);
        jsonObject.put("type", this.type);

        return jsonObject;
    }
}
