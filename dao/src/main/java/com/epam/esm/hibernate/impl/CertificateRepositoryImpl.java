package com.epam.esm.hibernate.impl;

import com.epam.esm.exception.DaoException;
import com.epam.esm.hibernate.CertificateRepository;
import com.epam.esm.persistence.GiftCertificateEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Repository
public class CertificateRepositoryImpl implements CertificateRepository {

    private static final String HQL_RETRIEVE_ALL = "from GiftCertificateEntity cert ";
    private static final String HQL_ORDER_BY_ID = "order by cert.id ";
    private static final String HQL_ORDER_BY_NAME = "order by cert.name ";
    private static final String HQL_ORDER_BY_CREATE_DATE = "order by cert.createDate ";
    private static final String HQL_CONDITION_DESCRIPTION = "where cert.description =: description ";
    private static final String HQL_CONDITION_NAME = "where cert.name =: name ";

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<GiftCertificateEntity> find(long id) throws DaoException {
        return Optional.of(em.find(GiftCertificateEntity.class, id));
    }

    @Override
    public List<GiftCertificateEntity> findAll(int limit, int page) throws DaoException {
        return em.createQuery(HQL_RETRIEVE_ALL + HQL_ORDER_BY_ID)
                .setFirstResult((page - 1) * limit)
                .setMaxResults(limit)
                .getResultList();
    }


    @Override
    public int create(GiftCertificateEntity entity) throws DaoException {
        em.persist(entity);
        int idNewCert = (int) entity.getId();
        return idNewCert;
    }


    @Override
    public void delete(long id) throws DaoException {
        GiftCertificateEntity certToDelete = em.find(GiftCertificateEntity.class, id);
        em.remove(certToDelete);
    }

    @Override
    public void update(GiftCertificateEntity certificateExampleForUpdate, long id) throws DaoException {
        Optional<GiftCertificateEntity> certWrapper = find(id);
        if (certWrapper.isPresent()) {
            GiftCertificateEntity cert = certWrapper.get();
            insertDataForUpdate(certificateExampleForUpdate, cert);
            em.merge(cert);
        }
    }

    private GiftCertificateEntity insertDataForUpdate
            (GiftCertificateEntity example, GiftCertificateEntity cert) {
        if (example.getName() != null) {
            cert.setName(example.getName());
        }
        if (example.getDescription() != null) {
            cert.setDescription(example.getDescription());
        }
        if (example.getLastUpdateDate() != null) {
            cert.setLastUpdateDate(example.getLastUpdateDate());
        }
        if (example.getCreateDate() != null) {
            cert.setCreateDate(example.getCreateDate());
        }
        if (example.getDuration() != 0) {
            cert.setDuration(example.getDuration());
        }
        if (example.getPrice() != 0) {
            cert.setPrice(example.getPrice());
        }
        if (example.getTagsDependsOnCertificate() != null) {
            cert.setTagsDependsOnCertificate(example.getTagsDependsOnCertificate());
        }
        return cert;
    }

    @Override
    public List<GiftCertificateEntity> sortCertificatesByName(String method, int limit, int page)
            throws DaoException {
        return getSortedCertificates(method, limit, page, HQL_ORDER_BY_NAME);
    }

    @Override
    public List<GiftCertificateEntity> sortCertificatesByCreateDate
            (String method, int limit, int page)
            throws DaoException {
        return getSortedCertificates(method, limit, page, HQL_ORDER_BY_CREATE_DATE);

    }

    private List<GiftCertificateEntity> getSortedCertificates
            (String method, int limit, int page, String hqlOrderBy) throws DaoException {
        try {
            return em.createQuery(HQL_RETRIEVE_ALL + hqlOrderBy + method)
                    .setFirstResult(limit * (page - 1))
                    .setMaxResults(limit)
                    .getResultList();
        } catch (Throwable e) {
            throw new DaoException(e.getMessage());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public List<GiftCertificateEntity> searchByName
            (String name, int limit, int page) throws DaoException {
        return searchCerts(limit, page, HQL_CONDITION_NAME, name);
    }

    @Override
    public List<GiftCertificateEntity> searchByDescription
            (String description, int limit, int page) throws DaoException {
        return searchCerts(limit, page, HQL_CONDITION_DESCRIPTION, description);
    }

    private List<GiftCertificateEntity> searchCerts
            (int limit, int page, String hqlCondition, String param) throws DaoException {
        try {
            Query query = em.createQuery(HQL_RETRIEVE_ALL + hqlCondition);
            query.setParameter(1, param);
            return query.setFirstResult(limit * (page - 1))
                    .setMaxResults(limit)
                    .getResultList();
        } catch (Throwable e) {
            throw new DaoException(e.getMessage());
        }
    }

    // TODO: 09.02.2021
    @Override
    public List<GiftCertificateEntity> searchByTag(String nameOfTag, int limit, int page)
            throws DaoException {
        return null;
    }
}