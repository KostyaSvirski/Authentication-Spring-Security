package com.epam.esm.dto;

import org.springframework.hateoas.RepresentationModel;

public class CreateActionHypermedia extends RepresentationModel<CreateActionHypermedia> {

    private final long id;

    public CreateActionHypermedia(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
