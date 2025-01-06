package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.PutMapping;

@RestController
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    // @GetMapping("/users/create")
    @PostMapping("/users")
    public ResponseEntity<User> getMethodName(@RequestBody User user) {
        // User user = new User();
        // user.setEmail("thebinh@gmail.com");
        // user.setName("thebinh");
        // user.setPassword("0281");
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        this.userService.handleSaveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) throws IdInvalidException {
        if (id >= 1500) {
            throw new IdInvalidException("Id khong lon hon 1500");
        } else {
            System.out.println(id);
            this.userService.deleteUserById(id);
            return ResponseEntity.ok("Da xoa nguoi dung thanh cong");
        }

    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
        User user = this.userService.findUser(id);
        return ResponseEntity.ok(user);
    }

    // @GetMapping("/users")
    // public ResponseEntity<List<User>> getAllUser(
    // @RequestParam("current") Optional<String> currentOptional,
    // @RequestParam("pageSize") Optional<String> pageSizeOptional) {
    // String sCurrent = currentOptional.isPresent() ? currentOptional.get() : "";
    // String sPageSizeOptional = pageSizeOptional.isPresent() ?
    // pageSizeOptional.get() : "";
    // Pageable pageable = PageRequest.of(Integer.parseInt(sCurrent) - 1,
    // Integer.parseInt(sPageSizeOptional));
    // return ResponseEntity.ok(this.userService.handleAllUsers(pageable));
    // }

    @GetMapping("/users")
    public ResponseEntity<ResultPaginationDTO> getAllUser(
            @RequestParam("current") Optional<String> currentOptional,
            @RequestParam("pageSize") Optional<String> pageSizeOptional) {
        String sCurrent = currentOptional.isPresent() ? currentOptional.get() : "";
        String sPageSizeOptional = pageSizeOptional.isPresent() ? pageSizeOptional.get() : "";
        Pageable pageable = PageRequest.of(Integer.parseInt(sCurrent) - 1, Integer.parseInt(sPageSizeOptional));
        // từ lớp cha gọi xuống lớp con
        return ResponseEntity.ok(this.userService.fetchAllUser(pageable));
    }

    @PutMapping("/users")
    public ResponseEntity<User> putMethodName(@RequestBody User user) {
        User find = this.userService.findUser(user.getId());
        if (find == null) {
            return ResponseEntity.ok(null);
        } else {
            this.userService.handleSaveUser(user);
            find.setEmail(user.getEmail());
            find.setName(user.getName());
            find.setPassword(this.passwordEncoder.encode(user.getPassword()));
            return ResponseEntity.ok(find);
        }
    }
}
