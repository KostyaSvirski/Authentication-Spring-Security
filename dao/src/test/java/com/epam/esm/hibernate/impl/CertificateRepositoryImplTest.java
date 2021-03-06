package com.epam.esm.hibernate.impl;

import com.epam.esm.config.ConfigDB;
import com.epam.esm.hibernate.CertificateRepository;
import com.epam.esm.persistence.GiftCertificateEntity;
import com.epam.esm.persistence.TagEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = {ConfigDB.class},
        loader = AnnotationConfigContextLoader.class)
class CertificateRepositoryImplTest {

    @Autowired
    private CertificateRepository repository;

    @BeforeEach
    public void defaultData() {
        for (int i = 0; i < 100; i++) {
            GiftCertificateEntity cert = new GiftCertificateEntity();
            cert.setName("name " + i);
            cert.setDescription("descr " + i);
            cert.setCreateDate(LocalDate.now());
            cert.setPrice(100);
            cert.setDuration(10);
            cert.setLastUpdateDate(LocalDateTime.now());
            TagEntity tag = new TagEntity();
            tag.setName("tag " + i);
            cert.addTag(tag);
            repository.create(cert);
        }
    }

    @Test
    void testCreate() {
        GiftCertificateEntity cert = new GiftCertificateEntity();
        cert.setName("name");
        cert.setDescription("descr");
        cert.setCreateDate(LocalDate.now());
        cert.setPrice(100);
        cert.setDuration(10);
        cert.setLastUpdateDate(LocalDateTime.now());
        TagEntity tag = new TagEntity();
        tag.setName("tag");
        cert.addTag(tag);
        long result = repository.create(cert);
        assertTrue(result > 0);
    }

    @Test
    void testCreateCertWithSomeTags() {
        GiftCertificateEntity cert = new GiftCertificateEntity();
        cert.setName("name");
        cert.setDescription("descr");
        cert.setCreateDate(LocalDate.now());
        cert.setPrice(100);
        cert.setDuration(10);
        cert.setLastUpdateDate(LocalDateTime.now());
        TagEntity tag = new TagEntity();
        tag.setName("tag");
        cert.addTag(tag);
        TagEntity tagSec = new TagEntity();
        tagSec.setName("tag sec");
        cert.addTag(tagSec);
        long result = repository.create(cert);
        assertTrue(result > 0);
        GiftCertificateEntity certFromRepo = repository.find(result).get();
        assertEquals(2, certFromRepo.getTagsDependsOnCertificate().size());
    }

    @Test
    void testCreateTwoCertWithSimilarTags() {
        GiftCertificateEntity cert = new GiftCertificateEntity();
        cert.setName("name");
        cert.setDescription("descr");
        cert.setCreateDate(LocalDate.now());
        cert.setPrice(100);
        cert.setDuration(10);
        cert.setLastUpdateDate(LocalDateTime.now());
        TagEntity tag = new TagEntity();
        tag.setName("tag");
        cert.addTag(tag);
        long result = repository.create(cert);
        assertTrue(result > 0);
        GiftCertificateEntity certSec = new GiftCertificateEntity();
        certSec.setName("nameSec");
        certSec.setDescription("descrSec");
        certSec.setCreateDate(LocalDate.now());
        certSec.setPrice(100);
        certSec.setDuration(10);
        certSec.setLastUpdateDate(LocalDateTime.now());
        TagEntity tagSec = new TagEntity();
        tagSec.setName("tag");
        tagSec.setId(1);
        certSec.addTag(tagSec);
        long resultSec = repository.create(certSec);
        assertTrue(resultSec > 0);
        GiftCertificateEntity certFromRepo = repository.find(result).get();
        assertEquals(1, certFromRepo.getTagsDependsOnCertificate().stream().findAny().get()
                .getCertificateEntitySet().size());
    }

