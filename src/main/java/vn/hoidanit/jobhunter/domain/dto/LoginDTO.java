package vn.hoidanit.jobhunter.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginDTO {

    @Email
    @NotBlank(message = "username không được để trống ")
    private String username;

    @NotBlank(message = "password không được để trống ")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginDTO [username=" + username + ", password=" + password + "]";
    }

}