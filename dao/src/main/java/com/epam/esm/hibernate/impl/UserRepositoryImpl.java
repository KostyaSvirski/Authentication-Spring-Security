package com.epam.esm.hibernate.impl;

import com.epam.esm.hibernate.UserRepository;
import com.epam.esm.persistence.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final static String HQL_FIND_ALL = "from UserEntity user order by user.id";
    private final static String HQL_SUFFIX_FIND_BY_EMAIL = " where user.email = ?1";

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<UserEntity> find(long id) {
        return Optional.ofNullable(em.find(UserEntity.class, id));
    }

    @Override
    public List<UserEntity> findAll(int limit, int page) {
        return em.createQuery(HQL_FIND_ALL)
                .setFirstResult((page - 1) * limit)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public long create(UserEntity newUser) {
        em.persist(newUser);
        return newUser.getId();
    }

    @Override
    public Optional loadUserByUsername(String email) {
        return em.createQuery("from UserEntity user where user.email =: email order by user.id ")
                .setParameter("email", email).getResultList().stream().findAny();

    }
}
