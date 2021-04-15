package com.epam.esm.hibernate.impl;

import com.epam.esm.config.ConfigDB;
import com.epam.esm.hibernate.UserRepository;
import com.epam.esm.persistence.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = {ConfigDB.class},
        loader = AnnotationConfigContextLoader.class)
class UserRepositoryImplTest {

    @Autowired
    private UserRepository repository;

    @BeforeEach
    void createUsers() {
        int i = 1;
        while (i < 25) {
            UserEntity newUser = UserEntity.builder()
                    .name("aaa" + i).surname("bbb" + i).email("aaa" + i).password("aaaa" + i)
                    .build();
            repository.create(newUser);
            i++;
        }
    }

    @Test
    void testCreateUser() {
        UserEntity newUser = UserEntity.builder()
                .name("aaa").surname("bbb").email("aaa").password("aaaa")
                .build();
        long id = repository.create(newUser);
        assertTrue(id > 0);
    }

    @ParameterizedTest
    @ValueSource(strings = {"aaa1", "aaa5", "aaa10", "aaa24"})
    void testLoadUserByUsername(String email) {
        Optional<UserEntity> entity = repository.loadUserByUsername(email);
        assertTrue(entity.isPresent());
    }

    @ParameterizedTest
    @ValueSource(strings = {"bbb", "ccc"})
    void testLoadUserByIncUsername(String email) {
        Optional<UserEntity> entity = repository.loadUserByUsername(email);
        assertFalse(entity.isPresent());
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 5L, 10L, 15L, 24L})
    void testFindUserById(long id) {
        Optional<UserEntity> entity = repository.find(id);
        assertTrue(entity.isPresent());
    }

    @ParameterizedTest
    @ValueSource(longs = {0L})
    void testFindUserByIncId(long id) {
        Optional<UserEntity> entity = repository.find(id);
        assertFalse(entity.isPresent());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    void testFindUsersByPage(int page) {
        int limit = 5;
        List<UserEntity> entities = repository.findAll(limit, page);
        int expectedFirst = (page - 1) * limit + 1;
        int expectedLast = page * limit;
        assertEquals(expectedFirst, entities.get(0).getId());
        assertEquals(expectedLast, entities.get(entities.size() - 1).getId());
    }

    @ParameterizedTest
    @ValueSource(ints = {3, 4, 5})
    void testFindUsersByLimit(int limit) {
        int page = 2;
        List<UserEntity> entities = repository.findAll(limit, page);
        int expectedFirst = (page - 1) * limit + 1;
        int expectedLast = page * limit;
        assertEquals(expectedFirst, entities.get(0).getId());
        assertEquals(expectedLast, entities.get(entities.size() - 1).getId());
    }
}