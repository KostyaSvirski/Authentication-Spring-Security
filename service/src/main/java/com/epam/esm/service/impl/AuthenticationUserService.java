package com.epam.esm.service.impl;

import com.epam.esm.converter.UserEntityToUserDTOConverter;
import com.epam.esm.hibernate.UserRepository;
import com.epam.esm.persistence.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationUserService implements UserDetailsService {

    @Autowired
    private UserEntityToUserDTOConverter toUserDTOConverter;
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> userWrapper = userRepository.loadUserByUsername(username);
        return toUserDTOConverter.apply(userWrapper
                .orElseThrow(() -> new UsernameNotFoundException("user with email " + username + " not exist")
                ));
    }
}
