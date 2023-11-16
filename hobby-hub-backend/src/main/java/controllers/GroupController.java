/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import httpRequestJson.HttpRequestJson;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import serviceResult.ServiceResult;
import services.GroupService;

/**
 *
 * @author Zuucker
 */
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", allowCredentials = "true")
@RequestMapping("/group")
@RestController
public class GroupController {

    @PostMapping("/add")
    public String addGroup(@RequestBody HttpRequestJson requestJson) {

        GroupService userService = new GroupService();

        ServiceResult result = userService.addGroup(requestJson.getGroupName(), requestJson.getGroupDescription(), requestJson.getGroupOwnerId());

        return result.toJson();
    }

    @PostMapping("/subscribe")
    public String subscribeToGroup(@RequestBody HttpRequestJson requestJson) {

        GroupService userService = new GroupService();

        ServiceResult result = userService.subcribeToGroup(Integer.parseInt(requestJson.getId()), requestJson.getGroupId());

        return result.toJson();
    }
}
