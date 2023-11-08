/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import httpRequestJson.HttpRequestJson;
import imageManager.ImageManager;
import models.User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import serviceResult.ServiceResult;
import services.AuthorizationService;
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

    @PostMapping("/edit")
    public String editUserData(@RequestBody HttpRequestJson requestJson) {

        UserService userService = new UserService();

        User newUser = new User(requestJson);
        ServiceResult result = userService.editUserData(newUser, requestJson.getJwtToken(), requestJson.getProfilePic());

        return result.toJson();
    }
}
