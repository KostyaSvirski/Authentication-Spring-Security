package com.epam.esm.service;

import com.epam.esm.dto.UserDTO;
import org.springframework.transaction.annotation.Transactional;

public interface UserService extends BaseService<UserDTO> {

    @Transactional
    long createUser(UserDTO newUser);

}
