package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // @GetMapping("/users/create")
    @PostMapping("/users")
    public ResponseEntity<User> getMethodName(@RequestBody User user) {
        // User user = new User();
        // user.setEmail("thebinh@gmail.com");
        // user.setName("thebinh");
        // user.setPassword("0281");
        this.userService.handleSaveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) {
        System.out.println(id);
        this.userService.deleteUserById(id);
        return ResponseEntity.ok("Da xoa nguoi dung thanh cong");
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
        User user = this.userService.findUser(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUser() {
        return ResponseEntity.ok(this.userService.handleAllUsers());
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
            find.setPassword(user.getPassword());
            return ResponseEntity.ok(find);
        }
    }
}
