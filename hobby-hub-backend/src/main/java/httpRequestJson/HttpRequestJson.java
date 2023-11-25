/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package httpRequestJson;

/**
 *
 * @author Zuucker
 */
public class HttpRequestJson {

    private String username;
    private String password;
    private String confirmPassword;
    private String email;
    private String code;
    private String jwtToken;
    private String profilePic;
    private String bio;
    private String id;
    private String groupName;
    private String groupDescription;
    private String groupId;
    private String postAuthorId;
    private String postTitle;
    private String postType;
    private String postLink;
    private String postId;

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getConfirmPassword() {
        return this.confirmPassword;
    }

    public String getEmail() {
        return this.email;
    }

    public String getCode() {
        return this.code;
    }

    public String getJwtToken() {
        return this.jwtToken;
    }

    public String getProfilePic() {
        return this.profilePic;
    }

    public String getBio() {
        return this.bio;
    }

    public String getId() {
        return this.id;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public String getGroupDescription() {
        return this.groupDescription;
    }

    public int getGroupId() {
        return Integer.parseInt(this.groupId);
    }

    public String getPostTitle() {
        return this.postTitle;
    }

    public String getPostType() {
        return this.postType;
    }

    public String getPostLink() {
        return this.postLink;
    }

    public int getPostAuthorId() {
        return Integer.parseInt(this.postAuthorId);
    }

    public int getPostId() {
        return Integer.parseInt(this.postId);
    }

    public void print() {
        System.out.println(this.username);
        System.out.println(this.password);
        System.out.println(this.confirmPassword);
        System.out.println(this.email);
        System.out.println(this.code);
        System.out.println(this.jwtToken);
        System.out.println("image long :(");
        System.out.println(this.bio);
        System.out.println(this.id);
    }
}