    @Test
    void testUpdateCert() {
        GiftCertificateEntity cert = new GiftCertificateEntity();
        cert.setName("name");
        cert.setDescription("descr");
        cert.setCreateDate(LocalDate.now());
        cert.setPrice(100);
        cert.setDuration(10);
        cert.setLastUpdateDate(LocalDateTime.now());
        TagEntity tag = new TagEntity();
        tag.setName("tag");
        cert.addTag(tag);
        long result = repository.create(cert);
        cert = repository.find(result).get();
        assertTrue(result > 0);
        cert.setName("new name");
        TagEntity newTag = new TagEntity();
        newTag.setName("tag new ");
        cert.removeTag(tag);
        cert.addTag(newTag);
        repository.update(cert);
        GiftCertificateEntity certFromRepo = repository.find(result).get();
        assertEquals(result, certFromRepo.getId());
        assertEquals("new name", certFromRepo.getName());
    }

    @Test
    void testFindSpecificCert() {
        Optional<GiftCertificateEntity> cert = repository.find(2);
        assertTrue(cert.isPresent());
        Set<TagEntity> tagsDependsOnCert = cert.get().getTagsDependsOnCertificate();
        assertNotNull(cert);
        assertNotNull(tagsDependsOnCert);
    }

    @Test
    void testFindAllCerts() {
        List<GiftCertificateEntity> list = repository.findAll(10, 1);
        assertEquals(10, list.size());
        assertEquals(1, list.get(0).getId());
        assertEquals(10, list.get(list.size() - 1).getId());
    }

    @Test
    void testDeleteCert() {
        GiftCertificateEntity cert = new GiftCertificateEntity();
        cert.setName("name");
        cert.setDescription("descr");
        cert.setCreateDate(LocalDate.now());
        cert.setPrice(100);
        cert.setDuration(10);
        cert.setLastUpdateDate(LocalDateTime.now());
        TagEntity tag = new TagEntity();
        tag.setName("tag");
        cert.addTag(tag);
        long id = repository.create(cert);
        assertTrue(id > 0);
        Optional<GiftCertificateEntity> certFromDaoAfterCreate = repository.find(id);
        assertTrue(certFromDaoAfterCreate.isPresent());
        repository.delete(id);
        Optional<GiftCertificateEntity> certFromDao = repository.find(id);
        assertFalse(certFromDao.isPresent());
    }

    @Test
    void testRetrieveByNameCerts() {
        List<GiftCertificateEntity> entities = repository
                .searchByName("name 1", 10, 1);
        assertEquals(10, entities.size());
    }

    @Test
    void testRetrieveSortedByNameCertsAsc() {
        List<GiftCertificateEntity> entities = repository
                .sortCertificatesByName("asc", 10, 1);
        assertEquals(10, entities.size());
        assertTrue(entities.get(0).getName().compareTo(entities.get(entities.size() - 1).getName()) < 0);
    }

    @Test
    void testRetrieveSortedByNameCertsDesc() {
        List<GiftCertificateEntity> entities = repository
                .sortCertificatesByName("desc", 10, 1);
        assertEquals(10, entities.size());
        assertTrue(entities.get(0).getName().compareTo(entities.get(entities.size() - 1).getName()) > 0);
    }

    @Test
    void testRetrieveSortedByNameCertsException() {
        assertThrows(RuntimeException.class, () -> repository
                .sortCertificatesByName("awef", 10, 1));
    }

    @Test
    void testRetrieveSortedByDateCertsAsc() {
        List<GiftCertificateEntity> entities = repository
                .sortCertificatesByCreateDate("asc", 10, 1);
        assertEquals(10, entities.size());
        assertEquals(entities.get(0).getCreateDate().compareTo(entities.get(entities.size() - 1).getCreateDate()), 0);
    }

    @Test
    void testRetrieveSortedByDateCertsDesc() {
        List<GiftCertificateEntity> entities = repository
                .sortCertificatesByCreateDate("desc", 10, 1);
        assertEquals(10, entities.size());
        assertEquals(entities.get(0).getCreateDate().compareTo(entities.get(entities.size() - 1).getCreateDate()), 0);
    }

    @Test
    void testRetrieveSortedByDateCertsException() {
        assertThrows(IllegalArgumentException.class, () -> repository
                .sortCertificatesByCreateDate("awef", 10, 1));
    }
}