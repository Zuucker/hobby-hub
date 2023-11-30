/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import org.json.JSONObject;

/**
 *
 * @author Zuucker
 */
public class Group {

    private int id;
    private int ownerId;
    private String owner;
    private String name;
    private String description;

    public Group(int id, int ownerId, String name, String description) {
        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
        this.description = description;
    }

    public Group(int id, int ownerId, String name, String description, String ownerName) {
        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
        this.description = description;
        this.owner = ownerName;
    }

    public Group(String name) {
        this.name = name;
    }

    public Group() {
    }

    public void setOwner(String name) {
        this.owner = name;
    }

    public int getId() {
        return this.id;
    }

    public int getOwnerId() {
        return this.ownerId;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", this.id);
        jsonObject.put("ownerId", this.ownerId);
        jsonObject.put("owner", this.owner);
        jsonObject.put("name", this.name);
        jsonObject.put("description", this.description);

        return jsonObject;
    }
}
