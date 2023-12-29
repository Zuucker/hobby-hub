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

        ServiceResult result = userService.addGroup(requestJson.getGroupName(), requestJson.getGroupDescription(), requestJson.getJwtToken());

        return result.toJson();
    }

    @PostMapping("/subscribe")
    public String subscribeToGroup(@RequestBody HttpRequestJson requestJson) {

        GroupService userService = new GroupService();

        ServiceResult result = userService.subcribeToGroup(requestJson.getJwtToken(), requestJson.getGroupId());

        return result.toJson();
    }

    @PostMapping("/isNameFree")
    public String checkIfNameIsFree(@RequestBody HttpRequestJson requestJson) {

        GroupService groupService = new GroupService();

        ServiceResult result = groupService.isNameFree(requestJson.getGroupName());

        return result.toJson();
    }

    @PostMapping("/data")
    public String getGroupData(@RequestBody HttpRequestJson requestJson) {

        GroupService groupService = new GroupService();

        ServiceResult result = groupService.getGroupData(requestJson.getGroupName());

        return result.toJson();
    }

    @PostMapping("/edit")
    public String editGroup(@RequestBody HttpRequestJson requestJson) {

        GroupService groupService = new GroupService();
        ServiceResult result = groupService.editGroup(requestJson.getGroupId(), requestJson.getGroupName(), requestJson.getGroupDescription(), requestJson.getJwtToken());

        return result.toJson();
    }

    @PostMapping("/leave")
    public String leaveGroup(@RequestBody HttpRequestJson requestJson) {

        GroupService groupService = new GroupService();
        ServiceResult result = groupService.leaveGroup(requestJson.getGroupId(), requestJson.getJwtToken());

        return result.toJson();
    }

    @PostMapping("/posts")
    public String getGroupPosts(@RequestBody HttpRequestJson requestJson) {

        GroupService groupService = new GroupService();
        ServiceResult result = groupService.getGroupPosts(requestJson.getGroupId());

        return result.toJson();
    }
}
