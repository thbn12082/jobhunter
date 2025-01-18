package vn.hoidanit.jobhunter.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.GetUserByIdDTO;
import vn.hoidanit.jobhunter.domain.dto.Meta;
import vn.hoidanit.jobhunter.domain.dto.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.dto.UpdateUserDTO;
import vn.hoidanit.jobhunter.domain.dto.UserDTO;
import vn.hoidanit.jobhunter.repository.UserRepository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void handleSaveUser(User user) {
        this.userRepository.save(user);
    }

    public void deleteUserById(long id) {
        this.userRepository.deleteById(id);
    }

    public User findUser(long id) {
        return this.userRepository.findById(id);
    }

    public List<User> handleAllUsers(Pageable pageable) {
        Page<User> page = this.userRepository.findAll(pageable);
        return page.getContent();
    }

    public User findUserByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    public boolean checkExistUserByEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }

    // public ResultPaginationDTO fetchAllUser(Pageable pageable) {
    // Page<User> pageUser = this.userRepository.findAll(pageable);
    // ResultPaginationDTO res = new ResultPaginationDTO();
    // Meta mt = new Meta();
    // mt.setPage(pageUser.getNumber() + 1);
    // mt.setPageSize(pageUser.getSize());
    // mt.setPages(pageUser.getTotalPages());
    // mt.setTotal(pageUser.getTotalElements());

    // res.setMeta(mt);
    // res.setResult(pageUser.getContent());
    // return res;
    // }

    public ResultPaginationDTO fetchAllUser(Specification spe, Pageable pageable) {
        Page<User> pageUser = this.userRepository.findAll(spe, pageable);
        ResultPaginationDTO res = new ResultPaginationDTO();
        Meta mt = new Meta();
        mt.setPage(pageUser.getNumber() + 1);
        mt.setPageSize(pageUser.getSize());
        mt.setPages(pageUser.getTotalPages());
        mt.setTotal(pageUser.getTotalElements());

        res.setMeta(mt);
        List<User> lstUser = pageUser.getContent();
        List<UserDTO> lstUserResponse = new ArrayList<>();
        for (User x : lstUser) {
            UserDTO tmp = new UserDTO();
            tmp.setAddress(x.getAddress());
            tmp.setAge(x.getAge());
            tmp.setCreatedAt(x.getCreatedAt());
            tmp.setEmail(x.getEmail());
            tmp.setGender(x.getGender());
            tmp.setId(x.getId());
            tmp.setName(x.getName());
            tmp.setUpdatedAt(x.getUpdatedAt());
            lstUserResponse.add(tmp);
        }
        res.setResult(lstUserResponse);
        return res;
    }

    public UserDTO convertUserToUserDTO(User user) {
        UserDTO userResponse = new UserDTO();
        userResponse.setAddress(user.getAddress());
        userResponse.setAge(user.getAge());
        userResponse.setCreatedAt(user.getCreatedAt());
        userResponse.setEmail(user.getEmail());
        userResponse.setGender(user.getGender());
        userResponse.setId(user.getId());
        userResponse.setName(user.getName());
        return userResponse;
    }

    public GetUserByIdDTO convertUserToGetUserByIdDTO(User user) {
        GetUserByIdDTO res = new GetUserByIdDTO();
        res.setId(user.getId());
        res.setEmail(user.getEmail());
        res.setName(user.getName());
        res.setGender(user.getGender());
        res.setAddress(user.getAddress());
        res.setAge(user.getAge());
        res.setUpdatedAt(user.getUpdatedAt());
        res.setCreatedAt(user.getCreatedAt());
        return res;
    }

    public UpdateUserDTO convertUserToUpdateUserDTO(User user) {
        UpdateUserDTO updateUser = new UpdateUserDTO();
        updateUser.setId(user.getId());
        updateUser.setAddress(user.getAddress());
        updateUser.setAge(user.getAge());
        updateUser.setGender(user.getGender());
        updateUser.setName(user.getName());
        updateUser.setUpdatedAt(Instant.now());
        return updateUser;
    }

    public User updateUser(User user) {
        User find = new User();
        find.setId(user.getId());
        find.setName(user.getName());
        find.setGender(user.getGender());
        find.setAge(user.getAge());
        find.setAddress(user.getAddress());
        find.setEmail(user.getEmail());
        find.setUpdatedAt(Instant.now());
        return find;
    }

    public void updateUserToken(String email, String token) {
        if (this.userRepository.existsByEmail(email)) {
            User user = this.findUserByEmail(email);
            user.setRefreshToken(token);
            this.userRepository.save(user);
        }
    }
}