/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import database.DatabaseManager;
import java.util.List;
import jwtManager.JWTManager;
import models.Comment;
import models.Post;
import org.json.JSONArray;
import org.json.JSONObject;
import serviceResult.ServiceResult;

/**
 *
 * @author Zuucker
 */
public class PostService {

    public ServiceResult addPost(Post post, String jwtToken) {

        ServiceResult result = new ServiceResult();
        DatabaseManager manager = DatabaseManager.getInstance();
        JWTManager jwtManager = new JWTManager();

        int userId = Integer.parseInt(jwtManager.getSubject(jwtToken));

        post.setAuthorId(userId);

        result.status = manager.addPost(post);

        result.value = result.status ? "ok" : "nieok";

        return result;
    }

    public ServiceResult likePost(int postId, String jwtToken) {

        ServiceResult result = new ServiceResult();
        DatabaseManager manager = DatabaseManager.getInstance();
        JWTManager jwtManager = new JWTManager();

        int userId = Integer.parseInt(jwtManager.getSubject(jwtToken));

        if (!manager.isLikedByUser(postId, userId)) {
            result.status = manager.likePost(userId, postId);
        }

        Post post = manager.getPost(postId);
        if (post != null) {

            int likes = post.getUpVotes();

            if (likes == 10 || likes == 100 || likes == 1000 || likes == 10000) {
                String notificationContent = "Congrats your post reached " + String.valueOf(likes);
                result.status = manager.addNotifcation(post.getAuthorId(), notificationContent);
            }
        }
        result.value = result.status ? "ok" : "nieok";

        return result;
    }

    public ServiceResult dislikePost(int postId, String jwtToken) {

        ServiceResult result = new ServiceResult();
        DatabaseManager manager = DatabaseManager.getInstance();
        JWTManager jwtManager = new JWTManager();

        int userId = Integer.parseInt(jwtManager.getSubject(jwtToken));

        if (!manager.isDislikedByUser(postId, userId)) {
            result.status = manager.dislikePost(userId, postId);
        }

        result.value = result.status ? "ok" : "nieok";

        return result;
    }

    public ServiceResult unLikePost(int postId, String jwtToken) {

        ServiceResult result = new ServiceResult();
        DatabaseManager manager = DatabaseManager.getInstance();
        JWTManager jwtManager = new JWTManager();

        int userId = Integer.parseInt(jwtManager.getSubject(jwtToken));

        if (manager.isLikedByUser(postId, userId)) {
            result.status = manager.unLikePost(userId, postId);
        }

        result.value = result.status ? "ok" : "nieok";

        return result;
    }

    public ServiceResult unDislikePost(int postId, String jwtToken) {

        ServiceResult result = new ServiceResult();
        DatabaseManager manager = DatabaseManager.getInstance();
        JWTManager jwtManager = new JWTManager();

        int userId = Integer.parseInt(jwtManager.getSubject(jwtToken));

        if (manager.isDislikedByUser(postId, userId)) {
            result.status = manager.unDislikePost(userId, postId);
        }

        result.value = result.status ? "ok" : "nieok";

        return result;
    }

    public ServiceResult addCommentToPost(int postId, String content, String jwtToken) {

        ServiceResult result = new ServiceResult();
        DatabaseManager manager = DatabaseManager.getInstance();
        JWTManager jwtManager = new JWTManager();

        int userId = Integer.parseInt(jwtManager.getSubject(jwtToken));

        result.status = manager.addCommentToPost(userId, postId, content);

        result.value = result.status ? "ok" : "nieok";

        return result;
    }

    public ServiceResult addSubcomment(int commentId, String content, String jwtToken) {

        ServiceResult result = new ServiceResult();
        DatabaseManager manager = DatabaseManager.getInstance();
        JWTManager jwtManager = new JWTManager();

        int userId = Integer.parseInt(jwtManager.getSubject(jwtToken));

        result.status = manager.addsubcomment(userId, commentId, content);

        result.value = result.status ? "ok" : "nieok";

        return result;
    }

    public ServiceResult interactWithCommentPoint(int commentId, String jwtToken, boolean up) {

        ServiceResult result = new ServiceResult();
        DatabaseManager manager = DatabaseManager.getInstance();
        JWTManager jwtManager = new JWTManager();

        int userId = Integer.parseInt(jwtManager.getSubject(jwtToken));

        if (!manager.hasInteractedWithCommentPoint(commentId, userId)) {
            result.status = manager.interactWithCommentPoint(userId, commentId, up);
            //System.out.println("dodaje " + up);
        } else {
            boolean upvoted = manager.getInteractionType(userId, commentId);
            if (upvoted != up) {
                result.status = manager.updateInteractionWithCommnet(commentId, userId, up);
                //System.out.println("update " + up);
            } else {
                result.status = manager.removeInteraction(commentId, userId, up);
                //System.out.println("remove " + up);
            }
        }

        Comment comment = manager.getComment(commentId);
        if (comment != null) {

            int points = comment.getPoints();

            if (points == 10 || points == 100 || points == 1000 || points == 10000) {
                String notificationContent = "Congrats your comment reached " + String.valueOf(points);
                result.status = manager.addNotifcation(comment.getAuthorId(), notificationContent);
            }
        }
        result.value = result.status ? "ok" : "nieok";

        return result;
    }

    public ServiceResult getPostComments(int postId, String jwtToken) {

        ServiceResult result = new ServiceResult();
        DatabaseManager manager = DatabaseManager.getInstance();
        JWTManager jwtManager = new JWTManager();

        int userId = Integer.parseInt(jwtManager.getSubject(jwtToken));

        List<Comment> comments = manager.getPostcomments(postId, userId);

        JSONObject newJson = new JSONObject();
        for (int i = 0;
                i < comments.size();
                i++) {

            JSONObject commentJson = comments.get(i).toJson();
            JSONObject subcommentsJson = getSubComments(userId, comments.get(i).getId());
            if (subcommentsJson != null && !subcommentsJson.isEmpty()) {

                for (String key : subcommentsJson.keySet()) {
                    commentJson.put(key, subcommentsJson.get(key));
                }
            }

            newJson.append("comments", commentJson);

        }

        result.json = newJson;
        result.status = true;

        return result;
    }

    private JSONObject getSubComments(int userId, int commentId) {

        DatabaseManager manager = DatabaseManager.getInstance();
        List<Comment> subComments = manager.getSubcomments(commentId, userId);

        if (subComments.isEmpty()) {
            return null;
        }

        JSONObject json = new JSONObject();

        for (int j = 0;
                j < subComments.size();
                j++) {

            JSONObject commenJson = subComments.get(j).toJson();
            JSONObject subCommentsJson = getSubComments(userId, subComments.get(j).getId());
            if (subCommentsJson != null && !subCommentsJson.isEmpty()) {
                for (String key : subCommentsJson.keySet()) {
                    commenJson.put(key, subCommentsJson.get(key));
                }
            }

            json.append("subcomments", commenJson);

        }

        return json;
    }

    public ServiceResult getPostData(int postId) {

        ServiceResult result = new ServiceResult();
        DatabaseManager manager = DatabaseManager.getInstance();

        Post post = manager.getPost(postId);

        if (post != null) {

            JSONObject newJson = new JSONObject();

            newJson.put("post", post.toJson());

            result.json = newJson;
        }

        result.status = true;

        return result;
    }
}
