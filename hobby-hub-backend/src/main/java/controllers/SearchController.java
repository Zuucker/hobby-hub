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
import services.PostService;
import services.SearchService;

/**
 *
 * @author Zuucker
 */
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", allowCredentials = "true")
@RequestMapping("/search")
@RestController
public class SearchController {

    @PostMapping("/data")
    public String getSearchResults(@RequestBody HttpRequestJson requestJson) {

        SearchService searchService = new SearchService();
        ServiceResult result = searchService.getSerachResults(requestJson.getTitleQuery(), requestJson.getGroupsQuery(), requestJson.getUsersQuery(), requestJson.getSortBy());

        return result.toJson();
    }
}
