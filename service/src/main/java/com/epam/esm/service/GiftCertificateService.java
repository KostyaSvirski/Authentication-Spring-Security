package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.IncorrectDataException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface GiftCertificateService extends BaseService<GiftCertificateDTO> {

    long create(GiftCertificateDTO bean) throws IncorrectDataException;

    @Transactional
    void delete(long id) throws EntityNotFoundException;

    @Transactional
    void update(GiftCertificateDTO certificate, long id) throws EntityNotFoundException;

    List<GiftCertificateDTO> findByPartOfName(String partOfName, int limit, int page);

    List<GiftCertificateDTO> findByPartOfDescription(String partOfDescription, int limit, int page);

    List<GiftCertificateDTO> findByTag(String idOfTag, int limit, int page);

    List<GiftCertificateDTO> sortByField(String field, String method, int limit, int page);
}
