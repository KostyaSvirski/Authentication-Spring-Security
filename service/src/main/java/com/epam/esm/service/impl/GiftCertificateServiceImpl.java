package com.epam.esm.service.impl;

import com.epam.esm.converter.GiftCertificateDTOToEntityConverter;
import com.epam.esm.converter.GiftCertificateEntityToDTOConverter;
import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.IncorrectDataException;
import com.epam.esm.hibernate.CertificateRepository;
import com.epam.esm.persistence.GiftCertificateEntity;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.validator.PreparedValidatorChain;
import com.epam.esm.validator.realisation.IntermediateCertificateLink;
import com.epam.esm.validator.realisation.IntermediateSortLink;
import com.epam.esm.validator.realisation.certificate.*;
import com.epam.esm.validator.realisation.sort.FieldValidatorLink;
import com.epam.esm.validator.realisation.sort.MethodValidatorLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateEntityToDTOConverter converterToDto;
    private final GiftCertificateDTOToEntityConverter converterToEntity;
    private final CertificateRepository repository;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateEntityToDTOConverter converterToDto,
                                      GiftCertificateDTOToEntityConverter converterToEntity,
                                      CertificateRepository repository) {
        this.converterToDto = converterToDto;
        this.converterToEntity = converterToEntity;
        this.repository = repository;
    }

    @Override
    public List<GiftCertificateDTO> findAll(int limit, int page) {
        return createResultList(repository.findAll(limit, page));
    }

    @Override
    public GiftCertificateDTO find(long id) throws EntityNotFoundException {
        Optional<GiftCertificateEntity> certificateFromDao = repository.find(id);
        if (certificateFromDao.isEmpty()) {
            throw new EntityNotFoundException("certificate with id " + id + " not found");
        }
        return converterToDto.apply(certificateFromDao.get());

    }

    @Override
    public long create(GiftCertificateDTO certificateDTO) throws IncorrectDataException {
        certificateDTO.setCreateDate(LocalDate.now().toString());
        certificateDTO.setLastUpdateDate(LocalDateTime.now().toString());
        PreparedValidatorChain<GiftCertificateDTO> chain = new IntermediateCertificateLink();
        chain.linkWith(new CertificateNameValidatorLink())
                .linkWith(new DescriptionValidatorLink())
                .linkWith(new DurationValidatorLink())
                .linkWith(new PriceValidatorLink())
                .linkWith(new TagsValidatorLink());
        if (!chain.validate(certificateDTO)) {
            throw new IncorrectDataException("not valid data in certificate");
        }
        return repository.create(converterToEntity.apply(certificateDTO));
    }

    @Override
    public void update(GiftCertificateDTO certificate, long id) throws EntityNotFoundException {
        certificate.setLastUpdateDate(LocalDateTime.now().toString());
        PreparedValidatorChain<GiftCertificateDTO> chain = new CertificateNameValidatorLink();
        chain.linkWith(new DescriptionValidatorLink())
                .linkWith(new DurationValidatorLink())
                .linkWith(new PriceValidatorLink());
        if (chain.validate(certificate)) {
            GiftCertificateEntity certificateForUpdate = converterToEntity.apply((certificate));
            Optional<GiftCertificateEntity> existingWrapper = repository.find(id);
            if (existingWrapper.isPresent()) {
                GiftCertificateEntity existing = existingWrapper.get();
                prepareDataForUpdate(certificateForUpdate, existing);
                repository.update(existing);
            } else {
                throw new EntityNotFoundException("not found");
            }
        }
    }

    private void prepareDataForUpdate
            (GiftCertificateEntity incomming, GiftCertificateEntity existing) {
        if (incomming.getName() != null) {
            existing.setName(incomming.getName());
        }
        if (incomming.getDescription() != null) {
            existing.setDescription(incomming.getDescription());
        }
        if (incomming.getLastUpdateDate() != null) {
            existing.setLastUpdateDate(incomming.getLastUpdateDate());
        }
        if (incomming.getDuration() != 0) {
            existing.setDuration(incomming.getDuration());
        }
        if (incomming.getPrice() != 0) {
            existing.setPrice(incomming.getPrice());
        }
        if (incomming.getTagsDependsOnCertificate() != null) {
            existing.setTagsDependsOnCertificate(incomming.getTagsDependsOnCertificate());
        }
    }

    @Override
    public void delete(long id) throws EntityNotFoundException {
        Optional<GiftCertificateEntity> certWrapper = repository.find(id);
        if (certWrapper.isPresent()) {
            repository.delete(id);
        } else {
            throw new EntityNotFoundException("not found");
        }
    }

    @Override
    public List<GiftCertificateDTO> findByPartOfName(String partOfName, int limit, int page) {
        return createResultList(repository.searchByName(partOfName, limit, page));
    }

    @Override
    public List<GiftCertificateDTO> findByPartOfDescription(String partOfDescription, int limit, int page) {
        return createResultList(repository.searchByDescription(partOfDescription, limit, page));
    }

    @Override
    public List<GiftCertificateDTO> findByTag(String nameOfTag, int limit, int page) {
        List<GiftCertificateEntity> listOfEntities = repository.searchByTag(nameOfTag, limit, page);
        if (listOfEntities.isEmpty()) {
            throw new EntityNotFoundException("certificates with tag name: " + nameOfTag + " - not found");
        } else {
            return createResultList(listOfEntities);
        }
    }

    @Override
    public List<GiftCertificateDTO> sortByField(String field, String method, int limit, int page) {
        Map<String, String> tempMapForValidateParams = new HashMap<>();
        tempMapForValidateParams.put("method", method);
        tempMapForValidateParams.put("field", field);
        PreparedValidatorChain<Map<String, String>> chain = new IntermediateSortLink();
        chain.linkWith(new FieldValidatorLink()).linkWith(new MethodValidatorLink());
        if (chain.validate(tempMapForValidateParams)) {
            List<GiftCertificateEntity> listFromDao;
            if (field.equals("name_of_certificate")) {
                listFromDao = repository.sortCertificatesByName(method, limit, page);
            } else {
                listFromDao = repository.sortCertificatesByCreateDate
                        (method, limit, page);
            }
            return createResultList(listFromDao);
        }
        return new ArrayList<>();
    }

    private List<GiftCertificateDTO> createResultList(List<GiftCertificateEntity> listFromDao) {
        return listFromDao.stream()
                .map(converterToDto)
                .collect(Collectors.toList());
    }
}
