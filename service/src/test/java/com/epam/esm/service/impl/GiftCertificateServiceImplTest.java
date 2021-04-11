package com.epam.esm.service.impl;

import com.epam.esm.config.ServiceConfig;
import com.epam.esm.converter.GiftCertificateDTOToEntityConverter;
import com.epam.esm.converter.GiftCertificateEntityToDTOConverter;
import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.hibernate.CertificateRepository;
import com.epam.esm.persistence.GiftCertificateEntity;
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

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ContextConfiguration(classes = ServiceConfig.class)
@ExtendWith(SpringExtension.class)
@SpringJUnitConfig
class GiftCertificateServiceImplTest {

    @Mock
    private CertificateRepository repository;
    @Mock
    private GiftCertificateEntityToDTOConverter entityToDTOConverter;
    @Mock
    private GiftCertificateDTOToEntityConverter dtoToEntityConverter;
    @InjectMocks
    private GiftCertificateServiceImpl service;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        GiftCertificateEntity entity = new GiftCertificateEntity();
        entity.setId(1);
        List<GiftCertificateEntity> resultList = new ArrayList<>();
        resultList.add(entity);
        Mockito.when(repository.findAll(1, 1)).thenReturn(resultList);
        GiftCertificateDTO dto = new GiftCertificateDTO();
        dto.setId(1);
        Mockito.when(entityToDTOConverter.apply(entity)).thenReturn(dto);
        assertNotNull(service.findAll(1, 1));
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3, 4, 5})
    void testFindSpecificCert(long id) {
        GiftCertificateEntity entity = new GiftCertificateEntity();
        entity.setId(id);
        Optional<GiftCertificateEntity> certWrapper = Optional.of(entity);
        Mockito.when(repository.find(id)).thenReturn(certWrapper);
        GiftCertificateDTO dto = new GiftCertificateDTO();
        dto.setId(id);
        Mockito.when(entityToDTOConverter.apply(Mockito.any())).thenReturn(dto);
        assertEquals(new GiftCertificateDTO().builder().id(id).build(), service.find(id));
    }

    @Test
    void testFindNotExistingCert() {
        Mockito.when(repository.find(Mockito.anyLong()))
                .thenReturn(Optional.empty());
        Mockito.when(entityToDTOConverter.apply(Mockito.any())).thenReturn(null);
        assertThrows(EntityNotFoundException.class, () -> service.find(Mockito.anyLong()));
    }

    @Test
    void testCreate() {
        GiftCertificateDTO certDTO = new GiftCertificateDTO();
        certDTO.setName("aaaa");
        certDTO.setPrice(100L);
        certDTO.setCreateDate(Instant.now().toString());
        certDTO.setLastUpdateDate(Instant.now().toString());
        certDTO.setDuration(1);
        TagDTO dto = TagDTO.builder().id(1L).name("aaa").build();
        Set<TagDTO> setTag = new HashSet<>();
        setTag.add(dto);
        certDTO.setTags(setTag);
        Mockito.when(repository.create(dtoToEntityConverter.apply(certDTO))).thenReturn(1L);
        long actual = service.create(certDTO);
        assertEquals(1, actual);
    }

    @Test
    void testUpdate() {
        GiftCertificateDTO certDTO = new GiftCertificateDTO();
        certDTO.setName("aaaa");
        certDTO.setPrice(100);
        Mockito.doThrow(new RuntimeException()).when(repository).update(Mockito.any());
        assertThrows(RuntimeException.class, () -> service.update(certDTO, 1));
    }

    @ParameterizedTest
    @ValueSource(strings = {"asc", "desc"})
    void testSortingMethod(String method) {
        List<GiftCertificateEntity> resultList = new ArrayList<>();
        resultList.add(new GiftCertificateEntity().builder().id(1).build());
        Mockito.when(repository.sortCertificatesByName(Mockito.eq(method), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(resultList);
        Mockito.when(entityToDTOConverter.apply(Mockito.any())).thenReturn(new GiftCertificateDTO().builder().id(1)
                .build());
        List<GiftCertificateDTO> actual = service.sortByField("name_of_certificate", method, 1, 1);
        assertEquals(Collections.singletonList(new GiftCertificateDTO().builder().id(1).build()), actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {"ask", "desk"})
    void testSortingIncMethod(String method) {
        List<GiftCertificateEntity> resultList = new ArrayList<>();
        resultList.add(new GiftCertificateEntity().builder().id(1).build());
        Mockito.when(repository.sortCertificatesByName(Mockito.eq(method), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(resultList);
        Mockito.when(entityToDTOConverter.apply(Mockito.any())).thenReturn(new GiftCertificateDTO().builder().id(1)
                .build());
        List<GiftCertificateDTO> actual = service.sortByField("name_of_certificate", method, 1, 1);
        assertEquals(Collections.emptyList(), actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {"name_of_certificate"})
    void testSortingField(String field) {
        List<GiftCertificateEntity> resultList = new ArrayList<>();
        resultList.add(new GiftCertificateEntity().builder().id(1).build());
        Mockito.when(repository.sortCertificatesByName(Mockito.any(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(resultList);
        Mockito.when(entityToDTOConverter.apply(Mockito.any())).thenReturn(new GiftCertificateDTO().builder().id(1)
                .build());
        List<GiftCertificateDTO> actual = service.sortByField(field, "asc", 1, 1);
        assertEquals(Collections.singletonList(new GiftCertificateDTO().builder().id(1).build()), actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {"description", "price"})
    void testSortingIncField(String field) {
        List<GiftCertificateEntity> resultList = new ArrayList<>();
        resultList.add(new GiftCertificateEntity().builder().id(1).build());
        Mockito.when(repository.sortCertificatesByCreateDate(Mockito.any(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(resultList);
        Mockito.when(entityToDTOConverter.apply(Mockito.any())).thenReturn(new GiftCertificateDTO().builder().id(1)
                .build());
        List<GiftCertificateDTO> actual = service.sortByField(field, "asc", 1, 1);
        assertEquals(Collections.emptyList(), actual);
    }

    @Test
    void testExceptionSort() {
        Mockito.when(repository.sortCertificatesByCreateDate(Mockito.any(), Mockito.anyInt(), Mockito.anyInt()))
                .thenThrow(new RuntimeException());
        Mockito.when(entityToDTOConverter.apply(Mockito.any())).thenReturn(new GiftCertificateDTO().builder().id(1)
                .build());
        assertThrows(RuntimeException.class, () -> service.sortByField("create_date",
                "asc", 1, 1));

    }

    @Test
    void testFindByPartOfDescription() {
        List<GiftCertificateEntity> resultList = new ArrayList<>();
        resultList.add(new GiftCertificateEntity().builder().id(1).build());
        Mockito.when(repository.searchByDescription(Mockito.any(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(resultList);
        Mockito.when(entityToDTOConverter.apply(Mockito.any())).thenReturn(new GiftCertificateDTO().builder().id(1)
                .build());
        List<GiftCertificateDTO> actual = service.findByPartOfDescription
                (Mockito.any(), Mockito.anyInt(), Mockito.anyInt());
        assertEquals(Collections.singletonList(new GiftCertificateDTO().builder().id(1).build()), actual);
    }

    @Test
    void testFindByPartOfName() {
        List<GiftCertificateEntity> resultList = new ArrayList<>();
        resultList.add(new GiftCertificateEntity().builder().id(1).build());
        Mockito.when(repository.searchByName(Mockito.any(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(resultList);
        Mockito.when(entityToDTOConverter.apply(Mockito.any())).thenReturn(new GiftCertificateDTO().builder().id(1)
                .build());
        List<GiftCertificateDTO> actual = service.findByPartOfName
                (Mockito.any(), Mockito.anyInt(), Mockito.anyInt());
        assertEquals(Collections.singletonList(new GiftCertificateDTO().builder().id(1).build()), actual);
    }

    @Test
    void testFindByTagName() {
        List<GiftCertificateEntity> resultList = new ArrayList<>();
        resultList.add(new GiftCertificateEntity().builder().id(1).build());
        Mockito.when(repository.searchByTag(Mockito.any(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(resultList);
        Mockito.when(entityToDTOConverter.apply(Mockito.any())).thenReturn(new GiftCertificateDTO().builder().id(1)
                .build());
        List<GiftCertificateDTO> actual = service.findByTag
                (Mockito.any(), Mockito.anyInt(), Mockito.anyInt());
        assertEquals(Collections.singletonList(new GiftCertificateDTO().builder().id(1).build()), actual);
    }

}