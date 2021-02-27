package com.epam.esm.service;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.exception.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

public interface TagService extends BaseService<TagDTO> {

    long create(TagDTO bean);

    @Transactional
    void delete(long id) throws EntityNotFoundException;
}
