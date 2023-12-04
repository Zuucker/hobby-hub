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
public class Comment {

    private int id;
    private int authorId;
    private String author;
    private int postId;
    private int commentId;
    private String content;
    private int points;
    private boolean interacted;
    private boolean upvoted;

    public Comment(int id, int authorId, String author, int postId, int commentId, String content, int points) {
        this.id = id;
        this.authorId = authorId;
        this.author = author;
        this.postId = postId;
        this.commentId = commentId;
        this.content = content;
        this.points = points;
    }

    public Comment(int id, int authorId, String author, int postId, int commentId, String content, int points, boolean interacted, boolean upvoted) {
        this.id = id;
        this.authorId = authorId;
        this.author = author;
        this.postId = postId;
        this.commentId = commentId;
        this.content = content;
        this.points = points;
        this.interacted = interacted;
        this.upvoted = upvoted;
    }

    public int getId() {
        return this.id;
    }

    public int getAuthorId() {
        return this.authorId;
    }

    public int getPostId() {
        return this.postId;
    }

    public String getAuthor() {
        return this.author;
    }

    public int getCommentId() {
        return this.commentId;
    }

    public String getContent() {
        return this.content;
    }

    public int getPoints() {
        return this.points;
    }

    public void print() {
        System.out.println(this.id);
        System.out.println(this.authorId);
        System.out.println(this.author);
        System.out.println(this.postId);
        System.out.println(this.commentId);
        System.out.println(this.content);
        System.out.println(this.points);
    }

    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", this.id);
        jsonObject.put("authorId", this.authorId);
        jsonObject.put("author", this.author);
        jsonObject.put("postId", this.postId);
        jsonObject.put("commentId", this.commentId);
        jsonObject.put("content", this.content);
        jsonObject.put("points", this.points);
        jsonObject.put("interacted", this.interacted);
        jsonObject.put("upvoted", this.upvoted);

        return jsonObject;
    }
}
