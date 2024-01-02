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
public class Notification {

    private Integer id;
    private String content;
    private Integer userId;
    private Integer postId;
    private String groupName;
    private String userName;
    private Integer type;
    private Integer likesAmmount;

    public Notification(int id, int type, String content, String groupName, String userName, Integer likesAmmount, Integer postId, Integer userId) {

        this.id = id;
        this.content = content;
        this.userId = userId;
        this.postId = postId;
        this.groupName = groupName;
        this.userName = userName;
        this.type = type;
        this.likesAmmount = likesAmmount;
    }

    public Notification(int id) {
        this.id = id;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setLikesAmmount(Integer likesAmmount) {
        this.likesAmmount = likesAmmount;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", this.id);
        jsonObject.put("type", this.type);
        jsonObject.put("groupName", this.groupName);
        jsonObject.put("username", this.userName);
        jsonObject.put("likesAmmount", this.likesAmmount);
        jsonObject.put("content", this.content);
        jsonObject.put("postId", this.postId);
        jsonObject.put("userId", this.userId);

        return jsonObject;
    }
}
