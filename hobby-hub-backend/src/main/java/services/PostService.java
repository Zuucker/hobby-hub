/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import database.DatabaseManager;
import httpRequestJson.HttpRequestJson;
import imageManager.ImageManager;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import jwtManager.JWTManager;
import models.Comment;
import models.Post;
import org.json.JSONObject;
import serviceResult.ServiceResult;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Zuucker
 */
public class PostService {

    public ServiceResult addPost(Post post, String jwtToken, HttpRequestJson json) {

        ServiceResult result = new ServiceResult();
        DatabaseManager manager = DatabaseManager.getInstance();
        JWTManager jwtManager = new JWTManager();
        ImageManager imageManager = new ImageManager();

        int userId = Integer.parseInt(jwtManager.getSubject(jwtToken));

        post.setAuthorId(userId);

        String postType = post.getType();

        result.value = String.valueOf(manager.addPost(post));

        if (result.value != "-1") {

            switch (postType) {
                case "text" -> {
                    String filePath = "..\\front-end\\public\\media\\posts\\text\\";

                    try {
                        FileWriter fileWriter = new FileWriter(filePath + result.value + ".txt");

                        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

                        bufferedWriter.write(json.getTextContent());

                        bufferedWriter.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                        result.status = false;
                    }
                }

                case "image" -> {
                    imageManager.saveBase64toDisk(json.getImageContent(), "posts\\image\\" + result.value);
                }

                case "video" -> {
                    String filePath = "..\\front-end\\public\\media\\posts\\video\\";

                    String[] parts = json.getVideoContent().split(",");
                    if (parts.length != 2) {
                        break;
                    }

                    String base64Content = parts[1];

                    byte[] videoBytes = Base64.getDecoder().decode(base64Content);

                    try {
                        Path path = Paths.get(filePath + result.value + ".mp4");
                        Files.write(path, videoBytes);

                    } catch (Exception e) {
                        e.printStackTrace();
                        result.status = false;
                    }
                }

                default -> {
                    System.out.println("wrong type of post");
                }
            }
        } else {
            result.status = false;
        }

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
                result.status = manager.addNotification(1, "Your post reached ", null, null, likes, post.getId(), null, post.getAuthorId());
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

        if (result.status && content.contains("@")) {

            Pattern pattern = Pattern.compile("@[a-zA-Z0-9_]+");
            Matcher matcher = pattern.matcher(content);

            while (matcher.find()) {
                String tag = matcher.group();

                int ownerId = manager.getUser(tag.replace("@", "")).getId();

                manager.addNotification(2, "mentioned you!", null, null, null, postId, userId, ownerId);
            }
        }

        result.value = result.status ? "ok" : "nieok";

        return result;
    }

    public ServiceResult addSubcomment(int commentId, String content, String jwtToken) {

        ServiceResult result = new ServiceResult();
        DatabaseManager manager = DatabaseManager.getInstance();
        JWTManager jwtManager = new JWTManager();

        int userId = Integer.parseInt(jwtManager.getSubject(jwtToken));

        result.status = manager.addsubcomment(userId, commentId, content);

        if (result.status) {
            int commentAuthorId = manager.getComment(commentId).getAuthorId();
            int postId = manager.getComment(commentId).getPostId();
            manager.addNotification(3, "replied to you!", null, null, null, postId, userId, commentAuthorId);
        }

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
