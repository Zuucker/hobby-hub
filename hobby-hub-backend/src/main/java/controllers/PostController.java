/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import httpRequestJson.HttpRequestJson;
import models.Post;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import serviceResult.ServiceResult;
import services.PostService;

/**
 *
 * @author Zuucker
 */
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", allowCredentials = "true")
@RequestMapping("/post")
@RestController
public class PostController {

    @PostMapping("/add")
    public String addPost(@RequestBody HttpRequestJson requestJson) {

        PostService postService = new PostService();
        Post post = new Post(requestJson);

        ServiceResult result = postService.addPost(post, requestJson.getJwtToken(), requestJson);

        return result.toJson();
    }

    @PostMapping("/like")
    public String likePost(@RequestBody HttpRequestJson requestJson) {

        PostService postService = new PostService();

        ServiceResult result = postService.likePost(requestJson.getPostId(), requestJson.getJwtToken());

        return result.toJson();
    }

    @PostMapping("/dislike")
    public String dislikePost(@RequestBody HttpRequestJson requestJson) {

        PostService postService = new PostService();

        ServiceResult result = postService.dislikePost(requestJson.getPostId(), requestJson.getJwtToken());

        return result.toJson();
    }

    @PostMapping("/unLike")
    public String unLikePost(@RequestBody HttpRequestJson requestJson) {

        PostService postService = new PostService();

        ServiceResult result = postService.unLikePost(requestJson.getPostId(), requestJson.getJwtToken());

        return result.toJson();
    }

    @PostMapping("/unDislike")
    public String unDislikePost(@RequestBody HttpRequestJson requestJson) {

        PostService postService = new PostService();

        ServiceResult result = postService.unDislikePost(requestJson.getPostId(), requestJson.getJwtToken());

        return result.toJson();
    }

    @PostMapping("/addComment")
    public String addComment(@RequestBody HttpRequestJson requestJson) {

        PostService postService = new PostService();
        ServiceResult result = new ServiceResult();

        if (requestJson.getCommentId() != -1) {
            result = postService.addSubcomment(requestJson.getCommentId(), requestJson.getContent(), requestJson.getJwtToken());
        }

        if (requestJson.getPostId() != -1) {
            result = postService.addCommentToPost(requestJson.getPostId(), requestJson.getContent(), requestJson.getJwtToken());
        }

        return result.toJson();
    }

    @PostMapping("/interactWithCommentPoint")
    public String interactWithCommentPoint(@RequestBody HttpRequestJson requestJson) {

        PostService postService = new PostService();

        ServiceResult result = postService.interactWithCommentPoint(requestJson.getCommentId(), requestJson.getJwtToken(), requestJson.getIsCommentLiked());

        return result.toJson();
    }

    @PostMapping("/comments")
    public String getGroupPosts(@RequestBody HttpRequestJson requestJson) {

        PostService postService = new PostService();
        ServiceResult result = postService.getPostComments(requestJson.getPostId(), requestJson.getJwtToken());

        return result.toJson();
    }

    @PostMapping("/data")
    public String getPostData(@RequestBody HttpRequestJson requestJson) {

        PostService postService = new PostService();
        ServiceResult result = postService.getPostData(requestJson.getPostId());

        return result.toJson();
    }
}
