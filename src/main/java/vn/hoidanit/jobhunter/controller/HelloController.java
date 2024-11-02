package vn.hoidanit.jobhunter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.jobhunter.service.error.IdInvalidException;

@RestController
public class HelloController {
    @ExceptionHandler(value = IdInvalidException.class)
    public ResponseEntity<String> handleBlogAlreadyExistsException(IdInvalidException idInvalidException) {
        // return new ResponseEntity("Id khong duoc vuot qua 1500",
        // HttpStatus.CONFLICT);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(idInvalidException.getMessage());
    }

    @GetMapping("/")
    public String getHelloWorld() throws IdInvalidException {
        if (true)
            throw new IdInvalidException("check");
        return "Hello World (Hỏi Dân IT & Eric)";
    }
}
