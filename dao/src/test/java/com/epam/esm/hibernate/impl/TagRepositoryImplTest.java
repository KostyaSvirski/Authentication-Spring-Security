package com.epam.esm.hibernate.impl;

import com.epam.esm.config.ConfigDB;
import com.epam.esm.exception.DaoException;
import com.epam.esm.hibernate.TagRepository;
import com.epam.esm.persistence.GiftCertificateEntity;
import com.epam.esm.persistence.TagEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = {ConfigDB.class},
        loader = AnnotationConfigContextLoader.class)
class TagRepositoryImplTest {

    private static final String PREFIX_NAME = "tag ";

    @Autowired
    private TagRepository repository;

    @BeforeEach
    public void injectDefaultData() throws DaoException {
        for (int i = 0; i < 100; i++) {
            TagEntity tag = new TagEntity();
            tag.setName(PREFIX_NAME + i);
            repository.create(tag);
        }
    }

    @Test
    public void testCreateTag() throws DaoException {
        TagEntity tag = new TagEntity();
        tag.setName("name");
        GiftCertificateEntity cert = new GiftCertificateEntity();
        cert.setName("cert");
        cert.setDescription("description");
        cert.setDuration(10);
        cert.setCreateDate(Date.valueOf(LocalDate.now()));
        cert.setPrice(1000);
        cert.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
        tag.addCertificate(cert);
        int newTagId = repository.create(tag);
        assertEquals(101, newTagId);
    }

    @Test
    public void testCreateTwoTags() throws DaoException {
        TagEntity tag = new TagEntity();
        tag.setName("name");
        GiftCertificateEntity cert = new GiftCertificateEntity();
        cert.setName("cert");
        cert.setDescription("description");
        cert.setDuration(10);
        cert.setCreateDate(Date.valueOf(LocalDate.now()));
        cert.setPrice(1000);
        cert.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
        tag.addCertificate(cert);
        GiftCertificateEntity certSec = new GiftCertificateEntity();
        certSec.setName("certSec");
        certSec.setDescription("descriptionSec");
        certSec.setDuration(100);
        certSec.setCreateDate(Date.valueOf(LocalDate.now()));
        certSec.setPrice(1500);
        certSec.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
        tag.addCertificate(certSec);
        int id = repository.create(tag);
        assertEquals(101, id);
        TagEntity tagFromRepo = repository.find(id).get();
        assertEquals(2, tagFromRepo.getCertificateEntitySet().size());
    }

    @Test
    public void testDeleteTag() throws DaoException {
        repository.delete(1);
        Optional<TagEntity> result = repository.find(1);
        assertFalse(result.isPresent());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    public void testFindAllByPage(int page) throws DaoException {
        List<TagEntity> result = repository.findAll(10, page);
        assertNotNull(result);
        assertEquals((page - 1) * 10L + 1, result.get(0).getId());
        assertEquals(page * 10L, result.get(result.size() - 1).getId());
    }

    @ParameterizedTest
    @ValueSource(ints = {3, 4, 5, 6, 7})
    public void testFindAllByLimit(int limit) throws DaoException {
        List<TagEntity> result = repository.findAll(limit, 1);
        assertNotNull(result);
        assertEquals(1, result.get(0).getId());
        assertEquals(limit, result.get(result.size() - 1).getId());
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 101, 250, 380, 500})
    public void testFindSpecificTag(long id) throws DaoException {
        Optional<TagEntity> result = repository.find(id);
        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
    }
}