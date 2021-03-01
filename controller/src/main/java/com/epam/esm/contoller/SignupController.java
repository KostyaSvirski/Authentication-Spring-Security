package com.epam.esm.contoller;

import com.epam.esm.dto.CreateActionHypermedia;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/signup")
public class SignupController {

    @Autowired
    private UserService service;

    @PostMapping("/")
    public ResponseEntity<?> signup(@RequestBody UserDTO newUser) {
        long result = service.createUser(newUser);
        return new ResponseEntity<>(new CreateActionHypermedia(result), HttpStatus.CREATED);
    }
}