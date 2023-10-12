/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import httpRequestJson.HttpRequestJson;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import serviceResult.ServiceResult;
import services.AuthorizationService;

/**
 *
 * @author Zuucker
 */
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", allowCredentials = "true")
@RequestMapping("/auth")
@RestController
public class AuthorizationController {

    @PostMapping("/usernameAvailability")
    public String checkUsernameAvailability(@RequestBody HttpRequestJson requestJson) {

        AuthorizationService authService = new AuthorizationService();
        ServiceResult result = new ServiceResult();

        result.status = authService.isUsernameFree(requestJson.getUsername());
        result.value = "ok";

        return result.toJson();
    }

    @PostMapping("/register")
    public String registerUser(@RequestBody HttpRequestJson requestJson) {

        AuthorizationService authService = new AuthorizationService();

        ServiceResult result = authService.registerUser(requestJson.getUsername(),
                requestJson.getEmail(),
                requestJson.getPassword(),
                requestJson.getConfirmPassword());

        return result.toJson();
    }

    @PostMapping("/login")
    public String loginUser(@RequestBody HttpRequestJson requestJson) {

        AuthorizationService authService = new AuthorizationService();
        ServiceResult result = authService.loginUser(requestJson.getUsername(), requestJson.getPassword());

        return result.toJson();
    }

    @GetMapping("/verify/{code}")
    public String verify(@PathVariable String code) {

        AuthorizationService authService = new AuthorizationService();
        ServiceResult result = authService.verifyUser(code);

        return result.toJson();
    }

    @PostMapping("/isVerified")
    public String checkIfIsVerified(@RequestBody HttpRequestJson requestJson) {

        AuthorizationService authService = new AuthorizationService();
        ServiceResult result = authService.checkIfUserIfVerified(requestJson.getUsername());

        return result.toJson();
    }
}
