package com.epam.esm;

import java.util.List;
import java.util.Optional;

public interface Repository<T> {

    Optional<T> find(long id);

    List<T> findAll(int limit, int page);


}
