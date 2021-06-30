package com.epam.esm.service;

import com.epam.esm.exception.EntityNotFoundException;

import java.util.List;

public interface BaseService<T> {

    List<T> findAll(int limit, int page);

    T find(long id) throws EntityNotFoundException;
}
