package com.epam.esm.service.impl;

import com.epam.esm.converter.OrderEntityToOrderDTOConverter;
import com.epam.esm.dto.OrderDTO;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.hibernate.OrderRepository;
import com.epam.esm.hibernate.impl.OrderRepositoryImpl;
import com.epam.esm.persistence.OrderEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private final OrderRepository repository = Mockito.mock(OrderRepositoryImpl.class);
    @Mock
    private final OrderEntityToOrderDTOConverter entityToDTOConverter =
            Mockito.mock(OrderEntityToOrderDTOConverter.class);
    @InjectMocks
    private OrderServiceImpl service;

    @Test
    void testFindAll() {
        Mockito.when(repository.findAll(1, 1))
                .thenReturn(Collections.singletonList(OrderEntity.builder().id(1).build()));
        Mockito.when(entityToDTOConverter.apply(Mockito.any())).thenReturn(OrderDTO.builder().id(1).build());
        List<OrderDTO> actual = service.findAll(1, 1);
        assertEquals(Collections.singletonList(OrderDTO.builder().id(1).build()), actual);
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3, 4})
    void testFindSpecificOrder(long id) throws EntityNotFoundException {
        Mockito.when(repository.find(Mockito.eq(id)))
                .thenReturn(Optional.of(OrderEntity.builder().id(id).build()));
        Mockito.when(entityToDTOConverter.apply(Mockito.any())).thenReturn(OrderDTO.builder().id(1).build());
        Optional<OrderDTO> actual = Optional.ofNullable(service.find(id));
        assertEquals(Optional.of(OrderDTO.builder().id(1).build()), actual);
    }

    @Test
    void testFindNotExistingSpecificOrder() throws EntityNotFoundException {
        Mockito.when(repository.find(Mockito.anyLong()))
                .thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.find(0));
    }
}