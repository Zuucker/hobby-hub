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

        ServiceResult result = postService.addPost(post, requestJson.getJwtToken());

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
}
