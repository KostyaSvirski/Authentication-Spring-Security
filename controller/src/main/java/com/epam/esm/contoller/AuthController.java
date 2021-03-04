package com.epam.esm.contoller;

import com.epam.esm.dto.CreateActionHypermedia;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.util.jwt.JwtProvider;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private UserService service;
    @Autowired
    private JwtProvider provider;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserDTO newUser) {
        long result = service.createUser(newUser);
        return new ResponseEntity<>(new CreateActionHypermedia(result), HttpStatus.CREATED);
    }

    @PostMapping("/auth")
    public ResponseEntity<?> authenticate(@RequestBody UserDTO userToAuthenticate) {
        UserDTO user = service.findUserByLoginAndPassword(userToAuthenticate.getEmail(),
                userToAuthenticate.getPassword());
        String token = provider.generateToken(user);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }


}
