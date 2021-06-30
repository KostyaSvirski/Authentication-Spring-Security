package com.epam.esm.service.impl;

import com.epam.esm.converter.UserDTOToUserEntityConverter;
import com.epam.esm.converter.UserEntityToUserDTOConverter;
import com.epam.esm.dto.RoleDTO;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.exception.EntityIsAlreadyExistException;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.IncorrectDataException;
import com.epam.esm.hibernate.UserRepository;
import com.epam.esm.persistence.UserEntity;
import com.epam.esm.service.UserService;
import com.epam.esm.validator.PreparedValidatorChain;
import com.epam.esm.validator.realisation.IntermediateUserLink;
import com.epam.esm.validator.realisation.user.EmailValidatorLink;
import com.epam.esm.validator.realisation.user.PasswordValidatorLink;
import com.epam.esm.validator.realisation.user.SurnameValidatorLink;
import com.epam.esm.validator.realisation.user.UserNameValidatorLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private UserEntityToUserDTOConverter toUserDTOConverter;
    private UserDTOToUserEntityConverter toUserEntityConverter;


    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           UserEntityToUserDTOConverter toUserDTOConverter,
                           UserDTOToUserEntityConverter toUserEntityConverter) {
        this.userRepository = userRepository;
        this.toUserDTOConverter = toUserDTOConverter;
        this.toUserEntityConverter = toUserEntityConverter;
    }

    @Override
    public List<UserDTO> findAll(int limit, int page) {
        List<UserEntity> listFromDao = userRepository.findAll(limit, page);
        return listFromDao.stream().map(toUserDTOConverter)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO find(long id) throws EntityNotFoundException {
        Optional<UserEntity> userFromDao = userRepository.find(id);
        if (!userFromDao.isPresent()) {
            throw new EntityNotFoundException("user with id " + id + " not found");
        }
        return toUserDTOConverter.apply(userFromDao.get());
    }

    @Override
    public long createUser(UserDTO newUser) {
        newUser.setRole(RoleDTO.builder().id(2).build());
        PreparedValidatorChain<UserDTO> chain = new IntermediateUserLink();
        chain.linkWith(new EmailValidatorLink()).linkWith(new PasswordValidatorLink())
                .linkWith(new UserNameValidatorLink()).linkWith(new SurnameValidatorLink());
        if (!chain.validate(newUser)) {
            throw new IncorrectDataException("not valid data in user");
        }
        if (userRepository.loadUserByUsername(newUser.getUsername()).isPresent()) {
            throw new EntityIsAlreadyExistException("user with this email is already exist");
        }
        return userRepository.create(toUserEntityConverter.apply(newUser));
    }
}
