package vn.hoidanit.jobhunter.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
    @Value("${hoidanit.jwt.refresh-token-validity-in-seconds}")
    private long jwtRefreshExpiration;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;
    private final UserService userService;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil securityUtil,
            UserService userService) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
        this.userService = userService;
    }

    @PostMapping("/auth/login")
    @ApiMessage("Login Form")
    public ResponseEntity<RestLoginDTO> login(@Valid @RequestBody LoginDTO login) {

        // Nạp input gồm username/password vào Security
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                login.getUsername(), login.getPassword());
        // xác thực người dùng => cần viết hàm loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // create a token, có 1 điểm hay của authentication là nó không lưu mật khẩu
        // người dùng, đây là 1 cơ chế của Spring Security
        SecurityContextHolder.getContext().setAuthentication(authentication);

        RestLoginDTO restLoginDTO = new RestLoginDTO();
        User currentUserDB = this.userService.findUserByEmail(login.getUsername());
        UserLoginDTO user = new UserLoginDTO(currentUserDB.getEmail(), currentUserDB.getName(), currentUserDB.getId());
        String accessToken = this.securityUtil.createAccessToken(authentication, user);
        restLoginDTO.setAccessToken(accessToken);
        restLoginDTO.setUserLoginDTO(user);

        // create refreshtoken
        String refreshToken = this.securityUtil.createRefreshToken(login.getUsername(), user);

        // update user refreshtoken ở trong database, update ngay khi người dùng vừa mới
        // login
        this.userService.updateUserToken(login.getUsername(), refreshToken);

        // set cookies
        ResponseCookie resCookies = ResponseCookie
                .from("refresh_token", refreshToken)
                .httpOnly(true)
                // cookies này sẽ được sử dụng cho tất cả các đường link trong dự án của ta
                .path("/")
                .maxAge(jwtRefreshExpiration)
                .build();

        // .header(HttpHeaders.SET_COOKIE, resCookies.toString()) ý nghĩa như sau:
        // HttpHeaders.SET_COOKIE: header sẽ thiết lập cookie trong trình duyệt
        // resCookies.toString() là giá trị của cookie được gửi kèm. Biến resCookies
        // chứa thông tin cookie (có thể là danh sách hoặc một chuỗi đơn giản).
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, resCookies.toString()).body(restLoginDTO);

    }

    @GetMapping("/auth/account")
    @ApiMessage("fetch account")
    public String getAccount() {
        return "fetch account";
    }

}
