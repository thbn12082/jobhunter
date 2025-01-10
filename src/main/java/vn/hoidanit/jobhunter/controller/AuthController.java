package vn.hoidanit.jobhunter.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.LoginDTO;
import vn.hoidanit.jobhunter.domain.dto.RestLoginDTO;
import vn.hoidanit.jobhunter.domain.dto.UserLoginDTO;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.SecurityUtil;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;
    private final UserService userService;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil securityUtil,
            UserService userService) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
        this.userService = userService;
    }

    @PostMapping("/login")
    @ApiMessage("Login Form")
    public ResponseEntity<RestLoginDTO> login(@Valid @RequestBody LoginDTO login) {

        // Nạp input gồm username/password vào Security
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                login.getUsername(), login.getPassword());
        // xác thực người dùng => cần viết hàm loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // create a token, có 1 điểm hay của authentication là nó không lưu mật khẩu
        // người dùng, đây là 1 cơ chế của Spring Security
        String accessToken = this.securityUtil.createToken(authentication);
        RestLoginDTO restLoginDTO = new RestLoginDTO();

        User currentUserDB = this.userService.findUserByEmail(login.getUsername());

        UserLoginDTO user = new UserLoginDTO(currentUserDB.getEmail(), currentUserDB.getName(), currentUserDB.getId());
        restLoginDTO.setAccessToken(accessToken);
        restLoginDTO.setUserLoginDTO(user);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return ResponseEntity.ok().body(restLoginDTO);
    }
}
