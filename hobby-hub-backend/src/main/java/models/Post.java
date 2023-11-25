/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import httpRequestJson.HttpRequestJson;
import org.json.JSONObject;

/**
 *
 * @author Zuucker
 */
public class Post {

    private int id;
    private int authorId;
    private int groupId;
    private String title;
    private String type;
    private String link;
    private int upVotes;
    private int downVotes;

    public Post(HttpRequestJson request) {
        this.authorId = request.getPostAuthorId();
        this.groupId = request.getGroupId();
        this.title = request.getPostTitle();
        this.type = request.getPostType();
        this.link = request.getPostLink();
        this.upVotes = 0;
        this.downVotes = 0;
    }

    public Post(int id, int authorId, int groupId, String title, String type, String link, int upVotes, int downVotes) {
        this.id = id;
        this.authorId = authorId;
        this.groupId = groupId;
        this.title = title;
        this.type = type;
        this.link = link;
        this.upVotes = upVotes;
        this.downVotes = downVotes;
    }

    public int getId() {
        return this.id;
    }

    public int getAuthorId() {
        return this.authorId;
    }

    public int getGroupId() {
        return this.groupId;
    }

    public String getTitle() {
        return this.title;
    }

    public String getType() {
        return this.type;
    }

    public String getLink() {
        return this.link;
    }

    public int getUpVotes() {
        return this.upVotes;
    }

    public int getDownVotes() {
        return this.downVotes;
    }

    public void setAuthorId(int id) {
        this.authorId = id;
    }

    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", this.id);
        jsonObject.put("authorId", this.authorId);
        jsonObject.put("groupId", this.groupId);
        jsonObject.put("title", this.title);
        jsonObject.put("type", this.type);
        jsonObject.put("link", this.link);

        return jsonObject;
    }
}
