package com.epam.esm.service;

import com.epam.esm.dto.OrderDTO;
import com.epam.esm.dto.UserDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserService extends BaseService<UserDTO> {

    List<OrderDTO> findOrdersOfUser(long idUser, int limit, int page);

    Optional<OrderDTO> findSpecificOrderOfUser(long idUser, long idOrder);

    @Transactional
    long createUser(UserDTO newUser);

    @Transactional
    UserDTO findUserByLoginAndPassword(String login, String password);

}
