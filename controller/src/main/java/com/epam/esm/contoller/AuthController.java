package com.epam.esm.contoller;

import com.epam.esm.dto.CreateActionHypermedia;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.dto.auth.AuthRequest;
import com.epam.esm.dto.auth.AuthResponse;
import com.epam.esm.exception.NotValidDataForAuthenticateException;
import com.epam.esm.util.jwt.JwtProvider;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private UserService service;
    @Autowired
    private JwtProvider provider;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserDTO newUser) {
        long result = service.createUser(newUser);
        return new ResponseEntity<>(new CreateActionHypermedia(result), HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody AuthRequest request) {
        if(request.getUsername() == null || request.getPassword() == null) {
            throw new NotValidDataForAuthenticateException("username or password didn't matched in request");
        }
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),
                        request.getPassword()));
        String token = provider.generateToken((UserDTO) authentication.getPrincipal());
        return new ResponseEntity<>(new AuthResponse(token), HttpStatus.OK);
    }

}
