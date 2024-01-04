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
    private String commentId;
    private String content;
    private String textContent;
    private String imageContent;
    private String videoContent;
    private String isCommentLiked;
    private String titleQuery;
    private String groupsQuery;
    private String usersQuery;
    private String sortBy;
    private String type;
    private String likesAmmount;
    private String userId;
    private String url;

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
        return Integer.parseInt(this.postId == null ? "-1" : this.postId);
    }

    public int getCommentId() {
        return Integer.parseInt(this.commentId == null ? "-1" : this.commentId);
    }

    public String getTextContent() {
        return this.textContent;
    }

    public String getVideoContent() {
        return this.videoContent;
    }

    public String getImageContent() {
        return this.imageContent;
    }

    public boolean getIsCommentLiked() {
        return Boolean.parseBoolean(this.isCommentLiked);
    }

    public String getContent() {
        return this.content;
    }

    public String getTitleQuery() {
        return this.titleQuery;
    }

    public String getGroupsQuery() {
        return this.groupsQuery;
    }

    public String getUsersQuery() {
        return this.usersQuery;
    }

    public String getSortBy() {
        return this.sortBy;
    }

    public int getType() {
        return Integer.parseInt(this.type);
    }

    public int getLikes() {
        return Integer.parseInt(this.likesAmmount);
    }

    public int getUserId() {
        return Integer.parseInt(this.userId);
    }

    public String getUrl() {
        return this.url;
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
