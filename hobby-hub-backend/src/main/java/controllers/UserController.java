/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import httpRequestJson.HttpRequestJson;
import models.User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import serviceResult.ServiceResult;
import services.UserService;

/**
 *
 * @author Zuucker
 */
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", allowCredentials = "true")
@RequestMapping("/user")
@RestController
public class UserController {

    @PostMapping("/data")
    public String getUserData(@RequestBody HttpRequestJson requestJson) {

        UserService userService = new UserService();
        ServiceResult result = userService.getUserData(requestJson.getUsername());

        return result.toJson();
    }

    @PostMapping("/data2")
    public String getUserData2(@RequestBody HttpRequestJson requestJson) {

        UserService userService = new UserService();
        ServiceResult result = userService.getUserData2(requestJson.getJwtToken());

        return result.toJson();
    }

    @PostMapping("/edit")
    public String editUserData(@RequestBody HttpRequestJson requestJson) {

        UserService userService = new UserService();

        User newUser = new User(requestJson);
        ServiceResult result = userService.editUserData(newUser, requestJson.getJwtToken(), requestJson.getProfilePic());

        return result.toJson();
    }

    @PostMapping("/getUsernameFromJwt")
    public String getUsernameFromJwt(@RequestBody HttpRequestJson requestJson) {

        UserService userService = new UserService();

        ServiceResult result = userService.getUsernameFromJwt(requestJson.getJwtToken());

        return result.toJson();
    }

    @PostMapping("/getUserGroups")
    public String getUserGroups(@RequestBody HttpRequestJson requestJson) {

        UserService userService = new UserService();

        ServiceResult result = userService.getUserGroups(requestJson.getUserId());

        return result.toJson();
    }

    @PostMapping("/hasJoinedGroup")
    public String joinedGroup(@RequestBody HttpRequestJson requestJson) {

        UserService userService = new UserService();

        ServiceResult result = userService.hasJoinedGroup(requestJson.getJwtToken(), requestJson.getGroupId());

        return result.toJson();
    }

    @PostMapping("/posts")
    public String getUserPosts(@RequestBody HttpRequestJson requestJson) {

        UserService userService = new UserService();
        ServiceResult result = userService.getUserPosts(requestJson.getUserId());

        return result.toJson();
    }

    @PostMapping("/feed")
    public String getFeed(@RequestBody HttpRequestJson requestJson) {

        UserService userService = new UserService();
        ServiceResult result = userService.getAllUserPosts(requestJson.getJwtToken());

        return result.toJson();
    }

    @PostMapping("/notifications")
    public String getUserNotifications(@RequestBody HttpRequestJson requestJson) {

        UserService userService = new UserService();

        ServiceResult result = userService.getUserNotifications(requestJson.getJwtToken());

        return result.toJson();
    }

    @PostMapping("/block")
    public String blockUser(@RequestBody HttpRequestJson requestJson) {

        UserService userService = new UserService();

        ServiceResult result = userService.blockUser(requestJson.getJwtToken(), requestJson.getUserId());

        return result.toJson();
    }

    @PostMapping("/unblock")
    public String unBlockUser(@RequestBody HttpRequestJson requestJson) {

        UserService userService = new UserService();

        ServiceResult result = userService.unBlockUser(requestJson.getJwtToken(), requestJson.getUserId());

        return result.toJson();
    }

    @PostMapping("/blocked")
    public String getBlockedUsers(@RequestBody HttpRequestJson requestJson) {

        UserService userService = new UserService();

        ServiceResult result = userService.getBlockedUsers(requestJson.getUserId());

        return result.toJson();
    }
}
