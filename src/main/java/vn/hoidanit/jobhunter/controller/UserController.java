package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.dto.UpdateUserDTO;
import vn.hoidanit.jobhunter.domain.dto.UserDTO;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    // @GetMapping("/users/create")
    @PostMapping("/users")
    @ApiMessage("Create a user")
    public ResponseEntity<UserDTO> createUser(@RequestBody User user) {
        // User user = new User();
        // user.setEmail("thebinh@gmail.com");
        // user.setName("thebinh");
        // user.setPassword("0281");
        if (this.userService.checkExistUserByEmail(user.getEmail()) == false) {
            user.setPassword(this.passwordEncoder.encode(user.getPassword()));
            this.userService.handleSaveUser(user);
            UserDTO userResponse = new UserDTO();
            userResponse.setAddress(user.getAddress());
            userResponse.setAge(user.getAge());
            userResponse.setCreatedAt(user.getCreatedAt());
            userResponse.setEmail(user.getEmail());
            userResponse.setGender(user.getGender());
            userResponse.setId(user.getId());
            userResponse.setName(user.getName());
            return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
        } else {
            // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            return ResponseEntity.ok(null);
        }

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
    @ApiMessage("fetch user by id")
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

    // @GetMapping("/users")
    // public ResponseEntity<ResultPaginationDTO> getAllUser(
    // @RequestParam("current") Optional<String> currentOptional,
    // @RequestParam("pageSize") Optional<String> pageSizeOptional) {
    // String sCurrent = currentOptional.isPresent() ? currentOptional.get() : "";
    // String sPageSizeOptional = pageSizeOptional.isPresent() ?
    // pageSizeOptional.get() : "";
    // Pageable pageable = PageRequest.of(Integer.parseInt(sCurrent) - 1,
    // Integer.parseInt(sPageSizeOptional));
    // // từ lớp cha gọi xuống lớp con
    // return ResponseEntity.ok(this.userService.fetchAllUser(pageable));
    // }

    @GetMapping("/users")
    @ApiMessage("Fetch all users")
    public ResponseEntity<ResultPaginationDTO> getAllUser(
            @Filter Specification<User> spe,
            @RequestParam("current") Optional<String> currentOptional,
            @RequestParam("pageSize") Optional<String> pageSizeOptional,
            Pageable pageable) {
        return ResponseEntity.ok(this.userService.fetchAllUser(spe, pageable));
    }

    @PutMapping("/users")
    @ApiMessage("update a user")
    public ResponseEntity<UpdateUserDTO> updateUserById(@RequestBody User user) {
        User find = this.userService.findUser(user.getId());
        System.out.println(find);
        if (find == null) {
            return ResponseEntity.ok(null);
        } else {
            UpdateUserDTO updateUser = new UpdateUserDTO();

            updateUser.setId(user.getId());
            updateUser.setAddress(user.getAddress());
            find.setAddress(user.getAddress());
            updateUser.setAge(user.getAge());
            find.setAge(user.getAge());
            updateUser.setGender(user.getGender());
            find.setGender(user.getGender());
            updateUser.setName(user.getName());
            find.setName(user.getName());
            updateUser.setUpdatedAt(Instant.now());
            find.setUpdatedAt(Instant.now());
            // find.setUpdatedBy(vn.hoidanit.jobhunter.util.SecurityUtil.isPresent() ? );
            this.userService.handleSaveUser(user);
            return ResponseEntity.ok(updateUser);
        }
    }
}
