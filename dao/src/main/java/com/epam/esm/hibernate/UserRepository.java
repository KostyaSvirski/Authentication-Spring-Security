package com.epam.esm.hibernate;

import com.epam.esm.Repository;
import com.epam.esm.persistence.UserEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends Repository<UserEntity> {

    @Transactional
    long create(UserEntity user);

    Optional<UserEntity> loadUserByUsername(String email);
}
