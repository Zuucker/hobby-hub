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

    public void print() {
        System.out.println(this.username);
        System.out.println(this.password);
        System.out.println(this.confirmPassword);
        System.out.println(this.email);
    }
}
