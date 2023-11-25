/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import database.DatabaseManager;
import jwtManager.JWTManager;
import models.Post;
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

        int likes = post.getUpVotes();

        if (likes == 10 || likes == 100 || likes == 1000 || likes == 10000) {
            String notificationContent = "Congrats your post reached " + String.valueOf(likes);
            result.status = manager.addNotifcation(post.getAuthorId(), notificationContent);
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
}
