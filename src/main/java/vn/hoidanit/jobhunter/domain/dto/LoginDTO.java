package vn.hoidanit.jobhunter.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO {

    @Email
    @NotBlank(message = "username không được để trống ")
    private String username;

    @NotBlank(message = "password không được để trống ")
    private String password;
}
