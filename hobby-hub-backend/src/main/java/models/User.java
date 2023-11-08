/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import httpRequestJson.HttpRequestJson;
import java.time.LocalDate;
import org.json.JSONObject;

/**
 *
 * @author Zuucker
 */
public class User {

    private int id;
    private String username;
    private String password;
    private String confirmPassword;
    private String email;
    private boolean isVerified;
    private LocalDate registerDate;
    private String bio;

    public User() {
    }

    public User(HttpRequestJson json) {
        this.id = Integer.parseInt(json.getId());
        this.username = json.getUsername();
        this.password = json.getPassword();
        this.confirmPassword = json.getConfirmPassword();
        this.email = json.getEmail();
        this.bio = json.getBio();
    }

    public User id(int id) {
        this.id = id;
        return this;
    }

    public User username(String username) {
        this.username = username;
        return this;
    }

    public User password(String password) {
        this.password = password;
        return this;
    }

    public User email(String email) {
        this.email = email;
        return this;
    }

    public User isVerified(boolean isVerified) {
        this.isVerified = isVerified;
        return this;
    }

    public User registerDate(LocalDate registerDate) {
        this.registerDate = registerDate;
        return this;
    }

    public User bio(String bio) {
        this.bio = bio;
        return this;
    }

    public String getUsername() {
        return this.username;
    }

    public int getId() {
        return this.id;
    }

    public String getBio() {
        return this.bio;
    }

    public void print() {
        System.out.println(this.id);
        System.out.println(this.username);
        System.out.println(this.email);
        System.out.println(this.password);
        System.out.println(this.isVerified);
        System.out.println(this.registerDate);
    }

    public String toJsonToString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", this.id);
        jsonObject.put("username", this.username);
        jsonObject.put("email", this.email);
        jsonObject.put("isVerified", this.isVerified);
        jsonObject.put("registerDate", this.registerDate);

        return jsonObject.toString();
    }

    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", this.id);
        jsonObject.put("username", this.username);
        jsonObject.put("email", this.email);
        jsonObject.put("isVerified", this.isVerified);
        jsonObject.put("registerDate", this.registerDate);
        jsonObject.put("bio", this.bio);

        return jsonObject;
    }

}
