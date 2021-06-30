package com.epam.esm.service.impl;

import com.epam.esm.converter.TagDTOToTagEntityConverter;
import com.epam.esm.converter.TagEntityToTagDTOConverter;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.hibernate.TagRepository;
import com.epam.esm.hibernate.impl.TagRepositoryImpl;
import com.epam.esm.persistence.TagEntity;
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

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {

    @Mock
    private final TagRepository repository = Mockito.mock(TagRepositoryImpl.class);
    @Mock
    private final TagDTOToTagEntityConverter converterToEntity = Mockito.mock(TagDTOToTagEntityConverter.class);
    @Mock
    private final TagEntityToTagDTOConverter converterToDTO = Mockito.mock(TagEntityToTagDTOConverter.class);
    @InjectMocks
    private TagServiceImpl service;

    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3, 4})
    void testCreate(long id) {
        TagDTO tag = new TagDTO(id, "gg");
        Mockito.when(converterToEntity.apply(tag)).thenReturn(TagEntity.builder().id(id).build());
        Mockito.when(repository.create(Mockito.any())).thenReturn(id);
        long actual = service.create(tag);
        assertEquals(id, actual);
    }

    @Test
    void testFindAll() {
        Mockito.when(repository.findAll(Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(Collections.singletonList(TagEntity.builder().id(1).build()));
        Mockito.when(converterToDTO.apply(Mockito.any())).thenReturn(new TagDTO());
        List<TagDTO> actual = service.findAll(Mockito.anyInt(), Mockito.anyInt());
        assertEquals(Collections.singletonList(new TagDTO()), actual);
    }

    @Test
    void testFindAllException() {
        Mockito.when(repository.findAll(Mockito.anyInt(), Mockito.anyInt())).thenThrow(new RuntimeException());
        assertThrows(Throwable.class,
                () -> service.findAll(Mockito.anyInt(), Mockito.anyInt()));
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3, 4})
    void testFindSpecificTag(long id) {
        Mockito.when(repository.find(Mockito.eq(id)))
                .thenReturn(Optional.of(TagEntity.builder().id(id).build()));
        Mockito.when(converterToDTO.apply(Mockito.any())).thenReturn(TagDTO.builder().id(id).build());
        Optional<TagDTO> actual = Optional.ofNullable(service.find(id));
        assertEquals(Optional.of(TagDTO.builder().id(id).build()), actual);
    }

    @Test
    void testFindSpecificTagNotFound() {
        Mockito.when(repository.find(Mockito.anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.find(Mockito.anyLong()));
    }


    @ParameterizedTest
    @ValueSource(longs = {1, 3, 6})
    void testDelete(long id) {
        Mockito.when(repository.find(Mockito.anyLong())).thenReturn(Optional.of(TagEntity.builder().id(id).build()));
        Mockito.doNothing().when(repository).delete(id);
        service.delete(id);
        assertTrue(true);
    }
}