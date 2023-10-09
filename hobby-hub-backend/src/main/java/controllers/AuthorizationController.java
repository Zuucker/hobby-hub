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
    public boolean checkUsernameAvailability(@RequestBody HttpRequestJson requestJson) {

        AuthorizationService authService = new AuthorizationService();

        boolean isUsernameFree = authService.isUsernameFree(requestJson.getUsername());

        return isUsernameFree;
    }

    @PostMapping("/register")
    public boolean registerUser(@RequestBody HttpRequestJson requestJson) {

        AuthorizationService authService = new AuthorizationService();

        boolean registered = authService.registerUser(requestJson.getUsername(),
                requestJson.getEmail(),
                requestJson.getPassword(),
                requestJson.getConfirmPassword());

        return registered;
    }

    @PostMapping("/login")
    public String loginUser(@RequestBody HttpRequestJson requestJson) {

        AuthorizationService authService = new AuthorizationService();

        String token = authService.loginUser(requestJson.getUsername(), requestJson.getPassword());

        return token;
    }

    @GetMapping("/verify/{code}")
    public boolean verify(@PathVariable String code) {

        AuthorizationService authService = new AuthorizationService();

        return authService.verifyUser(code);
    }

    @PostMapping("/isVerified")
    public boolean checkIfIsVerified(@RequestBody HttpRequestJson requestJson) {

        AuthorizationService authService = new AuthorizationService();

        return authService.checkIfUserIfVerified(requestJson.getUsername());
    }
}
