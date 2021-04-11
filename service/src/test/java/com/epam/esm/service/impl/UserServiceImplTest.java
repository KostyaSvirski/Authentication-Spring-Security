package com.epam.esm.service.impl;

import com.epam.esm.config.ServiceConfig;
import com.epam.esm.converter.UserEntityToUserDTOConverter;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.hibernate.UserRepository;
import com.epam.esm.hibernate.impl.UserRepositoryImpl;
import com.epam.esm.persistence.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ContextConfiguration(classes = ServiceConfig.class)
@ExtendWith(SpringExtension.class)
@SpringJUnitConfig
class UserServiceImplTest {

    @Mock
    private final UserRepository userRepository = Mockito.mock(UserRepositoryImpl.class);
    @Mock
    private final UserEntityToUserDTOConverter toUserDTOConverter =
            Mockito.mock(UserEntityToUserDTOConverter.class);
    @InjectMocks
    private UserServiceImpl service;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAll() {
        Mockito.when(userRepository.findAll(Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(Collections.singletonList(new UserEntity().builder().id(1).build()));
        Mockito.when(toUserDTOConverter.apply(Mockito.any())).thenReturn(new UserDTO().builder().id(1).build());
        List<UserDTO> actual = service.findAll(Mockito.anyInt(), Mockito.anyInt());
        assertEquals(Collections.singletonList(new UserDTO().builder().id(1).build()), actual);
    }

    @Test
    public void testFindAllException() {
        Mockito.when(userRepository.findAll(Mockito.anyInt(), Mockito.anyInt())).thenThrow(new RuntimeException());
        assertThrows(RuntimeException.class,
                () -> service.findAll(Mockito.anyInt(), Mockito.anyInt()));
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3, 4})
    public void testFindSpecificUser(long id) {
        Mockito.when(userRepository.find(Mockito.eq(id)))
                .thenReturn(Optional.of(new UserEntity().builder().id(id).build()));
        Mockito.when(toUserDTOConverter.apply(Mockito.any())).thenReturn(new UserDTO().builder().id(1).build());
        Optional<UserDTO> actual = Optional.ofNullable(service.find(id));
        assertEquals(Optional.of(new UserDTO().builder().id(1).build()), actual);
    }

    @Test
    public void testFindSpecificUserNotFound() {
        Mockito.when(userRepository.find(Mockito.anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.find(Mockito.anyLong()));
    }

}