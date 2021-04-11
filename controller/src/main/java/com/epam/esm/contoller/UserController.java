package com.epam.esm.contoller;

import com.epam.esm.auth.UserPrincipal;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.exception.UnknownPrincipalException;
import com.epam.esm.service.UserService;
import com.epam.esm.util.builder.UserLinkBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final int DEFAULT_LIMIT_INT = 5;
    private static final int DEFAULT_PAGE_INT = 1;
    private static final String DEFAULT_LIMIT = "5";
    private static final String DEFAULT_PAGE = "1";
    @Autowired
    private UserService service;

    @GetMapping("/")
    public ResponseEntity<?> retrieveAllUsers
            (@RequestParam(defaultValue = DEFAULT_LIMIT) int limit,
             @RequestParam(defaultValue = DEFAULT_PAGE) int page) {
        List<UserDTO> resultList = service.findAll(limit, page);
        for (int i = 0; i < resultList.size(); i++) {
            UserLinkBuilder builder = new UserLinkBuilder(resultList.get(i));
            builder.buildRetrieveSpecificUserLink();
            resultList.set(i, builder.getHypermedia());
        }
        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> retrieveSpecificUser(@PathVariable long id) {
        UserDTO user = service.find(id);
        UserLinkBuilder builder = new UserLinkBuilder(user);
        builder.buildOrdersReferencesLink(DEFAULT_LIMIT_INT, DEFAULT_PAGE_INT);
        return new ResponseEntity<>(builder.getHypermedia(), HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<?> retrieveMe() {
        Object me = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (me instanceof UserPrincipal) {
            return retrieveSpecificUser(((UserPrincipal) me).getId());
        } else {
            throw new UnknownPrincipalException("principal of " + me.getClass() + " is unknown");
        }
    }
}
